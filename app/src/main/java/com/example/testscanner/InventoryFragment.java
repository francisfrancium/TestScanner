package com.example.testscanner;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;



public class InventoryFragment extends Fragment {

    private DatabaseHelper db;
    private ListView userlist;

    private ArrayList<String> listItem;

    private String inputtedIPAdd;



    public InventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View inflate = inflater.inflate(R.layout.fragment_inventory, container, false);


      requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

        db = new DatabaseHelper(this.getContext());
        listItem = new ArrayList<>();
        userlist = inflate.findViewById(R.id.inventory);
        Button sendinven = inflate.findViewById(R.id.sendinven_button);

        Objects.requireNonNull(getActivity()).setTitle("Inventory");






       sendinven.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final AlertDialog.Builder dele = new AlertDialog.Builder(getActivity());
               dele.create();
               dele.setCancelable(false);
               dele.setTitle("Send File To Server?");
               dele.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                       AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                       alert.setTitle("Input Configuration");

                       LinearLayout layout = new LinearLayout(getActivity());
                       layout.setOrientation(LinearLayout.VERTICAL);

                       // Set an EditText view to get user input
                       final EditText input = new EditText(getActivity());
                       final EditText adminun = new EditText(getActivity());
                       final EditText adminpass = new EditText(getActivity());
                       input.setHint("IP Address");
                       input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                       layout.addView(input);
                       adminun.setHint("Username");
                       adminun.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                       layout.addView(adminun);
                       adminpass.setHint("Password");
                       adminpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                       layout.addView(adminpass);

                       alert.setView(layout);

                       alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               String value = String.valueOf(input.getText());
                               String checkun = String.valueOf(adminun.getText());
                               String checkpass = String.valueOf(adminpass.getText());

                               if (checkun.equals("admin") && checkpass.equals("123456")) {
                                   inputtedIPAdd = value;
                                   sendGRToServer();
                               }
                               else {
                                   dialog.dismiss();
                                   new toastview().toast("Wrong Username/Password!", getActivity()).show();
                               }

                           }
                       });

                       alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               dialog.dismiss();
                           }
                       });
                       alert.show();
                   }
               });
               dele.setPositiveButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               });
               dele.show();

           }
       });


        // Inflate the layout for this fragment
        return inflate;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewData();
        savePOOnClick();
        saveDeliOnClick();
    }


    private void viewData() {
        Cursor cursor = db.viewData();

        if (!(cursor.getCount() ==0)) {
            listItem.add("ID\t\tItem\t\t\t\t\t\tReceived\t\tReleased");
            while (cursor.moveToNext()){
                listItem.add(cursor.getString(0) + "\t\t" + cursor.getString(1) + "\t\t\t\t\t"  + cursor.getString(4) + "\t\t\t\t\t\t\t\t" + cursor.getString(5) );
            }

            ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()), android.R.layout.simple_list_item_1, listItem);
            userlist.setAdapter(adapter);

            new toastview().toast("Data Loaded!", getActivity()).show();

        }
        else {
            new toastview().toast("No Data to Show! :(", getActivity()).show();
        }

    }

    private void savePOOnClick(){

        final String FILENAME = "TestScannerGRReport.txt";

        Cursor cursor = db.viewData();

        StringBuilder entry = new StringBuilder();


        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),FILENAME);


        if (!(cursor.getCount() ==0)) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);

                    entry.append("ID").append(",").append("BARCODE").append(",").append("ITEM DESCRIPTION").append(",").append("PURCHASE QUANTITY").append(",").append("RECEIVED QUANTITY").append(",").append("VARIANCE").append("\n");

                    while (cursor.moveToNext()) {
                        entry = entry + cursor.getString(0) + "\t" + cursor.getString(1) + "\t" + cursor.getString(2) + "\t" + cursor.getString(3) + "\t" + cursor.getString(4) + "\t" + (cursor.getInt(4) - cursor.getInt(3)) + "\n";
                    }
                    fos.write(entry.toString().getBytes());
                    fos.close();

                } catch (Exception e){
                    e.printStackTrace();
                }

            new toastview().toast("Data Automatically Saved", getActivity()).show();


        }
        else {
            new toastview().toast("Can't Print! :(", getActivity()).show();


        }

    }

    private void saveDeliOnClick(){

        final String FILENAME = "TestScannerDeliReport.csv";

        Cursor cursor = db.viewData();

        StringBuilder entry = new StringBuilder();


        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),FILENAME);


        if (!(cursor.getCount() ==0)) {
            try {
                FileOutputStream fos = new FileOutputStream(file);

                entry.append("ID").append("\t").append("BARCODE").append("\t").append("ITEM DESCRIPTION").append("\t").append("DELIVERY QUANTITY").append("\t").append("RELEASED QUANTITY").append("\t").append("VARIANCE").append("\n");

                while (cursor.moveToNext()) {
                    entry.append(cursor.getString(0)).append("\t").append(cursor.getString(1)).append("\t").append(cursor.getString(2)).append("\t").append("Test").append("\t").append(cursor.getString(5)).append("\t").append(cursor.getInt(5) - cursor.getInt(5)).append("\n");
                }
                fos.write(entry.toString().getBytes());
                fos.close();

            } catch (Exception e){
                e.printStackTrace();
            }

            new toastview().toast("Data Automatically Saved", getActivity()).show();


        }
        else {
            new toastview().toast("Can't Print! :(", getActivity()).show();

        }

    }



    private void sendGRToServer(){


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
       if (SDK_INT > 8)
       {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                 .permitAll().build();
          StrictMode.setThreadPolicy(policy);


          //your codes here

            Socket sock ;

            try {
                new toastview().toast("Searching...", getActivity()).show();
                sock = new Socket(inputtedIPAdd, 8998);

                new toastview().toast("Connecting...", getActivity()).show();

                System.out.println("Connecting...");


                DataOutputStream DOS = new DataOutputStream(sock.getOutputStream());
                DOS.writeUTF("GR");

                // sendfile
                File sdcard = Environment.getExternalStorageDirectory();
                File myFile = new File(sdcard,"TestScannerGRReport.txt");


                byte [] mybytearray  = new byte [(int)myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int read = bis.read(mybytearray, 0, mybytearray.length);
                OutputStream os = sock.getOutputStream();
                new toastview().toast("Sending...", getActivity()).show();

                System.out.println("Sending...");
                os.write(mybytearray,0,mybytearray.length);
                os.flush();

                sock.close();
                new toastview().toast("File Sent!", getActivity()).show();
                System.out.println("File Sent!");

            }
            catch (UnknownHostException e){
                new toastview().toast("Unknown Host!", getActivity()).show();
                e.printStackTrace();

            }

            catch (IOException e) {
                new toastview().toast("Error in File!", getActivity()).show();
                e.printStackTrace();

            }

            catch(Exception e) {
                new toastview().toast("Configuration Error!", getActivity()).show();
                e.printStackTrace();

            }




        }


    }



}
