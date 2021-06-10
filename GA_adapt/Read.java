package GA_adapt;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

class Read {
    public static void readCSV(String path, ArrayList<Course> courses_ex, ArrayList<Student> student_ex, HashMap<Integer, String> student_table, HashMap<Integer, String> course_table)
    {
        /**
         *  这个函数用来读取csv文件，主要返回所有课程实例的集合，和所有学生实例的集合；
         *  path是文件路径，courses_ex是所有课程实例的集合，student_ex是所有学生实例的集合，
         *  student_table是学生的键值对应表，course_table是课程的键值对应表
         *  该函数需运行大约100分钟
         */
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
                        course_table.put(course_id, course_name);
                        Course new_one = new Course(course_name, course_id);
                        course_id ++;
                        courses_ex.add(new_one);
                    }
                    if(!student_pool.contains(student_name)){         // 将不在学生池里的学生加入学生池
                        student_pool.add(student_name);
                        student_table.put(student_id, student_name);
                        Student new_one_ = new Student(student_name, student_id);
                        student_id ++;
                        student_ex.add(new_one_);
                    }

                    for(Course a : courses_ex){                     // 将选课信息加入对应的课程实例
                        for(Student b : student_ex){
                            if(a.name.equals(course_name) && b.name.equals(student_name)){
                                a.students.add(b);
                                b.courses.add(a);
                                System.out.println("one get ready " + c);
                                c ++;
                                break;
                            }
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
}

