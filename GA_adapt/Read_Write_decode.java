package GA_adapt;

/**
 *   之前的Read.class没有保存每个课程的学生数，后来发现评估函数需要这个东西，于是补加了一项读写操作
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Read_Write_decode {

    ArrayList<Course> courses_ex = new ArrayList<>();

    public void readCSV(String path)
    {
        File inFile = new File(path);
        HashSet<String> student_pool = new HashSet<>();
        HashSet<String> course_pool = new HashSet<>();
        int student_id = 0;
        int course_id = 0;
        int c = 0;


        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            boolean sign = false;       //用来跳过第一行的名称
            while(reader.ready())
            {
                String line = reader.readLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                String class_name, course_name, student_name;

                if (st.hasMoreTokens() && sign)
                {
                    class_name = String.valueOf(st.nextToken().trim());
                    course_name = String.valueOf(st.nextToken().trim());
                    student_name = String.valueOf(st.nextToken().trim());

                    if(!course_pool.contains(course_name)){           // 将不在课程池里的课程实例化并加入课程池
                        course_pool.add(course_name);
                        Course new_one = new Course(course_name, course_id);
                        course_id ++;
                        courses_ex.add(new_one);
                    }

                    for(Course a : courses_ex){                     // 将选课信息加入对应的课程实例
                        if(a.name.equals(course_name)){
                            a.students_number ++;
                            break;
                        }
                    }

                }
                else
                {
                    sign = true;
                }
            }
            reader.close();

        }
        catch (FileNotFoundException e)
        {

            e.printStackTrace();
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
    }
    void save(String path) {
        File outFile = new File(path);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            writer.write("course_id,course_name,");
            writer.newLine();
            for(int i = 0; i < Public.courses; i ++){
                writer.write(courses_ex.get(i).id + ",");
                writer.write(courses_ex.get(i).name + ",");
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            System.out.println("读写文件出错。");
        }
    }
}

class Read_Write_encodeTest{
    public static void main(String[] args){
        Read_Write_decode a = new Read_Write_decode();
        String path = "D:\\java\\21data-group.csv";
        String savePath = "D:\\java\\decode.csv";
        a.readCSV(path);
        a.save(savePath);
    }
}
