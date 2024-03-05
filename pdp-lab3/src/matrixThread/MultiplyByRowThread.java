package matrixThread;

import matrix.Matrix;
import matrix.MatrixMultiplication;
import threads.MultiplyPartiallyByRowThread;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByRowThread extends Thread {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int tasksNo;

    public MultiplyByRowThread(Matrix a, Matrix b, int tasksNo) {
        super();
        this.a = a;
        this.b = b;
        this.tasksNo = tasksNo;
        this.c = MatrixMultiplication.getResultMatrix(a, b);

//        if (tasksNo > c.getRowsNo() * c.getColsNo()) {
//            throw new Exception("MatrixMultiplication Exception: the tasksNo is greater than the capacity of matrix C.");
//        }
    }

    @Override
    public void run() {
        List<Thread> threads = new ArrayList<>();
        int elementsNoPerTask = c.getCapacity() / tasksNo;

        for (int taskIndex = 0; taskIndex < tasksNo; taskIndex++) {
            int index = taskIndex * elementsNoPerTask;
            int startRowIndex = index / c.getColsNo();
            int startColumnIndex = index % c.getColsNo();
            if (taskIndex == tasksNo - 1) {
                elementsNoPerTask += c.getCapacity() % tasksNo;
            }

            Thread thread = null;
            try {
                thread = new MultiplyPartiallyByRowThread(a, b, this.c, elementsNoPerTask, startRowIndex, startColumnIndex);
            } catch (Exception e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
            }
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
