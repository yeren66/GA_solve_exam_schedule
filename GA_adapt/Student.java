package GA_adapt;

import java.util.ArrayList;

public class Student {
    ArrayList<Course> courses = new ArrayList<>();
    String name;
    int id;
    Student(String name, int id){
        this.name = name;
        this.id = id;
    }
}
