import matrixThread.MultiplyByColumnThread;
import matrixThread.MultiplyByRowKthThread;
import matrixThread.MultiplyByRowThread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import matrix.Matrix;
import matrixThreadPool.MultiplyByColumnThreadPool;
import matrixThreadPool.MultiplyByRowKthThreadPool;
import matrixThreadPool.MultiplyByRowThreadPool;

public class Main {
    public static final int MAX_TASKS_NO = 500;

    public static void main(String[] args) throws Exception {
//        Matrix A = new Matrix(3, 2, Arrays.asList(1,2,3,4,5,6));
//        Matrix B = new Matrix(2, 4, Arrays.asList(1,2,3,4,5,6,7,8));
//        Matrix A = new Matrix(2, 2, Arrays.asList(1,2,3,4));
//        Matrix B = new Matrix(2, 3, Arrays.asList(1,2,3,4,5,6));
        Matrix A = new Matrix(1000, 1000, 1);
        Matrix B = new Matrix(1000, 1000, 1);
        if (A.getColsNo() != B.getRowsNo()) {
            throw new Exception("The A and B can't be multiplied.");
        }

        try(BufferedWriter writer = new BufferedWriter((new FileWriter("performance.csv")))) {
            // header
            writer.write("No. of Tasks,Thread Row,Thread Column,Thread Row Kth," +
                    "Thread Pool(2) Row,Thread Pool(2) Column,Thread Pool(2) Row Kth," +
                    "Thread Pool(5) Row,Thread Pool(5) Column,Thread Pool(5) Row Kth," +
                    "Thread Pool(10) Row,Thread Pool(10) Column,Thread Pool(10) Row Kth");

            long tic = 0;
            long tac = 0;

            for (int tasksNo = 1; tasksNo <= MAX_TASKS_NO; tasksNo++) {
                writer.newLine();
                writer.write(tasksNo + ",");

//                Thread thread = new MultiplyByRowThread(A, B, tasksNo);
//                tic = System.nanoTime();
//                thread.start();
//                thread.join();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");
//
//                thread = new MultiplyByColumnThread(A, B, tasksNo);
//                tic = System.nanoTime();
//                thread.start();
//                thread.join();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");
//
//                thread = new MultiplyByRowKthThread(A, B, tasksNo);
//                tic = System.nanoTime();
//                thread.start();
//                thread.join();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");

                // thread pool(2)
                Runnable runnable = new MultiplyByRowThreadPool(A, B, tasksNo, 32);
                tic = System.nanoTime();
                runnable.run();
                tac = System.nanoTime();
                writer.write(Long.toString(tac - tic) + ",");

                runnable = new MultiplyByColumnThreadPool(A, B, tasksNo, 32);
                tic = System.nanoTime();
                runnable.run();
                tac = System.nanoTime();
                writer.write(Long.toString(tac - tic) + ",");

                runnable = new MultiplyByRowKthThreadPool(A, B, tasksNo, 32);
                tic = System.nanoTime();
                runnable.run();
                tac = System.nanoTime();
                writer.write(Long.toString(tac - tic) + ",");

                // thread pool(5)
//                runnable = new MultiplyByRowThreadPool(A, B, tasksNo, 16);
//                tic = System.nanoTime();
//                runnable.run();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");
//
//                runnable = new MultiplyByColumnThreadPool(A, B, tasksNo, 16);
//                tic = System.nanoTime();
//                runnable.run();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");
//
//                runnable = new MultiplyByRowKthThreadPool(A, B, tasksNo, 16);
//                tic = System.nanoTime();
//                runnable.run();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");
//
//                // thread pool(10)
//                runnable = new MultiplyByRowThreadPool(A, B, tasksNo, 10);
//                tic = System.nanoTime();
//                runnable.run();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");
//
//                runnable = new MultiplyByColumnThreadPool(A, B, tasksNo, 10);
//                tic = System.nanoTime();
//                runnable.run();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");
//
//                runnable = new MultiplyByRowKthThreadPool(A, B, tasksNo, 10);
//                tic = System.nanoTime();
//                runnable.run();
//                tac = System.nanoTime();
//                writer.write(Long.toString(tac - tic) + ",");

                System.out.println("END\n");
            }
        }

    }
}