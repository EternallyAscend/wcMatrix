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

    public static void buildKCM() {
        // TODO Build kCM Matrix.
    }
}
