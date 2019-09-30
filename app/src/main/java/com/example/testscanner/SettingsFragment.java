package com.example.testscanner;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.WIFI_SERVICE;
import static android.content.DialogInterface.*;

public class SettingsFragment extends Fragment {
    private static Socket socket;
    DatabaseHelper db;
    Button UplButton;

    Button POButton;

    String inputtedFileName;
    String inputtedIPAdd;



    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_settings, container, false);
        UplButton = inflate.findViewById(R.id.upload_button);
        POButton = inflate.findViewById(R.id.receive_button);
        db = new DatabaseHelper(this.getContext());
        new toastview();


        getActivity().setTitle("Settings");

        saveTransLog(getView());

        UplButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dele = new AlertDialog.Builder(getActivity());
                dele.create();
                dele.setCancelable(false);
                dele.setTitle("Upload New Purchase Order File?");
                dele.setMessage("Clicking 'Yes' will delete the existing data. ");
                dele.setNegativeButton("Yes", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /*--------------------------------------------------------------*/
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                        alert.setTitle("Input Password");

                        // Set an EditText view to get user input
                        final EditText input = new EditText(getActivity());
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        alert.setView(input);

                        alert.setNegativeButton("Ok", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = String.valueOf(input.getText());
                                if (value.equals("123456")){
                                    UploadMasterFile();
                                }
                                else {

                                    new toastview().toast("Wrong Password!", getActivity()).show();
                                }

                            }
                        });

                        alert.setPositiveButton("Cancel", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                        alert.show();

                    /*--------------------------------------------------------------*/

                }
                });
                dele.setPositiveButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                dele.show();

            }
        });




        POButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder dele;
                final AlertDialog dialog;

                dele = new AlertDialog.Builder(getActivity());



                dele.create();
               // dele.setCancelable(false);
                dele.setTitle("Waiting for File. . .");
                final String ip = getIpAddress(getContext());
                dele.setMessage("IP Address:\n" + ip);





                dialog = dele.show();


               new Thread(new Runnable() {
                   @Override
                  public void run(){

                      try {


                              final ServerSocket server = new ServerSocket(8998);
                              socket = server.accept();

                              Date date = new Date();
                              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                              String curDate = dateFormat.format(date);

                              File sdcard = Environment.getExternalStorageDirectory();
                              File myFile = new File(sdcard,"TestReceived"+curDate+".csv");

                              final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                              final PrintWriter pw = new PrintWriter(new FileWriter(myFile));
                              try {
                                  String line;
                                  String[] wordsarray;

                                  int bc = 0;
                                  int dc = 0;
                                  int pq = 0;
                                  int rq = 0;
                                  int id = 0;

                                  line = br.readLine();

                                  if (line.contains("\t")){
                                      wordsarray = line.split("\t");

                                  }
                                  else if (line.contains(";")){
                                      wordsarray = line.split(";");
                                  }
                                  else{
                                      wordsarray = line.split(",");

                                  }

                                  for (int x = 0; x<wordsarray.length; x++){

                                      switch(wordsarray[x]){
                                          case "ID": id = x;
                                              break;
                                          case "BARCODE": bc = x;
                                              break;
                                          case "ITEM DESCRIPTION": dc = x;
                                              break;
                                          case "PURCHASE QUANTITY": pq = x;
                                              break;
                                          case "RECEIVED QUANTITY": rq = x;
                                              break;
                                      }
                                  }
                                  pw.println(wordsarray[dc]+"\t"+wordsarray[rq]+"\t"+wordsarray[pq]+"\t"+wordsarray[bc]+"\t"+wordsarray[id]);

                                  for (line = br.readLine(); line != null; line = br.readLine()){

                                      if (line.contains("\t")){
                                          wordsarray = line.split("\t");

                                      }
                                      else if (line.contains(";")){
                                          wordsarray = line.split(";");
                                      }
                                      else{
                                          wordsarray = line.split(",");

                                      }
                                      pw.println(wordsarray[dc]+"\t"+wordsarray[rq]+"\t"+wordsarray[pq]+"\t"+wordsarray[bc]+"\t"+wordsarray[id]);

                                  }
                                  pw.flush();
                                  pw.close();
                                  br.close();
                                  socket.close();
                                  server.close();



                              }
                              catch (Exception e){
                                  System.out.printf("Can't write to file. ", e);
                              }
                              finally {
                                  if (socket != null){
                                      socket.close();
                                      server.close();
                                  }
                                  dialog.dismiss();
                              }


                      }
                      catch (Exception e){
                          e.printStackTrace();
                      }

                  }
               }).start();

               dialog.setOnDismissListener(new OnDismissListener() {
                   @Override
                   public void onDismiss(DialogInterface dialogInterface) {
                       socket = null;
                       new toastview().toast("File Received", getActivity()).show();


                   }
               });




                dialog.show();




            }
        });


        return inflate;
    }




    public static String getIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(WIFI_SERVICE);

        String ipAddress = intToInetAddress(wifiManager.getDhcpInfo().ipAddress).toString();

        ipAddress = ipAddress.substring(1);

        return ipAddress;
    }

    public static InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = { (byte)(0xff & hostAddress),
                (byte)(0xff & (hostAddress >> 8)),
                (byte)(0xff & (hostAddress >> 16)),
                (byte)(0xff & (hostAddress >> 24)) };

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }




    private void UploadMasterFile(){


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Input File Name");
        alert.setMessage("Must include file extension(.csv/.txt)");

        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        alert.setView(input);

        alert.setNegativeButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                db.deleteTable();
                String value = String.valueOf(input.getText());
                inputtedFileName = value;

                try {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File file = new File(sdcard,inputtedFileName);

                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;



                    while ((line = br.readLine()) != null) {
                        String[] str = line.split(",");

                        if(str.length == 3) {

                            String Barcode = str[0];
                            String Description = str[1];
                            Integer Received = Integer.parseInt(str[2]);
                            Integer Released = 0;

                            db.insertData(Barcode,Description,Received,Released,0);

                            new toastview().toast("New Purchase Order File Uploaded!", getActivity()).show();
                        }
                        else {
                            new toastview().toast("Upload Failed!", getActivity()).show();
                        }

                    }
                    br.close() ;
                }
                catch (IOException e) {
                    new toastview().toast("Upload Failed!", getActivity()).show();

                    e.printStackTrace();
                }

            }
        });

        alert.setPositiveButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        alert.show();
    }


    private void sendToServer(){


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here


            Socket sock;
            try {

                new toastview().toast("Searching...", getActivity()).show();
                sock = new Socket(inputtedIPAdd, 8998);

                new toastview().toast("Connecting...", getActivity()).show();

                DataOutputStream DOS = new DataOutputStream(sock.getOutputStream());
                DOS.writeUTF("PO");



                // sendfile
                File sdcard = Environment.getExternalStorageDirectory();
                File myFile = new File(sdcard,"testmasterfile.csv");
                byte [] mybytearray  = new byte [(int)myFile.length()];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                OutputStream os = sock.getOutputStream();

                new toastview().toast("Sending...!", getActivity()).show();
                System.out.println("Sending...");


                os.write(mybytearray,0,mybytearray.length);
                os.flush();

                sock.close();
                new toastview().toast("File Sent!", getActivity()).show();

                System.out.println("File Sent!");

            } catch (UnknownHostException e) {
                new toastview().toast("Unknown Host!", getActivity()).show();

                Log.d("UHE", "UNKNOWN HOST!!!!");
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                new toastview().toast("Error in File!", getActivity()).show();
                Log.d("IOE", "I O ERROR!!!!1");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch(Exception e) {
                new toastview().toast("Configuration Error!", getActivity()).show();

                e.printStackTrace();
            }

        }


    }


    public void saveTransLog (View view){

        final String FILENAME = "TestMasterFile.csv";

        Cursor cursor = db.viewData();

        String entry = "";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),FILENAME);


        if (!(cursor.getCount() ==0)) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                entry = entry + "ID" + "\t" + "BARCODE" + "\t" + "ITEM DESCRIPTION" + "\t" + "PURCHASED QUANTITY" + "\n";


                while (cursor.moveToNext()) {
                    entry = entry + cursor.getString(0) + "\t" + cursor.getString(1) + "\t" + cursor.getString(2) + "\t" + cursor.getString(3) + "\n";
                }
                fos.write(entry.getBytes());
                fos.close();

            } catch (Exception e){
                e.printStackTrace();
            }


        }
        else {

            new toastview().toast("Empty!", getActivity()).show();


        }

    }




}