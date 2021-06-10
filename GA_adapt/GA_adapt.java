package GA_adapt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class GA_adapt {
    Greedy_arithmetic initial = new Greedy_arithmetic(0.5);
    ArrayList<Course> courses_ex = Quick_read.readCsv("D:\\java\\students_number.csv");
    Scheme[] initial_group = new Scheme[Public.group_size];
    Random r = new Random(Public.random_seed);

    GA_adapt(){
        Public.conflict = Quick_read.readCSV("D:\\java\\conflict.csv");
        for(int i = 0; i < Public.group_size; i ++){
            Scheme a = initial.production(courses_ex);
            a.calculate();
            initial_group[i] = a;
        }
    }

    public void sort(Scheme[] group){
        Arrays.sort(group);
    }

    Scheme roulette(Scheme[] group){
        double[] probability = new double[Public.group_size];
        double[] cumulative_probability = new double[Public.group_size];
        double max = group[Public.group_size - 1].adaption_score;
        double min = group[0].adaption_score;
        double sum = 0;
        double fate = Math.random();

        for(int i = 0; i < Public.group_size; i ++){
            sum += Math.exp((max - group[i].adaption_score) / (max - min));
        }

        for(int i = 0; i < Public.group_size; i ++){
            probability[i] = Math.exp((max - group[i].adaption_score) / (max - min)) / sum;
            if(i == 0){
                cumulative_probability[i] = probability[i];
                if(fate <= cumulative_probability[i])
                    return group[i];
            }else{
                cumulative_probability[i] = cumulative_probability[i - 1] + probability[i];
                if (fate <= cumulative_probability[i])
                    return group[i];
            }
        }
        return group[Public.group_size - 1];
    }

    Scheme cross_operator(Scheme[] group){
        Scheme father = roulette(group);
        Scheme mother = roulette(group);
        ArrayList<Course>[] son_scheme = new ArrayList[Public.chang_ci];
        for(int i = 0; i < Public.chang_ci; i ++){
            son_scheme[i] = new ArrayList<>();
        }
        while (mother.equals(father))
            mother = roulette(group);

        int r1 = r.nextInt(Public.chang_ci);
        int r2 = r1 + r.nextInt(Public.chang_ci) * (Public.chang_ci - r1) / Public.chang_ci;
        HashSet<Course> hashSet = new HashSet<>();

        for(int i = r1; i < r2; i ++){
            for(int j = 0; j < father.schedule[i].size(); j ++){
                hashSet.add((Course)father.schedule[i].get(j));
                son_scheme[i].add(father.schedule[i].get(j));
            }
        }
        for(int i = 0; i < mother.chang_ci; i ++){
            for(int j = 0; j < mother.schedule[i].size(); j ++){
                Course k = (Course)mother.schedule[i].get(j);
                if(!hashSet.contains(k)){
                    son_scheme[i].add(k);
                }
            }
        }
        Scheme son = new Scheme(son_scheme);
        son.calculate();
        return son;
    }

    Scheme variation_2(Scheme[] group) throws CloneNotSupportedException {
        Scheme n = group[0].clone();
        int chang_ci_1 = r.nextInt(Public.chang_ci);
        int chang_ci_2 = r.nextInt(Public.chang_ci);
        while (n.schedule[chang_ci_1].size() < 3)
            chang_ci_1 = r.nextInt(Public.chang_ci);
        while (chang_ci_1 == chang_ci_2)
            chang_ci_2 = r.nextInt(Public.chang_ci);

        int begin_1 = r.nextInt(n.schedule[chang_ci_1].size() - 2) + 1;
        for (int a = begin_1; a != begin_1 - 1; a++) {
            int cflict = 0;
            for (Course x : n.schedule[chang_ci_2]) {
                cflict += Public.conflict[x.id][a];
                if (cflict != 0)
                    break;
            }
            if (cflict == 0) {
                n.schedule[chang_ci_2].add(n.schedule[chang_ci_1].get(a));
                n.schedule[chang_ci_1].remove(n.schedule[chang_ci_1].get(a));
                n.calculate();
                break;
            }
            if (a == n.schedule[chang_ci_1].size() - 1)
                a = -1;
        }
        return n;
    }

    void variation(Scheme[] group){
        if(Math.random() > Public.variation_probability){
            return;
        }
        for(int i = 0; i < group.length; i ++) {
            int t = 0;
            while (t == 0) {
                int chang_ci_1 = r.nextInt(Public.chang_ci);
                int chang_ci_2 = r.nextInt(Public.chang_ci);
                while (group[i].schedule[chang_ci_1].size() < 3)
                    chang_ci_1 = r.nextInt(Public.chang_ci);
                while (chang_ci_1 == chang_ci_2)
                    chang_ci_2 = r.nextInt(Public.chang_ci);

                int begin_1 = r.nextInt(group[i].schedule[chang_ci_1].size() - 2) + 1;
                for(int a = begin_1; a != begin_1 - 1; a ++){
                    int cflict = 0;
                    for(Course x : group[i].schedule[chang_ci_2]){
                        cflict += Public.conflict[x.id][a];
                        if(cflict != 0)
                            break;
                    }
                    if(cflict == 0){
                        t = 1;
                        group[i].schedule[chang_ci_2].add(group[i].schedule[chang_ci_1].get(a));
                        group[i].schedule[chang_ci_1].remove(group[i].schedule[chang_ci_1].get(a));
                        group[i].calculate();
                        break;
                    }
                    if(a == group[i].schedule[chang_ci_1].size() - 1)
                        a = -1;
                }
            }
        }
    }
}

class GA_adaptTest{
    public static void main(String[] args) throws CloneNotSupportedException {
        GA_adapt test = new GA_adapt();
        Scheme[] group = test.initial_group;
        long Time_1 = System.currentTimeMillis();
        for(int i = 0; i < Public.iterations; i ++){
            test.sort(group);
            System.out.println(group[0].adaption_score + "\t" + group[49].adaption_score);
            Scheme[] sons = new Scheme[Public.replace];
            for(int j = 0; j < Public.replace; j ++){
                sons[j] = test.variation_2(group);
            }
            for(int j = group.length - 1; j > group.length - Public.replace; j --){
                group[j] = sons[group.length - 1 - j];
            }
            test.variation(group);
        }
        long Time_2 = System.currentTimeMillis();
        System.out.println("total cost: " + (Time_2 - Time_1) + "ms");
    }
}
