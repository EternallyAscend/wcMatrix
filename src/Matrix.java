import java.math.BigDecimal;
import java.math.RoundingMode;

public class Matrix implements Cloneable {
    public static int MAX_LENGTH = 256; // Integer.MAX_VALUE / 1024;
    private int rows;
    private int columns;
    private BigDecimal[][] matrix;
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new BigDecimal[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = BigDecimal.ZERO;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                sb.append(this.matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean compareTo(Matrix other) {
        if (this.rows != other.rows || this.columns != other.columns) {
            return false;
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (0 != this.matrix[i][j].compareTo(other.matrix[i][j])) {
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

    public BigDecimal determinant() throws UnsupportedOperationException {
        if (this.rows != this.columns) {
            throw new UnsupportedOperationException("Determinant can only be calculated for square matrices.");
        }

        if (1 == this.rows) {
            return this.matrix[0][0];
        }
        BigDecimal result = BigDecimal.ZERO;
        int sign = 1;
        for (int i = 0; i < this.rows; i++) {
            Matrix subMatrix = new Matrix(this.rows - 1, this.columns - 1);
            for (int j = 1; j < this.rows; j++) {
                int t = 0;
                for (int k = 0; k < this.columns; k++) {
                    if (k != i) {
                        subMatrix.set(j - 1, t++, matrix[j][k]);
                    }
                }
            }
            result = result.add(this.matrix[0][i].multiply(subMatrix.determinant()).multiply(BigDecimal.valueOf(sign)));
            sign = -sign;
        }

        return result;
    }

    public Matrix inverse() throws UnsupportedOperationException, ArithmeticException, CloneNotSupportedException {
        System.out.println(this.rows + " " + this.columns);
        if (this.rows != this.columns) {
            throw new UnsupportedOperationException("Inverse can only be calculated for square matrices.\n");
        }

        BigDecimal determinant = this.determinant();
        if (determinant.equals(BigDecimal.ZERO)) {
            throw new ArithmeticException("Matrix is singular, cannot find inverse.\n" + this);
        }

        Matrix copy = new Matrix(this.rows, this.columns);
        int sign;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                Matrix subMatrix = new Matrix(this.rows - 1, this.columns - 1);
                int m = 0, n;
                for (int k = 0; k < this.rows; k++) {
                    if (k == i) continue;
                    n = 0;
                    for (int l = 0; l < this.columns; l++) {
                        if (l == j) continue;
                        subMatrix.set(m, n++, this.matrix[k][l]);
                    }
                    m++;
                }
                copy.set(j, i, subMatrix.determinant().multiply(BigDecimal.valueOf(sign)));
            }
        }

        return copy.multiply(BigDecimal.ONE.divide(determinant, MAX_LENGTH, RoundingMode.CEILING));
    }

    public Matrix matmul(Matrix right) {
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

    public Matrix multiply(BigDecimal scalar) throws CloneNotSupportedException {
        Matrix result = this.clone();
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.columns; j++) {
                result.matrix[i][j] = result.matrix[i][j].multiply(scalar);
            }
        }
        return result;
    }

    public Matrix pow(Matrix input, int power) throws UnsupportedOperationException, CloneNotSupportedException {
        if (this.rows != this.columns) {
            throw new UnsupportedOperationException("Matrix is not square");
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
                    result = result.matmul(base);
                }
                power /= 2;
                base = base.matmul(base);
            }
            result = result.matmul(base);
        }
        return result;
    }

//    public Matrix wcInverse() {
//        Matrix result = new Matrix(this.rows, this.columns);
//        // TODO Finish wcInverse logic based on wc paper.
//        return result;
//    }
}