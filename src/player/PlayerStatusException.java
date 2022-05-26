package player;

public class PlayerStatusException extends Exception{
    public PlayerStatusException(String playerName){
        super("The player has not yet made a choice " + playerName);
    }
}
