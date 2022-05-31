package ui;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class UITests {
    final String entryMessage = """
            Welcome to Rock-Paper-Scissors-Lizard-Spock version 0.01a
            Welcome Test
            Let's play a game


            Valid commands are:
            connect.. connect as tcp client
            open.. open port become tcp server
            score.. print score
            choose.. choose between rock, scissors, paper, lizard and spock
            rules.. print rules
            exit.. exit
            """;
    @Test
    public void testRules() {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        String input = "rules";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        RockPaperScissorsLizardSpockUI.main(new String[]{"Test"});
        String standardOutput = testOut.toString();
        final String mustBeOut = entryMessage+
                "+---------------------+-------+--------+----------+-------+------+\n" +
                "|       Your choice → | Spock | Lizard | Scissors | Paper | Rock |\n" +
                "| ↓ Opponent's choice |       |        |          |       |      |\n" +
                "+---------------------+-------+--------+----------+-------+------+\n" +
                "| Spock               | tie   | win    | lose     | win   | lose |\n" +
                "+---------------------+-------+--------+----------+-------+------+\n" +
                "| Lizard              | lose  | tie    | win      | lose  | win  |\n" +
                "+---------------------+-------+--------+----------+-------+------+\n" +
                "| Scissors            | win   | lose   | tie      | lose  | win  |\n" +
                "+---------------------+-------+--------+----------+-------+------+\n" +
                "| Paper               | lose  | win    | win      | tie   | lose |\n" +
                "+---------------------+-------+--------+----------+-------+------+\n" +
                "| Rock                | win   | lose   | lose     | win   | tie  |\n" +
                "+---------------------+-------+--------+----------+-------+------+\n";
        Assert.assertEquals(standardOutput, mustBeOut);
    }

    @Test
    public void testChoose1()  {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        String input = "choose kek";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        RockPaperScissorsLizardSpockUI.main(new String[]{"Test"});
        String standardOutput = testOut.toString();
        final String mustBeOut = entryMessage +
                "incorrect choice\n" +
                "please type in rock or scissors or paper or lizard or spock\n";
        Assert.assertEquals(mustBeOut, standardOutput);
    }
    @Test
    public void testChoose2()  {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        String input = "choose";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        RockPaperScissorsLizardSpockUI.main(new String[]{"Test"});
        String standardOutput = testOut.toString();
        final String mustBeOut = entryMessage +
                "don't have any choice :(\n";
        Assert.assertEquals(mustBeOut, standardOutput);
    }
    @Test
    public void invalidInput(){
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        String input = "eat";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        RockPaperScissorsLizardSpockUI.main(new String[]{"Test"});
        String standardOutput = testOut.toString();
        final String mustBeOut = entryMessage +
                "unknown command: eat\n" +
                "\n" +
                "\n" +
                "Valid commands are:\n" +
                "connect.. connect as tcp client\n" +
                "open.. open port become tcp server\n" +
                "score.. print score\n" +
                "choose.. choose between rock, scissors, paper, lizard and spock\n" +
                "rules.. print rules\n" +
                "exit.. exit\n";
        Assert.assertEquals(mustBeOut, standardOutput);
    }
}
