package com.abiolasoft.mysimesapp.Interfaces;

import java.util.List;

public interface EbookRepository<Ebook> {

    List<Ebook> loadAllBooks();
    Ebook GetBookById(int book_id);
    void AddBook(Ebook book);
}
