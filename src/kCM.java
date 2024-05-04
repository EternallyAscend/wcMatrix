import java.math.BigDecimal;

public class kCM {
    int n;
    int k;
    int[] x;
    Matrix matrix;
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

    public static Matrix wcInverseKCM(Matrix input) {
        Matrix result = new Matrix(input.getRows(), input.getColumns());
        // Step 1. Calculate det of input matrix.
        BigDecimal det = input.determinant();
        // Step 2. Calculate y_1 to y_k-1 according to Eq. 14. TODO
        Matrix A = new Matrix(input.getRows(), input.getColumns());
        // Step 3. Calculate y_k to y_n according to Eq. 15. TODO

        // Step 4. Construct M^-1. TODO

        return result;
    }

    public static boolean testWcInverse(Matrix input) {
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
