package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditStudents extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DBHandler db;

    ListView studentsList;
    ArrayList<String> listItem;
    ArrayList<Integer> listID;
    ArrayAdapter myAdapter;
    Button addStudent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_students);
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

        db = new DBHandler(this, null, null, 1);
        listItem = new ArrayList<>();
        listID = new ArrayList<>();
        studentsList = findViewById(R.id.studentList);


        //Inflate Dialog with save_popup.xml layout
        LayoutInflater inflater = LayoutInflater.from(this);
        final View saveView = inflater.inflate(R.layout.manager_save_popup, null, false);

        //Reference to Objects in custom layout
        Button saveBtn = (Button) saveView.findViewById(R.id.savePathwayBtn);
        Button cancelBtn = (Button) saveView.findViewById(R.id.cancelBtn);
        final EditText studentID = (EditText) saveView.findViewById(R.id.studentIDEt);
        final EditText studentName = (EditText) saveView.findViewById(R.id.studentNameEt);
        final EditText degree = (EditText) saveView.findViewById(R.id.degreeTv);
        final EditText pathway = (EditText) saveView.findViewById(R.id.pathwayTv);

        //Dialog Popup for saving Pathway
        final Dialog dialog = new Dialog(this);


        listStudents();

        addStudent = findViewById(R.id.addStudentBtn);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(saveView);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        //Cancel button on Dialog
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close Dialog
                dialog.dismiss();
            }
        });

        //Save button on Dialog
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Try/Catch to handle inserting Student
                try{
                    //Set variables based on Dialog
                    int s_ID = Integer.parseInt(studentID.getText().toString());
                    String s_name = studentName.getText().toString();
                    String s_degree = degree.getText().toString();
                    String s_pathway = pathway.getText().toString();
                    //Create Student Object based on Dialog
                    Student student = new Student(s_ID, s_name, s_degree, s_pathway);
                    //Inset Student, give feedback and close Dialog
                    db.insertStudent(student);
                    Toast.makeText(EditStudents.this, "Pathway Saved - ID: " + s_ID, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);

                }catch (Exception e){
                    //Housekeeping
                    Toast.makeText(EditStudents.this, "Error Saving Student!", Toast.LENGTH_LONG).show();

                }
                //Reset popup
                studentID.setText("");
                studentID.setHint("Student ID");
                studentName.setText("");
                studentName.setHint("Student Name");
            }
        });
    }


    private void listStudents() {
        Cursor myCursor = db.viewStudent();

        if(myCursor.getCount() > 0){
            while(myCursor.moveToNext()){
                listItem.add(myCursor.getString(0));
                // listID.add(myCursor.getInt(0));
            }

            myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            studentsList.setAdapter(myAdapter);

            studentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(),StudentDetails.class);
                    intent.putExtra("StudentID",studentsList.getItemAtPosition(position).toString());
                    startActivity(intent);
                }
            });
        }
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
        // getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> studentsLists = new ArrayList<>();

                for (String student : listItem){
                    if (student.toLowerCase().contains(newText.toLowerCase())){
                        studentsLists.add(student);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditStudents.this,android.R.layout.simple_expandable_list_item_1, studentsLists);
                studentsList.setAdapter(adapter);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
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
