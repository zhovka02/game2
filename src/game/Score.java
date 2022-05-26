package game;

import java.io.PrintStream;

public interface Score {

    /**
     * prints current score of two players to the stream
     * @param os - stream to output
     */
    void printScore(PrintStream os);


}
