package player;

public interface Player {
    /**
     * Gets the current Player's score
     *
     * @return current score of Player
     */
    int getScore();

    /**
     * Increments the Player's score
     */
    void incScore();

    /**
     * Gets the name of Player
     *
     * @return name of Player
     */
    String getPlayerName();

    /**
     * Updates the current Player's status
     * from CHOICE_MADE to CHOICE_NOT_MADE and vice versa
     */
    void updateStatus();

    /**
     * Gets the current status of player
     *
     * @return enum {@link Status} - player's status at the moment
     */
    Status getStatus();

    /**
     * Updates the current Player's choice
     *
     * @param choice - instance of {@link Choices} to set
     */
    void setChoice(Choices choice);

    /**
     * Gets the current choice of player
     *
     * @return enum Choices - player's choice at the moment
     * @throws PlayerStatusException If the player has not yet made a choice
     */
    Choices getChoice() throws PlayerStatusException;


}
