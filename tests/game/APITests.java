package game;

import org.junit.Assert;
import org.junit.Test;
import player.Choices;
import player.Player;
import player.PlayerImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class APITests {

    private final Player playerOne = new PlayerImpl("Alice");
    private final Player playerTwo = new PlayerImpl("Bob");
    private final RoPaScLiSpAPI gameEngine = new RockPaperScissorsLizardSpockImpl(playerOne, playerTwo);
    private final RockPaperScissorsLizardSpockImpl gameEngineWithPrinter = new RockPaperScissorsLizardSpockImpl(playerOne, playerTwo);

    @Test
    public void judgeTest1() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.LIZARD);
        Result result = gameEngine.judge();
        // Rock > Lizard
        Assert.assertEquals(Result.WIN, result);
    }
    @Test

    public void judgeTest2() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.PAPER);
        Result result = gameEngine.judge();
        // Rock < Paper
        Assert.assertEquals(Result.LOSE, result);
    }
    @Test
    public void judgeTest3() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.ROCK);
        Result result = gameEngine.judge();
        // Rock == Rock
        Assert.assertEquals(Result.DRAW, result);
    }
    @Test
    public void judgeFailTest1() {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob still doesn't have any choice
        Result result = null;
        try {
            result = gameEngine.judge();
            Assert.fail();
        } catch (GameStatusException e) {
            // should be here
        }
    }
    @Test
    public void judgeFailTest2() {
        // Bob makes choice
        playerTwo.setChoice(Choices.PAPER);
        // Alice still doesn't have any choice
        Result result = null;
        try {
            result = gameEngine.judge();
            Assert.fail();
        } catch (GameStatusException e) {
            // should be here
        }
    }

    @Test
    public void judgeFailTest3() {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.LIZARD);
        try {
             Result result = gameEngine.judge();
             result = gameEngine.judge();
             Assert.fail();
        } catch (GameStatusException e) {
            // should be here
        }
    }

    @Test
    public void printScoreAndJudgeTest1() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.LIZARD);
        // Rock > Lizard
        Result result = gameEngineWithPrinter.judge();
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        gameEngineWithPrinter.printScore(new PrintStream(testOut));
        String mustBeOut = """
                Current score is\s
                Alice: 1
                Bob: 0
                Alice wins
                Bob loses
                """;
        Assert.assertEquals(mustBeOut,testOut.toString());
    }

    @Test
    public void printScoreAndJudgeTest2() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.LIZARD);
        // Bob makes choice
        playerTwo.setChoice(Choices.ROCK);
        // Rock < Lizard
        Result result = gameEngineWithPrinter.judge();
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        gameEngineWithPrinter.printScore(new PrintStream(testOut));
        String mustBeOut = """
                Current score is\s
                Alice: 0
                Bob: 1
                Bob wins
                Alice loses
                """;
        Assert.assertEquals(mustBeOut,testOut.toString());
    }

    @Test
    public void printScoreAndJudgeTest3() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.LIZARD);
        // Rock > Lizard
        Result result = gameEngine.judge();
        // Alice makes choice
        playerOne.setChoice(Choices.LIZARD);
        // Bob makes choice
        playerTwo.setChoice(Choices.ROCK);
        // Rock < Lizard
        result = gameEngineWithPrinter.judge();
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        gameEngineWithPrinter.printScore(new PrintStream(testOut));
        String mustBeOut = """
                Current score is\s
                Alice: 1
                Bob: 1
                It's a tie
                """;
        Assert.assertEquals(mustBeOut,testOut.toString());
    }
}