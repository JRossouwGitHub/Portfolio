package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Reference to DBHandler
    DBHandler dbHandler;

/*  all new activities need to be navigation drawer type activity as that is the only current way to get the hamburger menu consistant with the rest of the layout and on every tool bar
*   which will create 4 XML files, actvity_, app_bar_, content_, nav_header_, and a Menu XML called menu_activity_name
*
*  All other XML layouts of this type have the consistency of how we will further design the app until further notice, which have a pretty simple structure not too complex if needing to change anything.
*  Each of the Java Classes will need to have consistant methods - onBackPressed , onCreateOptionsSelected , onCreateOptionsMenu , onNavigationItemSelected as they are the navigation of the app through Hamburger menu(navigation drawer)
*  And 3dot Menu(overflow)
*
*  XML files are a bit bloated in terms of how many there are displaying, which is from each interactable activity creating 5 XML layouts, as we couldn't get it working having one Navigation Drawer and Inheriting from that java class.
*  if at some point we can manage to get it working, either through inheritance or using fragments, maybe we could implement it but isn't required.
*
* */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Action toolbar for the Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Connect to Database
        dbHandler = new DBHandler(this, null, null, 1);
        //Get Shared Preference for 1 time database construction
        //This is to avoid duplication of database values
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DBPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //USE THESE TO RESET DATABASE 1 TIME CONSTRUCTION
        //editor.clear();
        //editor.apply();
        //editor.commit();

        //Check if Database has been created based on Shared Preference
        boolean isCreated = pref.getBoolean("isCreated", false);
        if(!isCreated) {
            //Create Database if it doesn't exist
            createDatabaseFromCSV();
        }

        // Goes to pathway selection
        Intent intent = new Intent(this, PathwaySelection.class);
        startActivity(intent);
    }

    public void createDatabaseFromCSV(){
        //Reference to Shared Preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DBPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //Initialize CSV reader
        InputStream is = getResources().openRawResource(R.raw.dpmdata);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        //Variable for storing reader data
        String line = "";
        //Try/Catch to handing reader
        try {
            //While a line of data exists
            while ((line = reader.readLine()) != null) {
                //Create array by splitting data by "_"
                String[] values = line.split("_");
                //Connect to Database
                dbHandler = new DBHandler(this, null, null, 1);
                //Create Course Objects
                Course module = new Course(values[0], values[1], values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4]), values[5], values[6], Integer.parseInt(values[7]));
                //Insert Course as Module
                dbHandler.insertModule(module);
            }
        }catch (IOException e){
            //Housekeeping
            e.printStackTrace();
        }
        //Set Database preference to TRUE for 1 time creation
        editor.putBoolean("isCreated", true);
        editor.apply();
        editor.commit();
    }

    // On back button function for Quality of Life interactions for navigation drawer, comes default with creating the activity.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Creating the 3 Dot menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Creating the options in the 3 Dot menu
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_About:
                //startSettings();
                Intent intent = new Intent(this, AboutPage.class);
                startActivity(intent);
                return true;

            case R.id.action_visitWebsite:
                //startSettings();
                intent = new Intent(this, WebPageView.class);
                startActivity(intent);
                return true;

                // 3Dot menu for viewing disclaimer
            case R.id.action_disclaimer:
                //startSettings();
                final Dialog disclaimerDialog;
                TextView disclaimerTitle, disclaimerMsg;
                Button dismissDis;

                disclaimerDialog = new Dialog(this);

                disclaimerDialog.setContentView(R.layout.disclaimer_message);
                dismissDis = disclaimerDialog.findViewById(R.id.btn_i_understand);

                disclaimerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                disclaimerDialog.show();

                dismissDis.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        disclaimerDialog.dismiss();
                    }
                });


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Navigation Drawer, Defining the Options that the menu has and the uses of them.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main_menu) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            // Skeleton of the Manager Mode card that requires the password "WinITDMP01"
        } else if (id == R.id.nav_managers) {

            final Dialog loginDialogue;
            TextView loginTitle, loginMsg;
            final String password;

            password ="WinITDMP01";

            final Intent intent = new Intent(getApplicationContext(), ManagerMode.class);
            loginDialogue = new Dialog(this);

            loginDialogue.setContentView(R.layout.manager_mode_login);
            Button dismissDis = loginDialogue.findViewById(R.id.btn_i_understand);

            loginDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loginDialogue.show();

            dismissDis.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    EditText ManagerModePasswordInput = (EditText) loginDialogue.findViewById(R.id.ManagerModePasswordInput);
                    String ManagerInput = ManagerModePasswordInput.getText().toString();

                    if(ManagerInput.equals(password)) {
                        loginDialogue.dismiss();
                        startActivity(intent);
                    }
                    else
                    {
                        ManagerModePasswordInput.setText("");
                        ManagerModePasswordInput.setHint("Manager Password");

                        Toast.makeText(MainActivity.this, "Error: Password Incorrect!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
