package dev.booknetwork.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import dev.booknetwork.catalog.BookRepository.BookWithStats;
import dev.booknetwork.catalog.dto.BookResponse;
import dev.booknetwork.user.User;

class BookMapperTest {

    private final UUID owner = UUID.randomUUID();
    private final UUID viewer = UUID.randomUUID();
    private final Book book = new Book(
            new User(owner, "owner@example.com", "Ada", "Lovelace"),
            "A Book", "An Author", "", "", Genre.OTHER, true);

    @Test
    void mapsBorrowCountFromStats() {
        BookResponse response = BookMapper.toResponse(stats(2L, 1L), viewer);

        assertThat(response.borrowCount()).isEqualTo(2L);
        // the new trailing component must not shift the existing ones
        assertThat(response.queueLength()).isEqualTo(1L);
        assertThat(response.id()).isEqualTo(book.getId());
        assertThat(response.ownerId()).isEqualTo(owner.toString());
    }

    @Test
    void defaultsBorrowCountToZeroWhenProjectionIsNull() {
        BookResponse response = BookMapper.toResponse(stats(null, 0L), viewer);

        assertThat(response.borrowCount()).isZero();
    }

    private BookWithStats stats(Long borrowCount, Long queueLength) {
        return new BookWithStats() {
            @Override
            public Book getBook() {
                return book;
            }

            @Override
            public Double getRating() {
                return null;
            }

            @Override
            public Boolean getBorrowed() {
                return false;
            }

            @Override
            public Boolean getBorrowedByMe() {
                return false;
            }

            @Override
            public Boolean getWishlisted() {
                return false;
            }

            @Override
            public Boolean getReservedByMe() {
                return false;
            }

            @Override
            public Long getQueueLength() {
                return queueLength;
            }

            @Override
            public Long getBorrowCount() {
                return borrowCount;
            }
        };
    }
}
