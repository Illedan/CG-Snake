import com.codingame.gameengine.runner.MultiplayerGameRunner;

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
        MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();

        // Adds as many player as you need to test your game
        gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent(Agent1.class);
        gameRunner.addAgent(Agent1.class);

        // Another way to add a player
        //gameRunner.addAgent("dotnet run --project /Users/erikkvanli/Repos/CG-Snake/config/BossProj/BossProj.csproj ");
        //gameRunner.addAgent("dotnet run --project /Users/erikkvanli/Repos/CG-Snake/config/BossProj/BossProj.csproj ");
        //gameRunner.addAgent("dotnet run --project /Users/erikkvanli/Repos/CG-Snake/config/BossProj/BossProj.csproj ");

        gameRunner.start();
    }
}
