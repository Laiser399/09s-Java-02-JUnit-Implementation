package org.mai.library;

import org.mai.library.exceptions.BookNotBorrowedException;
import org.mai.library.exceptions.BookUnavailableException;
import org.mai.library.exceptions.WrongStudentException;

import java.util.*;

public class LibraryImpl implements Library {

    private final Set<Book> availableBooks = new HashSet<>();
    private final Map<Book, String> borrowedBooks = new HashMap<>();

    @Override
    public void addNewBook(Book book) {
        availableBooks.add(book);
    }

    @Override
    public void borrowBook(Book book, String student) {
        if (!availableBooks.contains(book)) {
            throw new BookUnavailableException(
                    "Book with id=%s and title=\"%s\" is unavailable."
                            .formatted(book.id, book.title));
        }

        availableBooks.remove(book);
        borrowedBooks.put(book, student);
    }

    @Override
    public void returnBook(Book book, String student) throws WrongStudentException {
        var currentOwner = borrowedBooks.get(book);
        if (currentOwner == null) {
            throw new BookNotBorrowedException("%s is not borrowed.".formatted(book));
        }
        if (!currentOwner.equals(student)) {
            throw new WrongStudentException("Current owner of book and received student are different.");
        }

        availableBooks.add(book);
        borrowedBooks.remove(book);
    }

    @Override
    public List<Book> findAvailableBooks() {
        return new ArrayList<>(availableBooks);
    }
}
