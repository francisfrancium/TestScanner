package com.example.testscanner;



        import android.annotation.SuppressLint;
        import android.database.Cursor;
        import android.os.Bundle;


        import androidx.fragment.app.Fragment;
        import android.text.Html;
        import android.text.method.ScrollingMovementMethod;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.inputmethod.EditorInfo;
        import android.widget.Button;

        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.ScrollView;
        import android.widget.Switch;
        import android.widget.TextView;

        import com.google.android.material.snackbar.Snackbar;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.HashSet;
        import java.util.Objects;
        import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    private EditText editTextBarc ;
    private EditText editTextLoc;
    private EditText editTextUs;
    private EditText editTextQty;

    private ScrollView scrll;

    private TextView textViewA;

    private  Button startcount;
    private  Button endcount;
    private DatabaseHelper db;

    public static   String inputed;

    private int[] currentcount = new int[10000];
    private int inputedQty;
    public static   String inputedLocation;
    private   String inputedSerial;
    private String inputedUser;



    private Set<String> setchecker;

    private  String ser = "<font color = 'red'> SERIAL NO.:</font>" ;
    private String barcode = "<font color = 'red'> BARCODE:</font>" ;
    private String item = "<font color = '#4f4f4f'> ITEM DESCRIPTION:</font>" ;
    private String rcv = "<font color = '#4f4f4f'> CURRENT IN ITEM MASTER:</font>" ;



    public CountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_count, container, false);
        editTextBarc = inflate.findViewById(R.id.input_count);
        editTextLoc = inflate.findViewById(R.id.input_count_location);
        editTextUs = inflate.findViewById(R.id.input_count_user);
        editTextQty = inflate.findViewById(R.id.input_qty);
        db = new DatabaseHelper(this.getContext());


        scrll = inflate.findViewById(R.id.scrll);
        startcount = inflate.findViewById(R.id.startcount);
        endcount = inflate.findViewById(R.id.endcount);
        textViewA = inflate.findViewById(R.id.view_count);


        Objects.requireNonNull(getActivity()).setTitle("Physical Count");

        inputed = "--------------------";
        inputedLocation = "--------------------";
        inputedSerial = "--------------------";


        clearView();
        editTextBarc.setEnabled(false);
        editTextQty.setEnabled(false);
        endcount.setEnabled(false);
        barcodeonly();



        editTextLoc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    inputedLocation = editTextLoc.getText().toString();
                    editTextUs.requestFocus();



                }
                return true;
            }
        });


        startcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextLoc.getText().toString().matches("")
                        && editTextUs.getText().toString().matches("")) {



                    Snackbar.make(Objects.requireNonNull(getActivity().getCurrentFocus()), "Input Location and Username!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    editTextLoc.requestFocus();

                }
                else if (editTextLoc.getText().toString().matches("")) {
                    Snackbar.make(Objects.requireNonNull(getActivity().getCurrentFocus()), "Input Item Location!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    editTextLoc.requestFocus();

                }
                else if (editTextUs.getText().toString().matches("")) {
                    Snackbar.make(Objects.requireNonNull(getActivity().getCurrentFocus()), "Input Username!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    editTextUs.requestFocus();

                }
                else {
                    editTextUs.setEnabled(false);
                    editTextBarc.requestFocus();
                    editTextBarc.setEnabled(true);
                    editTextQty.setEnabled(true);
                    editTextLoc.setEnabled(false);
                    startcount.setEnabled(false);
                    endcount.setEnabled(true);
                    editTextQty.setText("1");

                }


            }
        });

        endcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                scrll.fullScroll(ScrollView.FOCUS_UP);


                startcount.setEnabled(true);
                endcount.setEnabled(false);

                editTextUs.setEnabled(true);
                editTextBarc.setEnabled(false);
                editTextLoc.setEnabled(true);


                editTextLoc.requestFocus();

                editTextLoc.setText("");
                editTextBarc.setText("");
                inputed = "";
                inputedSerial = "";
                inputedLocation = "";

                clearView();
  /*

                String joined = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    joined = String.join("\n",setchecker);
                }

                textViewA.setText(joined);
*/


            }
        });

        return inflate;
    }


    private void UpdateReceivedData() {

        Cursor cursor = db.validateTaggedData();

        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            new toastview().toast("Failed! Item not Found!", getActivity()).show();

            clearView();


        } else {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                new toastview().toast("Received Success!", getActivity()).show();
                //db.updateReceivedData(inputed, cursor.getInt(3) + 1);
                textViewA.setText(Html.fromHtml(barcode));
                textViewA.append("\n" + cursor.getString(1) + "\n\n");
                textViewA.append(Html.fromHtml(item));
                textViewA.append("\n"  + cursor.getString(2)  +"\n\n");
                textViewA.append(Html.fromHtml(rcv));
                textViewA.append("\n" + (cursor.getInt(3)) + "\n");
                currentcount[cursor.getInt(0)] = currentcount[cursor.getInt(0)] + inputedQty;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String date = sdf.format(new Date());

                db.logInsert(inputed, "Received", date);
                textViewA.append("RECEIVED QUANTITY:\n\t"   + currentcount[cursor.getInt(0)] + "\n");
                db.updateMasterData(inputed, currentcount[cursor.getInt(0)], inputedUser, inputedLocation);

            }
        }
    }






    private void clearView(){

        String barcode = "<font color = 'red'> BARCODE:</font>";
        textViewA.setText(Html.fromHtml(barcode));
        textViewA.append("\n"+inputed+"\n\n");
        String item = "<font color = '#4f4f4f'> ITEM DESCRIPTION:</font>";
        textViewA.append(Html.fromHtml(item));
        textViewA.append("\n--------------------\n\n");
        String loc = "<font color = '#4f4f4f'> LOCATION:</font>";
        textViewA.append(Html.fromHtml(loc));
        textViewA.append("\n"+inputedLocation+"\n\n");


    }

    private void barcodeonly(){

        editTextBarc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    inputed = editTextBarc.getText().toString();
                    inputedLocation = editTextLoc.getText().toString();
                    inputedUser = editTextUs.getText().toString();
                    inputedQty = Integer.parseInt(editTextQty.getText().toString());

                    UpdateReceivedData();
                    //clearView();

                    editTextBarc.setText("");
                    editTextBarc.requestFocus();
                }
                return true;
            }
        });

    }

}