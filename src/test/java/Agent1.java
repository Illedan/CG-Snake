import java.util.Random;
import java.util.Scanner;

public class Agent1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String def = scanner.nextLine();
        System.err.println(def);
        Random rnd = new Random();
        String[] poss = new String[]{ "S",  "E"};
        int i = 0;
        while (true) {
            System.out.println(poss[rnd.nextInt(2)]);
            if(i++ > 500) break;
        }
    }
}
