import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class VirtualThreadExample {
    public static void main(String[] args) throws InterruptedException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < 10; i++) {
                int taskId = i;
                executor.submit(() -> {
                    System.out.println("Task " + taskId + " is running on " + Thread.currentThread());
                    Thread.sleep(1000); // 仮想スレッドでもsleep可能
                    return null;
                });
            }


            List<Path> files = List.of(
                    Path.of("file1.txt"),
                    Path.of("file2.txt"),
                    Path.of("file3.txt")
            ); // Example file paths

            List<Future<Integer>> results = new ArrayList<>();
            for (Path file : files) {
                results.add(executor.submit(() -> {
                    // ファイルを読み込み、データを集計
                    return processFile(file);
                }));
            }
            // 結果を待機
            for (Future<Integer> result : results) {
                try {
                    System.out.println("Result: " + result.get());
                } catch (ExecutionException e) {
                    System.err.println("Error processing task: " + e.getMessage());
                }
            }
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static int processFile(Path file) {
        // Simulate processing the file and returning a result
        System.out.println("Processing file: " + file);
        return file.toString().length(); // Example: return the length of the file path as a result
    }
}

