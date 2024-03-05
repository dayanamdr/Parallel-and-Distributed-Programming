package matrix;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Matrix {
    private int rowsNo;
    private int colsNo;
    private Vector<Vector<Integer>> elems;

    public Matrix(int rowsNo, int colsNo) {
        this.rowsNo = rowsNo;
        this.colsNo = colsNo;
        initMatrix();
    }

    public Matrix (int rowsNo, int colsNo, int elem) {
        this.rowsNo = rowsNo;
        this.colsNo = colsNo;

        this.elems = new Vector<>(rowsNo);
        for (int i = 0; i < rowsNo; i++) {
            Vector<Integer> row = new Vector<>();
            for (int j = 0; j < colsNo; j++) {
                row.add(elem);
            }
            this.elems.add(row);
        }
    }

    public Matrix(int rowsNo, int colsNo, List<Integer> elems) {
        this.rowsNo = rowsNo;
        this.colsNo = colsNo;

        this.elems = new Vector<>(rowsNo);
        for (int i = 0; i < rowsNo; i++) {
            Vector<Integer> row = new Vector<>(elems.subList(i * colsNo, (i + 1) * colsNo));
            this.elems.add(row);
        }
    }

    public int getCapacity() {
        return rowsNo * colsNo;
    }

    public int getRowsNo() {
        return rowsNo;
    }

    public int getColsNo() {
        return colsNo;
    }

    public Vector<Vector<Integer>> getElems() {
        return elems;
    }

    public Integer getElem(int rowIdx, int colIdx) {
        return elems.get(rowIdx).get(colIdx);
    }

    public void setElem(int rowIdx, int colIdx, int elem) {
        elems.get(rowIdx).set(colIdx, elem);
    }

    private void initMatrix() {
        this.elems = new Vector<>(rowsNo);
        for (int i = 0; i < rowsNo; i++) {
            Vector<Integer> row = new Vector<>(colsNo);
            row.addAll(Collections.nCopies(colsNo, 0));
            this.elems.add(row);
        }
    }

    public void print() {
        for (int i = 0; i < rowsNo; i++) {
            for (int j = 0; j < colsNo; j++) {
                System.out.print(this.getElem(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
