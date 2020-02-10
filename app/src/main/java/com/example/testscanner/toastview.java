package com.example.testscanner;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

class toastview {

    Toast toast(String message, Context current){


        final Toast toast = Toast.makeText(current, message, Toast.LENGTH_SHORT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);

        return toast;
    }



}
