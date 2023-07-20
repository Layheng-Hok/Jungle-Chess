package test;

import model.board.Board;
import model.board.BoardUtils;
import model.board.Move;
import model.board.MoveTransition;
import model.piece.animal.*;
import model.player.PlayerColor;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GameOverScenarioTest {
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
    @DisplayName("Test Den Penetrated")
    @Tag("finished")
    void testDenPenetrated() {
        final Board.Builder builder1 = new Board.Builder();
        builder1.setPiece(new Rat(4, PlayerColor.BLUE));
        builder1.setPiece(new Elephant(53, PlayerColor.BLUE));
        builder1.setPiece(new Rat(60, PlayerColor.RED));
        builder1.setPiece(new Elephant(11, PlayerColor.RED));
        builder1.setNextMovePlayer(PlayerColor.BLUE);
        final Board board1 = builder1.build();
        assertFalse(board1.redPlayer().isDenPenetrated());
        final MoveTransition t1 = board1.getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(board1, 4, 3));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().redPlayer().isDenPenetrated());
        assertTrue(BoardUtils.isGameOverScenario(t1.getToBoard()));

        final Board.Builder builder2 = new Board.Builder();
        builder2.setPiece(new Rat(12, PlayerColor.BLUE));
        builder2.setPiece(new Elephant(53, PlayerColor.BLUE));
        builder2.setPiece(new Rat(60, PlayerColor.RED));
        builder2.setPiece(new Elephant(11, PlayerColor.RED));
        builder2.setNextMovePlayer(PlayerColor.RED);
        final Board board2 = builder2.build();
        assertFalse(board2.bluePlayer().isDenPenetrated());
        final MoveTransition t2 = board2.getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(board2, 60, 59));
        assertTrue(t2.getMoveStatus().isDone());
        assertTrue(t2.getToBoard().bluePlayer().isDenPenetrated());
        assertTrue(BoardUtils.isGameOverScenario(t2.getToBoard()));
    }

    @Test
    @DisplayName("Test Empty Active Pieces")
    @Tag("finished")
    void testEmptyActivePieces() {
        final Board.Builder builder1 = new Board.Builder();
        builder1.setPiece(new Cat(38, PlayerColor.BLUE));
        builder1.setPiece(new Dog(24, PlayerColor.BLUE));
        builder1.setPiece(new Cat(31, PlayerColor.RED));
        builder1.setNextMovePlayer(PlayerColor.BLUE);
        final Board board1 = builder1.build();
        assertFalse(board1.redPlayer().getActivePieces().isEmpty());
        final MoveTransition t1 = board1.getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(board1, 38, 31));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getTransitionMove().isCaptureMove());
        assertTrue(t1.getToBoard().redPlayer().getActivePieces().isEmpty());
        assertTrue(BoardUtils.isGameOverScenario(t1.getToBoard()));

        final Board.Builder builder2 = new Board.Builder();
        builder2.setPiece(new Lion(10, PlayerColor.BLUE));
        builder2.setPiece(new Wolf(38, PlayerColor.RED));
        builder2.setPiece(new Leopard(11, PlayerColor.RED));
        builder2.setNextMovePlayer(PlayerColor.RED);
        final Board board2 = builder2.build();
        assertFalse(board2.bluePlayer().getActivePieces().isEmpty());
        final MoveTransition t2 = board2.getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(board2, 11, 10));
        assertTrue(t2.getMoveStatus().isDone());
        assertTrue(t2.getTransitionMove().isCaptureMove());
        assertTrue(t2.getToBoard().bluePlayer().getActivePieces().isEmpty());
        assertTrue(BoardUtils.isGameOverScenario(t2.getToBoard()));
    }

    @Test
    @DisplayName("Test Empty Valid Moves")
    @Tag("finished")
    void testEmptyValidMoves() {
        final Board.Builder builder1 = new Board.Builder();
        builder1.setPiece(new Dog(49, PlayerColor.BLUE));
        builder1.setPiece(new Tiger(58, PlayerColor.BLUE));
        builder1.setPiece(new Cat(56, PlayerColor.RED));
        builder1.setNextMovePlayer(PlayerColor.BLUE);
        final Board board1 = builder1.build();
        assertFalse(board1.redPlayer().getValidMoves().isEmpty());
        final MoveTransition t1 = board1.getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(board1, 58, 57));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().redPlayer().getValidMoves().isEmpty());
        assertTrue(BoardUtils.isGameOverScenario(t1.getToBoard()));

        final Board.Builder builder2 = new Board.Builder();
        builder2.setPiece(new Cat(27, PlayerColor.BLUE));
        builder2.setPiece(new Dog(21, PlayerColor.BLUE));
        builder2.setPiece(new Wolf(38, PlayerColor.BLUE));
        builder2.setPiece(new Tiger(31, PlayerColor.BLUE));
        builder2.setPiece(new Elephant(34, PlayerColor.BLUE));
        builder2.setPiece(new Rat(41, PlayerColor.RED));
        builder2.setPiece(new Dog(20, PlayerColor.RED));
        builder2.setPiece(new Wolf(14, PlayerColor.RED));
        builder2.setPiece(new Leopard(45, PlayerColor.RED));
        builder2.setPiece(new Lion(35, PlayerColor.RED));
        builder2.setPiece(new Elephant(24, PlayerColor.RED));
        builder2.setNextMovePlayer(PlayerColor.BLUE);
        final Board board2 = builder2.build();
        assertFalse(board2.bluePlayer().getValidMoves().isEmpty());
        assertEquals(2, board2.bluePlayer().getValidMoves().size());
        assertTrue(board2.bluePlayer().getValidMoves().contains(
                Move.MoveFactory.createMove(board2, 31, 28)));
        assertTrue(board2.bluePlayer().getValidMoves().contains(
                Move.MoveFactory.createMove(board2, 21, 28)));
        final MoveTransition t2 = board2.getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(board2, 31, 28));
        assertTrue(t2.getMoveStatus().isDone());
        assertFalse(t2.getToBoard().bluePlayer().getValidMoves().isEmpty());
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().
                makeMove(Move.MoveFactory.createMove(t2.getToBoard(), 24, 31));
        assertTrue(t3.getMoveStatus().isDone());
        assertTrue(t3.getToBoard().bluePlayer().getValidMoves().isEmpty());
        assertTrue(BoardUtils.isGameOverScenario(t3.getToBoard()));
    }
}
