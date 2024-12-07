package CourseWork.Basic;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Create extends Element {

    @Getter
    @Setter
    private PriorityQueue<Pair<Process, Integer>> nextElementPr = new PriorityQueue<>(Comparator.comparing(Pair::getValue));


    public Create(double delay) {
        super(delay);
        super.setTnext(0.0);
    }

    @Override
    public void outAct() {
        super.doubleOutAct();
        super.setTnext(super.getTcurr() + super.getDelay());

        Process nextProc = null;
        int minQueue = Integer.MAX_VALUE;
        for (Pair<Process, Integer> pair : getNextElementPr()) {
            Process p = pair.getKey();
            // If the process has a free state (0), go to it immediately
            if (p.getState() == 0) {
                nextProc = p;
                break;
            }
            // Otherwise, we select the process with the smallest queue, given the maxQueue constraint
            if (p.getQueue() < minQueue && p.getQueue() < p.getMaxQueue()) {
                minQueue = p.getQueue();
                nextProc = p;
            }
        }
        nextProc.inAct();
    }


    public void addNextElementPrior(Process element, int priority) {
        getNextElementPr().add(new Pair<>(element, priority));
    }

}

