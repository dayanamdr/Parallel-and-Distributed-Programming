package matrix;

public class MatrixMultiplication {
    public static Matrix getResultMatrix(Matrix a, Matrix b) {
        return new Matrix(a.getRowsNo(), b.getColsNo());
    }

    private static int computeElem(Matrix a, Matrix b, int rowIdx, int colIdx) {
        int result = 0;
        for (int mutualIdx = 0; mutualIdx < a.getColsNo(); mutualIdx++) {
            result += a.getElem(rowIdx, mutualIdx) * b.getElem(mutualIdx, colIdx);
        }
        return result;
    }

    public static void multiplyByRow(Matrix a, Matrix b, Matrix c, int elemsNo, int startRowIdx, int startColIdx) {
        int rowIdx = startRowIdx;
        int colIdx = startColIdx;

        for (int elemNo = 0; elemNo < elemsNo; elemNo++) {
            if (colIdx >= c.getColsNo()) {
                colIdx = 0; // reset the column index
                rowIdx++;  // move to the next row
            }
            if (rowIdx >= c.getRowsNo()) {
                break;
            }

            int elem = computeElem(a, b, rowIdx, colIdx);
            c.setElem(rowIdx, colIdx, elem);
            colIdx++; // move to the next element
        }
    }

    public static void multiplyByColumn(Matrix a, Matrix b, Matrix c, int elemsNo, int startRowIdx, int startColIdx) throws Exception {
        int rowIdx = startRowIdx;
        int colIdx = startColIdx;

        for (int elemNo = 0; elemNo < elemsNo; elemNo++) {
            if (rowIdx >= c.getRowsNo()) {
                rowIdx = 0;
                colIdx++;
                if (colIdx >= c.getColsNo()) {
                    break;
                }
            }
            int elem = computeElem(a, b, rowIdx, colIdx);
            c.setElem(rowIdx, colIdx, elem);
            rowIdx++;
        }
    }

    public static void multiplyByRowKthElem(Matrix a, Matrix b, Matrix c, int k, int orderNo) {
        int idx = orderNo;
        if (k <= orderNo || idx >= c.getCapacity()) {
            return;
        }

        while (idx < c.getCapacity()) {
            int rowIdx = idx / c.getColsNo();
            int colIdx = idx % c.getColsNo();

            int elem = computeElem(a, b, rowIdx, colIdx);
            c.setElem(rowIdx, colIdx, elem);

            idx += k;
        }
    }
}
