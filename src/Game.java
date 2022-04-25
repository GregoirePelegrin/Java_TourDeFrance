public class Game {
    public static GameManager gameManager;

    public static void main(String[] args) {
        Game.gameManager = new GameManager();
        while(!Game.gameManager.gameFinished){
            Game.gameManager.nextStage();
        }
        System.out.println("Winner is Player " + Game.gameManager.winner);
        System.out.println(Game.gameManager.getScores());
    }
}
