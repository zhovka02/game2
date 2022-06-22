package game;

public interface RoPaScLiSpAPI {

    int DEFAULT_PORT = 1408;

    /**
     * Determines the result of the first player in the round.
     * <p>
     * Before judging, verifies that both players have made a selection.
     * <p>
     * Also, at the end of the judging, updates the players' statuses to CHOICE_NOT_MADE.
     *
     * @return enum {@link Result} - result of first player
     * @throws GameStatusException
     */
    Result judge() throws GameStatusException;

}
