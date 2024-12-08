package CourseWork.TwoPrimary;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        List<String> data = new ArrayList<>(); //Array for saving pairs (time, value)
        data.add("Time,Probability,FullLoading,MaxBuffer");
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
            double fullLoading = ((Process) list.get(3)).getQueueLoading() / tcurr;
//            double maxBuffer = Process.getMeanShearedQueue() / tcurr;
            int maxBuffer = Process.getMaxObservedSharedQueue();
            double probability = (double) (list.get(0).getQuantity() - list.get(1).getQuantity() -
                    list.get(2).getQuantity() + list.get(1).getState() + list.get(2).getState() +
                    ((Process) list.get(1)).getQueue() + ((Process) list.get(2)).getQueue()) / list.get(0).getQuantity();
            data.add(tcurr + "," + probability + "," + fullLoading + "," + maxBuffer);
        }
        printResult();
        exportToCSV(data, "simulation_data.csv");
    }

    public void printInfo() {
        for (Element e : list) {
            e.printInfo();
        }
    }

    public void printResult() {
        double totalFailure = 0;
        System.out.println("\n-------------RESULTS-------------");
        for (Element e : list) {
            e.printResult();
            if (e instanceof Process) {
                Process p = (Process) e;
                totalFailure += p.getFailure();
                System.out.println("Mean length of queue = " + p.getMeanQueue() / tcurr);
                System.out.println("Mean queue loading = " + p.getQueueLoading() / tcurr);
                System.out.println("Max observed queue length = " + p.getMaxObservedQueue());
                System.out.println("Amount of failures = " + p.getFailure());
                System.out.println("Queue probability of failure = " + p.getFailure() / (double) (p.getFailure() + p.getQuantity()));


            }
            System.out.println("---------------------------------");
        }
        System.out.println("Probability of refusal in primary regulation = " + (double) (list.get(0).getQuantity() -
                list.get(1).getQuantity() - list.get(2).getQuantity() + list.get(1).getState() + list.get(2).getState() +
                ((Process) list.get(1)).getQueue() + ((Process) list.get(2)).getQueue()) / list.get(0).getQuantity());
        System.out.println("Total failures: " + totalFailure);
        System.out.println("Buffer probability of failure: " + totalFailure / (double) list.get(0).getQuantity());
        System.out.println("Max observed number of units in buffer = " + Process.getMaxObservedSharedQueue());
        System.out.println("Mean size of buffer = " + Process.getMeanShearedQueue() / tcurr);

    }

    public void exportToCSV(List<String> data, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : data) {
                writer.write(line + "\n");
            }
            System.out.println("Data successfully exported to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}