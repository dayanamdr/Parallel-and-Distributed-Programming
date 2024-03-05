package matrixThreadPool;

import matrix.Matrix;
import matrix.MatrixMultiplication;
import runnables.MultiplyByRowRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiplyByRowThreadPool implements Runnable {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int tasksNo;
    private ExecutorService executorService;

    public MultiplyByRowThreadPool(Matrix a, Matrix b, int tasksNo, int maxThreadsNo) {
        this.a = a;
        this.b = b;
        this.c = MatrixMultiplication.getResultMatrix(a, b);
        this.tasksNo = tasksNo;
        executorService = Executors.newFixedThreadPool(maxThreadsNo);
    }

    @Override
    public void run() {
        List<Runnable> runnables = new ArrayList<>();
        int elementsNoPerTask = c.getCapacity() / tasksNo;

        for (int taskIndex = 0; taskIndex < tasksNo; taskIndex++) {
            int index = taskIndex * elementsNoPerTask;
            int startRowIndex = index / c.getColsNo();
            int startColumnIndex = index % c.getColsNo();
            if (taskIndex == tasksNo - 1) {
                elementsNoPerTask += c.getCapacity() % tasksNo;
            }

            Runnable runnable = new MultiplyByRowRunnable(a, b, c, elementsNoPerTask, startRowIndex, startColumnIndex);
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
