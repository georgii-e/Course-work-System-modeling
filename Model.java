package CourseWork.Basic;

import java.util.ArrayList;

public class Model {
    private ArrayList<Element> list;
    double tnext, tcurr;
    int event;

    public Model(ArrayList<Element> elements) {
        list = elements;
        tnext = 0.0;
        event = 0;
        tcurr = tnext;
    }

    public void simulate(double time) {
        while (tcurr < time) {
            tnext = Double.MAX_VALUE;
            for (Element e : list) {
                if (e.getTnext() < tnext) {
                    tnext = e.getTnext();
                    event = e.getId();
                }
            }
            System.out.println("\nIt's time for event in " +
                    list.get(event).getName() +
                    ", time = " + tnext);
            for (Element e : list) {
                e.doStatistics(tnext - tcurr);
            }
            Process.doGlobalStatistics(tnext - tcurr);
            tcurr = tnext;
            for (Element e : list) {
                e.setTcurr(tcurr);
            }
            list.get(event).outAct();
            for (Element e : list) {
                if (e.getTnext() == tcurr) {
                    e.outAct();
                }
            }
            printInfo();
        }
        printResult();
    }

    public void printInfo() {
        for (Element e : list) {
            e.printInfo();
        }
    }

    public void printResult() {
        int totalFailure = 0;
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult();
            if (e instanceof Process) {
                Process p = (Process) e;
                totalFailure += p.getFailure();
                System.out.println("Mean length of queue = " + p.getMeanQueue() / tcurr);
                System.out.println("Max observed queue length = " + p.getMaxObservedQueue());
            }
            System.out.println("---------------------------------");
        }
        System.out.println("Probability of refusal in primary regulation = " + (double) (list.get(0).getQuantity() -
                list.get(1).getQuantity() + list.get(1).getState()) / list.get(0).getQuantity());
        System.out.println("Total failures: " + totalFailure);
        System.out.println("Max observed number of units in buffer = " + Process.getMaxObservedSharedQueue());
        System.out.println("Mean size of buffer = " + Process.getMeanShearedQueue() / tcurr);

    }


}