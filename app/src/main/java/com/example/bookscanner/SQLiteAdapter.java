package com.example.bookscanner;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.ListView;

public class SQLiteAdapter {

    public static final String MYDATABASE_NAME = "BOOKS_DB";
    public static final String MYDATABASE_TABLE = "BOOKS";
    public static final int MYDATABASE_VERSION = 1;
    public static final String KEY_ID = "_id";
    public static final String ISBN = "isbn";
    public static final String TITLE = "title";
    public static final String AUTHORS = "author";
    public static final String PAGE_COUNT = "pagecount";
    public static final String READ_STATUS = "read_or_not";
    public static final String BOOK_IMAGE="book_image";


    //create table MY_DATABASE (ID integer primary key, Content text not null);
    private static final String SCRIPT_CREATE_DATABASE =
            "create table " + MYDATABASE_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + ISBN + " text not null, "
                    + TITLE+ " text not null, "
                    + AUTHORS+ " text not null, "
                    + PAGE_COUNT+ " integer not null, "
                    + READ_STATUS+ " integer DEFAULT 0, "
                    + BOOK_IMAGE+ " blob); ";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    public SQLiteAdapter(Context c){
        context = c;
    }

    public SQLiteAdapter openToRead() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException {
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insert(Book book){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+MYDATABASE_TABLE+" WHERE "+ISBN+"="+book.isbn, null);
        if (cursor.getCount()>=1){

            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ISBN, book.isbn);
        contentValues.put(TITLE, book.title);
        contentValues.put(AUTHORS, book.author);
        contentValues.put(PAGE_COUNT, book.pages);
        contentValues.put(READ_STATUS ,book.read_or_not);
        contentValues.put(BOOK_IMAGE,book.image);

        return sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
    }
    byte[] toPrimitives(Byte[] oBytes)
    {

        byte[] bytes = new byte[oBytes.length];
        for(int i = 0; i < oBytes.length; i++){
            bytes[i] = oBytes[i];
        }
        return bytes;

    }

    Book find_book(Book book){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+MYDATABASE_TABLE+" WHERE "+ISBN+"="+book.isbn+" limit 1", null);
        return get_book(cursor);
    }
    Book find_book1(String isbn){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+MYDATABASE_TABLE+" WHERE "+ISBN+"="+isbn, null);
        cursor.moveToFirst();
//        Log.d("db_output",""+cursor.getString(1));
        return get_book(cursor);
    }

    public int deleteAll(){
        return sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
    }

    public Books queueAll(){
//        String[] columns = new String[]{KEY_ID, KEY_CONTENT};
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, null,
                null, null, null, null, null);
        int books_count=cursor.getCount();

        Books books=new Books();
        if (cursor.moveToFirst()){

            while (!cursor.isAfterLast()){

                books.addBook(get_book(cursor));
                cursor.moveToNext();
            }
        }


        Log.d("database_log",""+cursor.getCount());
        return books;
    }

    Byte[] toObjects(byte[] bytesPrim) {

        Byte[] bytes = new Byte[bytesPrim.length];
        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; //Autoboxing
        return bytes;

    }
    Book get_book(Cursor cursor){
        Book book =new Book();
        Log.d("db_output",cursor.getString(cursor.getColumnIndex(ISBN)));
        book.isbn=cursor.getString(cursor.getColumnIndex(ISBN));
        book.image=cursor.getBlob(cursor.getColumnIndex(BOOK_IMAGE));
        book.pages=cursor.getInt(cursor.getColumnIndex(PAGE_COUNT));
        book.title=cursor.getString(cursor.getColumnIndex(TITLE));
        book.author=cursor.getString(cursor.getColumnIndex(AUTHORS));
        book.read_or_not=cursor.getInt(cursor.getColumnIndex(READ_STATUS))==1?true:false;
        return book;
    }

//    public String cursorToString(Cursor cursor){
//        String cursorString = "";
//        if (cursor.moveToFirst() ){
//            String[] columnNames = cursor.getColumnNames();
//            for (String name: columnNames)
//                cursorString += String.format("%s ][ ", name);
//            cursorString += "\n";
//            do {
//                for (String name: columnNames) {
//                    cursorString += String.format("%s ][ ",
//                            cursor.getString(cursor.getColumnIndex(name)));
//                }
//                cursorString += "\n";
//            } while (cursor.moveToNext());
//        }
//        return cursorString;
//    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

        }

    }

}