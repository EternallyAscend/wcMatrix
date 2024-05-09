import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        try {
            kCM kCM = new kCM(8, new int[]{1, 2, 3, 4});

//            kCM kCM = new kCM(8, new int[]{1, 2, 3});
//            kCM kCM = new kCM(7, new int[]{1, 2, 3});
//            kCM kCM = new kCM(6, new int[]{1, 2, 3});

//            kCM kCM = new kCM(8, new int[]{1, 2});
//            kCM kCM = new kCM(7, new int[]{1, 2});
//            kCM kCM = new kCM(6, new int[]{1, 2});
//            kCM kCM = new kCM(5, new int[]{1, 2});
//            kCM kCM = new kCM(4, new int[]{1, 2});

//            System.out.println(kCM.getMatrix().determinant());
//            System.out.println(kCM.wcDeterminant());
            System.out.println(kCM.testWcInverse());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("============");
        }
    }
}
