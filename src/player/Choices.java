package player;

public enum Choices {
    ROCK("rock"),
    SCISSORS("scissors"),
    PAPER("paper"),
    LIZARD("lizard"),
    SPOCK("spock");

    private final String value;
    private final int number;

    Choices(String s) {
        this.value = s;
        this.number = this.ordinal();
    }

    public String getValue() {
        return this.value;
    }

    public int getNumber() {
        return number;
    }


}
