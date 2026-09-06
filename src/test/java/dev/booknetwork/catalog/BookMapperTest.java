package dev.booknetwork.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import dev.booknetwork.catalog.BookRepository.BookWithStats;
import dev.booknetwork.catalog.dto.BookResponse;
import dev.booknetwork.user.User;

/**
 * The mapper is the single place a Book plus its stats becomes an API shape.
 * These tests pin the borrowCount behaviour the mapper owns: it carries the
 * projection's count through and normalises a missing count to zero so the API
 * never returns an empty borrowCount.
 */
class BookMapperTest {

    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final UUID VIEWER_ID = UUID.randomUUID();

    @Test
    void reportsTotalBorrowRecordsForABorrowedBook() {
        BookResponse response = BookMapper.toResponse(
                stats(5L, OWNER_ID), OWNER_ID);

        assertEquals(5, response.borrowCount());
    }

    @Test
    void showsZeroForANeverBorrowedBook() {
        BookResponse response = BookMapper.toResponse(
                stats(0L, OWNER_ID), OWNER_ID);

        assertEquals(0, response.borrowCount());
    }

    @Test
    void normalisesAMissingBorrowCountToZero() {
        BookResponse response = BookMapper.toResponse(
                stats(null, OWNER_ID), OWNER_ID);

        assertEquals(0, response.borrowCount());
    }

    private static BookWithStats stats(Long borrowCount, UUID ownerId) {
        return new BookWithStats() {
            @Override
            public Book getBook() {
                return new Book(new User(ownerId, "owner@example.com", "Ada", "Lovelace"),
                        "Test Book", "An Author", "", "", Genre.OTHER, true);
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
                return 0L;
            }

            @Override
            public Long getBorrowCount() {
                return borrowCount;
            }
        };
    }
}
