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
            int alive = scanner.nextInt();
            System.err.println(alive);
            for(int i = 0; i < alive; i++){
                String input = scanner.nextLine();
                System.err.println(input);
            }

            for(int i = 0; i < 30; i++){
                String input = scanner.nextLine();
                System.err.println(input);
            }

            System.out.println(poss[rnd.nextInt(4)]);
        }
    }
}
