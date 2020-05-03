package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class EditModulesPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Declare Objects
    GridView gv1;
    GridView gv2;
    GridView gv3;
    GridView gv4;
    GridView gv5;
    GridView gv6;
    ImageView pathwayIMG;
    Button addModulesBtn;

    //Get reference to Database
    DBHandler dbHandler;

    //For unlocking feature
    ArrayAdapter<String> gridViewArrayAdapter1;
    ArrayAdapter<String> gridViewArrayAdapter2;
    ArrayAdapter<String> gridViewArrayAdapter3;
    ArrayAdapter<String> gridViewArrayAdapter4;
    ArrayAdapter<String> gridViewArrayAdapter5;
    ArrayAdapter<String> gridViewArrayAdapter6;
    String pathwayExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_modules_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Connect to Database
        dbHandler = new DBHandler(this, null, null, 1);

        //Initialize Objects
        gv1 = (GridView) findViewById(R.id.semester1Gv);
        gv2 = (GridView) findViewById(R.id.semester2Gv);
        gv3 = (GridView) findViewById(R.id.semester3Gv);
        gv4 = (GridView) findViewById(R.id.semester4Gv);
        gv5 = (GridView) findViewById(R.id.semester5Gv);
        gv6 = (GridView) findViewById(R.id.semester6Gv);
        pathwayIMG = (ImageView) findViewById(R.id.pathwayIV);
        addModulesBtn = (Button) findViewById(R.id.saveBtn);

        //Get Extras from Intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        pathwayExtra = (String) extras.get("pathway");
        //Set ImageView based on Extra
        setImage(pathwayExtra);
        //Set Module Cards based on Extra
        setModuleCards(pathwayExtra);

        addModulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create custom popup for Cards and Popup
                final LayoutInflater inflater = LayoutInflater.from(EditModulesPage.this);
                final View editModulePopupView = inflater.inflate(R.layout.edit_module_popup, null, false);

                //Get reference to Object in module_edit_popup.xml
                final TextView mEditTitle = (TextView) editModulePopupView.findViewById(R.id.codeTv);
                final EditText mName = (EditText) editModulePopupView.findViewById(R.id.mNameInput);
                final EditText mCode = (EditText) editModulePopupView.findViewById(R.id.editText2);
                final EditText mLevel = (EditText) editModulePopupView.findViewById(R.id.editText3);
                final EditText mCredits = (EditText) editModulePopupView.findViewById(R.id.editText4);
                final EditText mPrereq = (EditText) editModulePopupView.findViewById(R.id.editText5);
                final EditText mAim = (EditText) editModulePopupView.findViewById(R.id.editText6);
                final EditText mSemester = (EditText) editModulePopupView.findViewById(R.id.editText7);
                Button mUpdateBtn = (Button) editModulePopupView.findViewById(R.id.updateModulesBtn);

                mEditTitle.setText("Add Module");
                mUpdateBtn.setText("Add Module");

                //Create Dialog
                final Dialog dialog = new Dialog(EditModulesPage.this);
                dialog.setContentView(editModulePopupView);
                dialog.show();

                mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            Course module = new Course(pathwayExtra,
                                    mCode.getText().toString(),
                                    mName.getText().toString(),
                                    Integer.parseInt(mLevel.getText().toString()),
                                    Integer.parseInt(mCredits.getText().toString()),
                                    mPrereq.getText().toString(),
                                    mAim.getText().toString(),
                                    Integer.parseInt(mSemester.getText().toString()));
                            dbHandler.insertModule(module);
                            Toast.makeText(EditModulesPage.this, "Module: " + mCode.getText().toString() + " has been added!", Toast.LENGTH_LONG).show();
                            resetModules(EditModulesPage.this);
                            dialog.dismiss();
                        }catch (Exception e){
                            Toast.makeText(EditModulesPage.this, "Error Adding Module: " + mCode.getText().toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                });

                //resetModules(EditModulesPage.this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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

                        Toast.makeText(EditModulesPage.this, "Error: Password Incorrect!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setImage(String pathwayExtra){
        if(pathwayExtra.equals("Software Engineering")){
            pathwayIMG.setImageResource(R.drawable.pathway_software);
        }

        if(pathwayExtra.equals("Web Development")){
            pathwayIMG.setImageResource(R.drawable.pathway_media);
        }

        if(pathwayExtra.equals("Network Engineering")){
            pathwayIMG.setImageResource(R.drawable.pathway_networking);
        }

        if(pathwayExtra.equals("Database Architecture")){
            pathwayIMG.setImageResource(R.drawable.pathway_databases);
        }
    }

    public void setModuleCards(String pathwayExtra){
        String[] semester1Codes = dbHandler.getModuleSemester(pathwayExtra, 1);
        gridViewArrayAdapter1 = new EditModulesAdapter(this, semester1Codes, pathwayExtra);
        gv1.setAdapter(gridViewArrayAdapter1);

        String[] semester2Codes = dbHandler.getModuleSemester(pathwayExtra, 2);
        gridViewArrayAdapter2 = new EditModulesAdapter(this, semester2Codes, pathwayExtra);
        gv2.setAdapter(gridViewArrayAdapter2);

        String[] semester3Codes = dbHandler.getModuleSemester(pathwayExtra, 3);
        gridViewArrayAdapter3 = new EditModulesAdapter(this, semester3Codes, pathwayExtra);
        gv3.setAdapter(gridViewArrayAdapter3);

        String[] semester4Codes = dbHandler.getModuleSemester(pathwayExtra, 4);
        gridViewArrayAdapter4 = new EditModulesAdapter(this, semester4Codes, pathwayExtra);
        gv4.setAdapter(gridViewArrayAdapter4);

        String[] semester5Codes = dbHandler.getModuleSemester(pathwayExtra, 5);
        gridViewArrayAdapter5 = new EditModulesAdapter(this, semester5Codes, pathwayExtra);
        gv5.setAdapter(gridViewArrayAdapter5);

        String[] semester6Codes = dbHandler.getModuleSemester(pathwayExtra, 6);
        gridViewArrayAdapter6 = new EditModulesAdapter(this, semester6Codes, pathwayExtra);
        gv6.setAdapter(gridViewArrayAdapter6);

        //Set GridView based on height
        setGridViewHeightBasedOnChildren(gv1, 2);
        setGridViewHeightBasedOnChildren(gv2, 2);
        setGridViewHeightBasedOnChildren(gv3, 2);
        setGridViewHeightBasedOnChildren(gv4, 2);
        setGridViewHeightBasedOnChildren(gv5, 2);
        setGridViewHeightBasedOnChildren(gv6, 2);
    }

    //Code for setting GridView height
    //Source: https://stackoverflow.com/a/22555947
    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        double x = 1;
        if( items > columns ){
            x = ((double) items ) /columns;
            rows =(int) Math.ceil(x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }

    //Invalidate view in order to refresh
    public void resetModules(Context context){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
