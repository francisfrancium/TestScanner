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


public class ReceiveFragment extends Fragment {

    private EditText editText ;
    private EditText editTextRUs ;
    private EditText editTextRQty ;
    private DatabaseHelper db;
    private TextView textViewBC;
    private TextView textViewPO;
    static String inputed;
    private int[] currentcount = new int[10000];
    private int inputedQty;
    private String inputedUser;

    private String barcode = "<font color = 'red'> BARCODE:</font>" ;
    private String item = "<font color = '#4f4f4f'> ITEM DESCRIPTION:</font>" ;
    private String rcv = "<font color = '#4f4f4f'> PURCHASE ORDER COUNT:</font>" ;

    public ReceiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_receive, container, false);
        textViewBC = inflate.findViewById(R.id.view_recieve);
        textViewPO = inflate.findViewById(R.id.view_POtable);
        editText = inflate.findViewById(R.id.input_receiving);
        editTextRUs = inflate.findViewById(R.id.input_rec_user);
        editTextRQty = inflate.findViewById(R.id.input_rec_qty);
        db = new DatabaseHelper(this.getContext());

        Objects.requireNonNull(getActivity()).setTitle("Receiving");
        inputed = "--------------------";
        editTextRQty.setText("1");

        clearView();


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    inputed = editText.getText().toString();
                    inputedUser = editTextRUs.getText().toString();
                    inputedQty = Integer.parseInt(editTextRQty.getText().toString());

                    UpdateReceivedData();

                }
                editText.setText("");
                return true;
            }
        });


        return inflate;
    }


    @SuppressLint("SetTextI18n")
    private void UpdateReceivedData() {

        Cursor cursor = db.validateReceivedData();

        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            new toastview().toast("Failed! Item not Found!", getActivity()).show();

                clearView();


        } else {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                new toastview().toast("Received Success!", getActivity()).show();
                //db.updateReceivedData(inputed, cursor.getInt(3) + 1);
                textViewBC.setText(Html.fromHtml(barcode));
                textViewBC.append("\n" + cursor.getString(1) + "\n\n");
                textViewBC.append(Html.fromHtml(item));
                textViewBC.append("\n"  + cursor.getString(2)  +"\n\n");
                textViewBC.append(Html.fromHtml(rcv));
                textViewBC.append("\t" + (cursor.getInt(3)) );
                currentcount[cursor.getInt(0)] = currentcount[cursor.getInt(0)]+ inputedQty ;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sdf.format(new Date());

                db.logInsert(inputed, "Received", date);
                textViewPO.setText("RECEIVED QUANTITY:\n\t"   + currentcount[cursor.getInt(0)] + "\n");
                db.updateReceivedData(inputed, currentcount[cursor.getInt(0)], inputedUser);

            }
        }
    }

    private void clearView(){
        textViewBC.setText(Html.fromHtml(barcode));
        textViewBC.append("\n"+inputed+"\n\n");
        textViewBC.append(Html.fromHtml(item));
        textViewBC.append("\n--------------------\n\n");
        textViewBC.append(Html.fromHtml(rcv));
        textViewBC.append("\n--------------------\n");
        textViewPO.setText("RECEIVED QUANTITY:\n--------------------"  );
    }

}