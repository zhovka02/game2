package ui;

import computer.Computer;
import game.GameStatusException;
import game.RoPaScLiSpAPI;
import game.RockPaperScissorsLizardSpockImpl;
import network.RockPaperScissorsLizardSpockProtocolEngine;
import network.TCPStream;
import player.Choices;
import player.Player;
import player.PlayerImpl;

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
    private Player opponent;
    private TCPStream tcpStream;
    private RockPaperScissorsLizardSpockProtocolEngine protocolEngine;

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
            } catch (IOException | GameStatusException ex) {
                this.outStream.println("cannot connect to opponent - probably, opponent is not available");
                this.doExit();
            } catch (RuntimeException ex) {
                this.outStream.println("runtime problems: " + ex.getLocalizedMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doExit() {
        try {
            protocolEngine.kill();
        } catch (IOException e) {
            // ignore
        }
        this.close();
    }

    private void close() {
        tcpStream.kill();
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

    private void doChoose(String[] parameters) throws GameStatusException, IOException {
        if (this.checkConnection()) {
            try {
                Choices userChoice = setChoose(parameters[0]);
                this.player.setChoice(userChoice);
                this.protocolEngine.sendToOpponent(userChoice);
                System.out.println("you have chosen " + userChoice.getValue() + ", waiting for an answer from your opponent");
                Choices opponentChoice = this.protocolEngine.waitAndGetOpponentChoice();
                System.out.println("the opponent chose the " + opponentChoice.getValue());
                this.opponent.setChoice(opponentChoice);
                this.gameEngine.judge();
                this.gameEngine.printScore(outStream);
            } catch (IllegalArgumentException ex) {
                this.outStream.println(ex.getLocalizedMessage());
                this.outStream.println("please type in rock or scissors or paper or lizard or spock");
            } catch (IndexOutOfBoundsException ex) {
                this.outStream.println("don't have any choice :(");
            }


        } else {
            System.out.println("no connection to the opponent");
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

    private void doOpen() throws IOException {
        this.tcpStream = new TCPStream(RoPaScLiSpAPI.DEFAULT_PORT, true, this.player.getPlayerName());
        this.tcpStream.start();
        this.handshake();


    }

    private void handshake() throws IOException {
        System.out.println("wait for opponent");
        this.tcpStream.waitForConnection();
        this.protocolEngine = new RockPaperScissorsLizardSpockProtocolEngine(this.tcpStream.getInputStream(), this.tcpStream.getOutputStream());
        this.opponent = new PlayerImpl(this.protocolEngine.handshake(player.getPlayerName()));
        this.gameEngine = new RockPaperScissorsLizardSpockImpl(player, opponent);
        System.out.println("game session successfully created! " + player.getPlayerName() + " vs " + opponent.getPlayerName());
    }

    private void doConnect(String[] parameters) throws IOException, InterruptedException {
        int port = Integer.parseInt(parameters[0]);
        this.tcpStream = new TCPStream(port, false, this.player.getPlayerName());
        tcpStream.start();
        this.handshake();
    }

    private boolean checkConnection() {
        if (this.tcpStream == null)
            return false;
        if (!this.tcpStream.checkConnected())
            return false;
        return true;
    }

    private void doScore() {
        if (this.checkConnection())
            this.gameEngine.printScore(outStream);
        else {
            System.out.println("no connection to the opponent");
        }

    }

}
