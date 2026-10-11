import java.io.*;
import java.nio.file.*;
import java.util.*;

// Helper (not part of the assignment): runs every test case and builds the tables.
// Usage:  javac Main.java MyThread.java RunAll.java   then   java RunAll
public class RunAll {

    static String run(int n, int k, boolean safe) throws Exception {
        Process p = new ProcessBuilder("java", "-cp", ".", "Main",
                String.valueOf(n), String.valueOf(k), String.valueOf(safe))
                .redirectErrorStream(true).start();
        String out = new String(p.getInputStream().readAllBytes()).trim();
        p.waitFor();
        return out;
    }

    static long field(String out, String name) {
        for (String line : out.split("\\R"))
            if (line.startsWith(name)) return Long.parseLong(line.split(":")[1].trim());
        throw new RuntimeException("Missing '" + name + "' in output:\n" + out);
    }

    public static void main(String[] args) throws Exception {
        int[][] tcs = {{1,1000},{2,10000},{5,10000},{10,50000},{20,50000},{50,50000},{100,50000}};
        StringBuilder raw = new StringBuilder();
        StringBuilder safeT = new StringBuilder(
            "### Thread-safe experiment (AtomicLong)\n" +
            "| Test | N | K | Expected | Static (S) | Non-static (I) | Abs diff | Diff (%) |\n" +
            "|---|---|---|---|---|---|---|---|\n");
        StringBuilder unsafeT = new StringBuilder(
            "\n### Unsynchronized experiment (5 runs)\n" +
            "| Threads | Expected | Run1 S | Run2 S | Run3 S | Run4 S | Run5 S | Non-static (I) | Avg abs diff | Avg diff (%) |\n" +
            "|---|---|---|---|---|---|---|---|---|---|\n");

        for (int i = 0; i < tcs.length; i++) {
            int n = tcs[i][0], k = tcs[i][1];
            String out = run(n, k, true);
            raw.append("===== TC").append(i+1).append("  N=").append(n).append(" K=").append(k)
               .append("  SAFE =====\n").append(out).append("\n");
            System.out.println("TC" + (i+1) + " safe done");
            long s = field(out, "Static Count"), inst = field(out, "Non-static Total");
            long d = Math.abs(s - inst);
            safeT.append(String.format("| TC%d | %d | %d | %d | %d | %d | %d | %.4f%% |\n",
                i+1, n, k, (long) n * k, s, inst, d, inst == 0 ? 0.0 : d * 100.0 / inst));

            StringBuilder row = new StringBuilder("| " + n + " | " + ((long) n * k) + " |");
            double sumD = 0, sumP = 0; long lastI = 0;
            for (int r = 1; r <= 5; r++) {
                out = run(n, k, false);
                raw.append("===== TC").append(i+1).append("  N=").append(n).append(" K=").append(k)
                   .append("  UNSAFE run ").append(r).append(" =====\n").append(out).append("\n");
                s = field(out, "Static Count"); inst = field(out, "Non-static Total");
                d = Math.abs(s - inst);
                sumD += d; sumP += inst == 0 ? 0.0 : d * 100.0 / inst; lastI = inst;
                row.append(" ").append(s).append(" |");
            }
            System.out.println("TC" + (i+1) + " unsafe x5 done");
            unsafeT.append(row).append(String.format(" %d | %.1f | %.4f |\n", lastI, sumD / 5, sumP / 5));
        }
        Files.writeString(Path.of("results.txt"), raw.toString());
        Files.writeString(Path.of("results_table.md"), safeT.toString() + unsafeT);
        System.out.println("\nDone. See results.txt and results_table.md");
    }
}
