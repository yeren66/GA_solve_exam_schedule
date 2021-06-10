package GA_adapt;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Quick_read {
    public static int[][] readCSV(String path) {
        /**
         *   这个函数用来读取已存储的 conflict table
         */
        File inFile = new File(path);
        int i = 0;
        int[][] conflict = new int[Public.courses][Public.courses];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            boolean sign = false;       //用来跳过第一行的名称
            while (reader.ready()) {
                String line = reader.readLine();
                StringTokenizer st = new StringTokenizer(line, ",");

                if (st.hasMoreTokens() && sign) {
                    for (int j = 0; j < Public.courses; j++) {
                        int input = Integer.parseInt(st.nextToken().trim());
                        conflict[i][j] = input;
                    }
                } else {
                    sign = true;
                }
                i++;
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return conflict;
    }

    public static ArrayList<Course> readCsv(String path){
        /**
         *   这个函数用来读取已存储的 students_number
         */

        File inFile = new File(path);
        ArrayList<Course> courses_ex = new ArrayList<>();
        int i = 0;

        try{
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            while(reader.ready()){
                String line = reader.readLine();
                StringTokenizer st = new StringTokenizer(line, ",");

                if(st.hasMoreTokens()){
                    int input = Integer.parseInt(st.nextToken().trim());
                    Course a = new Course(i ++, input);
                    courses_ex.add(a);
                }
            }
            reader.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return courses_ex;
    }
}

class Quick_read_Test{
    public static void main(String[] args){
        String savePath = "D:\\java\\students_number.csv";
        String path = "D:\\java\\conflict.csv";
        int[][] conflict = new int[Public.courses][Public.courses];
        ArrayList<Course> courses_ex = new ArrayList<>();

        conflict = Quick_read.readCSV(path);
        courses_ex = Quick_read.readCsv(savePath);

        for(int i = 0; i < 100; i ++){
            for(int j = 0; j < 100; j ++){
                System.out.print(conflict[i][j] + " ");
            }
            System.out.println();
        }
        for(int i = 0; i < 100; i ++){
            System.out.println(courses_ex.get(i).students_number);
        }
    }
}
