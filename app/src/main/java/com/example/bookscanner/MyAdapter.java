package com.example.bookscanner;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MyAdapter implements ListAdapter {

    byte[][] image;
    String[] title;
    String[] authors;
    String[] isbn;
    int[] pagecount;
    boolean[] read_or_not;
    Activity context;
    Books books;

    public MyAdapter(Activity context,Books books){
        this.context=context;
        this.books=books;

    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {

        return books.isbn.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);
        TextView book_author=(TextView) rowView.findViewById(R.id.book_author);
        ImageView book_image=(ImageView) rowView.findViewById(R.id.book_image);
        TextView book_isbn=(TextView) rowView.findViewById(R.id.book_isbn);
        TextView book_title=(TextView) rowView.findViewById(R.id.book_title);
        TextView book_read_status=(TextView) rowView.findViewById(R.id.book_read_status);
        TextView book_page_count=(TextView) rowView.findViewById(R.id.book_page_count);

        book_author.setText(books.authors.get(position));
        Bitmap bitmap=null;
        if (books.image.get(position)!=null){
        bitmap = BitmapFactory.decodeByteArray(books.image.get(position), 0, books.image.get(position).length);

        book_image.setImageBitmap(bitmap);}
        book_isbn.setText(books.isbn.get(position));
        book_title.setText(books.title.get(position));
        book_read_status.setText((books.read_or_not.get(position)==true)?"Yes":"No");
        book_page_count.setText(books.pagecount.get(position).toString());
        return rowView;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return (books.isbn.size()>0)?books.isbn.size():1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
