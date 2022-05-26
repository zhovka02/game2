package player;

public class PlayerImpl implements Player{

    private int score;
    private Status currentStatus;
    private String playerName;
    private Choices playerChoice;


    public PlayerImpl(String playerName){
        this.playerName = playerName;
        this.score = 0;
        this.currentStatus = Status.CHOICE_NOT_MADE;
        this.playerChoice = null;
    }
    @Override
    public int getScore() {
        return this.score;
    }


    @Override
    public void incScore() {
        this.score++;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void setPLayerName(String name) {
        this.playerName = name;
    }

    @Override
    public void updateStatus() {
        if (this.currentStatus == Status.CHOICE_NOT_MADE)
            this.currentStatus = Status.CHOICE_MADE;
        else
        {
            this.currentStatus = Status.CHOICE_NOT_MADE;
            this.playerChoice = null;
        }
    }

    @Override
    public Status getStatus() {
        return currentStatus;
    }


    @Override
    public void setChoice(Choices choice) {
        this.playerChoice = choice;
        this.updateStatus();
    }

    @Override
    public Choices getChoice() throws PlayerStatusException {
        if (this.currentStatus == Status.CHOICE_NOT_MADE)
            throw new PlayerStatusException(this.playerName);
        return this.playerChoice;
    }
}
