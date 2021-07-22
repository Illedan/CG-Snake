import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        // Uncomment this section and comment the other one to create a Solo Game
        /* Solo Game */
        // SoloGameRunner gameRunner = new SoloGameRunner();

        // Sets the player
        // gameRunner.setAgent(Player1.class);

        // Sets a test case
        // gameRunner.setTestCase("test1.json");

        /* Multiplayer Game */
        SoloGameRunner gameRunner = new SoloGameRunner();

        // Adds as many player as you need to test your game
        gameRunner.setAgent(Agent1.class);
        gameRunner.setTestCase("test1.json");

        // Another way to add a player
        //gameRunner.addAgent("dotnet run --project /Users/erikkvanli/Repos/CG-Snake/config/BossProj/BossProj.csproj ");
        //gameRunner.addAgent("dotnet run --project /Users/erikkvanli/Repos/CG-Snake/config/BossProj/BossProj.csproj ");
        //gameRunner.addAgent("dotnet run --project /Users/erikkvanli/Repos/CG-Snake/config/BossProj/BossProj.csproj ");

        gameRunner.start();
    }
}
