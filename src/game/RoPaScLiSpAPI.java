package game;

import player.PlayerStatusException;

public interface RoPaScLiSpAPI {
    /**
     * Determines the result of the first player in the round.
     * Before judging, verifies that both players have made a selection.
     * Also, at the end of the judging, deletes the players' choices.
     *
     * @throws GameStatusException in case at least one of the players has not yet made a choice.
     * @return enum Result - result of first player
     */
    Result judge() throws GameStatusException;

}
