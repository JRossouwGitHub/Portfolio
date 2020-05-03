package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PathwaySelection extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences firstLoad;
    final String disclaimerShownPref = "disclaimerFirstLoad";

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathway_selection);
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

        // Gets shared preference
        firstLoad = PreferenceManager.getDefaultSharedPreferences(this);
        // Second argument to default if the preference can't be found.
        Boolean disclaimerFistLoad = firstLoad.getBoolean(disclaimerShownPref, false);

        // Makes disclaimer popup first.
        checkFirstRun();

        // Image carousell
        tabIndicator = findViewById(R.id.tab_indicator);

        // fill list screen - can be reproduced with loop and DB
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Software Engineering", R.drawable.pathway_software));
        mList.add(new ScreenItem("Network Engineering", R.drawable.pathway_networking));
        mList.add(new ScreenItem("Database Architecture", R.drawable.pathway_databases));
        mList.add(new ScreenItem("Web Development", R.drawable.pathway_media));

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablelayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);


        // tablayout add changes listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == mList.size()-1){
                    Log.i("tab", "Tab Selected: " + tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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

    // Pulled from splash screen, as this and showDisclaimer are together.
    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        int currentVersionCode = BuildConfig.VERSION_CODE;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if (!firstLoad.getBoolean(disclaimerShownPref, false)) {
            showDisclaimer();
        }

    }

    // Disclaimer code pulled from splash screen - For better flow having it on different intent, and before actually using the app
    public void showDisclaimer(){
        final Dialog disclaimerDialog;
        TextView disclaimerTitle, disclaimerMsg;
        Button dismissDis;
        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        disclaimerDialog = new Dialog(this);

        disclaimerDialog.setContentView(R.layout.disclaimer_message);
        dismissDis = disclaimerDialog.findViewById(R.id.btn_i_understand);

        disclaimerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        disclaimerDialog.show();

        final SharedPreferences.Editor editor = firstLoad.edit();
        disclaimerDialog.setCanceledOnTouchOutside(false);

        dismissDis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editor.putBoolean(disclaimerShownPref, true);
                editor.apply();
                editor.commit();
                disclaimerDialog.dismiss();
            }
        });
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

                            Toast.makeText(PathwaySelection.this, "Error: Password Incorrect!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }
