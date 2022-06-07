package ui;

import computer.Computer;
import game.GameStatus;
import game.GameStatusException;
import game.Result;
import game.RockPaperScissorsLizardSpockImpl;
import player.Choices;
import player.Player;
import player.PlayerImpl;
import player.PlayerStatusException;

import java.io.*;
import java.util.Arrays;


public class RockPaperScissorsLizardSpockUI {

    private static final String SCORE = "score";
    private static final String EXIT = "exit";
    private static final String CONNECT = "connect";
    private static final String OPEN = "open";
    private static final String CHOOSE = "choose";
    private static final String RULES = "rules";
    private final PrintStream outStream;
    private final BufferedReader inBufferedReader;
    private final Player player;
    private RockPaperScissorsLizardSpockImpl gameEngine;
    private final Player opponent;

    public static void main(String[] args) {
        System.out.println("Welcome to Rock-Paper-Scissors-Lizard-Spock version 0.01a");

        if (args.length < 1) {
            System.err.println("need playerName as parameter");
            System.exit(1);
        }

        System.out.println("Welcome " + args[0]);
        System.out.println("Let's play a game");

        RockPaperScissorsLizardSpockUI userCmd = new RockPaperScissorsLizardSpockUI(args[0], System.out, System.in);
        userCmd.printUsage();
        userCmd.runCommandLoop();

    }

    public RockPaperScissorsLizardSpockUI(String playerName, PrintStream os, InputStream is) {

        this.player = new PlayerImpl(playerName);
        this.outStream = os;
        this.inBufferedReader = new BufferedReader(new InputStreamReader(is));
        this.opponent = new Computer();
    }

    private void printUsage() {
        StringBuilder b = new StringBuilder();

        b.append("\n");
        b.append("\n");
        b.append("Valid commands are:");
        b.append("\n");
        b.append(CONNECT);
        b.append(".. connect as tcp client");
        b.append("\n");
        b.append(OPEN);
        b.append(".. open port become tcp server");
        b.append("\n");
        b.append(SCORE);
        b.append(".. print score");
        b.append("\n");
        b.append(CHOOSE);
        b.append(".. choose between rock, scissors, paper, lizard and spock");
        b.append("\n");
        b.append(RULES);
        b.append(".. print rules");
        b.append("\n");
        b.append(EXIT);
        b.append(".. exit");

        this.outStream.println(b);
    }

    public void runCommandLoop() {
        boolean again = true;

        while (again) {
            String cmdLineString;

            try {
                // read user input
                cmdLineString = inBufferedReader.readLine();

                // finish that loop if less than nothing came in
                if (cmdLineString == null) break;

                // trim whitespaces on both sides
                cmdLineString = cmdLineString.trim();

                // extract command and parameters (can be only command)
                String[] cmdLineStringParsed = cmdLineString.split("\\s+");
                String command = cmdLineStringParsed[0];
                String[] parameters = Arrays.copyOfRange(cmdLineStringParsed, 1, cmdLineStringParsed.length);
                switch (command) {
                    case SCORE -> this.doScore();
                    case CONNECT -> this.doConnect(parameters);
                    case OPEN -> this.doOpen();
                    case CHOOSE -> this.doChoose(parameters);
                    case RULES -> this.doRules();
                    case EXIT -> {
                        again = false;
                        this.doExit();
                    }
                    default -> {
                        this.outStream.println("unknown command: " + cmdLineString);
                        this.printUsage();
                    }
                }
            } catch (IOException ex) {
                this.outStream.println("cannot read from input stream");
                this.doExit();
            } catch (RuntimeException ex) {
                this.outStream.println("runtime problems: " + ex.getLocalizedMessage());
            }
        }
    }

    private void doExit() {
    }

    private void doRules() {
        StringBuilder b = new StringBuilder();

        b.append("+---------------------+-------+--------+----------+-------+------+\n");
        b.append("|       Your choice → | Spock | Lizard | Scissors | Paper | Rock |\n");
        b.append("| ↓ Opponent's choice |       |        |          |       |      |\n");
        b.append("+---------------------+-------+--------+----------+-------+------+\n");
        b.append("| Spock               | tie   | win    | lose     | win   | lose |\n");
        b.append("+---------------------+-------+--------+----------+-------+------+\n");
        b.append("| Lizard              | lose  | tie    | win      | lose  | win  |\n");
        b.append("+---------------------+-------+--------+----------+-------+------+\n");
        b.append("| Scissors            | win   | lose   | tie      | lose  | win  |\n");
        b.append("+---------------------+-------+--------+----------+-------+------+\n");
        b.append("| Paper               | lose  | win    | win      | tie   | lose |\n");
        b.append("+---------------------+-------+--------+----------+-------+------+\n");
        b.append("| Rock                | win   | lose   | lose     | win   | tie  |\n");
        b.append("+---------------------+-------+--------+----------+-------+------+");

        this.outStream.println(b);
    }

    private void doChoose(String[] parameters) {
        try {
            Choices userChoice = setChoose(parameters[0]);
            this.player.setChoice(userChoice);
            this.outStream.println("You have chosen " + userChoice.getValue());
            if (this.gameEngine.getCurrentStatus() != GameStatus.READY_TO_JUDGE)
                this.outStream.println("waiting to the opponent");
            while (this.gameEngine.getCurrentStatus() != GameStatus.READY_TO_JUDGE) {
                Choices[] choices = {Choices.SPOCK, Choices.ROCK, Choices.PAPER, Choices.SCISSORS, Choices.LIZARD};
                opponent.setChoice(choices[(int) (Math.random() * 5 + 1) - 1]);
            }
            this.outStream.println(opponent.getPlayerName() + " chose " + opponent.getChoice());
            Result result = gameEngine.judge();
            switch (result) {
                case WIN -> this.outStream.println("You win");
                case LOSE -> this.outStream.println(opponent.getPlayerName() + " wins");
                case TIE -> this.outStream.println(Result.TIE.getResult());
            }

        } catch (IllegalArgumentException ex) {
            this.outStream.println(ex.getLocalizedMessage());
            this.outStream.println("please type in rock or scissors or paper or lizard or spock");
        } catch (IndexOutOfBoundsException ex) {
            this.outStream.println("don't have any choice :(");
        } catch (GameStatusException | PlayerStatusException e) {
            e.printStackTrace();
        }
    }

    private Choices setChoose(String userChoice) throws IllegalArgumentException {
        return switch (userChoice) {
            case "rock" -> Choices.ROCK;
            case "scissors" -> Choices.SCISSORS;
            case "paper" -> Choices.PAPER;
            case "lizard" -> Choices.LIZARD;
            case "spock" -> Choices.SPOCK;
            default -> throw new IllegalArgumentException("incorrect choice");
        };
    }

    private void doOpen() {

    }

    private void doConnect(String[] parameters) {

        this.gameEngine = new RockPaperScissorsLizardSpockImpl(player, opponent);
    }

    private void doScore() {
        // TODO: 25.05.2022
        /*
            correct status handling, it means, we must check, that second player exist right now
            and we can print his name and score.
         */
        this.gameEngine.printScore(outStream);

    }

}
