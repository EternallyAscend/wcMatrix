import java.math.BigDecimal;

;

public class Main {
    public static void main(String[] args) {
        int rows = 4;
        int columns = 4;
        Matrix matrix = new Matrix(rows, columns);

//        matrix.print();
        try {
            testInverse();
//            matrix.clone().print();
            buildKCM(8, 2, new int[]{1, 2}).print();
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
        // TODO Build kCM Matrix.
        Matrix result = new Matrix(n, n);
        int head = n - k + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                result.set(i, (head + i + j) % n, BigDecimal.valueOf(x[j]));
            }
        }
        return result;
    }
}

