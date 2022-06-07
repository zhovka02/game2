package computer;

import player.Choices;
import player.Player;
import player.PlayerStatusException;
import player.Status;

public class Computer implements Player {

    private int score;
    private Status currentStatus;
    private String playerName;
    private Choices playerChoice;

    public Computer() {
        this.playerName = "Big Brother";
        this.playerChoice = null;
        this.currentStatus = Status.CHOICE_NOT_MADE;
        this.score = 0;

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
        return this.playerName;
    }

    @Override
    public void updateStatus() {
        if (this.currentStatus == Status.CHOICE_NOT_MADE)
            this.currentStatus = Status.CHOICE_MADE;
        else {
            this.currentStatus = Status.CHOICE_NOT_MADE;
            this.playerChoice = null;
        }
    }

    @Override
    public Status getStatus() {
        return this.currentStatus;
    }

    @Override
    public void setChoice(Choices choice) {
        this.updateStatus();
        this.playerChoice = choice;
    }

    @Override
    public Choices getChoice() throws PlayerStatusException {
        if (this.currentStatus == Status.CHOICE_NOT_MADE)
            throw new PlayerStatusException(this.playerName);
        return this.playerChoice;
    }
}
