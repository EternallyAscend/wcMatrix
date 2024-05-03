import java.math.BigDecimal;

;

public class Main {
    public static void main(String[] args) {
        int rows = 4;
        int columns = 4;
        Matrix matrix = new Matrix(rows, columns);
        try {
            testInverse();
//            matrix.clone().print();
//            buildKCM(8, 2, new int[]{1, 2}).print();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("============");
        }
    }

    public static void testInverse() throws Exception {
        int rows = 3;
        int columns = 3;
        Matrix result = new Matrix(rows, columns);
        Matrix matrix = new Matrix(rows, columns);
        int[][] t = {
                {2, 1, 0},
                {0, 3, 1},
                {1, 0, 4}
        };
        for (int i = 0; i < rows; i++) {
            result.set(i, i, BigDecimal.ONE);
            for (int j = 0; j < columns; j++) {
                matrix.set(i, j, BigDecimal.valueOf(t[i][j]));
            }
        }
        Matrix inverse = matrix.inverse();
        Matrix temp = matrix.matmul(inverse);
        System.out.println(result.compareTo(temp));
//        for (int i = 0; i < rows; i++) {
//            System.out.println(temp.get(i, i));
//            System.out.println(result.get(i, i));
//        }
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

