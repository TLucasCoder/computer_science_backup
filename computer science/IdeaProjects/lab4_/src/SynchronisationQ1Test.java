/**
 *
 */

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the Lab on threading, exceptions and synchronisation. Question 1: Zoo
 * @author jan
 *
 */
class SynchronisationQ1Test {

    /**
     * Tests whether the counter has counted every guest.
     * @throws InterruptedException
     */
    @Test
    void zooTest() throws InterruptedException {
        Counter counter = new Counter();
        int numberGates = 20;
        int numberGuestsPerGate = 5000;
        List<Gate> allGates = new ArrayList<>();
        List<Thread> allGateThreads = new ArrayList<>();
        for (int i = 0; i < numberGates; i++) {
            allGates.add(new Gate(counter, numberGuestsPerGate));
            // this line below
            Thread gateThread = new Thread(allGates.get(i),Integer.toString(i));
            allGateThreads.add(gateThread);
            gateThread.start();
        }
        for (int i = 0; i < numberGates; i++) {
            allGateThreads.get(i).join();
        }
        assertEquals(numberGates * numberGuestsPerGate, counter.getCounter(), "Wrong number of guests counted.");
    }

}