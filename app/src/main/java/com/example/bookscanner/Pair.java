package com.example.bookscanner;

import java.util.HashMap;

public class Pair {
    HashMap<String,String> output;
    byte[] image;
    Pair(HashMap<String,String> output,byte[] image){
        this.output=output;
        this.image=image;
    }
}
