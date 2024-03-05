package matrixThread;

import matrix.Matrix;
import matrix.MatrixMultiplication;
import threads.MultiplyPartiallyByRowKthThread;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByRowKthThread extends Thread {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int tasksNo;

    public MultiplyByRowKthThread(Matrix a, Matrix b, int tasksNo) {
        super();
        this.a = a;
        this.b = b;
        this.tasksNo = tasksNo;
        this.c = MatrixMultiplication.getResultMatrix(a, b);
    }

    @Override
    public void run() {
        List<Thread> threads = new ArrayList<>();

        for (int orderNo = 0; orderNo < tasksNo; orderNo++) {
            Thread thread = new MultiplyPartiallyByRowKthThread(a, b, c, tasksNo, orderNo);
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
