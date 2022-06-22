package network;

import game.GameStatusException;
import player.Choices;

import java.io.*;

public class RockPaperScissorsLizardSpockProtocolEngine implements ProtocolEngine {

    private final DataInputStream is;
    private final DataOutputStream os;
    private boolean connected;
    private static final int KILL_CHOICE = 10;

    public RockPaperScissorsLizardSpockProtocolEngine(InputStream is, OutputStream os) {
        this.is = new DataInputStream(is);
        this.os = new DataOutputStream(os);
        connected = true;
    }

    public void kill() throws IOException {
        connected = false;
        this.os.writeInt(KILL_CHOICE);
        is.close();
        os.close();
    }

    public String handshake(String playerName) {
        try {
            this.os.writeUTF(playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String opponentName = null;
        while (connected) {
            try {
                opponentName = this.is.readUTF();
                break;
            } catch (EOFException e) {
                // another try to read
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return opponentName;
    }

    public void sendToOpponent(Choices choice) throws IOException {
        int intChoice = choice.ordinal(); // serialization via ordinal() from Choices
        this.os.writeInt(intChoice);
    }

    public Choices waitAndGetOpponentChoice() throws GameStatusException, IOException {
        int intOpponentChoice = -1;
        while (connected) {
            try {
                intOpponentChoice = this.is.readInt();
                break;
            } catch (EOFException e) {
                // another try to read
            }
        }
        // deserialization in the same way, via int values of Choices
        return switch (intOpponentChoice) {
            case 0 -> Choices.ROCK;
            case 1 -> Choices.SCISSORS;
            case 2 -> Choices.PAPER;
            case 3 -> Choices.LIZARD;
            case 4 -> Choices.SPOCK;
            case KILL_CHOICE -> throw new GameStatusException("opponent not available");
            default -> throw new IllegalArgumentException("incorrect choice");
        };
    }
}
