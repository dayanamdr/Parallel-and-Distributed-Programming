package matrixThreadPool;

import matrix.Matrix;
import matrix.MatrixMultiplication;
import runnables.MultiplyByRowKthRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiplyByRowKthThreadPool implements Runnable {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int tasksNo;
    private ExecutorService executorService;

    public MultiplyByRowKthThreadPool(Matrix a, Matrix b, int tasksNo, int maxThreadsNo) {
        this.a = a;
        this.b = b;
        this.c = MatrixMultiplication.getResultMatrix(a, b);
        this.tasksNo = tasksNo;
        executorService = Executors.newFixedThreadPool(maxThreadsNo);
    }

    @Override
    public void run() {
        List<Runnable> runnables = new ArrayList<>();

        for (int orderNo = 0; orderNo < tasksNo; orderNo++) {
            Runnable runnable = new MultiplyByRowKthRunnable(a, b, c, tasksNo, orderNo);
            runnables.add(runnable);
        }

        List<Future<?>> futures = new ArrayList<>();
        runnables.forEach(r -> {
            Future<?> future = executorService.submit(r);
            futures.add(future);
        });
        futures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
            }
        });
    }
}
