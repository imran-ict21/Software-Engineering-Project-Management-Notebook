import java.util.concurrent.atomic.AtomicLong;

public class MyThread implements Runnable {

    static AtomicLong safeCount = new AtomicLong(0);   // Experiment A (thread-safe)
    static long unsafeCount = 0;                       // Experiment B (unsynchronized)

    long count = 0;        // non-static: one per object
    int n;
    boolean safe;

    MyThread(int n, boolean safe) {
        this.n = n;
        this.safe = safe;
    }

    public void run() {
        for (int i = 0; i < n; i++) {
            if (safe) safeCount.incrementAndGet();
            else unsafeCount++;
            count++;
        }
    }
}
