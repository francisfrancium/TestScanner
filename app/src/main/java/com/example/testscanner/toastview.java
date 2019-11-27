package com.example.testscanner;

import android.content.Context;
import android.widget.Toast;

class toastview {

    Toast toast(String message, Context current){


        Toast toast = Toast.makeText(current, message, Toast.LENGTH_SHORT);

        return toast;
    }



}
