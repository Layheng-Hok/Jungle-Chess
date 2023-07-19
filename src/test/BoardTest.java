package test;

import com.google.common.collect.Iterables;
import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.player.PlayerColor;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @BeforeEach
    void reportTestInfo(TestReporter testReporter, TestInfo testInfo) {
        testReporter.publishEntry("Running " + testInfo.getDisplayName()
                + " with tags " + testInfo.getTags());
    }

    @Test
    @DisplayName("Test Board Initialization")
    @Tag("finished")
    void testBoardInitialization() {
        final Board board = Board.constructStandardBoard();
        assertAll("Check initial state of players",
                () -> assertEquals(board.getCurrentPlayer().getValidMoves().size(), 24),
                () -> assertEquals(board.getCurrentPlayer().getEnemyPlayer().getValidMoves().size(), 24),
                () -> assertFalse(board.getCurrentPlayer().isDenPenetrated()),
                () -> assertFalse(board.getCurrentPlayer().getEnemyPlayer().isDenPenetrated()),
                () -> assertEquals(PlayerColor.BLUE, board.getCurrentPlayer().getAllyColor()),
                () -> assertEquals(PlayerColor.RED, board.getCurrentPlayer().getEnemyPlayer().getAllyColor()),
                () -> assertEquals("Blue", board.getCurrentPlayer().toString()),
                () -> assertEquals("Red", board.getCurrentPlayer().getEnemyPlayer().toString())
        );

        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Move> allMoves = board.getAllValidMoves();
        for (final Move move : allMoves) {
            assertFalse(move.isCaptureMove());
        }
        assertEquals(14, Iterables.size(allPieces));
        assertEquals(48, Iterables.size(allMoves));
    }
}