package test;

import com.google.common.collect.Iterables;
import model.artificialintelligence.StandardBoardEvaluator;
import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.board.MoveTransition;
import model.piece.Piece;
import model.piece.animal.*;
import model.player.PlayerColor;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
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
    @DisplayName("Test Board Initialization")
    @Tag("finished")
    void testBoardInitialization() {
        final Board board = Board.constructStandardBoard();
        assertAll("Check initial state of players",
                () -> assertEquals(PlayerColor.BLUE, board.getCurrentPlayer().getAllyColor()),
                () -> assertEquals(PlayerColor.RED, board.getCurrentPlayer().getEnemyPlayer().getAllyColor()),
                () -> assertEquals("Blue", board.getCurrentPlayer().toString()),
                () -> assertEquals("Red", board.getCurrentPlayer().getEnemyPlayer().toString()),
                () -> assertEquals(24, board.bluePlayer().getValidMoves().size()),
                () -> assertEquals(24, board.redPlayer().getValidMoves().size()),
                () -> assertFalse(board.bluePlayer().isDenPenetrated()),
                () -> assertFalse(board.redPlayer().isDenPenetrated())
        );

        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Move> allMoves = board.getAllValidMoves();
        for (final Move move : allMoves) {
            assertFalse(move.isCaptureMove());
        }
        assertEquals(16, Iterables.size(allPieces));
        assertEquals(48, Iterables.size(allMoves));
        assertFalse(BoardUtils.isGameOverScenario(board));
        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
        for (int i = 21; i <= 41; i++) {
            assertNull(board.getPiece(i));
        }
    }

    @Test
    @DisplayName("Test Board Move")
    @Tag("finished")
    void testBoardMove() {
        final Board board = Board.constructStandardBoard();
        final Move move = Move.MoveFactory.createMove(board, 56, 49);
        final MoveTransition transition = board.getCurrentPlayer().makeMove(move);
        final Board transitionBoard = transition.getToBoard();
        assertTrue(transition.getMoveStatus().isDone());
        assertEquals(PlayerColor.RED, transitionBoard.getCurrentPlayer().getAllyColor());
        assertEquals(PlayerColor.BLUE, transitionBoard.getCurrentPlayer().getEnemyPlayer().getAllyColor());
        assertEquals("Red", transitionBoard.getCurrentPlayer().toString());
        assertEquals("Blue", transitionBoard.getCurrentPlayer().getEnemyPlayer().toString());
        assertEquals(24, transitionBoard.redPlayer().getValidMoves().size());
        assertEquals(21, transitionBoard.bluePlayer().getValidMoves().size());
    }

    @Test
    @DisplayName("Test Board Consistency")
    @Tag("finished")
    void testBoardConsistency() {
        final Board board = Board.constructStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e3"),
                        BoardUtils.getCoordinateAtPosition("d3")));
        assertTrue(t1.getMoveStatus().isDone());
        assertFalse(t1.getTransitionMove().isCaptureMove());
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("d7")));
        assertTrue(t2.getMoveStatus().isDone());
        assertFalse(t2.getTransitionMove().isCaptureMove());
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("d3"),
                        BoardUtils.getCoordinateAtPosition("d4")));
        assertTrue(t3.getMoveStatus().isDone());
        assertFalse(t3.getTransitionMove().isCaptureMove());
        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t4.getMoveStatus().isDone());
        assertFalse(t4.getTransitionMove().isCaptureMove());
        final MoveTransition t5 = t4.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t5.getMoveStatus().isDone());
        assertFalse(t5.getTransitionMove().isCaptureMove());
        final MoveTransition t6 = t5.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("a7"),
                        BoardUtils.getCoordinateAtPosition("a6")));
        assertTrue(t6.getMoveStatus().isDone());
        assertFalse(t6.getTransitionMove().isCaptureMove());
        final MoveTransition t7 = t6.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t7.getMoveStatus().isDone());
        assertTrue(t7.getTransitionMove().isCaptureMove());
        final MoveTransition t8 = t7.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("a6"),
                        BoardUtils.getCoordinateAtPosition("a5")));
        assertTrue(t8.getMoveStatus().isDone());
        assertFalse(t8.getTransitionMove().isCaptureMove());
        final MoveTransition t9 = t8.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("g2")));
        assertTrue(t9.getMoveStatus().isDone());
        assertFalse(t9.getTransitionMove().isCaptureMove());
        final MoveTransition t10 = t9.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("a5"),
                        BoardUtils.getCoordinateAtPosition("a4")));
        assertTrue(t10.getMoveStatus().isDone());
        assertFalse(t10.getTransitionMove().isCaptureMove());
        final MoveTransition t11 = t10.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("g3"),
                        BoardUtils.getCoordinateAtPosition("g4")));
        assertTrue(t11.getMoveStatus().isDone());
        assertFalse(t11.getTransitionMove().isCaptureMove());
        final MoveTransition t12 = t11.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("a4"),
                        BoardUtils.getCoordinateAtPosition("a3")));
        assertTrue(t12.getMoveStatus().isDone());
        assertTrue(t12.getTransitionMove().isCaptureMove());
        final MoveTransition t13 = t12.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("g4"),
                        BoardUtils.getCoordinateAtPosition("g5")));
        assertTrue(t13.getMoveStatus().isDone());
        assertFalse(t13.getTransitionMove().isCaptureMove());
        final MoveTransition t14 = t13.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t13.getToBoard(), BoardUtils.getCoordinateAtPosition("a3"),
                        BoardUtils.getCoordinateAtPosition("a4")));
        assertTrue(t14.getMoveStatus().isDone());
        assertFalse(t14.getTransitionMove().isCaptureMove());
        final MoveTransition t15 = t14.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t14.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g3")));
        assertTrue(t15.getMoveStatus().isDone());
        assertFalse(t15.getTransitionMove().isCaptureMove());
        final MoveTransition t16 = t15.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t15.getToBoard(), BoardUtils.getCoordinateAtPosition("a4"),
                        BoardUtils.getCoordinateAtPosition("b4")));
        assertTrue(t16.getMoveStatus().isDone());
        assertFalse(t16.getTransitionMove().isCaptureMove());
        final MoveTransition t17 = t16.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t16.getToBoard(), BoardUtils.getCoordinateAtPosition("g3"),
                        BoardUtils.getCoordinateAtPosition("g4")));
        assertTrue(t17.getMoveStatus().isDone());
        assertFalse(t17.getTransitionMove().isCaptureMove());
        final MoveTransition t18 = t17.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t17.getToBoard(), BoardUtils.getCoordinateAtPosition("b4"),
                        BoardUtils.getCoordinateAtPosition("c4")));
        assertTrue(t18.getMoveStatus().isDone());
        assertFalse(t18.getTransitionMove().isCaptureMove());
        final MoveTransition t19 = t18.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t18.getToBoard(), BoardUtils.getCoordinateAtPosition("g5"),
                        BoardUtils.getCoordinateAtPosition("f5")));
        assertTrue(t19.getMoveStatus().isDone());
        assertFalse(t19.getTransitionMove().isCaptureMove());
        final MoveTransition t20 = t19.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t19.getToBoard(), BoardUtils.getCoordinateAtPosition("c4"),
                        BoardUtils.getCoordinateAtPosition("d4")));
        assertTrue(t20.getMoveStatus().isDone());
        assertFalse(t20.getTransitionMove().isCaptureMove());
        final MoveTransition t21 = t20.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t20.getToBoard(), BoardUtils.getCoordinateAtPosition("g4"),
                        BoardUtils.getCoordinateAtPosition("d4")));
        assertTrue(t21.getMoveStatus().isDone());
        assertTrue(t21.getTransitionMove().isCaptureMove());
        final MoveTransition t22 = t21.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t21.getToBoard(), BoardUtils.getCoordinateAtPosition("c7"),
                        BoardUtils.getCoordinateAtPosition("d7")));
        assertTrue(t22.getMoveStatus().isDone());
        assertFalse(t22.getTransitionMove().isCaptureMove());
        final MoveTransition t23 = t22.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t22.getToBoard(), BoardUtils.getCoordinateAtPosition("d6"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t23.getMoveStatus().isDone());
        assertFalse(t23.getTransitionMove().isCaptureMove());
        final MoveTransition t24 = t23.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t23.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t24.getMoveStatus().isDone());
        assertFalse(t24.getTransitionMove().isCaptureMove());
        final MoveTransition t25 = t24.getToBoard().getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t24.getToBoard(), BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("e2")));
        assertTrue(t25.getMoveStatus().isDone());
        assertFalse(t25.getTransitionMove().isCaptureMove());

        final Board lastMoveBoard = t25.getToBoard();
        assertAll("Check current game's state after the final move",
                () -> assertTrue(lastMoveBoard.getCurrentPlayer().getAllyColor().isRed()),
                () -> assertEquals(6, lastMoveBoard.redPlayer().getActivePieces().size()),
                () -> assertEquals(7, lastMoveBoard.bluePlayer().getActivePieces().size()),
                () -> assertEquals(17, lastMoveBoard.redPlayer().getValidMoves().size()),
                () -> assertEquals(21, lastMoveBoard.bluePlayer().getValidMoves().size()),
                () -> assertFalse(BoardUtils.isGameOverScenario(lastMoveBoard))
        );
        assertAll("Check position of the remaining pieces",
                () -> assertEquals(Animal.LION, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("a9")).getPieceType()),
                () -> assertEquals(Animal.TIGER, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("g9")).getPieceType()),
                () -> assertEquals(Animal.DOG, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("b8")).getPieceType()),
                () -> assertEquals(Animal.CAT, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("f8")).getPieceType()),
                () -> assertEquals(Animal.ELEPHANT, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("g7")).getPieceType()),
                () -> assertEquals(Animal.LEOPARD, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("d6")).getPieceType()),
                () -> assertEquals(Animal.LEOPARD, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("d5")).getPieceType()),
                () -> assertEquals(Animal.RAT, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("f5")).getPieceType()),
                () -> assertEquals(Animal.WOLF, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("c3")).getPieceType()),
                () -> assertEquals(Animal.CAT, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("b2")).getPieceType()),
                () -> assertEquals(Animal.DOG, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("e2")).getPieceType()),
                () -> assertEquals(Animal.TIGER, lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("a1")).getPieceType())
        );
        assertAll("Check unoccupied positions (in this cases, traps of each side)",
                () -> assertNull(lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("c9"))),
                () -> assertNull(lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("e9"))),
                () -> assertNull(lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("d8"))),
                () -> assertNull(lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("d2"))),
                () -> assertNull(lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("c1"))),
                () -> assertNull(lastMoveBoard.getPiece(BoardUtils.getCoordinateAtPosition("e1")))
        );
    }

    @Test
    @DisplayName("Test Invalid Move")
    @Tag("finished")
    void testInvalidMove() {
        Board initialBoard = Board.constructStandardBoard();
        final MoveTransition t1 = initialBoard.getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(initialBoard, 42, 36));
        assertFalse(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(t1.getToBoard(), 6, 7));
        assertFalse(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(t2.getToBoard(), 12, 3));
        assertFalse(t3.getMoveStatus().isDone());
        assertEquals(initialBoard, t3.getToBoard());
    }

    @Test
    @DisplayName("Test Undo Moves")
    @Tag("unfinished")
    void testUndoMoves() {
        final Board initialBoard = Board.constructStandardBoard();
        final Move move1 = Move.MoveFactory.createMove(initialBoard, 48, 41);
        final MoveTransition t1 = initialBoard.getCurrentPlayer().makeMove(move1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move move2 = Move.MoveFactory.createMove(t1.getToBoard(), 20, 27);
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(move2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move move3 = Move.MoveFactory.createMove(t2.getToBoard(), 41, 34);
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().makeMove(move3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move move4 = Move.MoveFactory.createMove(t3.getToBoard(), 16, 17);
        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer().makeMove(move4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move move5 = Move.MoveFactory.createMove(t4.getToBoard(), 34, 27);
        final MoveTransition t5 = t4.getToBoard().getCurrentPlayer().makeMove(move5);
        assertTrue(t5.getMoveStatus().isDone());
        final Board boardAfterFirstUndo = t5.getToBoard().getCurrentPlayer().unmakeMove(move5).getToBoard();
        assertEquals(t4.getToBoard(), boardAfterFirstUndo);
        final Board boardAfterSecondUndo = boardAfterFirstUndo.getCurrentPlayer().unmakeMove(move4).getToBoard();
        assertEquals(t3.getToBoard(), boardAfterSecondUndo);
        final Board boardAfterThirdUndo = boardAfterSecondUndo.getCurrentPlayer().unmakeMove(move3).getToBoard();
        assertEquals(t2.getToBoard(), boardAfterThirdUndo);
        final Board boardAfterFourthUndo = boardAfterThirdUndo.getCurrentPlayer().unmakeMove(move2).getToBoard();
        assertEquals(t1.getToBoard(), boardAfterFourthUndo);
        final Board boardAfterUndoAllMoves = boardAfterFourthUndo.getCurrentPlayer().unmakeMove(move1).getToBoard();
        assertEquals(initialBoard, boardAfterUndoAllMoves);
    }

    @Test
    @DisplayName("Test Algebraic Notation")
    @Tag("finished")
    void testAlgebraicNotation() {
        final String[] expectedPositions = {
                "a9", "b9", "c9", "d9", "e9", "f9", "g9",
                "a8", "b8", "c8", "d8", "e8", "f8", "g8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1"
        };
        for (int i = 0; i < expectedPositions.length; i++) {
            assertEquals(expectedPositions[i], BoardUtils.getPositionAtCoordinate(i));
        }
        for (int i = 0; i < expectedPositions.length; i++) {
            assertEquals(i, BoardUtils.getCoordinateAtPosition(expectedPositions[i]));
        }
    }

    @RepeatedTest(3)
    @DisplayName("Check Standard Board Memory Usage")
    @Tag("finished")
    void mem(RepetitionInfo repetitionInfo) {
        if (repetitionInfo.getCurrentRepetition() == 1) {
            System.out.println("Memory usage of 1 board:");
            final Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            final long start = runtime.freeMemory();
            Board.constructStandardBoard();
            final long end = runtime.freeMemory();
            final float totalInMB = (start - end) / (1024F * 1024F);
            System.out.print("That took " + (start - end) + " bytes, approximately ");
            System.out.printf("%.2f megabytes.\n", totalInMB);
        } else if (repetitionInfo.getCurrentRepetition() == 2) {
            System.out.println("Memory usage of 1,000 board:");
            final Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            final long start = runtime.freeMemory();
            for (int i = 0; i < 1000; i++) {
                Board.constructStandardBoard();
            }
            final long end = runtime.freeMemory();
            final float totalInMB = (start - end) / (1024F * 1024F);
            System.out.print("That took " + (start - end) + " bytes, approximately ");
            System.out.printf("%.2f megabytes.\n", totalInMB);
        } else if (repetitionInfo.getCurrentRepetition() == 3) {
            System.out.println("Memory usage of 1,000,000 board:");
            final Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            final long start = runtime.freeMemory();
            for (int i = 0; i < 1000000; i++) {
                Board.constructStandardBoard();
            }
            final long end = runtime.freeMemory();
            final float totalInMB = (start - end) / (1024F * 1024F);
            System.out.print("That took " + (start - end) + " bytes, approximately ");
            System.out.printf("%.2f megabytes.\n", totalInMB);
        }
    }
}
