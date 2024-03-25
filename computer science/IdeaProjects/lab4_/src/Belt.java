public class Belt extends CyclicQueue {
    public Belt(int capacity) {
        super(capacity);
    }

    public  void enqueue(int i) throws IndexOutOfBoundsException {
        synchronized (this) {
            while (size == cyc_arr.length) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            {
                notify();
                super.enqueue(i);
            }
        }
    }

    @Override
    public  int dequeue() throws IndexOutOfBoundsException {
        synchronized (this) {
            while (size == 0) {
                try {
                    wait();
                    return dequeue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            {
                notify();
                int temp = super.dequeue();
                return temp;

            }
        }
    }
}
