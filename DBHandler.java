package studio.crunchyiee.degreeprogrammemapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    Context context;

    //Database Housekeeping
    private static final int DATABASE_VERSION = 32;

    private static final String DATABASE_NAME = "dpm_db.db";

    //Declare Variables for use in creating Database
    public static final String TABLE_COURSES = "_courses";
    public static final String COLUMN_NO = "_no";
    public static final String COLUMN_PATHWAY = "_pathway";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "_name";
    public static final String COLUMN_LEVEL = "_level";
    public static final String COLUMN_CREDITS = "_credits";
    public static final String COLUMN_PREREQ = "_prereq";
    public static final String COLUMN_DESC = "_description";
    public static final String COLUMN_SEMESTER = "_semester";

    public static final String TABLE_STUDENT = "student";
    public static final String COLUMN_DEGREE = "_degree";

    //Create Course and Student tables
    public static final String COURSE_TABLE = "CREATE TABLE " + TABLE_COURSES + "(" +
            COLUMN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PATHWAY + " TEXT, " +
            COLUMN_ID + " TEXT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_LEVEL + " INTEGER, " +
            COLUMN_CREDITS + " INTEGER DEFAULT 15, " +
            COLUMN_PREREQ + " TEXT, " +
            COLUMN_DESC + " TEXT, " +
            COLUMN_SEMESTER + " INTEGER " +
            ");";

    public static final String STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DEGREE + " TEXT, " +
            COLUMN_PATHWAY + " TEXT " +
            ");";


    //Database Required Methods
    //Start
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void createTables(SQLiteDatabase db) {
        db.execSQL(COURSE_TABLE);
        db.execSQL(STUDENT_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }
    //End

    //Inset Modules
    public void insertModule(Course module) {
        //Create content values variables
        ContentValues module_data = new ContentValues();
        //Set content values based on Module
        module_data.put(COLUMN_PATHWAY, module.get_pathway());
        module_data.put(COLUMN_ID, module.get_id());
        module_data.put(COLUMN_NAME, module.get_name());
        module_data.put(COLUMN_LEVEL, module.get_level());
        module_data.put(COLUMN_CREDITS, module.get_credits());
        module_data.put(COLUMN_PREREQ, module.get_prereq());
        module_data.put(COLUMN_DESC, module.get_desc());
        module_data.put(COLUMN_SEMESTER, module.get_semester());
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        //Insert Module
        db.insert(TABLE_COURSES, null, module_data);
        //Close connection to Database, housekeeping
        db.close();
    }

    //Remove Module
    public void removeModule(String pathway, String moduleCode){
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        //Remove module
        db.execSQL("DELETE FROM " + TABLE_COURSES + " WHERE " + COLUMN_PATHWAY + "=\"" + pathway + "\" AND " + COLUMN_ID + "=\"" + moduleCode + "\";");

    }

    //Update Module
    public void updateModule(String original, String pathway, String name, String code, int level, int credits, String prereq, String aim, int semester){
        Log.i("DBHandler", "Starting Update");
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        Log.i("DBHandler", "Connected to DB");
        //Create content values variables
        ContentValues module_data = new ContentValues();
        Log.i("DBHandler", "Initialize Content Values");
        //Set content values based on Module
        module_data.put(COLUMN_PATHWAY, pathway);
        module_data.put(COLUMN_ID, code);
        module_data.put(COLUMN_NAME, name);
        module_data.put(COLUMN_LEVEL, level);
        module_data.put(COLUMN_CREDITS, credits);
        module_data.put(COLUMN_PREREQ, prereq);
        module_data.put(COLUMN_DESC, aim);
        module_data.put(COLUMN_SEMESTER, semester);
        Log.i("DBHandler", "Context Values Set");
        //Update tables
        db.update(TABLE_COURSES, module_data, COLUMN_PATHWAY + "=\"" + pathway + "\" AND " + COLUMN_ID + "=\"" + original + "\";", null);
        Log.i("DBHandler", "Running Query");
        //db.setTransactionSuccessful();
        Log.i("DBHandler", "Success");
    }


    //Get Module Information
    public String[] getModuleInfo(String module) {
        //Declare variables to store Module info
        String courseCode = "";
        String courseName = "";
        int courseLevel = 0;
        int courseCredits = 0;
        String coursePrereq = "";
        String courseDesc = "";
        int courseSemester = 0;
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        //Create Query
        String query = "SELECT * FROM " + TABLE_COURSES + " WHERE 1";
        //Create Cursor to get pointer to each Row, and move to first Row
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        //While data exists
        while (!c.isAfterLast()) {
            //If Module exists in Database, set variables to return later
            if (c.getString(c.getColumnIndex("_id")).equals(module)) {
                courseCode = c.getString(c.getColumnIndex("_id"));
                courseName = c.getString(c.getColumnIndex("_name"));
                courseLevel = c.getInt(c.getColumnIndex("_level"));
                courseCredits = c.getInt(c.getColumnIndex("_credits"));
                coursePrereq = c.getString(c.getColumnIndex("_prereq"));
                courseDesc = c.getString(c.getColumnIndex("_description"));
                courseSemester = c.getInt(c.getColumnIndex("_semester"));
            }
            c.moveToNext();
        }
        //Housekeeping
        c.close();
        db.close();
        //Create array to store variables
        String[] results = new String[7];
        results[0] = courseCode;
        results[1] = courseName;
        results[2] = String.valueOf(courseLevel);
        results[3] = String.valueOf(courseCredits);
        results[4] = coursePrereq;
        results[5] = courseDesc;
        results[6] = String.valueOf(courseSemester);
        //Return Module information
        return results;
    }

    //Get Module by Semester
    public String[] getModuleSemester(String pathway, int semester) {
        //Create variables for Module code
        String courseCode = "";
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        //Create Query
        String query = "SELECT * FROM " + TABLE_COURSES + " WHERE 1";
        //Create Cursor to get pointer to each Row, and move to first Row
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        //Set counter for Array usage
        int count = 0;
        //While data exists
        while (!c.isAfterLast()) {
            //If Pathway and Semester exist, increase count for number of Modules in that Semester
            if (c.getString(c.getColumnIndex("_pathway")).equals(pathway) && c.getInt(c.getColumnIndex("_semester")) == semester) {
                count++;
            }
            c.moveToNext();
        }
        //Housekeeping
        c.close();
        //Recreate Query
        c = db.rawQuery(query, null);
        c.moveToFirst();
        //Create Array to store Modules, based on Semester
        String[] results = new String[count];
        //Reset counter for Array usage
        count = 0;
        //While data exists
        while (!c.isAfterLast()) {
            //If Pathway and Semester exist, add Modules in that Semester to array
            if (c.getString(c.getColumnIndex("_pathway")).equals(pathway) && c.getInt(c.getColumnIndex("_semester")) == semester) {
                courseCode = c.getString(c.getColumnIndex("_id"));
                results[count] = courseCode;
                count++;
            }
            c.moveToNext();
        }
        //Housekeeping
        c.close();
        db.close();
        //Return Array
        return results;
    }
    public String[] getStudent(int id){
        Log.i("StudentDB", "Starting Search");
        String s_ID = "";
        String s_Name = "";
        String s_Degree = "";
        String s_Pathway = "";
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        //Create Query
        String query = "SELECT * FROM " + TABLE_STUDENT + " WHERE 1";
        //Create Cursor to get pointer to each Row, and move to first Row
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        //While data exists
        while (!c.isAfterLast()) {
            //If Pathway and Semester exist, add Modules in that Semester to array
            if (c.getInt(c.getColumnIndex("_id")) == id) {
                Log.i("StudentDB", "Match Found!");
                s_ID = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                s_Name = String.valueOf(c.getString(c.getColumnIndex("_name")));
                s_Degree = String.valueOf(c.getString(c.getColumnIndex("_degree")));
                s_Pathway = String.valueOf(c.getString(c.getColumnIndex("_pathway")));
            }
            c.moveToNext();
        }
        //Housekeeping
        c.close();
        db.close();

        Log.i("StudentDB", "Returning Results");
        String[] results = new String[4];
        results[0] = s_ID;
        results[1] = s_Name;
        results[2] = s_Degree;
        results[3] = s_Pathway;

        return results;
    }

    //Remove Student
    public void removeStudent(int s_ID){
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        //Remove Student
        db.execSQL("DELETE FROM " + TABLE_STUDENT + " WHERE " + COLUMN_ID + "=\"" + s_ID + "\";");

    }

    //Insert Student
    public void insertStudent(Student student) {
        Log.i("StudentDB", "Starting Insert");
        //Create content values variables
        ContentValues student_data = new ContentValues();
        //Set content values based on Students
        student_data.put(COLUMN_ID, student.get_studentID());
        student_data.put(COLUMN_NAME, student.get_studentName());
        student_data.put(COLUMN_DEGREE, student.get_degree());
        student_data.put(COLUMN_PATHWAY, student.get_pathway());
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        //If Student exists, update otherwise insert
        try{
            Log.i("StudentDB", "Student Inserted");
            db.insert(TABLE_STUDENT, null, student_data);
        }catch(Exception e){
            Log.i("StudentDB", "Error Inserting Student");
        }
        Log.i("StudentDB", "Done");
        //Close connection to Database, housekeeping
        db.close();
    }

    // Update Student
    public void updateStudent(int originalID, int s_ID, String s_Name, String s_Degree, String s_Pathway){
        Log.i("DBHandler", "Starting Update");
        //Get reference to Database
        SQLiteDatabase db = getWritableDatabase();
        Log.i("DBHandler", "Connected to DB");
        //Create content values variables
        ContentValues student_data = new ContentValues();
        Log.i("DBHandler", "Initialize Content Values");
        //Set content values based on Module
        student_data.put(COLUMN_ID, s_ID);
        student_data.put(COLUMN_NAME, s_Name);
        student_data.put(COLUMN_DEGREE, s_Degree);
        student_data.put(COLUMN_PATHWAY, s_Pathway);
        Log.i("DBHandler", "Context Values Set");
        //Update tables
        db.update(TABLE_STUDENT, student_data, COLUMN_ID + "=\"" + originalID + "\";", null);
        Log.i("DBHandler", "Running Query");
        //db.setTransactionSuccessful();
        Log.i("DBHandler", "Success");
    }

    public Cursor viewStudent(){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STUDENT;
        Cursor myCursor = db.rawQuery(query, null);

        return myCursor;

    }
}