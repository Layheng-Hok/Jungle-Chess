package test;

import model.artificialintelligence.Minimax;
import model.artificialintelligence.MoveStrategy;
import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.piece.animal.*;
import model.player.PlayerColor;
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
    @Tag("fast")
    void testOpeningDepth1() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(1);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(24L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 2")
    @Tag("fast")
    void testOpeningDepth2() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(2);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(576L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 3")
    @Tag("fast")
    void testOpeningDepth3() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(3);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(12_240L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 4")
    @Tag("slow")
    void testOpeningDepth4() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(4);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(260_099L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 5")
    @Tag("slow")
    void testOpeningDepth5() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(5);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(5_111_620L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Opening Depth 6")
    @Tag("slow")
    void testOpeningDepth6() {
        final Board board = Board.constructStandardBoard();
        final MoveStrategy minimax = new Minimax(6);
        minimax.execute(board);
        final long numEvaluatedBoards = minimax.getNumEvaluatedBoards();
        assertEquals(100_453_636L, numEvaluatedBoards);
    }

    @Test
    @DisplayName("Test Endgame in One (Blue Wins With Enemy's Den Penetrated)")
    @Tag("fast")
    void testEndgameInOne1() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(4, PlayerColor.BLUE));
        builder.setPiece(new Elephant(53, PlayerColor.BLUE));
        builder.setPiece(new Rat(60, PlayerColor.RED));
        builder.setPiece(new Elephant(11, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        assertFalse(board.redPlayer().isDenPenetrated());

        final MoveStrategy minimax = new Minimax(6);
        final Move move = minimax.execute(board);
        assertEquals(Move.MoveFactory.createMove(board, 4, 3), move);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(move.execute()));
    }

    @Test
    @DisplayName("Test Endgame in One (Red Wins With Enemy'sDen Penetrated)")
    @Tag("fast")
    void testEndgameInOne2() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(12, PlayerColor.BLUE));
        builder.setPiece(new Elephant(53, PlayerColor.BLUE));
        builder.setPiece(new Rat(60, PlayerColor.RED));
        builder.setPiece(new Elephant(11, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();
        assertFalse(board.bluePlayer().isDenPenetrated());

        final MoveStrategy minimax = new Minimax(4);
        final Move move = minimax.execute(board);
        assertEquals(Move.MoveFactory.createMove(board, 60, 59), move);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(move.execute()));
    }

    @Test
    @DisplayName("Test Endgame in One (Blue Wins With Enemy Running Out of Pieces)")
    @Tag("fast")
    void testEndgameInOne3() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(24, PlayerColor.BLUE));
        builder.setPiece(new Cat(38, PlayerColor.BLUE));
        builder.setPiece(new Cat(31, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        assertFalse(board.redPlayer().getActivePieces().isEmpty());
        assertFalse(board.redPlayer().getValidMoves().isEmpty());

        final MoveStrategy minimax = new Minimax(4);
        final Move move = minimax.execute(board);
        final Board boardAfterExecution = move.execute();
        assertEquals(Move.MoveFactory.createMove(board, 38, 31), move);
        assertTrue(move.isCaptureMove());
        assertTrue(boardAfterExecution.redPlayer().getActivePieces().isEmpty());
        assertTrue(boardAfterExecution.redPlayer().getValidMoves().isEmpty());
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(boardAfterExecution));
    }

    @Test
    @DisplayName("Test Endgame in One (Red Wins With Enemy Running Out of Pieces)")
    @Tag("fast")
    void testEndgameInOne4() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Lion(10, PlayerColor.BLUE));
        builder.setPiece(new Wolf(38, PlayerColor.RED));
        builder.setPiece(new Leopard(11, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();
        assertFalse(board.bluePlayer().getActivePieces().isEmpty());
        assertFalse(board.redPlayer().getValidMoves().isEmpty());

        final MoveStrategy minimax = new Minimax(4);
        final Move move = minimax.execute(board);
        final Board boardAfterExecution = move.execute();
        assertEquals(Move.MoveFactory.createMove(board, 11, 10), move);
        assertTrue(move.isCaptureMove());
        assertTrue(boardAfterExecution.bluePlayer().getActivePieces().isEmpty());
        assertTrue(boardAfterExecution.bluePlayer().getValidMoves().isEmpty());
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(boardAfterExecution));
    }

    @Test
    @DisplayName("Test Endgame in One (Blue Wins With Enemy Running Out of Valid Moves)")
    @Tag("fast")
    void testEndgameInOne5() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Dog(49, PlayerColor.BLUE));
        builder.setPiece(new Tiger(58, PlayerColor.BLUE));
        builder.setPiece(new Cat(56, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        assertFalse(board.redPlayer().getValidMoves().isEmpty());

        final MoveStrategy minimax = new Minimax(4);
        final Move move = minimax.execute(board);
        final Board boardAfterExecution = move.execute();
        assertEquals(Move.MoveFactory.createMove(board, 58, 57), move);
        assertTrue(boardAfterExecution.redPlayer().getValidMoves().isEmpty());
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(boardAfterExecution));
    }

    @Test
    @DisplayName("Test Endgame in One (Red Wins With Enemy Running Out of Valid Moves)")
    @Tag("fast")
    void testEndgameInOne6() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Wolf(34, PlayerColor.BLUE));
        builder.setPiece(new Leopard(27, PlayerColor.RED));
        builder.setPiece(new Tiger(38, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();
        assertFalse(board.bluePlayer().getValidMoves().isEmpty());

        final MoveStrategy minimax = new Minimax(4);
        final Move move = minimax.execute(board);
        final Board boardAfterExecution = move.execute();
        assertEquals(Move.MoveFactory.createMove(board, 38, 41), move);
        assertTrue(boardAfterExecution.bluePlayer().getValidMoves().isEmpty());
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(boardAfterExecution));
    }

    @Test
    @DisplayName("Test Endgame in Two (Blue Wins With Enemy'sDen Penetrated)")
    @Tag("fast")
    void testEndgameInTwo1() {
        int searchDepth = 4;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Lion(11, PlayerColor.BLUE));
        builder.setPiece(new Elephant(55, PlayerColor.BLUE));
        builder.setPiece(new Rat(17, PlayerColor.RED));
        builder.setPiece(new Lion(62, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board initialBoard = builder.build();

        final MoveStrategy minimax1 = new Minimax(searchDepth);
        final Move move1 = minimax1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 11, 4), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy minimax2 = new Minimax(searchDepth);
        final Move move2 = minimax2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy minimax3 = new Minimax(searchDepth);
        final Move move3 = minimax3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 4, 3), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Red Wins With Enemy'sDen Penetrated)")
    @Tag("fast")
    void testEndgameInTwo2() {
        int searchDepth = 4;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(52, PlayerColor.BLUE));
        builder.setPiece(new Lion(6, PlayerColor.BLUE));
        builder.setPiece(new Leopard(56, PlayerColor.RED));
        builder.setPiece(new Lion(61, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board initialBoard = builder.build();

        final MoveStrategy minimax1 = new Minimax(searchDepth);
        final Move move1 = minimax1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 61, 60), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy minimax2 = new Minimax(searchDepth);
        final Move move2 = minimax2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy minimax3 = new Minimax(searchDepth);
        final Move move3 = minimax3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 60, 59), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Blue Wins With Enemy Running Out of Pieces)")
    @Tag("fast")
    void testEndgameInTwo3() {
        int searchDepth = 4;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Dog(45, PlayerColor.BLUE));
        builder.setPiece(new Wolf(18, PlayerColor.BLUE));
        builder.setPiece(new Cat(31, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board initialBoard = builder.build();

        final MoveStrategy minimax1 = new Minimax(searchDepth);
        final Move move1 = minimax1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 18, 17), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy minimax2 = new Minimax(searchDepth);
        final Move move2 = minimax2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy minimax3 = new Minimax(searchDepth);
        final Move move3 = minimax3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 17, 24), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Red Wins With Enemy Running Out of Pieces)")
    @Tag("fast")
    void testEndgameInTwo4() {
        int searchDepth = 4;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(36, PlayerColor.BLUE));
        builder.setPiece(new Rat(23, PlayerColor.RED));
        builder.setPiece(new Cat(42, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board initialBoard = builder.build();

        final MoveStrategy minimax1 = new Minimax(searchDepth);
        final Move move1 = minimax1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 23, 30), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy minimax2 = new Minimax(searchDepth);
        final Move move2 = minimax2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy minimax3 = new Minimax(searchDepth);
        final Move move3 = minimax3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 42, 35), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Blue Wins With Enemy Running Out of Valid Moves)")
    @Tag("fast")
    void testEndgameInTwo5() {
        int searchDepth = 4;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Dog(42, PlayerColor.BLUE));
        builder.setPiece(new Wolf(50, PlayerColor.BLUE));
        builder.setPiece(new Tiger(58, PlayerColor.BLUE));
        builder.setPiece(new Cat(56, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board initialBoard = builder.build();

        final MoveStrategy minimax1 = new Minimax(searchDepth);
        final Move move1 = minimax1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 58, 57), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy minimax2 = new Minimax(searchDepth);
        final Move move2 = minimax2.execute(board1);
        final Board board2 = move2.execute();
        assertEquals(Move.MoveFactory.createMove(board1, 56, 49), move2);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy minimax3 = new Minimax(searchDepth);
        final Move move3 = minimax3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 57, 56), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Red Wins With Enemy Running Out of Valid Moves)")
    @Tag("fast")
    void testEndgameInTwo6() {
        int searchDepth = 4;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(0, PlayerColor.BLUE));
        builder.setPiece(new Cat(14, PlayerColor.RED));
        builder.setPiece(new Dog(8, PlayerColor.RED));
        builder.setPiece(new Wolf(2, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board initialBoard = builder.build();

        final MoveStrategy minimax1 = new Minimax(searchDepth);
        final Move move1 = minimax1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 2, 1), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy minimax2 = new Minimax(searchDepth);
        final Move move2 = minimax2.execute(board1);
        final Board board2 = move2.execute();
        assertEquals(Move.MoveFactory.createMove(board1, 0, 7), move2);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy minimax3 = new Minimax(searchDepth);
        final Move move3 = minimax3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 1, 0), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }
}
