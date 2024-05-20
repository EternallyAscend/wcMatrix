import java.math.BigDecimal;
import java.math.RoundingMode;

public class kCBM {
    private final int n;
    private final int k;
    private final int[] x;
    private final Matrix matrix;

    public kCBM(int n, int[] x) throws UnsupportedOperationException {
        this.n = n;
        this.k = x.length;
        this.x = x;
        this.matrix = buildKCBM(n, k, x);
    }

    public static Matrix buildKCBM(int n, int k, int[] x) throws UnsupportedOperationException {
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
                result.set(i, (head + i + j) % n, BigDecimal.valueOf(x[j]).pow(i+1));
            }
        }
        //result.print();
        //System.out.println(result.determinant());
        return result;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public Matrix calculateDetA() {
        return calculateInverseB(0);
    }

    public Matrix calculateDetB() {
        return calculateInverseA(0);
    }

    public Matrix calculateDetC() {
        return calculateInverseA(n - k + 1);
    }

    public Matrix calculateDetD() {
        return calculateInverseB(k-1);
    }

    public Matrix calculateDetT(int j) {
        return calculateInverseT(j);
    }


    public BigDecimal wcDeterminant() throws CloneNotSupportedException {
        int i = 2;
        int flag = -1;
        if ((k - i) * (n - k + i) % 2 == 0) {
            flag = 1;
        }
        BigDecimal result = BigDecimal.valueOf(flag);
        Matrix A = calculateDetA();
        //System.out.println("A:");
        //A.print();
        Matrix B = calculateDetB();
        //System.out.println("B:");
        //B.print();
        Matrix C = calculateDetC();
        //System.out.println("C:");
        //C.print();
        Matrix D = calculateDetD();
        //System.out.println("D:");
        //D.print();
        BigDecimal M_x = BigDecimal.valueOf(1);
        for (i = 1; i <= n - k+1; ++i) {
            M_x = M_x.multiply(BigDecimal.valueOf(x[k-1]).pow(i));
        }
        result = result.multiply(M_x);
        Matrix T;
        Matrix Temp_T;
        T = calculateDetT(1);
        for (i = 2; i <= n - 2 * k + 2; ++i) {
            Temp_T = calculateDetT(i);
            T = T.matmul(Temp_T);
        }
        Matrix inverseA = A.inverse();
        D = D.subtract(C.matmul(inverseA).matmul(T).matmul(B));
        return result.multiply(D.determinant());
    }

    public Matrix calculateInverseA(int p) {
        Matrix A = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 1; i++) {
            for (int j = i; j < k - 1; j++) {
                A.set(i, j, BigDecimal.valueOf(x[j - i]).pow(p + 1 + i));
            }
        }
        return A;
    }

    public Matrix calculateInverseB(int p) {
        Matrix B = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 1; i++) {
            for (int j = 0; j <= i; j++) {
                B.set(i, j, BigDecimal.valueOf(x[k - 1 - (i - j)]).pow(n + p - 2 * k + 3+i));
            }
        }
        return B;
    }


    public Matrix calculateInverseC(int p) {
        Matrix C = new Matrix(k - 2, k - 1);
        for (int i = 0; i < k - 2; i++) {
            for (int j = 0; j <= i; j++) {
                C.set(i, j, BigDecimal.valueOf(x[k - 1 - (i - j)]).pow(n + p - k + 2 + i));
            }
        }
        return C;
    }


    public Matrix calculateInverseD(int p) {
        Matrix D = new Matrix(k - 2, k - 1);
        for (int i = 0; i < k - 2; i++) {
            for (int j = 0; i + j < k - 1; j++) {
                D.set(i, i + j, BigDecimal.valueOf(x[j]).pow(n + p - k + 2 + i));
            }
        }
        return D;
    }

    public Matrix calculateInverseT(int p) {
        Matrix T = new Matrix(k - 1, k - 1);
        for (int i = 0; i < k - 2; i++) {
            T.set(i, 0, BigDecimal.valueOf(x[k - 2 - i]).pow(p+1+i).divide(BigDecimal.valueOf(x[k - 1]).pow(p).negate(), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
            T.set(i, i + 1, BigDecimal.ONE);
        }

        T.set(k - 2, 0, BigDecimal.valueOf(x[0]).pow(p + k - 1).divide(BigDecimal.valueOf(x[k - 1]).pow(p).negate(), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
        return T;
    }

    public BigDecimal M_21(int i, int j) throws CloneNotSupportedException {
        int flag = -1;
        if (((i + 1) + k * (n - 2 * k + 2)) % 2 == 0) {
            flag = 1;
        }
        BigDecimal kk = BigDecimal.valueOf(1);
        for (int p = j+1; p <= n - 2 * k + 2 + j; ++p) {
            kk = kk.multiply(BigDecimal.valueOf(x[k-1]).pow(p));
        }
        Matrix T_ps = new Matrix(k - 1, k - 1);
        T_ps = calculateInverseT(j+1);
        for (int p = j + 2; p <= n - 2 * k + 2 + j; ++p) {
            T_ps = T_ps.matmul(calculateInverseT(p));
        }
        BigDecimal det = this.wcDeterminant();
        //BigDecimal det=this.matrix.determinant();
        Matrix A = calculateInverseA(j);
        Matrix B = calculateInverseB(j);
        Matrix C = calculateInverseC(j);
        Matrix D = calculateInverseD(j);
        Matrix Aa = new Matrix(k - 1, k - 2);
        Matrix Cc = new Matrix(k - 2, k - 2);
        for (int in_i = 0; in_i < k - 1; in_i++) {
            for (int in_j = 0; in_j < k - 2; in_j++) {
                if (in_j < i - 1) {
                    Aa.set(in_i, in_j, A.get(in_i, in_j));
                } else {
                    Aa.set(in_i, in_j, A.get(in_i, in_j + 1));
                }
            }
        }
        for (int in_i = 0; in_i < k - 2; in_i++) {
            for (int in_j = 0; in_j < k - 2; in_j++) {
                if (in_j < i - 1) {
                    Cc.set(in_i, in_j, C.get(in_i, in_j));
                } else {
                    Cc.set(in_i, in_j, C.get(in_i, in_j + 1));
                }
            }
        }
        Matrix At = T_ps.matmul(Aa);
        Matrix Ms = new Matrix(k - 1 + k - 2, k - 2 + k - 1);
        for (int in_i = 0; in_i < k - 1; in_i++) {
            for (int in_j = 0; in_j < k - 2; in_j++) {
                Ms.set(in_i, in_j, At.get(in_i, in_j));
            }
            for (int in_j = k - 2; in_j < k - 2 + k - 1; in_j++) {
                Ms.set(in_i, in_j, B.get(in_i, in_j - k + 2));
            }
        }
        for (int in_i = k - 1; in_i < k - 1 + k - 2; in_i++) {
            for (int in_j = 0; in_j < k - 2; in_j++) {
                Ms.set(in_i, in_j, C.get(in_i - k + 1, in_j + 1));
            }
            for (int in_j = k - 2; in_j < k - 2 + k - 1; in_j++) {
                Ms.set(in_i, in_j, D.get(in_i - k + 1, in_j - k + 2));
            }
        }
        return BigDecimal.valueOf(flag).multiply(kk).multiply(Ms.determinant()).divide(det, Precision.MAX_LENGTH, RoundingMode.HALF_UP);

    }


    public Matrix wcInverseKCBM() throws CloneNotSupportedException {
        Matrix result = new Matrix(this.matrix.getRows(), this.matrix.getColumns());
    // Step 1. Calculate det of input matrix.
        BigDecimal det = this.wcDeterminant();
        //BigDecimal det = this.matrix.determinant();
        //System.out.println("det: " + det);
    // Step 2. Calculate Inverse M[i+j-1][j] according to Eq. 21.
        for (int i = 1; i < k; ++i) {
            for (int j = 1; j < k; ++j) {
                result.set(i + j - 2, j - 1, M_21(i, j));
            }
        }
    // Step 3. Calculate Inverse M[((i-1)mod(n))+1][j] according to Eq. 22.
        BigDecimal temp_M;
        for (int j = 1; j < k; ++j) {
            for (int i = k - 1 + j; i < n + j; ++i) {
                temp_M = BigDecimal.valueOf(0);
                for (int p = 1; p <k; ++p) {
                    temp_M = temp_M.add((BigDecimal.valueOf(x[p -1]).pow(i - k + 2)).multiply(result.get(i-k, j - 1)));
                }

                result.set((i - 1) % n, j - 1, temp_M.divide(BigDecimal.valueOf(-1 * x[k - 1]).pow(i - k + 2), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
            }
        }
//        BigDecimal temp_M;
//        for (int j = 1; j < k; ++j) {
//            for (int i = k - 1 + j; i < n + j; ++i) {
//                temp_M = BigDecimal.valueOf(0);
//                for (int p = i - k; p < i - 1; ++p) {
//                    temp_M = temp_M.add((BigDecimal.valueOf(x[p - i + k]).pow(i - k + 2)).multiply(result.get(p, j - 1)));
//                }
//
//                result.set((i - 1) % n, j - 1, temp_M.divide(BigDecimal.valueOf(-1 * x[k - 1]).pow(i - k + 2), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
//            }
//        }
        // Step 4. Calculate Inverse M[i][j] according to Eq. 23.
        for (int i = 1; i <=n; ++i) {
            for (int j = k; j <=n; ++j) {
                if (i != j - 1) {
                    temp_M = BigDecimal.valueOf(0);
                    for (int p = j - k; p < j - 1; ++p) {
                        temp_M = temp_M.add((BigDecimal.valueOf(x[j - p - 1]).pow(p+1)).multiply(result.get(i - 1, p)));
                    }
                    result.set(i - 1, j - 1, temp_M.divide(BigDecimal.valueOf(-1 * x[0]).pow(j ), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
                } else {
                    temp_M = BigDecimal.valueOf(0);
                    for (int p = j - k; p < j - 1; ++p) {
                        temp_M = temp_M.add((BigDecimal.valueOf(x[j - p - 1])).pow(p+1).multiply(result.get(i - 1, p)));
                    }
                    temp_M = temp_M.subtract(BigDecimal.valueOf(1));
                    result.set(i - 1, j - 1, temp_M.divide(BigDecimal.valueOf(-1 * x[0]).pow(j ), Precision.MAX_LENGTH, RoundingMode.HALF_UP));
                }


            }
        }
        // Step 4. Construct Inverse M.
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
        Matrix inverse = wcInverseKCBM();
        System.out.println("wcInverseKCBM:");
        inverse.print();
//        Matrix Inverse = this.matrix.inverse();
//        System.out.println("Inverse:");
//        Inverse.print();
//        System.out.println(result.compareTo(this.matrix.matmul(Inverse)));
        Matrix temp = this.matrix.matmul(inverse);
        temp.print();
        return result.compareTo(temp);
    }

}









