package GA_adapt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Scheme implements Comparable<Scheme>, Cloneable{
    int chang_ci = Public.chang_ci;
    ArrayList<Course>[] schedule;
    double adaption_score = 0;
    int[] population = new int[chang_ci];
    int ave = 78908 / chang_ci;
    double var;

    Scheme(ArrayList<Course>[] schedule){
        this.schedule = schedule;
    }

    void calculate(){
        for(int i = 0; i < chang_ci - 1; i ++){
            for(int j = i + 1; j < chang_ci && j < i + Public.max_interval; j ++){
                for(int x = 0; x < schedule[i].size(); x ++){
                    for(int y = 0; y < schedule[j].size(); y ++){
                        adaption_score += (double)Public.conflict[schedule[i].get(x).id][schedule[j].get(y).id]/(j-i);
                    }
                    population[i] += schedule[i].get(x).students_number;
                }
            }
        }
        for(int i = 0; i < chang_ci; i ++){
            var += Math.pow(population[i] - ave, 2)/10000000;
        }
        adaption_score = adaption_score / 1000 + var;
    }


    @Override
    public int compareTo(Scheme o) {
        if(adaption_score == o.adaption_score)
            return 0;
        return adaption_score - o.adaption_score < 0 ? -1 : 1;
    }

    @Override
    protected Scheme clone() throws CloneNotSupportedException {
        Scheme cloned = (Scheme) super.clone();
        cloned.schedule = schedule.clone();
        cloned.population = population.clone();
        return cloned;
    }

    void str(){
        System.out.println("场次 \t 课程");
        for(int i = 0; i < Public.chang_ci; i ++){
            String name = "";
            for(int j = 0; j < schedule[i].size(); j ++){
                name = name + "  " + ((Course)schedule[i].get(j)).id;
            }
            System.out.println("" + i + "\t" + name);
        }
    }

    int[] population(){
        int[] popu = new int[Public.chang_ci];
        for(int i = 0; i < Public.chang_ci; i ++){
            int k = 0;
            for(Course j : schedule[i]){
                k += j.students_number;
            }
            popu[i] = k;
        }
        return popu;
    }

    void save(String path, String path2){
        Read_Write_decode a = new Read_Write_decode();
        String path3 = "D:\\java\\21data-group.csv";
        a.readCSV(path3);
        File outFile = new File(path);
        File out2File = new File(path2);
        int[] pp = population();
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(out2File));

            writer.write("exam_schedule" + ",");
            writer.write("the students number in each time slot" + ",");
            writer.write("courses" + ",");
            writer.newLine();

            writer2.write("exam_schedule" + ",");
            writer2.write("the students number in each time slot" + ",");
            writer2.write("courses" + ",");
            writer2.newLine();
            for(int i = 0; i < Public.chang_ci; i ++){
                writer.write(i + ",");
                writer.write(pp[i] + ",");
                for(Course j : schedule[i]){
                    writer.write(j.id + ",");
                }
                writer.newLine();

                writer2.write(i + ",");
                writer2.write(pp[i] + ",");
                for(Course j : schedule[i]){
                    for(Course q : a.courses_ex){
                        if(j.id == q.id){
                            writer2.write(q.name + ",");
                            break;
                        }
                    }

                }
                writer2.newLine();
            }
            writer.close();
            writer2.close();
        }catch (IOException ex){
            System.out.println("读写文件出错。");
        }

    }
}
