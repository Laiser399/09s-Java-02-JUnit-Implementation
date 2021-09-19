package org.mai.library;

import org.mai.library.exceptions.WrongStudentException;

import java.util.List;

/* Институтская библиотека */
public interface Library {

    /* Регистрация новой книги */
    void addNewBook(Book book);

    /* Студент берет книгу */
    void borrowBook(Book book, String student);

    /* Студент возвращает книгу */
    void returnBook(Book book, String student) throws WrongStudentException;

    /* Получить список свободных книг */
    List<Book> findAvailableBooks();
}