package game;

public enum Result {
    WIN("wins"),
    LOSE("loses"),
    TIE("It's a tie");

    private final String result;

    Result(String s) {
        this.result = s;
    }

    public String getResult() {
        return this.result;
    }
}
