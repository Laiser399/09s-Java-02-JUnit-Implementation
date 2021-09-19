package org.mai.library;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mai.library.exceptions.BookUnavailableException;

import static org.junit.jupiter.api.Assertions.*;

class LibraryImplTest {

    private Library library;

    private final Book book1 = new Book(1, "Сильмариллион");
    private final Book book2 = new Book(2, "Меч предназначения");

    private final String student1 = "Alex";
    private final String student2 = "Daniel";

    @BeforeEach
    void setUp() {
        library = new LibraryImpl();

        library.addNewBook(book1);
        library.addNewBook(book2);
    }

    @AfterEach
    void tearDown() {
        library = null;
    }

    @Test
    void addNewBook() {
        var availableBooks = library.findAvailableBooks();
        assertTrue(availableBooks.contains(book1));
        assertTrue(availableBooks.contains(book2));
    }

    @Test
    void borrowBook() {
        var availableBooks = library.findAvailableBooks();
        assertEquals(2, availableBooks.size());

        library.borrowBook(book1, student1);
        availableBooks = library.findAvailableBooks();
        assertEquals(1, availableBooks.size());
        assertFalse(availableBooks.contains(book1));
        assertTrue(availableBooks.contains(book2));

        assertThrows(BookUnavailableException.class, () ->
                library.borrowBook(book1, student2));
    }

    @Test
    void returnBook() {
        library.borrowBook(book1, student1);
        library.returnBook(book1, student1);
        var availableBooks = library.findAvailableBooks();
        assertEquals(2, availableBooks.size());
        assertTrue(availableBooks.contains(book1));

        library.returnBook(book1, student2);
        library.returnBook(book1, student1);
        library.returnBook(book1, student1);
    }

    @Test
    void findAvailableBooks() {
        var availableBooks = library.findAvailableBooks();
        assertEquals(2, availableBooks.size());

        availableBooks.add(new Book(3, "fake book"));
        availableBooks = library.findAvailableBooks();
        assertEquals(2, availableBooks.size());
    }
}