package com.example.bookscanner;

import java.io.Serializable;
import java.util.ArrayList;

public class Books {


    ArrayList<byte[]> image;
    ArrayList<String> title;
    ArrayList<String> authors;
    ArrayList<Integer> pagecount;
    ArrayList<Boolean> read_or_not;
    ArrayList<String> isbn;
//
//    public Books(int size){
//        this.image=new byte[size][];
//        this.title=new String[size];
//        this.authors=new String[size];
//        this.pagecount=new int[size];
//        this.read_or_not=new boolean[size];
//        this.isbn=new String[size];
//
//
//    }

    public Books() {
        image=new ArrayList<>();
        title=new ArrayList<>();
        authors=new ArrayList<>();
        pagecount=new ArrayList<>();
        read_or_not=new ArrayList<>();
        isbn=new ArrayList<>();

    }

    public void addBook(Book book){
        image.add(0,book.image);
        isbn.add(0,book.isbn);
        title.add(0,book.title);
        authors.add(0,book.author);
        read_or_not.add(0,book.read_or_not);
        pagecount.add(0,book.pages);


    }

    public Book getbook(int index){
        return new Book(isbn.get(index),title.get(index),authors.get(index),read_or_not.get(index),pagecount.get(index),image.get(index));
    }
}
