package com.example.testscanner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


import static android.content.Context.WIFI_SERVICE;
import static android.content.DialogInterface.*;

public class SettingsFragment extends Fragment {
    private static Socket socket;
    private DatabaseHelper db;



    private String inputtedFileName;




    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_settings, container, false);
        Button UplButton = inflate.findViewById(R.id.upload_button);
        Button POButton = inflate.findViewById(R.id.receive_button);
        db = new DatabaseHelper(this.getContext());
        new toastview();

        Objects.requireNonNull(getActivity()).setTitle("Settings");

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
                final String ip = getIpAddress(Objects.requireNonNull(getContext()));
                dele.setMessage("IP Address:\n" + ip +"\n\n(Use this IP Address in connecting with PC)");
                dialog = dele.show();

               new Thread(new Runnable() {
                   @Override
                  public void run(){

                      try {
                              final ServerSocket server = new ServerSocket(8998);
                              socket = server.accept();

                              Date date = new Date();
                              @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                              String curDate = dateFormat.format(date);

                              File sdcard = Environment.getExternalStorageDirectory();
                              File myFile = new File(sdcard,"TestReceived"+curDate+".csv");

                              final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                              final PrintWriter pw = new PrintWriter(new FileWriter(myFile));
                              try {
                                  String line;
                                  String[] wordsarray;

                                  String reg;
                                  int bc = 0;
                                  int dc = 0;
                                  int pq = 0;
                                  int rq = 0;
                                  int id = 0;

                                  line = br.readLine();

                                  if (line.contains("\t")){
                                      reg = "\t";
                                  }
                                  else if (line.contains(";")){
                                      reg = ";";
                                  }
                                  else{
                                      reg = ",";
                                  }


                                  wordsarray=line.split(reg);

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

                                  pw.println(wordsarray[id]+"\t"+wordsarray[dc]+"\t"+wordsarray[bc]+"\t"+wordsarray[pq]+"\t"+wordsarray[rq]);

                                  for (line = br.readLine(); line != null; line = br.readLine()){
                                      wordsarray=line.split(reg);

                                      pw.println(wordsarray[id]+"\t"+wordsarray[dc]+"\t"+wordsarray[bc]+"\t"+wordsarray[pq]+"\t"+wordsarray[rq]);

                                  }
                                  pw.flush();
                                  pw.close();
                                  br.close();
                                  socket.close();
                                  server.close();

                                  new toastview().toast("File Received.", getActivity()).show();

                              }
                              catch (Exception e){
                                  e.printStackTrace();
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

                   }
               });

                dialog.show();
            }
        });


        return inflate;
    }


    private static String getIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(WIFI_SERVICE);

        assert wifiManager != null;
        String ipAddress = intToInetAddress(wifiManager.getDhcpInfo().ipAddress).toString();
        ipAddress = ipAddress.substring(1);
        return ipAddress;
    }

    private static InetAddress intToInetAddress(int hostAddress) {
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
                inputtedFileName = String.valueOf(input.getText());

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
                            new toastview().toast("Upload Failed.", getActivity()).show();
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




}