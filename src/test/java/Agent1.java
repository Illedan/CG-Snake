import java.util.Random;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String def = scanner.nextLine();
        System.err.println(def);
        Random rnd = new Random();
        String[] poss = new String[]{ "S", "N", "W", "E"};
        while (true) {
            String input = scanner.nextLine();
            System.out.println(poss[rnd.nextInt(4)]);
        }
    }
}
