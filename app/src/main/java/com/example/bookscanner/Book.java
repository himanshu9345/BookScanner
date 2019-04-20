package com.example.bookscanner;

import android.util.Log;

public class Book {
    String isbn;
    String title;
    String author;
    boolean read_or_not;
    int pages;
    byte[] image;


    public  Book(){

    }

    public Book(String isbn,
                String title,
                String author,
                boolean read_or_not,
                int pages,
                byte[] image){
        this.isbn=isbn;
        this.title=title;
        this.author=author;
        this.read_or_not=read_or_not;
        this.pages=pages;
        this.image=image;
        Log.d("button_Value", String.valueOf(read_or_not));
    }

}
