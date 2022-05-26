package player;

import org.junit.Assert;
import org.junit.Test;

public class playerTests {

    private final Player player = new PlayerImpl("Alice");
    private final Choices CHOICE = Choices.ROCK;

    @Test
    public void playerTest1() throws PlayerStatusException {
        // Alice makes choice
        player.setChoice(CHOICE);
        // try to access Alice's choice
        Assert.assertEquals(CHOICE, player.getChoice());
    }
    @Test
    public void playerTest2() {
        // Alice doesn't make any choice
        // try to access Alice's choice
        try {
            player.getChoice();
            Assert.fail();
        }
        catch (PlayerStatusException e) {
            // should be here
        }
    }
}
