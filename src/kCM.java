import java.math.BigDecimal;
import java.math.RoundingMode;

public class kCM {
    private final int n;
    private final int k;
    private final int[] x;
    private final Matrix detMatrix;
    private final Matrix matrix;
    public kCM(int n, int[] x) throws UnsupportedOperationException {
        this.n = n;
        this.k = x.length;
        this.x = x;
        this.detMatrix = buildDetKCM(n, k, x);
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

    public static Matrix buildDetKCM(int n, int k, int[] x) throws UnsupportedOperationException {
        if (n < k) {
            throw new UnsupportedOperationException("Cannot build kCM Det Matrix: n < k");
        }
        if (k != x.length) {
            throw new UnsupportedOperationException("Cannot build kCM Det Matrix: x size is not equal to k");
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

    public static Matrix buildKCM(int n, int k, int[] x) throws UnsupportedOperationException {
        if (n < k) {
            throw new UnsupportedOperationException("Cannot build kCM Matrix: n < k");
        }
        if (k != x.length) {
            throw new UnsupportedOperationException("Cannot build kCM Matrix: x size is not equal to k");
        }
        Matrix result = new Matrix(n, n);
        int head = n - 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                result.set(i, (head + i + j) % n, BigDecimal.valueOf(x[j]));
            }
        }
        return result;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public Matrix calculateDetAD() {
        return calculateInverseB();
    }

    public Matrix calculateDetBC() {
        return  calculateInverseA();
    }

    public Matrix calculateDetT() {
        return calculateInverseT();
    }

    public BigDecimal wcDet() {

    }

    public Matrix calculateInverseA() {
        Matrix A = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 1; i++) {
            for (int j = i; j < k - 1; j++) {
                A.set(i, j, BigDecimal.valueOf(x[j - i]));
            }
        }
        return A;
    }

    public Matrix calculateInverseB() {
        Matrix B = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 1; i++) {
            for (int j = 0; j <= i; j++) {
                B.set(i, j, BigDecimal.valueOf(x[k - 1 - (i - j)]));
            }
        }
        return B;
    }

    public Matrix calculateInverseC() {
        Matrix C = new Matrix(k - 2, k - 1);
        for (int i = 0; i < k - 2; i++) {
            for (int j = 0; j <= i; j++) {
                C.set(i, j, BigDecimal.valueOf(x[k - 1 - (i - j)]));
            }
        }
        return C;
    }

    public Matrix calculateInverseD() {
        Matrix D = new Matrix(k - 2, k - 1);
        for (int i = 0; i < k - 2; i++) {
            for (int j = 0; i + j < k - 1; j++) {
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

    public Matrix wcInverseKCM(Matrix input) throws CloneNotSupportedException {
        Matrix result = new Matrix(input.getRows(), input.getColumns());
        // Step 1. Calculate det of input matrix.
        BigDecimal det = input.determinant(); // TODO Replace with wcDeterminant.
        System.out.println("det: " + det);
//        BigDecimal oneOverDet = BigDecimal.ONE.divide(det, Precision.MAX_LENGTH, RoundingMode.HALF_UP);
        // Step 2. Calculate y_1 to y_k-1 according to Eq. 14. TODO Check rightness.
        // Step 2.1 Calculate A, B, C, D, and T
        Matrix A, B, C, D, T;
        A = calculateInverseA();
        System.out.println("A:");
        A.print();
        B = calculateInverseB();
        System.out.println("B:");
        B.print();
        C = calculateInverseC();
        System.out.println("C:");
        C.print();
        D = calculateInverseD();
//        System.out.println("D:");
//        D.print();
        T = calculateInverseT();
        System.out.println("T:");
        T.print();
        T = T.power(n - 2 * k + 2); // TODO Replace with Quick Power.
        System.out.println(new StringBuilder().append("T pow ").append(n - 2*k + 2).append(":"));
        T.print();
        // Step 2.2 Construct Ms
        BigDecimal[] Cache = new BigDecimal[k - 1 + k - 2];
        BigDecimal[] Temp = new BigDecimal[k - 1 + k - 2];
        Matrix Aa = new Matrix(k - 1, k - 2);
        for (int i = 0; i < k - 1; i++) {
            Cache[i] = A.get(i, 0);
            for (int j = 0; j < k - 2; j++) {
                Aa.set(i, j, A.get(i, j + 1));
            }
        }
        System.out.println("Aa[0]:");
        Aa.print();
        Matrix At = T.matmul(Aa);
        At.print();
        Matrix Ms = new Matrix(k - 1 + k - 2, k - 2 + k - 1);
        for (int i = 0; i < k - 1; i++) {
            for (int j = 0; j < k - 2; j++) {
                Ms.set(i, j, At.get(i, j));
            }
            for (int j = k - 2; j < k - 2 + k - 1; j++) {
                Ms.set(i, j, B.get(i, j - k + 2));
            }
        }
        for (int i = k - 1; i < k - 1 + k - 2; i++) {
            Cache[i] = C.get(i - k + 1, 0);
            for (int j = 0; j < k - 2; j++) {
                Ms.set(i, j, C.get(i - k + 1, j + 1));
            }
            for (int j = k - 2; j < k - 2 + k - 1; j++) {
                Ms.set(i, j, D.get(i - k + 1, j - k + 2));
            }
        }
        System.out.println("Ms[0]:");
        Ms.print();

        // Step 2.3 Calculate y_1 to y_k-1
        BigDecimal[] Y = new BigDecimal[n];
        int flag = -1;
        if (k * n % 2 == 0) {
            flag = 1;
        }
        BigDecimal kk = BigDecimal.valueOf(x[k - 1]).pow(n - k - k + 2);
        for (int i = 0; i < k - 1; i++) {
            BigDecimal detMs = Ms.determinant(); // TODO Replace with wcDeterminant.
            System.out.println("detMs: " + detMs);
            Y[i] = BigDecimal.valueOf(flag).multiply(kk).multiply(detMs).divide(det, Precision.MAX_LENGTH, RoundingMode.HALF_UP);
            flag = -flag;
            if (i < k - 2) {
                // Exchange A.
                for (int j = 0; j < k - 1; j++) {
                    Temp[j] = Aa.get(j, i);
                    Aa.set(j, i, Cache[j]);
                    Cache[j] = Temp[j];
                }
                System.out.println("Aa[" + i + "]:");
                Aa.print();
                At = T.matmul(Aa);
                System.out.println("At[" + i + "]:");
                At.print();
                for (int j = 0; j < k - 1; j++) {
                    for (int l = 0; l < k - 2; l++) {
                        Ms.set(j, l, At.get(j, l));
                    }
                }
                // Exchange C.
                for (int j = 0; j < k - 2; j++) {
                    Temp[k - 1 + j] = Ms.get(k - 1 + j, i);
                    Ms.set(k - 1 + j, i, Cache[k - 1 + j]);
                    Cache[k - 1 + j] = Temp[k - 1 + j];
                }
                System.out.println("Ms[" + i + "]:");
                Ms.print();
            }
        }
        // Step 3. Calculate y_k to y_n according to Eq. 15. TODO
        BigDecimal base = BigDecimal.valueOf(-1).divide(BigDecimal.valueOf(x[k - 1]), Precision.MAX_LENGTH, RoundingMode.HALF_UP);
        for (int i = k - 1; i < n; i++) {
            Y[i] = BigDecimal.ZERO;
            for (int j = 0; j < k - 1; j++) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("i = ").append(i).append("\t j = ").append(j).append("\t i - k + 1 + j = ").append(j);
//                System.out.println(sb);
                Y[i] = Y[i].add(BigDecimal.valueOf(x[j]).multiply(Y[i - (k - 1) + j]));
            }
            Y[i] = Y[i].multiply(base);
        }
        for (int i = 0; i < n; i++) {
            System.out.println("Y[" + i + "] = " + Y[i]);
        }
        // Step 4. Construct M^-1. TODO
//        Matrix inverseM = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.set(i, j, Y[(n + i - j) % n]);
            }
        }
        return result;
    }

    public boolean testWcInverse() throws CloneNotSupportedException {
        if (this.matrix.getRows() != this.matrix.getColumns()) {
            throw new UnsupportedOperationException("Matrix is not square.\n");
        }
        Matrix result = new Matrix(this.matrix.getRows(), this.matrix.getColumns());
        for (int i = 0; i < this.matrix.getRows(); i++) {
            result.set(i, i, BigDecimal.ONE);
        }
        Matrix inverse = wcInverseKCM(this.matrix);
        System.out.println("wcInverseKCM:");
        inverse.print();
        Matrix Inverse = this.matrix.inverse();
        System.out.println("Inverse:");
        Inverse.print();
        System.out.println(result.compareTo(this.matrix.matmul(Inverse)));
        Matrix temp = this.matrix.matmul(inverse);
        return result.compareTo(temp);
    }

}


