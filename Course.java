package studio.crunchyiee.degreeprogrammemapper;

public class Course {

    //Set Constructor variables for inserting an Object into database in MainActivity
    private String _pathway;
    private String _id;
    private String _name;
    private int _level;
    private int _credits;
    private String _prereq;
    private String _desc;
    private int _semester;

    //Empty Constructor for eas of use
    public Course(){}

    //Public Constructor to create Object
    public Course(String pathway, String id, String name, int level, int credits, String prereq, String desc, int semeseter){
        this._pathway = pathway;
        this._id = id;
        this._name = name;
        this._level = level;
        this._credits = credits;
        this._prereq = prereq;
        this._desc = desc;
        this._semester = semeseter;
    }


    //Getter and Setter for Contructor
    //START
    public String get_pathway() {
        return _pathway;
    }

    public void set_pathway(String _pathway) {
        this._pathway = _pathway;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_level() {
        return _level;
    }

    public void set_level(int _level) {
        this._level = _level;
    }

    public int get_credits() {
        return _credits;
    }

    public void set_credits(int _credits) {
        this._credits = _credits;
    }

    public String get_prereq() {
        return _prereq;
    }

    public void set_prereq(String _prereq) {
        this._prereq = _prereq;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public int get_semester() {
        return _semester;
    }

    public void set_semester(int _semester) {
        this._semester = _semester;
    }
    //END
}
