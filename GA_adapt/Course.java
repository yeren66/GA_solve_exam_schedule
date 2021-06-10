package GA_adapt;

import java.util.ArrayList;

public class Course {
        ArrayList<Student> students = new ArrayList<>();
        String name;
        int students_number = 0;
        int id;
        Course(String name, int id){
            this.name = name;
            this.id = id;
        }
        Course(int id, int number){
            this.id = id;
            this.students_number = number;
        }
}
