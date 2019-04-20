package com.example.bookscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DisplayBookInfo extends AppCompatActivity {

    TextView book_info_isbn;
    TextView book_info_title;
    TextView book_info_author;
    TextView book_info_no_pages;
    TextView book_info_read_or_not;
    ImageView book_info_image;
    RadioGroup radioGroup;
    RadioButton but1;
    RadioButton but2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book_info);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String book_no= bundle.getString("bookinfo");

        Log.d("book_info",""+book_no);

        SQLiteAdapter  sqLiteAdapter=new SQLiteAdapter(this);
        sqLiteAdapter.openToRead();

        Book book=sqLiteAdapter.find_book1(book_no);

        book_info_isbn=(TextView)findViewById(R.id.book_info_isbn);
        book_info_author=(TextView)findViewById(R.id.book_info_authors);
        book_info_title=(TextView)findViewById(R.id.book_info_title);
        book_info_no_pages=(TextView)findViewById(R.id.book_info_no_pages);
        book_info_read_or_not=(TextView)findViewById(R.id.book_info_read_or_not);
        book_info_image=(ImageView) findViewById(R.id.book_info_image);

        book_info_isbn.setText(book.isbn);
        book_info_author.setText(book.author);
        book_info_title.setText(book.title);
        book_info_no_pages.setText(String.valueOf(book.pages));
        book_info_read_or_not.setText((book.read_or_not)?"Yes":"No");
        if (book.image!=null){
        Bitmap bitmap = BitmapFactory.decodeByteArray(book.image, 0, book.image.length);
        book_info_image.setImageBitmap(bitmap);}
    }

}
