package test;

import model.artificialintelligence.Minimax;
import model.artificialintelligence.MoveStrategy;
import model.board.Board;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxTest {
    @BeforeEach
    void reportTestInfo(TestReporter testReporter, TestInfo testInfo) {
        testReporter.publishEntry("Running " + testInfo.getDisplayName()
                + " with tags " + testInfo.getTags());
    }

    @AfterEach
    void printTestState() {
        System.out.println("Test's state: successful");
    }

    @Test
    @DisplayName("Test Opening Depth 1")
    @Tag("finished")
    void testOpeningDepth1() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(1);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(24L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 2")
    @Tag("finished")
    void testOpeningDepth2() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(2);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(576L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 3")
    @Tag("finished")
    void testOpeningDepth3() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(3);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(12_240L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 4")
    @Tag("finished")
    void testOpeningDepth4() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(4);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(260_099L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 5")
    @Tag("finished")
    void testOpeningDepth5() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(5);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(5_111_620L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 6")
    @Tag("finished")
    void testOpeningDepth6() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(6);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(100_453_636L, numEvaluatedBoards);
    }
}
