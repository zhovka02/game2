package game;

public class GameStatusException extends Exception {
    public GameStatusException(GameStatus gameStatus) {
        super("Incorrect game status:\n" + gameStatus.getStatus());
    }
}
