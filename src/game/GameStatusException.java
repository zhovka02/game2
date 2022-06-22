package game;

public class GameStatusException extends Exception {
    public GameStatusException(String message) {
        super("Incorrect game status:\n" + message);
    }
}
