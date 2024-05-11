import java.math.BigDecimal;

;

public class Main {
    public static void main(String[] args) {
        int rows = 4;
        int columns = 4;
        Matrix matrix = new Matrix(rows, columns);
        try {
//            testInverse();
//            matrix.clone().print();
//            buildKCM(8, 2, new int[]{1, 2}).print();
            kCM kCM = new kCM(8, new int[]{1, 2, 3, 4});
//            kCM.getMatrix().power(2).print();
//            System.out.println();
//            kCM.getMatrix().quickPower(2).print();

//            kCM kCM = new kCM(8, new int[]{1, 2, 3});
//            kCM kCM = new kCM(7, new int[]{1, 2, 3});
//            kCM kCM = new kCM(6, new int[]{1, 2, 3});

//            kCM kCM = new kCM(8, new int[]{1, 2});
//            kCM kCM = new kCM(7, new int[]{1, 2});
//            kCM kCM = new kCM(6, new int[]{1, 2});
//            kCM kCM = new kCM(5, new int[]{1, 2});
//            kCM kCM = new kCM(4, new int[]{1, 2});
//            kCM.print();
//            System.out.println(kCM.getMatrix().determinant());
//            System.out.println(kCM.wcDeterminant());
            System.out.println(kCM.testWcInverse());
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
}

