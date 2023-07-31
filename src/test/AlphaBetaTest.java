package test;

import model.artificialintelligence.PruningOrderingQuiescenceSearch;
import model.artificialintelligence.MoveSorter;
import model.artificialintelligence.MoveStrategy;
import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.piece.animal.*;
import model.player.PlayerColor;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlphaBetaTest {
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

        final MoveStrategy alphaBeta = new PruningOrderingQuiescenceSearch(6);
        final Move move = alphaBeta.execute(board);
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

        final MoveStrategy alphaBeta = new PruningOrderingQuiescenceSearch(6);
        final Move move = alphaBeta.execute(board);
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

        final MoveStrategy alphaBeta = new PruningOrderingQuiescenceSearch(6);
        final Move move = alphaBeta.execute(board);
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

        final MoveStrategy alphaBeta = new PruningOrderingQuiescenceSearch(6);
        final Move move = alphaBeta.execute(board);
        final Board boardAfterExecution = move.execute();
        assertEquals(Move.MoveFactory.createMove(board, 11, 10), move);
        assertTrue(move.isCaptureMove());
        assertTrue(boardAfterExecution.bluePlayer().getActivePieces().isEmpty());
        assertTrue(boardAfterExecution.bluePlayer().getValidMoves().isEmpty());
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(boardAfterExecution));
    }

    @Test
    @DisplayName("Test Endgame in Two (Blue Wins With Enemy'sDen Penetrated)")
    @Tag("fast")
    void testEndgameInTwo1() {
        int searchDepth = 6;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Lion(11, PlayerColor.BLUE));
        builder.setPiece(new Elephant(55, PlayerColor.BLUE));
        builder.setPiece(new Rat(17, PlayerColor.RED));
        builder.setPiece(new Lion(62, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board initialBoard = builder.build();

        final MoveStrategy alphaBeta1 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move1 = alphaBeta1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 11, 4), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy alphaBeta2 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move2 = alphaBeta2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy alphaBeta3 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move3 = alphaBeta3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 4, 3), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Red Wins With Enemy'sDen Penetrated)")
    @Tag("fast")
    void testEndgameInTwo2() {
        int searchDepth = 6;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(52, PlayerColor.BLUE));
        builder.setPiece(new Lion(6, PlayerColor.BLUE));
        builder.setPiece(new Leopard(56, PlayerColor.RED));
        builder.setPiece(new Lion(61, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board initialBoard = builder.build();

        final MoveStrategy alphaBeta1 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move1 = alphaBeta1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 61, 60), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy alphaBeta2 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move2 = alphaBeta2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy alphaBeta3 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move3 = alphaBeta3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 60, 59), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Blue Wins With Enemy Running Out of Pieces)")
    @Tag("fast")
    void testEndgameInTwo3() {
        int searchDepth = 6;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Dog(45, PlayerColor.BLUE));
        builder.setPiece(new Wolf(18, PlayerColor.BLUE));
        builder.setPiece(new Cat(31, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board initialBoard = builder.build();

        final MoveStrategy alphaBeta1 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move1 = alphaBeta1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 18, 17), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy alphaBeta2 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move2 = alphaBeta2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy alphaBeta3 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move3 = alphaBeta3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 17, 24), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Two (Red Wins With Enemy Running Out of Pieces)")
    @Tag("fast")
    void testEndgameInTwo4() {
        int searchDepth = 6;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(36, PlayerColor.BLUE));
        builder.setPiece(new Rat(23, PlayerColor.RED));
        builder.setPiece(new Cat(42, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board initialBoard = builder.build();

        final MoveStrategy alphaBeta1 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move1 = alphaBeta1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 23, 30), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy alphaBeta2 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move2 = alphaBeta2.execute(board1);
        final Board board2 = move2.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy alphaBeta3 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move3 = alphaBeta3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 42, 35), move3);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board3));
    }

    @Test
    @DisplayName("Test Endgame in Three (Blue Wins With Enemy's Den Penetrated)")
    @Tag("fast")
    void testEndgameInThree() {
        int searchDepth = 6;
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Tiger(17, PlayerColor.BLUE));
        builder.setPiece(new Lion(11, PlayerColor.BLUE));
        builder.setPiece(new Cat(5, PlayerColor.RED));
        builder.setPiece(new Lion(9, PlayerColor.RED));
        builder.setPiece(new Elephant(12, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board initialBoard = builder.build();

        final MoveStrategy alphaBeta1 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move1 = alphaBeta1.execute(initialBoard);
        final Board board1 = move1.execute();
        assertEquals(Move.MoveFactory.createMove(initialBoard, 17, 10), move1);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board1));

        final MoveStrategy alphaBeta2 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move2 = alphaBeta2.execute(board1);
        final Board board2 = move2.execute();
        assertEquals(Move.MoveFactory.createMove(board1, 9, 10), move2);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board2));

        final MoveStrategy alphaBeta3 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move3 = alphaBeta3.execute(board2);
        final Board board3 = move3.execute();
        assertEquals(Move.MoveFactory.createMove(board2, 11, 10), move3);
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board3));

        final MoveStrategy alphaBeta4 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move4 = alphaBeta4.execute(board3);
        final Board board4 = move4.execute();
        assertFalse(BoardUtils.isGameOverScenarioStandardConditions(board4));

        final MoveStrategy alphaBeta5 = new PruningOrderingQuiescenceSearch(searchDepth);
        final Move move5 = alphaBeta5.execute(board4);
        final Board board5 = move5.execute();
        assertEquals(Move.MoveFactory.createMove(board4, 10, 3), move5);
        assertTrue(BoardUtils.isGameOverScenarioStandardConditions(board5));
    }

    @Test
    @DisplayName("Test Standard Move Sorter 1")
    @Tag("fast")
    void testStandardMoveSorter1() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(0, PlayerColor.BLUE));
        builder.setPiece(new Cat(11, PlayerColor.BLUE));
        builder.setPiece(new Dog(31, PlayerColor.BLUE));
        builder.setPiece(new Wolf(2, PlayerColor.BLUE));
        builder.setPiece(new Leopard(60, PlayerColor.BLUE));
        builder.setPiece(new Tiger(57, PlayerColor.BLUE));
        builder.setPiece(new Lion(9, PlayerColor.BLUE));
        builder.setPiece(new Elephant(17, PlayerColor.BLUE));
        builder.setPiece(new Cat(16, PlayerColor.RED));
        builder.setPiece(new Elephant(58, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.STANDARD.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(22, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 16), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 58), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 16), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 8), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 10), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 50), orderedMoves.get(5)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 56), orderedMoves.get(6)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 10), orderedMoves.get(7)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 18), orderedMoves.get(8)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 24), orderedMoves.get(9)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 0, 1), orderedMoves.get(10)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 0, 7), orderedMoves.get(11)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 60, 53), orderedMoves.get(12)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 60, 61), orderedMoves.get(13)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 2, 1), orderedMoves.get(14)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 2, 3), orderedMoves.get(15)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 31, 24), orderedMoves.get(16)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 31, 38), orderedMoves.get(17)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 4), orderedMoves.get(18)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 10), orderedMoves.get(19)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 12), orderedMoves.get(20)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 18), orderedMoves.get(21))
        );
    }

    @Test
    @DisplayName("Test Standard Move Sorter 2")
    @Tag("fast")
    void testStandardMoveSorter2() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(51, PlayerColor.BLUE));
        builder.setPiece(new Cat(38, PlayerColor.BLUE));
        builder.setPiece(new Dog(54, PlayerColor.BLUE));
        builder.setPiece(new Rat(40, PlayerColor.RED));
        builder.setPiece(new Cat(34, PlayerColor.RED));
        builder.setPiece(new Dog(31, PlayerColor.RED));
        builder.setPiece(new Wolf(57, PlayerColor.RED));
        builder.setPiece(new Leopard(61, PlayerColor.RED));
        builder.setPiece(new Tiger(41, PlayerColor.RED));
        builder.setPiece(new Lion(45, PlayerColor.RED));
        builder.setPiece(new Elephant(24, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.STANDARD.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(17, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 45, 38), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 61, 54), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 31, 38), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 45, 44), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 45, 46), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 45, 52), orderedMoves.get(5)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 41, 48), orderedMoves.get(6)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 24, 17), orderedMoves.get(7)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 40, 33), orderedMoves.get(8)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 40, 39), orderedMoves.get(9)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 40, 47), orderedMoves.get(10)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 61, 60), orderedMoves.get(11)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 61, 62), orderedMoves.get(12)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 50), orderedMoves.get(13)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 56), orderedMoves.get(14)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 58), orderedMoves.get(15)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 34, 27), orderedMoves.get(16))
        );
    }

    @Test
    @DisplayName("Test Expensive Move Sorter 1")
    @Tag("fast")
    void testExpensiveMoveSorter1() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(0, PlayerColor.BLUE));
        builder.setPiece(new Cat(11, PlayerColor.BLUE));
        builder.setPiece(new Dog(31, PlayerColor.BLUE));
        builder.setPiece(new Wolf(2, PlayerColor.BLUE));
        builder.setPiece(new Leopard(24, PlayerColor.BLUE));
        builder.setPiece(new Tiger(57, PlayerColor.BLUE));
        builder.setPiece(new Lion(9, PlayerColor.BLUE));
        builder.setPiece(new Elephant(4, PlayerColor.BLUE));
        builder.setPiece(new Cat(16, PlayerColor.RED));
        builder.setPiece(new Leopard(7, PlayerColor.RED));
        builder.setPiece(new Lion(50, PlayerColor.RED));
        builder.setPiece(new Elephant(58, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.EXPENSIVE.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(15, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 4, 3), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 2, 3), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 10), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 10), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 16), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 58), orderedMoves.get(5)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 8), orderedMoves.get(6)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 57, 56), orderedMoves.get(7)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 4, 5), orderedMoves.get(8)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 0, 1), orderedMoves.get(9)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 24, 17), orderedMoves.get(10)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 2, 1), orderedMoves.get(11)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 31, 38), orderedMoves.get(12)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 12), orderedMoves.get(13)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 18), orderedMoves.get(14))
        );
    }

    @Test
    @DisplayName("Test Expensive Move Sorter 2")
    @Tag("fast")
    void testExpensiveMoveSorter2() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(51, PlayerColor.BLUE));
        builder.setPiece(new Cat(38, PlayerColor.BLUE));
        builder.setPiece(new Dog(54, PlayerColor.BLUE));
        builder.setPiece(new Rat(40, PlayerColor.RED));
        builder.setPiece(new Cat(34, PlayerColor.RED));
        builder.setPiece(new Dog(31, PlayerColor.RED));
        builder.setPiece(new Wolf(58, PlayerColor.RED));
        builder.setPiece(new Leopard(61, PlayerColor.RED));
        builder.setPiece(new Tiger(41, PlayerColor.RED));
        builder.setPiece(new Lion(53, PlayerColor.RED));
        builder.setPiece(new Elephant(24, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.EXPENSIVE.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(17, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 58, 59), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 60), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 61, 60), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 54), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 61, 54), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 58, 51), orderedMoves.get(5)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 31, 38), orderedMoves.get(6)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 46), orderedMoves.get(7)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 52), orderedMoves.get(8)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 41, 48), orderedMoves.get(9)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 24, 17), orderedMoves.get(10)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 40, 33), orderedMoves.get(11)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 40, 39), orderedMoves.get(12)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 40, 47), orderedMoves.get(13)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 61, 62), orderedMoves.get(14)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 58, 57), orderedMoves.get(15)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 34, 27), orderedMoves.get(16))
        );
    }

    @Test
    @DisplayName("Test Expensive Move Sorter 3")
    @Tag("fast")
    void testExpensiveMoveSorter3() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(1, PlayerColor.BLUE));
        builder.setPiece(new Cat(17, PlayerColor.BLUE));
        builder.setPiece(new Dog(5, PlayerColor.BLUE));
        builder.setPiece(new Cat(24, PlayerColor.RED));
        builder.setPiece(new Wolf(8, PlayerColor.RED));
        builder.setPiece(new Leopard(12, PlayerColor.RED));
        builder.setPiece(new Elephant(6, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.EXPENSIVE.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(7, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 1, 2), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 5, 4), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 10), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 24), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 1, 0), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 16), orderedMoves.get(5)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 17, 18), orderedMoves.get(6))
        );
    }

    @Test
    @DisplayName("Test Expensive Move Sorter 4")
    @Tag("fast")
    void testExpensiveMoveSorter4() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(46, PlayerColor.BLUE));
        builder.setPiece(new Cat(54, PlayerColor.BLUE));
        builder.setPiece(new Elephant(44, PlayerColor.BLUE));
        builder.setPiece(new Rat(51, PlayerColor.RED));
        builder.setPiece(new Cat(53, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.EXPENSIVE.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(8, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 51, 52), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 51, 58), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 52), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 60), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 51, 44), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 46), orderedMoves.get(5)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 54), orderedMoves.get(6)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 51, 50), orderedMoves.get(7))
        );
    }

    @Test
    @DisplayName("Test Expensive Move Sorter 5")
    @Tag("fast")
    void testExpensiveMoveSorter5() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Tiger(11, PlayerColor.BLUE));
        builder.setPiece(new Lion(9, PlayerColor.BLUE));
        builder.setPiece(new Rat(16, PlayerColor.RED));
        builder.setPiece(new Cat(1, PlayerColor.RED));
        builder.setPiece(new Dog(8, PlayerColor.RED));
        builder.setPiece(new Wolf(18, PlayerColor.RED));
        builder.setPiece(new Leopard(5, PlayerColor.RED));
        builder.setPiece(new Tiger(17, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.EXPENSIVE.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(8, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 8), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 16), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 18), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 2), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 9, 10), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 4), orderedMoves.get(5)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 10), orderedMoves.get(6)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 11, 12), orderedMoves.get(7))
        );
    }

    @Test
    @DisplayName("Test Expensive Move Sorter 6")
    @Tag("fast")
    void testExpensiveMoveSorter6() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(45, PlayerColor.BLUE));
        builder.setPiece(new Cat(61, PlayerColor.BLUE));
        builder.setPiece(new Dog(44, PlayerColor.BLUE));
        builder.setPiece(new Wolf(54, PlayerColor.BLUE));
        builder.setPiece(new Tiger(46, PlayerColor.BLUE));
        builder.setPiece(new Lion(50, PlayerColor.BLUE));
        builder.setPiece(new Elephant(57, PlayerColor.BLUE));
        builder.setPiece(new Wolf(51, PlayerColor.RED));
        builder.setPiece(new Leopard(53, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();

        final List<Move> orderedMoves = (List<Move>) MoveSorter.EXPENSIVE.sort((board.getCurrentPlayer().getValidMoves()));
        assertEquals(6, orderedMoves.size());
        assertAll("Check all ordered moves",
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 54), orderedMoves.get(0)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 51, 44), orderedMoves.get(1)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 52), orderedMoves.get(2)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 53, 60), orderedMoves.get(3)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 51, 52), orderedMoves.get(4)),
                () -> assertEquals(Move.MoveFactory.createMove(board, 51, 58), orderedMoves.get(5))
        );
    }
}
