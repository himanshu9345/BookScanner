package com.example.bookscanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.HashMap;

public class BookDetails extends AppCompatActivity implements View.OnClickListener,CatchAsycResponse {

    private static final int RC_BARCODE_CAPTURE = 9001;
    RadioGroup radioGroup;
    EditText text_barcode;
    EditText title_edittext;
    EditText authors_edittext;
    EditText no_pages_edittext;
    RadioButton yes_button;
    RadioButton but1;
    RadioButton but2;
    SQLiteAdapter sqLiteAdapter;
    ImageView book_image;
    Button submit_button;
    Book book;
    Barcode barcode;
    Pair pair;


    LinearLayout book_details_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        text_barcode = (EditText) findViewById(R.id.barcode);
        findViewById(R.id.scanbutton).setOnClickListener(this);
        title_edittext=(EditText)findViewById(R.id.title_edittext);
        authors_edittext=(EditText)findViewById(R.id.authors_edittext);
        no_pages_edittext=(EditText)findViewById(R.id.no_pages_edittext);
        book_details_layout=(LinearLayout)findViewById(R.id.book_details_layout);
        book_image=(ImageView)findViewById(R.id.book_image);
        book_details_layout.setVisibility(LinearLayout.INVISIBLE);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        but1 =(RadioButton) findViewById(R.id.radioButton);
        but1.setChecked(true);
        but2 =(RadioButton) findViewById(R.id.radioButton2);
        submit_button=(Button) findViewById(R.id.submit_button);

        final SQLiteAdapter  sqLiteAdapter=new SQLiteAdapter(this);
        sqLiteAdapter.openToWrite();
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("button_click", String.valueOf(radioGroup.getCheckedRadioButtonId()));
                book=new Book(text_barcode.getText().toString(),title_edittext.getText().toString(),authors_edittext.getText().toString(),((radioGroup.getCheckedRadioButtonId()==R.id.radioButton)?true:false),Integer.parseInt(no_pages_edittext.getText().toString()), pair.image);
                long value=sqLiteAdapter.insert(book);
                if (value!=-1){
                    finish();

                }
                else{
//                  Book book1=sqLiteAdapter.find_book(book);
                    Toast.makeText(getApplicationContext(), "Book Already Added", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),DisplayBookInfo.class);
                    Bundle extra = new Bundle();
                    extra.putString("bookinfo",book.isbn);
                    intent.putExtras(extra);
                    startActivity(intent);
                    finish();

                }
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                finishActivity(1);

//                startActivity(intent);
            }
        });


    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton2:
                if (checked)
                    but1.setChecked(false);
                    break;
            case R.id.radioButton:
                if (checked)
                    but2.setChecked(false);

                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scanbutton) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
    }

    public void get_book_details(Activity a, String isbn){
        BookReader bookReader;
        bookReader = new BookReader(a,isbn);
        bookReader.catchAsycResponse= this;
        bookReader.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    statusMessage.setText(R.string.barcode_success);
                    text_barcode.setText(barcode.displayValue);
                    get_book_details(this,barcode.displayValue);
                    Log.d("Barcode", "Barcode read: " + barcode.displayValue);
                } else {
//                    statusMessage.setText(R.string.barcode_failure);
//                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.barcode_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    byte[] toPrimitives(Byte[] oBytes)
    {

        byte[] bytes = new byte[oBytes.length];
        for(int i = 0; i < oBytes.length; i++){
            bytes[i] = oBytes[i];
        }
        return bytes;

    }
    @Override
    public void bookdataavailabe(Pair pair) {
        this.pair=pair;
        title_edittext.setText(pair.output.getOrDefault("title","").toString());
        authors_edittext.setText(pair.output.getOrDefault("authors",""));
        no_pages_edittext.setText(pair.output.getOrDefault("pages","0").toString());
        book_details_layout.setVisibility(LinearLayout.VISIBLE);

        if (pair.image!=null){
        Bitmap bitmap = BitmapFactory.decodeByteArray(pair.image, 0, pair.image.length);
        book_image.setImageBitmap(bitmap);}
//        book=new Book(barcode.displayValue,pair.output.getOrDefault("title",""),pair.output.getOrDefault("authors",""),,Integer.parseInt(pair.output.getOrDefault("pages","0")),pair.image);




    }

}
