package game;

public enum Result {
    WIN("wins"),
    LOSE("loses"),
    DRAW("It's a tie");

    private final String result;

    Result(String s) {
        this.result = s;
    }

    public String getResult() {
        return this.result;
    }
}
