package com.example.bookscanner;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ListView listContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getbookdetails=(Button)findViewById(R.id.bookdetails);
        getbookdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),BookDetails.class);
                startActivity(i);
            }
        });

        listContent = (ListView)findViewById(R.id.books_list_view);
        final SQLiteAdapter  sqLiteAdapter=new SQLiteAdapter(this);
        sqLiteAdapter.openToRead();
        Books all_books = sqLiteAdapter.queueAll();
        MyAdapter myAdapter=new MyAdapter(this,all_books);
        listContent.setAdapter(myAdapter);

        listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),DisplayBookInfo.class);
                Bundle extra = new Bundle();

                Books all_books1 = sqLiteAdapter.queueAll();
                extra.putString("bookinfo", all_books1.getbook(position).isbn);
//                intent.putExtra("name",position);
                intent.putExtras(extra);
//                sqLiteAdapter.deleteAll();
                startActivity(intent);
//                sqLiteAdapter.close();


            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        SQLiteAdapter sqLiteAdapter1=new SQLiteAdapter(this);
        sqLiteAdapter1.openToRead();
        Books all_books = sqLiteAdapter1.queueAll();
        MyAdapter myAdapter=new MyAdapter(this,all_books);
        listContent.setAdapter(myAdapter);
        sqLiteAdapter1.close();

    }

}
