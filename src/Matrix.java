import java.math.BigDecimal;

public class Matrix implements Cloneable {
    private int rows;
    private int columns;
    private BigDecimal[][] matrix;
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new BigDecimal[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = new BigDecimal(0);
            }
        }
    }

    public BigDecimal get(int i, int j) {
        return this.matrix[i][j];
    }

    public void set(int i, int j, BigDecimal value) {
        this.matrix[i][j] = value;
    }

    public void print() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                System.out.print(this.matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean equals(Matrix other) {
        if (this.rows != other.rows || this.columns != other.columns) {
            return false;
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (!this.matrix[i][j].equals(other.matrix[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Matrix clone() throws CloneNotSupportedException {
        Matrix result = (Matrix) super.clone();
//        Matrix result = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                result.matrix[i][j] = this.matrix[i][j];
            }
        }
        return result;
    }

    public BigDecimal determinant() {
        if (1 == this.rows) {
            if (1 == this.columns) {
                return this.matrix[0][0];
            }
        }
        BigDecimal result = new BigDecimal(1);
        int sign = 1;
        for (int i = 0; i < this.rows; i++) {
            Matrix subMatrix = new Matrix(this.rows - 1, this.columns - 1);
            for (int j = 0; j < this.columns; j++) {
                int k = 0;
                for (int c = 0; c < this.columns; c++) {
                    if (c != i) {
                        subMatrix.set(j - 1, k++, matrix[j][c]);
                    }
                }
            }
        }

        return result;
    }

    public Matrix inverse() throws CloneNotSupportedException {
        Matrix result = this.clone();
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.columns; j++) {
                result.matrix[i][j] = new BigDecimal(0);
                // TODO Finish inverse logic.
            }
        }
        return result;
    }

    public Matrix multiply(Matrix right) {
        Matrix result = new Matrix(this.rows, right.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < right.columns; j++) {
                for (int k = 0; k < this.columns; k++) {
                    result.matrix[i][j] = result.matrix[i][j].add(this.matrix[i][k].multiply(right.matrix[k][j]));
                }
            }
        }
        return result;
    }

    public Matrix pow(Matrix input, int power) throws CloneNotSupportedException, Exception {
        if (this.rows != this.columns) {
            throw new Exception("Matrix is not square");
        }
        Matrix result;
        if (0 == power) {
            result = new Matrix(this.rows, this.columns);
            for (int i = 0; i < this.rows; i++) {
                result.matrix[i][i] = new BigDecimal(1);
            }
        } else {
            result = this.clone();
            Matrix base = this.clone();
            while (power > 1) {
                if (power % 2 == 1) {
                    result = result.multiply(base);
                }
                power /= 2;
                base = base.multiply(base);
            }
            result = result.multiply(base);
        }
        return result;
    }

    public Matrix wcInverse() {
        Matrix result = new Matrix(this.rows, this.columns);
        // TODO Finish wcInverse logic based on wc paper.
        return result;
    }
}
