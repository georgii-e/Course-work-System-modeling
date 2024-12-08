package CourseWork.TwoPrimary;


public class Process extends Element {
    private int queue, maxQueue, maxObservedQueue, failure;
    private double meanQueue, queueLoading;
    private static int sharedQueue = 0, maxObservedSharedQueue = 0, maxSharedQueue = Integer.MAX_VALUE;
    private static double meanSharedQueue;


    public Process(double delay) {
        super(delay);
        super.setTnext(Double.MAX_VALUE);
        queue = 0;
        maxQueue = Integer.MAX_VALUE;
        maxObservedQueue = 0;
        meanQueue = 0.0;
        queueLoading = 0.0;
    }

    @Override
    public void inAct() {
        if (this.getName().equals("FULL")) {
            if (super.getState() == 0) {
                super.setState(1);
                super.setTnext(super.getTcurr() + super.getDelay());
                if (getSharedQueue() < getMaxSharedQueue()) {
                    setQueue(getQueue() + 1);
                    sharedQueue++;
                    updateMaxObservedQueue();
                    updateMaxObservedSharedQueue();
                } else {
                    failure++;
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    if (getSharedQueue() < getMaxSharedQueue()) {
                        setQueue(getQueue() + 1);
                        sharedQueue++;
                        updateMaxObservedQueue();
                        updateMaxObservedSharedQueue();
                    } else {
                        failure++;
                    }
                }
            }
        } else {
            if (super.getState() == 0) {
                super.setState(2);
                super.setTnext(super.getTcurr() + super.getDelay());
            } else {
                if (getSharedQueue() < getMaxSharedQueue() - 1) {
                    setQueue(getQueue() + 2);
                    sharedQueue += 2;
                    updateMaxObservedQueue();
                    updateMaxObservedSharedQueue();
                } else {
                    failure += 2;
                }
            }
        }

    }

    @Override
    public void outAct() {
        super.setTnext(Double.MAX_VALUE);
        super.setState(0);
        if (this.getName().equals("FULL")) {
            super.outAct();
            if (getQueue() > 0) {
                setQueue(getQueue() - 1);
                sharedQueue -= 1;
                super.setState(1);
                super.setTnext(super.getTcurr() + super.getDelay());
            }
            getNextElement().singleInAct();
        } else {
            super.doubleOutAct();
            if (getQueue() >= 2) {
                setQueue(getQueue() - 2);
                sharedQueue -= 2;
                super.setState(2);
                super.setTnext(super.getTcurr() + super.getDelay());
            }
            if (getNextElement() != null) {
                getNextElement().inAct();
            }
        }
    }


    public int getFailure() {
        return failure;
    }

    public int getQueue() {
        return queue;
    }

    public int getSharedQueue() {
        return sharedQueue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public int getMaxQueue() {
        return maxQueue;
    }

    public void setMaxQueue(int maxQueue) {
        this.maxQueue = maxQueue;
    }

    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("failure = " + this.getFailure());
    }

    @Override
    public void doStatistics(double delta) {
        meanQueue = getMeanQueue() + queue * delta;
        if (queue > 0) {
            queueLoading = getQueueLoading() + delta;
        }
    }

    public static void doGlobalStatistics(double delta) {
        meanSharedQueue = getMeanShearedQueue() + sharedQueue * delta;
    }

    public double getMeanQueue() {
        return meanQueue;
    }

    public double getQueueLoading() {
        return queueLoading;
    }

    public static double getMeanShearedQueue() {
        return meanSharedQueue;
    }

    private void updateMaxObservedQueue() {
        if (queue > maxObservedQueue) {
            maxObservedQueue = queue;
        }
    }

    private void updateMaxObservedSharedQueue() {
        if (sharedQueue > maxObservedSharedQueue) {
            maxObservedSharedQueue = sharedQueue;
        }
    }

    public int getMaxObservedQueue() {
        return maxObservedQueue;
    }

    public static int getMaxObservedSharedQueue() {
        return maxObservedSharedQueue;
    }


    private static int getMaxSharedQueue() {
        return maxSharedQueue;
    }

    public static void setMaxSharedQueue(int maxQueue) {
        maxSharedQueue = maxQueue;
    }


}
