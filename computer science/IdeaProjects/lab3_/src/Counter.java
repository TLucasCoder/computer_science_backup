import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.UnitCounter;

public class Counter implements UnitCounter {
    int counter_var = 0;
    @Override
    public void addOne() {
        counter_var++;
    }

    @Override
    public int getCounter() {
        return counter_var;
    }
}
