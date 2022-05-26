package game;

public enum GameStatus {
    NO_CHOICES("no choices are made"),
    ONLY_ONE_CHOICE("only one choice is made"),
    READY_TO_JUDGE("ready to be judged");

    private final String status;

    GameStatus(String s) {
        this.status = s;
    }

    public String getStatus() {
        return this.status;
    }
}
