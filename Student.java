package studio.crunchyiee.degreeprogrammemapper;

public class Student {

    //Initialize variables
    private int _studentID;
    private String _studentName;
    private String _degree;
    private String _pathway;

    public Student(){}

    //Constructor for making Student
    public Student(int studentID, String studentName, String degree, String pathway){
        this._studentID = studentID;
        this._studentName = studentName;
        this._degree = degree;
        this._pathway = pathway;
    }

    //Getter and Setter
    //Start
    public int get_studentID() {
        return _studentID;
    }

    public void set_studentID(int _studentID) {
        this._studentID = _studentID;
    }

    public String get_studentName() {
        return _studentName;
    }

    public void set_studentName(String _studentName) {
        this._studentName = _studentName;
    }

    public String get_degree() {
        return _degree;
    }

    public void set_degree(String _degree) {
        this._degree = _degree;
    }

    public String get_pathway() {
        return _pathway;
    }

    public void set_pathway(String _pathway) {
        this._pathway = _pathway;
    }
    //End
}
