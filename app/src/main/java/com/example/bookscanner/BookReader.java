package com.example.bookscanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.util.ArrayUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class BookReader extends AsyncTask<JSONObject,Integer, Pair> {

    CatchAsycResponse catchAsycResponse=null;

    HashMap<String,String> output;
    ProgressDialog progressDialog;
    String isbn_number;

    public BookReader(Activity a,String ISBN_number){
        this.isbn_number=ISBN_number;
        progressDialog = new ProgressDialog(a);
        progressDialog.setCancelable ( false ) ;
        progressDialog.setMessage ( "Retrieving data..." ) ;
        progressDialog.setTitle ( "Please wait" ) ;
        progressDialog.setIndeterminate ( true ) ;
        output=new HashMap<>();
}

    @Override
    protected Pair doInBackground(JSONObject... jsonObjects) {
        String api_url="https://www.googleapis.com/books/v1/volumes?q=isbn:"+isbn_number;
        try{

            URL url = new URL(String.format(api_url));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            byte[] out1=null;
            JSONObject data = new JSONObject(json.toString());

            JSONArray item_list=(data.has("items")?data.getJSONArray("items"):null);
            if (item_list!=null){
            JSONObject item_book=item_list.getJSONObject(0);

            JSONObject item_book_author_title=item_book.getJSONObject("volumeInfo");
            if(item_book.has("volumeInfo")){
            output.put("title",item_book_author_title.has("title")?item_book_author_title.getString("title"):"");
            JSONArray author=(item_book_author_title.has("authors")?item_book_author_title.getJSONArray("authors"):null);

            String authors1="";
            if(author!=null){
            for (int i=0;i<author.length();i++){
                if(i!=0){
                    authors1+=",";
                }
                authors1+=author.get(i);
            }}
            output.put("authors",authors1);
            output.put("pages",item_book_author_title.has("pageCount")?item_book_author_title.getString("pageCount"):"");

            JSONObject imageLinks= (item_book_author_title.has("imageLinks")?item_book_author_title.getJSONObject("imageLinks"):null);


            if (imageLinks!=null){
            output.put("image_link", imageLinks.getString("thumbnail"));
            Log.d("image_link",imageLinks.getString("thumbnail"));

            out1= recoverImageFromUrl( imageLinks.getString("thumbnail"));}}}

            Pair pair=new Pair(output,out1);





            return pair;
    } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Byte[] toObjects(byte[] bytesPrim) {

        Byte[] bytes = new Byte[bytesPrim.length];
        int i = 0;
        for (byte b : bytesPrim) bytes[i++] = b; //Autoboxing
        return bytes;

    }

    public byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }


    byte[] download_image(String url){
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.connect();

// Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }
        return  null;
    }
    protected void onPostExecute(Pair data){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss ( ) ;
        }
        super.onPostExecute(data);

        Log.d("output_string", String.valueOf(data));

        catchAsycResponse.bookdataavailabe(data);


    }
}
