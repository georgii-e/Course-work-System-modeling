package CourseWork.Basic;

import java.util.ArrayList;

public class SimModel {
    public static void main(String[] args) {
        Create c = new Create(30);
        Process p1 = new Process(30);
        Process p2 = new Process(100);
        Process p3 = new Process(30);
//        Process.setMaxSharedQueue(500);
        Dispose d = new Dispose();
        c.addNextElementPrior(p1, 1);
        c.addNextElementPrior(p2, 2);
        p1.setNextElement(p3);
        p2.setNextElement(d);
        p3.setNextElement(d);
        p1.setMaxQueue(0);
        c.setName("CREATOR");
        p1.setName("PRIMARY");
        p2.setName("FULL");
        p3.setName("SECONDARY");
        d.setName("DISPOSE");
        c.setDistribution("exp");
        p1.setDistribution("exp");
        p2.setDistribution("exp");
        p3.setDistribution("exp");
        ArrayList<Element> list = new ArrayList<>();
        list.add(c);
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(d);
        Model model = new Model(list);
        model.simulate(100000);
    }
}

