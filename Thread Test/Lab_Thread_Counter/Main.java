public class Main {

    public static void main(String[] args) throws InterruptedException {
        int threads = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        boolean safe = Boolean.parseBoolean(args[2]);

        MyThread[] obj = new MyThread[threads];
        Thread[] t = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            obj[i] = new MyThread(n, safe);
            t[i] = new Thread(obj[i]);
            t[i].start();
        }

        long total = 0;
        for (int i = 0; i < threads; i++) {
            t[i].join();
            total += obj[i].count;
        }

        long expected = (long) threads * n;
        long staticCount = safe ? MyThread.safeCount.get() : MyThread.unsafeCount;
        long diff = Math.abs(staticCount - total);
        double percent = total == 0 ? 0 : (double) diff / total * 100;

        System.out.println("Expected: " + expected);
        System.out.println("Static Count: " + staticCount);
        System.out.println("Non-static Total: " + total);
        System.out.println("Difference: " + diff);
        System.out.println("Percentage: " + percent + "%");
    }
}
