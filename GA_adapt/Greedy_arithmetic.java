package GA_adapt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Greedy_arithmetic {
    Boolean round = true;
    Random r = new Random();
    double p;

    Greedy_arithmetic(double p){
        this.p = p;
    }

    public Scheme production(ArrayList<Course> courses_ex){
        ArrayList<Course>[] schedule = new ArrayList[Public.chang_ci];
        ArrayList<Integer> course_number = new ArrayList<>();
        for(int i = 0; i < Public.courses; i ++){
            course_number.add(i);
        }
        for(int i = 0; i < Public.chang_ci; i ++){
            schedule[i] = new ArrayList<>();
        } // initial
        round = true;
        schedule = round_over(courses_ex, course_number, schedule, 0);
        return new Scheme(schedule);
    }
    private ArrayList<Course>[] round_over(ArrayList<Course> courses_ex, ArrayList<Integer> course_number, ArrayList<Course>[] schedule, int time){
        if(time > 18){
            System.out.println("limit is too tight, try again!");
            System.out.println(course_number);
            round = false;
            return schedule;
        }
        ArrayList<Integer> rest = new ArrayList<>();
        for(Integer i : course_number){
            for(int j = 0; j < Public.chang_ci; j ++){
                int t = 0;
                int number = 0;
                for(Course a : schedule[j]){
                    number += a.students_number;
                    if(Public.conflict[a.id][i] != 0 || schedule[j].size() > Public.initial_limit || number > Public.initial_limit_2){
                        t = 1;
                        break;
                    }
                }
                if(t == 1 || (double)r.nextInt(100)/100 < p - (double)time/20){
                    if(j == Public.chang_ci - 1){
                        rest.add(i);
                    }
                }
                else{
                    schedule[j].add(new Course(i, courses_ex.get(i).students_number));
                    break;
                }
            }
        }
        if(rest.size() != 0)
            return round_over(courses_ex, rest, schedule, time + 1);
        else
            return schedule;
    }

    public static void save_rate(String path, double[] p, double[] rate, double[] score, double[] time_cost) {
        File outFile = new File(path);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            writer.write("p" + ",");
            writer.write("rate" + ",");
            writer.write("score" + ",");
            writer.write("time_cost" + ",");
            writer.newLine();
            for(int i = 0; i < 20; i ++){
                writer.write(p[i] + ",");
                writer.write(rate[i] + ",");
                writer.write(score[i] + ",");
                writer.write(time_cost[i] + ",");
                writer.newLine();
            }
            writer.close();
        }catch (IOException ex){
            System.out.println("读写文件出错。");
        }
    }
}


class Greedy_arithmeticTest{
    public static void main (String[] args) throws CloneNotSupportedException {
        double[] p_ = new double[20];
        double[] rate_ = new double[20];
        double[] score_ = new double[20];
        double[] time_cost = new double[20];
        String savePath = "D:\\java\\result_encode_";
        String savePath2 = "D:\\java\\result_decode_";
        for(int r = 1; r < 4; r ++) {
            Greedy_arithmetic initial = new Greedy_arithmetic(0.6);
            ArrayList<Course> courses_ex = Quick_read.readCsv("D:\\java\\students_number.csv");
            Public.conflict = Quick_read.readCSV("D:\\java\\conflict.csv");
            int t = 0;
            double x = 10000;
            Scheme xx = null;
            int total = 0;
            int find = 0;
            for (int i = 0; i < 10000; i++) {
                total++;
                Scheme a = initial.production(courses_ex);
                a.calculate();
                if (initial.round)
                    find++;
                System.out.println(a.adaption_score);
                xx = a.clone();
                if (a.adaption_score < x && initial.round) {
                    x = a.adaption_score;
                    xx = a.clone();
                    t = 1;
                }
            }
            if (x != 10000) {
                System.out.println();
                System.out.println(x);
                xx.str();
                xx.save(savePath + r + "(score=" + x + ").csv", savePath2 + r + "(score=" + x + ").csv");

            }
            double rate = (double) find / total * 100;
            System.out.println("rate = " + rate + " %");
            if (t == 0) {
                System.out.println("can't find!");
            }
        }
        /*
        Scheme a = initial.production(courses_ex);
        a.calculate();
        System.out.println(a.adaption_score);
        a.str();
        a.population();
        */
    }
}
