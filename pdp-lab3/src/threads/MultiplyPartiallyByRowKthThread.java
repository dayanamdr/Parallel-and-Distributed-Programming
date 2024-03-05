package threads;

import matrix.Matrix;
import matrix.MatrixMultiplication;

public class MultiplyPartiallyByRowKthThread extends Thread {
    private Matrix a;
    private Matrix b;
    private Matrix c;
    private int k;
    private int orderNo;

    public MultiplyPartiallyByRowKthThread(Matrix a, Matrix b, Matrix c, int k, int orderNo) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
        this.k = k;
        this.orderNo = orderNo;
    }

    @Override
    public void run() {
        super.run();
        try {
            MatrixMultiplication.multiplyByRowKthElem(a, b, c, k, orderNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
