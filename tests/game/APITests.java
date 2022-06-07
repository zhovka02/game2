package game;

import org.junit.Assert;
import org.junit.Test;
import player.Choices;
import player.Player;
import player.PlayerImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class APITests {

    // set up names for two players
    private final String ALICE = "Alice";
    private final String BOB = "Bob";

    // set up two players
    private final Player playerOne = new PlayerImpl(ALICE);
    private final Player playerTwo = new PlayerImpl(BOB);

    // set up gameEngine without print functions
    private final RoPaScLiSpAPI gameEngine = new RockPaperScissorsLizardSpockImpl(playerOne, playerTwo);

    // set up gameEngine with print function
    private final RockPaperScissorsLizardSpockImpl gameEngineWithPrinter = new RockPaperScissorsLizardSpockImpl(playerOne, playerTwo);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////    One round scenario testing    //////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testRoundAliceWins() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.LIZARD);
        Result result = gameEngine.judge();
        // Rock > Lizard
        Assert.assertEquals(Result.WIN, result);
    }
    @Test

    public void testRoundBobWins() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.PAPER);
        Result result = gameEngine.judge();
        // Rock < Paper
        Assert.assertEquals(Result.LOSE, result);
    }
    @Test
    public void testRoundTie() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.ROCK);
        Result result = gameEngine.judge();
        // Rock == Rock
        Assert.assertEquals(Result.TIE, result);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////     Testing a single-round scenario in which player(s) do not make a choice     /////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testRoundGameStatusException1() {
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
    public void testRoundGameStatusException2() {
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
    public void testRoundGameStatusException3() {
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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////     Testing the scenario of one round followed by the output of the current score.     //////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testRoundWithScorePrint1() throws GameStatusException {
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
    public void testRoundWithScorePrint2() throws GameStatusException {
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
    public void testRoundWithScorePrint3() throws GameStatusException {
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
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////                 Testing a single round scenario with score processing                  //////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final int WINNER = 1;
    private final int LOSER = 0;

    @Test
    public void testRoundWithScore1() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.SCISSORS);
        // Rock > SCISSORS
        Result result = gameEngine.judge();
        Assert.assertEquals(WINNER, playerOne.getScore());
        Assert.assertEquals(LOSER, playerTwo.getScore());
    }
    @Test
    public void testRoundWithScore2() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.ROCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.SPOCK);
        // Rock < Spock
        Result result = gameEngine.judge();
        Assert.assertEquals(LOSER, playerOne.getScore());
        Assert.assertEquals(WINNER, playerTwo.getScore());
    }
    @Test
    public void testRoundWithScore3() throws GameStatusException {
        // Alice makes choice
        playerOne.setChoice(Choices.SPOCK);
        // Bob makes choice
        playerTwo.setChoice(Choices.SPOCK);
        // SPOCK = SPOCK
        Result result = gameEngine.judge();
        Assert.assertEquals(LOSER, playerOne.getScore());
        Assert.assertEquals(LOSER, playerTwo.getScore());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////                    Testing of judging for accordance with the rules                    //////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final Result[][] rules = {
            {Result.TIE, Result.WIN, Result.WIN, Result.LOSE, Result.LOSE},
            {Result.LOSE, Result.TIE, Result.LOSE, Result.WIN, Result.WIN},
            {Result.LOSE, Result.WIN, Result.TIE, Result.WIN, Result.LOSE},
            {Result.WIN, Result.LOSE, Result.LOSE, Result.TIE, Result.WIN},
            {Result.WIN, Result.LOSE, Result.WIN, Result.LOSE, Result.TIE},
    };

    @Test
    public void testJudge() throws GameStatusException {
        for (Choices playerOneChoice : Choices.values()){
            for (Choices playerTwoChoice : Choices.values()){
                playerOne.setChoice(playerOneChoice);
                playerTwo.setChoice(playerTwoChoice);
                Result currentResult = gameEngine.judge();
                int playerOneChoicePosition = rules.length - playerOneChoice.ordinal() - 1;
                int playerTwoChoicePosition = rules.length - playerTwoChoice.ordinal() - 1;
                Result expectedResult = rules[playerTwoChoicePosition][playerOneChoicePosition];
                Assert.assertEquals(expectedResult, currentResult);
            }
        }
    }
}