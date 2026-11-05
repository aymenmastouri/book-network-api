package dev.booknetwork.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import dev.booknetwork.catalog.BookRepository.BookWithStats;
import dev.booknetwork.catalog.dto.BookResponse;
import dev.booknetwork.user.User;

class BookMapperTest {

    private static final UUID OWNER = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID READER = UUID.fromString("00000000-0000-0000-0000-000000000002");

    private static BookWithStats stats(Book book, Double rating, Boolean borrowed, Long queue) {
        return new BookWithStats() {
            public Book getBook() { return book; }
            public Double getRating() { return rating; }
            public Boolean getBorrowed() { return borrowed; }
            public Boolean getBorrowedByMe() { return null; }
            public Boolean getWishlisted() { return null; }
            public Boolean getReservedByMe() { return null; }
            public Long getQueueLength() { return queue; }
        };
    }

    private static Book book() {
        User owner = new User(OWNER, "ada@example.org", "Ada", "Lovelace");
        return new Book(owner, "Dune", "Frank Herbert", "9780441013593", "Sand.", Genre.SCIFI, true);
    }

    @Test
    void theRatingIsRoundedToOneDecimal() {
        BookResponse response = BookMapper.toResponse(stats(book(), 4.25, true, 3L), READER);

        assertThat(response.rating()).isEqualTo(4.3);
        assertThat(response.borrowed()).isTrue();
        assertThat(response.queueLength()).isEqualTo(3L);
    }

    @Test
    void aBookWithoutStatsReadsAsUnratedFreeAndWithoutQueue() {
        BookResponse response = BookMapper.toResponse(stats(book(), null, null, null), READER);

        assertThat(response.rating()).isZero();
        assertThat(response.borrowed()).isFalse();
        assertThat(response.borrowedByMe()).isFalse();
        assertThat(response.queueLength()).isZero();
    }

    @Test
    void theOwnerSeesTheBookAsTheirs() {
        assertThat(BookMapper.toResponse(stats(book(), null, null, null), OWNER).mine()).isTrue();
        assertThat(BookMapper.toResponse(stats(book(), null, null, null), READER).mine()).isFalse();
    }
}
