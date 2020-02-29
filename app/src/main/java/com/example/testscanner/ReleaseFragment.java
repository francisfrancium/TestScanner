package com.example.testscanner;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class ReleaseFragment extends Fragment {

    private EditText editText ;
    private EditText STETUser ;
    private EditText STETQuant ;
    private EditText STETDest ;
    private DatabaseHelper db;
    private TextView textViewBC;
    private TextView textViewDO;
    static String inputed;
    static Integer quant;
    static String user;
    static String dest;
    private int[] currentcount = new int[100];

    private String barcode = "<font color = 'red'> BARCODE:</font>" ;
    private String item = "<font color = '#4f4f4f'> ITEM DESCRIPTION:</font>" ;
    private String rls = "<font color = '#4f4f4f'> CURRENT QUANTITY TRANSFERED:</font>" ;

    public ReleaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_release, container, false);
        textViewBC = inflate.findViewById(R.id.view_release);
        editText = inflate.findViewById(R.id.input_releasing);
        STETUser = inflate.findViewById(R.id.releasing_user);
        STETQuant = inflate.findViewById(R.id.releasing_qty);
        STETDest = inflate.findViewById(R.id.releasing_destination);

        db = new DatabaseHelper(this.getContext());

        Objects.requireNonNull(getActivity()).setTitle("Store Transfer");
        inputed = "--------------------";

        clearView();


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    inputed = editText.getText().toString();
                    quant = Integer.parseInt(STETQuant.getText().toString());
                    dest = STETDest.getText().toString();

                    UpdateReleaseData();

                }
                editText.setText("");
                return true;
            }
        });


        return inflate;
    }


    @SuppressLint("SetTextI18n")
    private void UpdateReleaseData() {

        Cursor cursor = db.validateReleasedData();


        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {

            new toastview().toast("Failed! Item not Found!", getActivity()).show();

            clearView();


        } else {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                new toastview().toast("Transfer Success!", getActivity()).show();
                //db.updateReleasedData(inputed, cursor.getInt(4) + 1);
                textViewBC.setText(Html.fromHtml(barcode));
                textViewBC.append("\n" + cursor.getString(1) + "\n\n");
                textViewBC.append(Html.fromHtml(item));
                textViewBC.append("\n"  + cursor.getString(2)  +"\n\n");
                textViewBC.append(Html.fromHtml(rls));
                textViewBC.append("\n" + (cursor.getInt(4)) + "\n");
                currentcount[cursor.getInt(0)]++;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sdf.format(new Date());



                db.logInsert(inputed, "Release", date);
                textViewDO.setText("RELEASED QUANTITY:\n\t"   + currentcount[cursor.getInt(0)] + "\n");
                db.updateReleasedData(inputed, currentcount[cursor.getInt(0)]);

            }
        }
    }

    //Test With new account 3



    private void clearView(){
        textViewBC.setText(Html.fromHtml(barcode));
        textViewBC.append("\n"+inputed+"\n\n");
        textViewBC.append(Html.fromHtml(item));
        textViewBC.append("\n--------------------\n\n");
        textViewBC.append(Html.fromHtml(rls));
        textViewBC.append("\n------- -------------\n");
    }

}