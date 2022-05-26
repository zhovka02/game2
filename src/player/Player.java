package player;

public interface Player {
    /**
     * Gets the current player.Player's score
     * @return current score of player.Player
     */
    int getScore();

    /**
     * Increments the player.Player's score
     */
    void incScore();

    /**
     * Gets the name of player.Player
     * @return name of player.Player
     */
    String getPlayerName();

    /**
     * Updates the current Player's name
     * @param name - player.Player's name to update the current one
     */
    void setPLayerName(String name);

    /**
     * Updates the current Player's status
     * from CHOICE_MADE to CHOICE_NOT_MADE and vice versa
     */
    void updateStatus();

    /**
     * Gets the current status of player
     * @return enum Status - player's status at the moment
     */
    Status getStatus();

    /**
     * Updates the current Player's choice
     *
     */
    void setChoice(Choices choice);

    /**
     * Gets the current choice of player
     * @throws PlayerStatusException If the player has not yet made a choice
     * @return enum Choices - player's choice at the moment
     */
    Choices getChoice() throws PlayerStatusException;


}
