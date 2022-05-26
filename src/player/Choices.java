package player;

public enum Choices {
    ROCK("rock"),
    SCISSORS("scissors"),
    PAPER("paper"),
    LIZARD("lizard"),
    SPOCK("spock");

    private final String value;

    Choices(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }
}
