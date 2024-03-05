package matrixThread;

import matrix.Matrix;
import matrix.MatrixMultiplication;
import threads.MultiplyPartiallyByColThread;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByColumnThread extends Thread {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int tasksNo;

    public MultiplyByColumnThread(Matrix a, Matrix b, int tasksNo) {
        super();
        this.a = a;
        this.b = b;
        this.tasksNo = tasksNo;
        this.c = MatrixMultiplication.getResultMatrix(a, b);
    }

    @Override
    public void run() {
        List<Thread> threads = new ArrayList<>();
        int elementsNoPerTask = c.getCapacity() / tasksNo;

        for (int taskIndex = 0; taskIndex < tasksNo; taskIndex++) {
            int index = taskIndex * elementsNoPerTask;
            int startRowIndex = index % c.getRowsNo();
            int startColumnIndex = index / c.getRowsNo();
            if (taskIndex == tasksNo - 1) {
                elementsNoPerTask += c.getCapacity() % tasksNo;
            }
            Thread thread = new MultiplyPartiallyByColThread(a, b, c, elementsNoPerTask, startRowIndex, startColumnIndex);
            threads.add(thread);
        }

        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }
}
