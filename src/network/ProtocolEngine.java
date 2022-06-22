package network;

import game.GameStatusException;
import player.Choices;

import java.io.IOException;

public interface ProtocolEngine {
    /**
     * Sends the player's name to the opponent and gets his name
     * @param playerName name of current player
     * @return name of opponent
     */
    String handshake(String playerName);

    /**
     * Send player's choice to the opponent to output stream
     * @param choice current player's choice
     * @throws IOException if IOStreams are closed
     */
    void sendToOpponent(Choices choice) throws IOException;

    /**
     * Gets  opponent's choice from input stream
     * @return opponent's choice
     * @throws GameStatusException if opponent is killed
     * @throws IOException if IOStreams are closed
     */
    Choices waitAndGetOpponentChoice() throws GameStatusException,IOException;

    /**
     * Closes the current stream and garant that all other methods can not try to access closed streams
     * @throws IOException if IOStream are already closed
     */

    void kill() throws IOException;
}
