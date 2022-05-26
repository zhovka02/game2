package game;

import player.Player;
import player.PlayerStatusException;
import player.Status;

import java.io.PrintStream;

public class RockPaperScissorsLizardSpockImpl implements RoPaScLiSpAPI, Score {

    Player playerOne, playerTwo;


    public RockPaperScissorsLizardSpockImpl(Player playerOne, Player playerTwo){
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    @Override
    public void printScore(PrintStream os) {
        StringBuilder stringToOutput = new StringBuilder();
        stringToOutput.append("Current score is \n");
        stringToOutput.append(this.playerOne.getPlayerName());
        stringToOutput.append(": ");
        stringToOutput.append(this.playerOne.getScore());
        stringToOutput.append("\n");
        stringToOutput.append(this.playerTwo.getPlayerName());
        stringToOutput.append(": ");
        stringToOutput.append(this.playerTwo.getScore());
        stringToOutput.append("\n");
        if (this.playerOne.getScore() == this.playerTwo.getScore())
            stringToOutput.append(Result.DRAW.getResult());
        else if (this.playerOne.getScore() > this.playerTwo.getScore()){
            stringToOutput.append(playerOne.getPlayerName());
            stringToOutput.append(" ");
            stringToOutput.append(Result.WIN.getResult());
            stringToOutput.append("\n");
            stringToOutput.append(playerTwo.getPlayerName());
            stringToOutput.append(" ");
            stringToOutput.append(Result.LOSE.getResult());
        }
        else
        {
            stringToOutput.append(playerTwo.getPlayerName());
            stringToOutput.append(" ");
            stringToOutput.append(Result.WIN.getResult());
            stringToOutput.append("\n");
            stringToOutput.append(playerOne.getPlayerName());
            stringToOutput.append(" ");
            stringToOutput.append(Result.LOSE.getResult());
        }
        os.println(stringToOutput);
    }

    private boolean isNotReadyToJudge(){
        return this.getCurrentStatus() != GameStatus.READY_TO_JUDGE;
    }

    public GameStatus getCurrentStatus() {
        if (playerOne.getStatus() == Status.CHOICE_MADE && playerTwo.getStatus() == Status.CHOICE_MADE){
            return GameStatus.READY_TO_JUDGE;
        }
        else if (playerOne.getStatus() == Status.CHOICE_MADE && playerTwo.getStatus() == Status.CHOICE_NOT_MADE) {
            return GameStatus.ONLY_ONE_CHOICE;
        }
        else if (playerOne.getStatus() == Status.CHOICE_NOT_MADE && playerTwo.getStatus() == Status.CHOICE_MADE){
            return GameStatus.ONLY_ONE_CHOICE;
        }
        else {
            return GameStatus.NO_CHOICES;
        }
    }

    @Override
    public Result judge() throws GameStatusException {
        if (this.isNotReadyToJudge()){
            throw new GameStatusException(this.getCurrentStatus());
        }
        Result result;
        try {
            if ((playerOne.getChoice().ordinal() == 0 && playerTwo.getChoice().ordinal() == 0)
                    || (playerOne.getChoice().ordinal() == 1 && playerTwo.getChoice().ordinal() == 1)
                    || (playerOne.getChoice().ordinal() == 2 && playerTwo.getChoice().ordinal() == 2)
                    || (playerOne.getChoice().ordinal() == 3 && playerTwo.getChoice().ordinal() == 3)
                    || (playerOne.getChoice().ordinal() == 4 && playerTwo.getChoice().ordinal() == 4)) {
                result = Result.DRAW;
            } else if ((playerOne.getChoice().ordinal() == 0 && (playerTwo.getChoice().ordinal() == 1 || playerTwo.getChoice().ordinal() == 3))
                    || (playerOne.getChoice().ordinal() == 1 && (playerTwo.getChoice().ordinal() == 2 || playerTwo.getChoice().ordinal() == 3))
                    || (playerOne.getChoice().ordinal() == 2 && (playerTwo.getChoice().ordinal() == 0 || playerTwo.getChoice().ordinal() == 4))
                    || (playerOne.getChoice().ordinal() == 3 && (playerTwo.getChoice().ordinal() == 2 || playerTwo.getChoice().ordinal() == 4))
                    || (playerOne.getChoice().ordinal() == 4 && (playerTwo.getChoice().ordinal() == 0 || playerTwo.getChoice().ordinal() == 1))) {
                playerOne.incScore();
                result = Result.WIN;
            } else {
                playerTwo.incScore();
                result = Result.LOSE;
            }
        } catch (PlayerStatusException e) {
            throw new GameStatusException(this.getCurrentStatus());
        }
        resetStatus();
        return result;
    }

    private void resetStatus() throws GameStatusException{
        if (isNotReadyToJudge())
            throw new GameStatusException(this.getCurrentStatus());
        playerOne.updateStatus();
        playerTwo.updateStatus();
    }
}
