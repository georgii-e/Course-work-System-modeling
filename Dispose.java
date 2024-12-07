package CourseWork.Basic;

public class Dispose extends Element {
    private int arrivals;

    public Dispose() {
        super();
        arrivals = 0;
        super.setTnext(Double.MAX_VALUE);
    }

    @Override
    public void inAct() {
        arrivals += 2;
    }

    @Override
    public void singleInAct() {
        arrivals++;
    }


    @Override
    public void printResult() {
        System.out.println(getName() + " arrivals = " + arrivals);
    }

    @Override
    public void printInfo() {
        System.out.println(getName() + " arrivals = " + arrivals);
    }
}
