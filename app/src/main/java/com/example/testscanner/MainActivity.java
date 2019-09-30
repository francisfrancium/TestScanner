package com.example.testscanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Handler;
import android.text.InputType;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment currentfragt;

    final int[] x = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






            currentfragt = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            logIn(new CheckFragment());











        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
             if (id == R.id.nav_pricecheck) {
            CheckFragment fragment = new CheckFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        }
       else if (id == R.id.nav_physcount) {

                 CountFragment fragment = new CountFragment();
                 FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container,fragment);
                 fragmentTransaction.commit();

       }


        else if (id == R.id.nav_receive) {

            ReceiveFragment fragment = new ReceiveFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_release) {

                 //Toast.makeText(this, "Under Construction! :(", Toast.LENGTH_SHORT).show();


                 ReleaseFragment fragment = new ReleaseFragment();
                 FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.fragment_container,fragment);
                 fragmentTransaction.commit();


        }
        else if (id == R.id.nav_inven) {

            InventoryFragment fragment = new InventoryFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_settings) {
                 logIn(new SettingsFragment());

        }
        else if (id == R.id.nav_about) {

            this.setTitle("About the Sample Scanner");
                 new toastview().toast("", this).cancel();
                 new toastview().toast("Under Construction!",this).show();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Pass the instance of the targeted fragment
    private void changeFragment(final Fragment targetFragment) {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragTag);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, targetFragment)
                .commit();
    }


    private void logIn(final Fragment targetFragment){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Input Password");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        alert.setView(input);
        alert.setCancelable(false);
        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = String.valueOf(input.getText());
                if (value.equals("123456")){
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();

                    changeFragment(targetFragment);
                }
                else {
                    x[0]++;

                    if (x[0] == 5) {

                        Toast.makeText(getApplicationContext(), "Wrong password entered too many times!", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.exit(0);
                            }
                        }, 800);
                        }
                    else {
                        Toast.makeText(getApplicationContext(), "Wrong Password! " + (5 - x[0]) + " tries left!" , Toast.LENGTH_SHORT).show();
                        logIn(targetFragment);


                    }

                }

            }


        });

        /*
        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(getApplicationContext(), "Bye Bye!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 500);
            }
        });
*/
        alert.show();


    }



}
