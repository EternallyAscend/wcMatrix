import java.math.BigDecimal;
import java.math.RoundingMode;

public class kCM {
    private final int n;
    private final int k;
    private final int[] x;
    private final Matrix matrix;
    public kCM(int n, int k, int[] x) throws UnsupportedOperationException {
        this.n = n;
        this.k = k;
        this.x = x;
        this.matrix = buildKCM(n, k, x);
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("n = ").append(n).append("; k = ").append(k).append("; x = ");
        for (int i = 0; i < k; i++) {
            sb.append(x[i]).append(" ");
        }
        System.out.println(sb);
        matrix.print();
    }

    public static Matrix buildKCM(int n, int k, int[] x) throws UnsupportedOperationException {
        if (n < k) {
            throw new UnsupportedOperationException("Cannot build kCM Matrix: n < k");
        }
        if (k != x.length) {
            throw new UnsupportedOperationException("Cannot build kCM Matrix: x size is not equal to k");
        }
        Matrix result = new Matrix(n, n);
        int head = n - k + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                result.set(i, (head + i + j) % n, BigDecimal.valueOf(x[j]));
            }
        }
        return result;
    }

    public Matrix calculateInverseA() {
        Matrix A = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 1; i++) {
            for (int j = i; j < k - 1; j++) {
                A.set(i, j - i, BigDecimal.valueOf(x[j - i]));
            }
        }
        return A;
    }

    public Matrix calculateInverseB() {
        Matrix B = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 1; i++) {
            for (int j = 0; j <= i; j++) {
                B.set(i, j, BigDecimal.valueOf(x[k - 1 - j]));
            }
        }
        return B;
    }

    public Matrix calculateInverseC() {
        Matrix C = new Matrix(k - 2, k - 1);
        for (int i = 0; i < k - 2; i++) {
            for (int j = i; j < k - 1; j++) {
                C.set(i, j - i, BigDecimal.valueOf(x[j - i]));
            }
        }
        return C;
    }

    public Matrix calculateInverseD() {
        Matrix D = new Matrix(k - 2, k - 1);
        for (int i = 0; i < k - 2; i++) {
            for (int j = 0; j < k - 1; j++) {
                D.set(i, i + j, BigDecimal.valueOf(x[j]));
            }
        }
        return D;
    }

    public Matrix calculateInverseT() {
        Matrix T = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 2; i++) {
            T.set(i, 0, BigDecimal.valueOf(-x[k - 2 - i]).divide(BigDecimal.valueOf(x[k - 1]), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
            T.set(i, i + 1, BigDecimal.ONE);
        }

        T.set(k - 2, 0, BigDecimal.valueOf(-x[0]).divide(BigDecimal.valueOf(x[k - 1]), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
        return T;
    }

    public Matrix wcInverseKCM(Matrix input) {
        Matrix result = new Matrix(input.getRows(), input.getColumns());
        // Step 1. Calculate det of input matrix.
        BigDecimal det = input.determinant(); // TODO Replace with wcDeterminant.
        BigDecimal oneOverDet = BigDecimal.ONE.divide(det, Precision.MAX_LENGTH, RoundingMode.HALF_UP);
        // Step 2. Calculate y_1 to y_k-1 according to Eq. 14. TODO Check rightness.
        // Step 2.1 Calculate A, B, C, D, and T
        Matrix A, B, C, D, T;
        A = calculateInverseA();
        B = calculateInverseB();
        C = calculateInverseC();
        D = calculateInverseD();
        T = calculateInverseT();
        // Step 2.2 Construct Ms
        BigDecimal[] Cache = new BigDecimal[k - 1 + k - 2];
        BigDecimal[] Temp = new BigDecimal[k - 1 + k - 2];

        Matrix Ms = new Matrix(k - 2 + k - 1, k - 1 + k - 2);
        for (int i = 0; i < k - 1; i++) {
            Cache[i] = A.get(i, 0);
            for (int j = 0; j < k - 2; j++) {
                Ms.set(i, j, A.get(i, j + 1));
            }
            for (int j = k - 2; j < k - 2 + k - 1; j++) {
                Ms.set(i, j, B.get(i, j - k + 2));
            }
        }
        for (int i = k - 1; i < k - 1 + k - 2; i++) {
            Cache[i] = C.get(i - k + 1, 0);
            for (int j = 0; j < k - 2; j++) {
                Ms.set(i, j, C.get(i - k + 1, j));
            }
            for (int j = k - 2; j < k - 2 + k - 1; j++) {
                Ms.set(i, j, D.get(i - k + 1, j - k + 2));
            }
        }
        // Step 2.3 Calculate y_1 to y_k-1
        BigDecimal[] Y = new BigDecimal[n];
        int flag = -1;
        if (k * n % 2 == 0) {
            flag = 1;
        }
        BigDecimal value = BigDecimal.valueOf(x[k - 1]).pow(n - k - k + 2);
        for (int i = 0; i < k - 1; i++) {
            Y[i] = BigDecimal.valueOf(flag).multiply(oneOverDet).multiply(value).multiply(Ms.determinant()); // TODO Replace with wcDeterminant.
            flag *= -1;
            for (int j = 0; j < k - 1 + k - 2; j++) {
                Temp[j] = Ms.get(j, i);
                Ms.set(j, i, Cache[j]);
                Cache[j] = Temp[j];
            }
        }
        // Step 3. Calculate y_k to y_n according to Eq. 15. TODO

        // Step 4. Construct M^-1. TODO

        return result;
    }

    public boolean testWcInverse(Matrix input) {
        if (input.getRows() != input.getColumns()) {
            throw new UnsupportedOperationException("Matrix is not square.\n");
        }
        Matrix result = new Matrix(input.getRows(), input.getColumns());
        for (int i = 0; i < input.getRows(); i++) {
            for (int j = 0; j < input.getColumns(); j++) {
                result.set(i, j, BigDecimal.ONE);
            }
        }
        Matrix inverse = wcInverseKCM(input);
        Matrix temp = input.matmul(inverse);
        return result.compareTo(temp);
    }

}
