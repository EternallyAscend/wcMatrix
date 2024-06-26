import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Matrix implements Cloneable {
    public static Logger logger = Logger.getLogger(Matrix.class.getName());
    private final int rows;
    private final int columns;
    private final BigDecimal[][] matrix;

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

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public BigDecimal[] getRowI(int i) {
        return this.matrix[i];
    }

    public BigDecimal[] getColumnI(int i) {
        BigDecimal[] result = new BigDecimal[this.rows];
        for (int j = 0; j < this.rows; j++) {
            result[j] = this.matrix[j][i];
        }
        return result;
    }

    public void set(int i, int j, BigDecimal value) {
        this.matrix[i][j] = value;
    }

    public void print() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                System.out.print(this.matrix[i][j].setScale(Precision.MAX_PRINT_LENGTH, RoundingMode.HALF_UP) + "\t");
            }
            System.out.println();
        }
    }

    @Override
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
                BigDecimal left = new BigDecimal(this.matrix[i][j].toString());
                BigDecimal right = new BigDecimal(other.matrix[i][j].toString());
                if (0 != left.setScale(Precision.MAX_SCALE, RoundingMode.HALF_UP).compareTo(right.setScale(Precision.MAX_SCALE, RoundingMode.HALF_UP))) {
                    logger.log(Level.INFO,
                            "At (" + i + ", " + j + ")\nL: " + left.setScale(Precision.MAX_SCALE, RoundingMode.HALF_UP) +
                                    "\nR: " + right.setScale(Precision.MAX_SCALE, RoundingMode.HALF_UP));
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
            if (this.columns >= 0) {
                System.arraycopy(this.matrix[i], 0, result.matrix[i], 0, this.columns);
            }
        }
        return result;
    }

    public BigDecimal determinant() throws UnsupportedOperationException {
        if (this.rows != this.columns) {
            throw new UnsupportedOperationException("Determinant can only be calculated for square matrices.\n");
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
        return copy.multiply(BigDecimal.ONE.divide(determinant, Precision.MAX_LENGTH, RoundingMode.HALF_UP));
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

    public Matrix subtract(Matrix right) throws CloneNotSupportedException {
        Matrix result = new Matrix(this.rows, right.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < right.columns; j++) {
                result.matrix[i][j] = this.matrix[i][j].subtract(right.matrix[i][j]);
            }
        }
        return result;
    }

    public Matrix quickPower(int power) throws CloneNotSupportedException {
        if (this.rows != this.columns) {
            throw new UnsupportedOperationException("Matrix is not square");
        }
        Matrix result;
        if (0 == power) {
            result = new Matrix(this.rows, this.columns);
            for (int i = 0; i < this.rows; i++) {
                result.matrix[i][i] = BigDecimal.ONE;
            }
        } else {
            result = this.clone();
            while (power > 1) {
                if (power % 2 == 0) {
                    result = result.matmul(result);
                    power /= 2;
                } else {
                    result = result.matmul(this);
                    --power;
                }
            }
        }
        return result;
    }

    public Matrix power(int power) throws CloneNotSupportedException {
        if (this.rows != this.columns) {
            throw new UnsupportedOperationException("Matrix is not square");
        }
        Matrix result;
        if (0 == power) {
            result = new Matrix(this.rows, this.columns);
            for (int i = 0; i < this.rows; i++) {
                result.matrix[i][i] = BigDecimal.ONE;
            }
        } else {
            result = this.clone();
            for (int i = 1; i < power; i++) {
                result = result.matmul(this);
            }
        }
        return result;
    }
}
