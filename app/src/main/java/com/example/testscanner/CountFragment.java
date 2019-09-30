package com.example.testscanner;


        import android.graphics.Color;
        import android.graphics.drawable.Drawable;
        import android.os.Build;
        import android.os.Bundle;

        import androidx.core.app.NotificationCompat;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.text.Html;
        import android.text.method.ScrollingMovementMethod;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.inputmethod.EditorInfo;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.ScrollView;
        import android.widget.Switch;
        import android.widget.TextView;

        import com.google.android.material.snackbar.Snackbar;

        import org.w3c.dom.Text;

        import java.lang.reflect.Array;
        import java.nio.channels.Channel;
        import java.util.HashSet;
        import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    EditText editTextBarc ;
    EditText editTextLoc;
    EditText editTextSer;
    EditText editTextUs;

    ScrollView scrll;

    TextView textViewA;
    TextView textViewB;

    Button startcount;
    Button endcount;
    Button newitem;

    Switch serialopt;

    String inputed;
    String inputedLocation;
    String inputedSerial;
    String inputedUser;



    Set<String> setchecker;
    int currentcount= 0;

    String barcode = "<font color = 'red'> BARCODE:</font>" ;
    String ser = "<font color = 'red'> SERIAL NO.:</font>" ;
    String item = "<font color = '#4f4f4f'> ITEM DESCRIPTION:</font>" ;
    String loc = "<font color = '#4f4f4f'> LOCATION:</font>" ;


    public CountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_count, container, false);
        editTextBarc = (EditText) inflate.findViewById(R.id.input_count);
        editTextLoc = (EditText) inflate.findViewById(R.id.input_count_location);
        editTextSer = (EditText) inflate.findViewById(R.id.input_count_serialno);
        editTextUs = (EditText) inflate.findViewById(R.id.input_count_user);


        scrll = (ScrollView) inflate.findViewById(R.id.scrll);

        serialopt = (Switch) inflate.findViewById(R.id.serialoption);

        newitem = (Button) inflate.findViewById(R.id.newitem);
        startcount = (Button) inflate.findViewById(R.id.startcount);
        endcount = (Button) inflate.findViewById(R.id.endcount);
        textViewA = (TextView) inflate.findViewById(R.id.view_count);
        textViewB = (TextView) inflate.findViewById(R.id.view_serial);
        textViewB.setMovementMethod(new ScrollingMovementMethod());


        getActivity().setTitle("Physical Count");

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
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

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



                    Snackbar.make(getActivity().getCurrentFocus(), "Input Location and Username!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    editTextLoc.requestFocus();

                }
                else if (editTextLoc.getText().toString().matches("")) {
                    Snackbar.make(getActivity().getCurrentFocus(), "Input Item Location!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    editTextLoc.requestFocus();

                }
                else if (editTextUs.getText().toString().matches("")) {
                    Snackbar.make(getActivity().getCurrentFocus(), "Input Username!", Snackbar.LENGTH_LONG)
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

        textViewA.setText(Html.fromHtml(barcode));
        textViewA.append("\n"+inputed+"\n\n");
        textViewA.append(Html.fromHtml(item));
        textViewA.append("\n--------------------\n\n");
        textViewA.append(Html.fromHtml(loc));
        textViewA.append("\n"+inputedLocation+"\n\n");


    }


    private void askserialnumber(){
        editTextBarc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    textViewB.setText(Html.fromHtml(ser));

                    editTextBarc.setEnabled(false);
                    editTextSer.setEnabled(true);
                    editTextSer.requestFocus();
                    serialopt.setEnabled(false);
                    newitem.setEnabled(true);

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
                        Snackbar.make(getActivity().getCurrentFocus(), "Duplicate Serial No.", Snackbar.LENGTH_LONG)
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