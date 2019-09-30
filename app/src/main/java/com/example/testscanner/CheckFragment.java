package com.example.testscanner;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;


public class CheckFragment extends Fragment {

    EditText editText ;
    DatabaseHelper db;
    TextView textViewID;

    public static String inputed;


    String ID = "<font color = '#4f4f4f'> ID NUMBER:</font>" ;
    String barcode = "<font color = 'red'> BARCODE:</font>" ;
    String item = "<font color = '#4f4f4f'> ITEM DESCRIPTION:</font>" ;
    String rcv = "<font color = '#4f4f4f'> PURCHASE ORDER QTY.:</font>" ;
    String rls = "<font color = '#4f4f4f'> RECEIVED COUNT:</font>" ;

    public CheckFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_check, container, false);
        editText = (EditText) inflate.findViewById(R.id.input_checking);
        textViewID = (TextView) inflate.findViewById(R.id.view_checking);
        db = new DatabaseHelper(this.getContext());

        getActivity().setTitle("Item Check");

        inputed = "--------------------";
        editText.requestFocus();

        clearView();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    inputed = editText.getText().toString();

                    searchData();

                }
                editText.setText("");
                return true;
            }
        });

        return inflate;

    }


    private void searchData() {

        Cursor cursor = db.searchData();

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0) {
            new toastview().toast("No Data to Show! :(", getActivity()).show();
            clearView();
        }
        else {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {

                new toastview().toast("Item Found!", getActivity()).show();
                // The Cursor is now set to the right position

                textViewID.setText(Html.fromHtml(ID));
                textViewID.append("\n" + cursor.getString(0) + "\n\n");
                textViewID.append(Html.fromHtml(barcode));
                textViewID.append("\n" + cursor.getString(1) + "\n\n");
                textViewID.append(Html.fromHtml(item));
                textViewID.append("\n" + cursor.getString(2) + "\n\n");
                textViewID.append(Html.fromHtml(rcv));
                textViewID.append("\n" + cursor.getString(3) + "\n");
                textViewID.append(Html.fromHtml(rls));
                textViewID.append("\n" + cursor.getString(4));
            }
        }

    }

    public void clearView(){
        textViewID.setText(Html.fromHtml(ID));
        textViewID.append("\n--------------------\n\n");
        textViewID.append(Html.fromHtml(barcode));
        textViewID.append("\n"+inputed+"\n\n");
        textViewID.append(Html.fromHtml(item));
        textViewID.append("\n--------------------\n\n");
        textViewID.append(Html.fromHtml(rcv));
        textViewID.append("\n--------------------\n");
        textViewID.append(Html.fromHtml(rls));
        textViewID.append("\n--------------------");
    }


}
