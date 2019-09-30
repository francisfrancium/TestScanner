package com.example.testscanner;


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


public class ReleaseFragment extends Fragment {

    EditText editText ;
    DatabaseHelper db;
    TextView textViewBC;
    TextView textViewDO;
    public static String inputed;
    int currentcount[] = new int[100];

    String barcode = "<font color = 'red'> BARCODE:</font>" ;
    String item = "<font color = '#4f4f4f'> ITEM DESCRIPTION:</font>" ;
    String rls = "<font color = '#4f4f4f'> CURRENT QUANTITY FOR DELIVERY:</font>" ;

    public ReleaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_release, container, false);
        textViewBC = (TextView) inflate.findViewById(R.id.view_release);
        textViewDO = (TextView) inflate.findViewById(R.id.view_release_qty);
        editText = (EditText) inflate.findViewById(R.id.input_releasing);
        db = new DatabaseHelper(this.getContext());

        getActivity().setTitle("Delivery");
        inputed = "--------------------";

        clearView();


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    inputed = editText.getText().toString();

                    UpdateReleaseData();

                }
                editText.setText("");
                return true;
            }
        });


        return inflate;
    }


    private void UpdateReleaseData() {

        Cursor cursor = db.validateReleasedData();

        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            new toastview().toast("Failed! Item not Found!", getActivity()).show();

            clearView();


        } else {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                new toastview().toast("Release Success!", getActivity()).show();
                //db.updateReleasedData(inputed, cursor.getInt(4) + 1);
                textViewBC.setText(Html.fromHtml(barcode));
                textViewBC.append("\n" + cursor.getString(1) + "\n\n");
                textViewBC.append(Html.fromHtml(item));
                textViewBC.append("\n"  + cursor.getString(2)  +"\n\n");
                textViewBC.append(Html.fromHtml(rls));
                textViewBC.append("\n" + (cursor.getInt(4)) + "\n");
                currentcount[cursor.getInt(0)]++;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sdf.format(new Date());

                db.logInsert(inputed, "Delivery", date);
                textViewDO.setText("RELEASED QUANTITY:\n\t"   + currentcount[cursor.getInt(0)] + "\n");
                db.updateReleasedData(inputed, currentcount[cursor.getInt(0)]);

            }
        }
    }

    private void clearView(){
        textViewBC.setText(Html.fromHtml(barcode));
        textViewBC.append("\n"+inputed+"\n\n");
        textViewBC.append(Html.fromHtml(item));
        textViewBC.append("\n--------------------\n\n");
        textViewBC.append(Html.fromHtml(rls));
        textViewBC.append("\n--------------------\n");
        textViewDO.setText("DELIVERY QUANTITY:\n--------------------"  );
    }

}