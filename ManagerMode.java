package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerMode extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LayoutInflater inflater;
    View popUpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_mode);
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

    }

    // On click methods
    public void onClickEditStudents(View view)
    {
        Intent intent = new Intent(this, EditStudents.class);
        startActivity(intent);
    }

    // Don't need to touch the layout for this for a while as the module pages still need to be setup, on click cards for the ability to edit the csv file.
    public void onClickEditModules(View view)
    {
        Intent intent = new Intent(this, EditPathwaySelection.class);
        startActivity(intent);
    }

    // Need to set up the Database tables for this, then have the about page pull from the database, also have the clickable cards.
    public void onClickEditAppDetails(View view)
    {
        inflater = LayoutInflater.from(ManagerMode.this);
        popUpView = inflater.inflate(R.layout.edit_app_popup, null, false);

        final EditText app_in =(EditText) popUpView.findViewById(R.id.ManagerModePasswordInput);
        final EditText api_in =(EditText) popUpView.findViewById(R.id.apiVersionInput);
        final EditText os_in =(EditText) popUpView.findViewById(R.id.osInput);

        Button saveButton =(Button) popUpView.findViewById(R.id.btn_i_understand);

        SharedPreferences appVersion = view.getContext().getSharedPreferences("appVersion", Context.MODE_PRIVATE);
        SharedPreferences apiVersion = view.getContext().getSharedPreferences("apiVersion", Context.MODE_PRIVATE);
        SharedPreferences osVersion = view.getContext().getSharedPreferences("os", Context.MODE_PRIVATE);

        final SharedPreferences.Editor appEditor = appVersion.edit();
        final SharedPreferences.Editor apiEditor = apiVersion.edit();
        final SharedPreferences.Editor osEditor = osVersion.edit();

        final Dialog dialog = new Dialog(view.getContext());

        dialog.setCanceledOnTouchOutside(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    appEditor.putString("appVersion", app_in.getText().toString());
                    appEditor.apply();
                    appEditor.commit();

                    apiEditor.putString("apiVersion", api_in.getText().toString());
                    apiEditor.apply();
                    apiEditor.commit();

                    osEditor.putString("os", os_in.getText().toString());
                    osEditor.apply();
                    osEditor.commit();

                    Toast.makeText(ManagerMode.this, "App Details Saved!", Toast.LENGTH_SHORT).show();
                    popUpView.setVisibility(View.GONE);
                    dialog.dismiss();
                }
                catch(Exception e)
                {
                    Toast.makeText(ManagerMode.this, "Error: Details not saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setContentView(popUpView);
        dialog.show();
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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
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
            final Intent intent = new Intent(getApplicationContext(), ManagerMode.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
