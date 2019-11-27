package com.example.testscanner;



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

        import java.util.HashSet;
        import java.util.Objects;
        import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    private EditText editTextBarc ;
    private EditText editTextLoc;
    private EditText editTextSer;
    private EditText editTextUs;

    private ScrollView scrll;

    private TextView textViewA;
    private TextView textViewB;

    private  Button startcount;
    private  Button endcount;
    private   Button newitem;

    private   Switch serialopt;

    private   String inputed;
    private   String inputedLocation;
    private   String inputedSerial;
    private String inputedUser;



    private Set<String> setchecker;

    private  String ser = "<font color = 'red'> SERIAL NO.:</font>" ;


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
        editTextSer = inflate.findViewById(R.id.input_count_serialno);
        editTextUs = inflate.findViewById(R.id.input_count_user);


        scrll = inflate.findViewById(R.id.scrll);

        serialopt = inflate.findViewById(R.id.serialoption);

        newitem = inflate.findViewById(R.id.newitem);
        startcount = inflate.findViewById(R.id.startcount);
        endcount = inflate.findViewById(R.id.endcount);
        textViewA = inflate.findViewById(R.id.view_count);
        textViewB = inflate.findViewById(R.id.view_serial);
        textViewB.setMovementMethod(new ScrollingMovementMethod());


        Objects.requireNonNull(getActivity()).setTitle("Physical Count");

        inputed = "--------------------";
        inputedLocation = "--------------------";
        inputedSerial = "--------------------";


        clearView();
        editTextBarc.setEnabled(false);
        editTextSer.setEnabled(false);
        endcount.setEnabled(false);
        newitem.setEnabled(false);

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

        newitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                newitem.setEnabled(false);
                editTextSer.setEnabled(false);
                editTextBarc.setText("");
                editTextBarc.setEnabled(true);
                editTextBarc.requestFocus();
                textViewB.setText(Html.fromHtml(ser));
            }
        });


        serialopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){


                    editTextSer.setText("");
                    askserialnumber();
                } else {
                    editTextSer.setText("N/A");
                    barcodeonly();
                }
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
                    editTextLoc.setEnabled(false);
                    startcount.setEnabled(false);
                    endcount.setEnabled(true);
                    serialopt.setEnabled(false);

                }


            }
        });

        endcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                scrll.fullScroll(ScrollView.FOCUS_UP);

                newitem.setEnabled(false);

                startcount.setEnabled(true);
                endcount.setEnabled(false);

                editTextUs.setEnabled(true);
                editTextBarc.setEnabled(false);
                editTextSer.setEnabled(false);
                editTextLoc.setEnabled(true);

                serialopt.setEnabled(true);

                editTextLoc.requestFocus();

                textViewB.setText("");
                editTextLoc.setText("");
                editTextBarc.setText("");
                editTextSer.setText("");
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


    private void askserialnumber(){
        editTextBarc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    textViewB.setText(Html.fromHtml(ser));
                    editTextBarc.clearFocus();
                    editTextSer.setEnabled(true);
                    serialopt.setEnabled(false);
                    newitem.setEnabled(true);
                    editTextBarc.setEnabled(false);


                    editTextSer.requestFocus();
                    editTextSer.setCursorVisible(true);





                    inputedLocation = editTextLoc.getText().toString();
                    inputed = editTextBarc.getText().toString();
                    setchecker = new HashSet<>();



                }
                return true;
            }
        });
        editTextSer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    inputed = editTextBarc.getText().toString();
                    inputedLocation = editTextLoc.getText().toString();
                    inputedSerial = editTextSer.getText().toString();
                    inputedUser = editTextUs.getText().toString();
                    clearView();
                    editTextSer.setText("");
                    editTextSer.requestFocus();


                    if (setchecker.contains(inputedSerial)){
                        textViewB.append("\n");
                        textViewB.append(Html.fromHtml("<font color = 'red'>" + inputedSerial +"</font>"));
                        Snackbar.make(Objects.requireNonNull(getActivity().getCurrentFocus()), "Duplicate Serial No.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else{
                        setchecker.add(inputedSerial);
                        textViewB.append("\n"+inputedSerial);
                    }


                }
                return true;
            }
        });

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
                    inputedSerial = editTextSer.getText().toString();

                    clearView();

                    editTextBarc.setText("");
                    editTextBarc.requestFocus();
                }
                return true;
            }
        });





    }

}