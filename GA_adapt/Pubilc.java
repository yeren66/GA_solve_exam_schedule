package GA_adapt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Public {
    public static int group_size = 50;        // 初始种群大小
    public static int initial_limit = 100;
    public static int initial_limit_2 = 1400;
    public static int chang_ci = 56;          // 场次
    public static int courses = 1242;          // 课程数（后面更新）
    public static int students = 11714;        // 学生人数（模拟用）
    public static int replace = 40;            // 交叉算子替换个数
    public static int iterations = 2000;      // 最大迭代次数
    public static double variation_probability = 0; // 变异几率
    public static int max_interval = 4;
    public static int random_seed = new Random().nextInt();

    public static int[][] conflict = new int[courses][courses];   // 带冲突的课程图

    // initialize method
    static void padding(ArrayList<Student> students_ex){
        for(Student a : students_ex){
            for(int i = 0; i < a.courses.size(); i ++){
                for(int j = i + 1; j < a.courses.size(); j ++){
                    conflict[a.courses.get(i).id][a.courses.get(j).id] ++;
                    conflict[a.courses.get(j).id][a.courses.get(i).id] ++;
                }
            }
        }
    }

    static void save(String path) {
        File outFile = new File(path);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            for(int i = 0; i < Public.courses; i ++){
                for(int j = 0; j < Public.courses; j ++){
                    writer.write(conflict[i][j] + ",");
                }
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            System.out.println("读写文件出错。");
        }
    }
    // can be updated
    public static int limit_courses = 30;
    public static int max_courses_of_student = 6;
}

class Publictest{
    public static void main(String[] args){
        long Time_1 = System.currentTimeMillis();
        String path = "D:\\java\\21data-group.csv";
        ArrayList<Course> courses_ex = new ArrayList<>();
        ArrayList<Student> student_ex = new ArrayList<>();
        HashMap<Integer, String> student_table = new HashMap<>();
        HashMap<Integer, String> course_table = new HashMap<>();

        Read.readCSV(path, courses_ex, student_ex, student_table, course_table);

        Public.padding(student_ex);
        for(int i = 0; i < Public.courses; i ++){
            for(int j = 0; j < Public.courses; j ++){
                System.out.print(Public.conflict[i][j] + " ");
            }
            System.out.println();
        }
        long Time_2 = System.currentTimeMillis();
        System.out.println("total cost: " + (Time_2 - Time_1) + "ms");

        Public.save("D://java//conflict.csv");
    }
}
