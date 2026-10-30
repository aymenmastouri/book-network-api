package dev.booknetwork.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository books;

    @Test
    void theMigratedDemoShelfIsReadable() {
        assertThat(books.findAll()).isNotEmpty();
    }
}
