import java.math.BigDecimal;
import java.math.BigInteger;

;

public class Main {
    public static void main(String[] args) {
        int rows = 16;
        int columns = 16;
        Matrix matrix = new Matrix(rows, columns);

        matrix.print();
        try {
            testInverse();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            matrix.print();
        }
    }

    public static void testInverse() throws Exception {
        int rows = 3;
        int columns = 3;
        Matrix matrix = new Matrix(rows, columns);
        Matrix result = new Matrix(rows, columns);
        Matrix inverse = matrix.inverse();
        Matrix temp = matrix.multiply(inverse);
        System.out.println(result.equals(temp));
    }
}

