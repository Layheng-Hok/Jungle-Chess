package test;

import com.google.common.collect.Sets;
import model.board.Board;
import model.board.Move;
import model.piece.Piece;
import model.piece.animal.*;
import model.player.PlayerColor;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
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
    @DisplayName("Test Rat's Swimming Ability")
    @Tag("fast")
    void testRatSwimmingAbility() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(40, PlayerColor.BLUE));
        builder.setPiece(new Elephant(38, PlayerColor.BLUE));
        builder.setPiece(new Rat(39, PlayerColor.RED));
        builder.setPiece(new Elephant(47, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        final Collection<Move> blueRatValidMoves = board.getPiece(40).determineValidMoves(board);
        final Collection<Move> redRatValidMoves = board.getPiece(39).determineValidMoves(board);
        assertEquals(3, blueRatValidMoves.size());
        assertEquals(3, redRatValidMoves.size());
        assertTrue(blueRatValidMoves.contains(Move.MoveFactory.createMove(board, 40, 33)));
        assertTrue(blueRatValidMoves.contains(Move.MoveFactory.createMove(board, 40, 39)));
        assertTrue(blueRatValidMoves.contains(Move.MoveFactory.createMove(board, 40, 41)));
        assertTrue(redRatValidMoves.contains(Move.MoveFactory.createMove(board, 39, 32)));
        assertTrue(redRatValidMoves.contains(Move.MoveFactory.createMove(board, 39, 40)));
        assertTrue(redRatValidMoves.contains(Move.MoveFactory.createMove(board, 39, 46)));
    }

    @Test
    @DisplayName("Test Lion and Tiger's Horizontal Jumping Ability")
    @Tag("fast")
    void testLionAndTigerHorizontalJumpingAbility() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Lion(16, PlayerColor.BLUE));
        builder.setPiece(new Tiger(19, PlayerColor.BLUE));
        builder.setPiece(new Lion(18, PlayerColor.RED));
        builder.setPiece(new Tiger(44, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        final Collection<Move> blueLionValidMoves = board.getPiece(16).determineValidMoves(board);
        final Collection<Move> blueTigerValidMoves = board.getPiece(19).determineValidMoves(board);
        final Collection<Move> redLionValidMoves = board.getPiece(18).determineValidMoves(board);
        final Collection<Move> redTigerValidMoves = board.getPiece(44).determineValidMoves(board);
        assertEquals(4, blueLionValidMoves.size());
        assertEquals(3, blueTigerValidMoves.size());
        assertEquals(4, redLionValidMoves.size());
        assertEquals(3, redTigerValidMoves.size());
        assertTrue(blueLionValidMoves.contains(Move.MoveFactory.createMove(board, 16, 9)));
        assertTrue(blueLionValidMoves.contains(Move.MoveFactory.createMove(board, 16, 15)));
        assertTrue(blueLionValidMoves.contains(Move.MoveFactory.createMove(board, 16, 17)));
        assertTrue(blueLionValidMoves.contains(Move.MoveFactory.createMove(board, 16, 44)));
        assertTrue(blueTigerValidMoves.contains(Move.MoveFactory.createMove(board, 19, 12)));
        assertTrue(blueTigerValidMoves.contains(Move.MoveFactory.createMove(board, 19, 20)));
        assertTrue(blueTigerValidMoves.contains(Move.MoveFactory.createMove(board, 19, 47)));
        assertTrue(redLionValidMoves.contains(Move.MoveFactory.createMove(board, 18, 11)));
        assertTrue(redLionValidMoves.contains(Move.MoveFactory.createMove(board, 18, 17)));
        assertTrue(redLionValidMoves.contains(Move.MoveFactory.createMove(board, 18, 19)));
        assertTrue(redLionValidMoves.contains(Move.MoveFactory.createMove(board, 18, 46)));
        assertTrue(redTigerValidMoves.contains(Move.MoveFactory.createMove(board, 44, 43)));
        assertTrue(redTigerValidMoves.contains(Move.MoveFactory.createMove(board, 44, 45)));
        assertTrue(redTigerValidMoves.contains(Move.MoveFactory.createMove(board, 44, 51)));
    }

    @Test
    @DisplayName("Test Lion and Tiger's Vertical Jumping Ability")
    @Tag("fast")
    void testLionAndTigerVerticalJumpingAbility() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Lion(21, PlayerColor.BLUE));
        builder.setPiece(new Tiger(31, PlayerColor.BLUE));
        builder.setPiece(new Lion(34, PlayerColor.RED));
        builder.setPiece(new Tiger(38, PlayerColor.RED));
        builder.setPiece(new Elephant(24, PlayerColor.RED));
        builder.setPiece(new Dog(28, PlayerColor.RED));
        builder.setPiece(new Cat(35, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        final Collection<Move> blueLionValidMoves = board.getPiece(21).determineValidMoves(board);
        final Collection<Move> blueTigerValidMoves = board.getPiece(31).determineValidMoves(board);
        final Collection<Move> redLionValidMoves = board.getPiece(34).determineValidMoves(board);
        final Collection<Move> redTigerValidMoves = board.getPiece(38).determineValidMoves(board);
        assertEquals(2, blueLionValidMoves.size());
        assertEquals(2, blueTigerValidMoves.size());
        assertEquals(3, redLionValidMoves.size());
        assertEquals(3, redTigerValidMoves.size());
        assertTrue(blueLionValidMoves.contains(Move.MoveFactory.createMove(board, 21, 14)));
        assertTrue(blueLionValidMoves.contains(Move.MoveFactory.createMove(board, 21, 28)));
        assertTrue(blueTigerValidMoves.contains(Move.MoveFactory.createMove(board, 31, 28)));
        assertTrue(blueTigerValidMoves.contains(Move.MoveFactory.createMove(board, 31, 38)));
        assertTrue(redLionValidMoves.contains(Move.MoveFactory.createMove(board, 34, 27)));
        assertTrue(redLionValidMoves.contains(Move.MoveFactory.createMove(board, 34, 31)));
        assertTrue(redLionValidMoves.contains(Move.MoveFactory.createMove(board, 34, 41)));
        assertTrue(redTigerValidMoves.contains(Move.MoveFactory.createMove(board, 38, 31)));
        assertTrue(redTigerValidMoves.contains(Move.MoveFactory.createMove(board, 38, 41)));
        assertTrue(redTigerValidMoves.contains(Move.MoveFactory.createMove(board, 38, 45)));
    }

    @Test
    @DisplayName("Test Rat's Valid Moves in the Middle of Empty Board")
    @Tag("fast")
    void testRatValidMovesInTheMiddleOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rat(31, PlayerColor.BLUE));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        assertTrue(board.getTerrain(31).isTerrainOccupied());
        assertEquals(0, board.redPlayer().getActivePieces().size());
        assertEquals(0, board.redPlayer().getValidMoves().size());
        final Collection<Move> blueValidMoves = board.bluePlayer().getValidMoves();
        assertEquals(4, blueValidMoves.size());
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 24)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 30)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 32)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 38)));
    }

    @Test
    @DisplayName("Test Wolf's Valid Moves in the Middle of Empty Board")
    @Tag("fast")
    void testWolfValidMovesInTheMiddleOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Wolf(31, PlayerColor.BLUE));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        assertTrue(board.getTerrain(31).isTerrainOccupied());
        assertEquals(0, board.redPlayer().getActivePieces().size());
        assertEquals(0, board.redPlayer().getValidMoves().size());
        final Collection<Move> blueValidMoves = board.bluePlayer().getValidMoves();
        assertEquals(2, blueValidMoves.size());
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 24)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 38)));
    }

    @Test
    @DisplayName("Test Lion's Valid Moves in the Middle of Empty Board")
    @Tag("fast")
    void testLionValidMovesInTheMiddleOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Lion(31, PlayerColor.BLUE));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        assertTrue(board.getTerrain(31).isTerrainOccupied());
        assertEquals(0, board.redPlayer().getActivePieces().size());
        assertEquals(0, board.redPlayer().getValidMoves().size());
        final Collection<Move> blueValidMoves = board.bluePlayer().getValidMoves();
        assertEquals(4, blueValidMoves.size());
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 24)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 28)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 34)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 38)));
    }

    @Test
    @DisplayName("Test Elephant's Valid Moves in the Middle of Empty Board")
    @Tag("fast")
    void testElephantValidMovesInTheMiddleOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Elephant(31, PlayerColor.BLUE));
        builder.setNextMovePlayer(PlayerColor.BLUE);
        final Board board = builder.build();
        assertTrue(board.getTerrain(31).isTerrainOccupied());
        assertEquals(0, board.redPlayer().getActivePieces().size());
        assertEquals(0, board.redPlayer().getValidMoves().size());
        final Collection<Move> blueValidMoves = board.bluePlayer().getValidMoves();
        assertEquals(2, blueValidMoves.size());
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 24)));
        assertTrue(blueValidMoves.contains(Move.MoveFactory.createMove(board, 31, 38)));
    }

    @Test
    @DisplayName("Test Piece's Valid Moves at Top-Left Corner of Empty Board")
    @Tag("fast")
    void testPieceValidMovesAtTopLeftCornerOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Leopard(0, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();
        assertTrue(board.getTerrain(0).isTerrainOccupied());
        assertEquals(0, board.bluePlayer().getActivePieces().size());
        assertEquals(0, board.bluePlayer().getValidMoves().size());
        final Collection<Move> redValidMoves = board.redPlayer().getValidMoves();
        assertEquals(2, redValidMoves.size());
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 0, 1)));
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 0, 7)));
    }

    @Test
    @DisplayName("Test Piece's Valid Moves at Top-Right Corner of Empty Board")
    @Tag("fast")
    void testPieceValidMovesAtTopRightCornerOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Cat(6, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();
        assertTrue(board.getTerrain(6).isTerrainOccupied());
        assertEquals(0, board.bluePlayer().getActivePieces().size());
        assertEquals(0, board.bluePlayer().getValidMoves().size());
        final Collection<Move> redValidMoves = board.redPlayer().getValidMoves();
        assertEquals(2, redValidMoves.size());
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 6, 5)));
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 6, 13)));
    }

    @Test
    @DisplayName("Test Piece's Valid Moves at Bottom-Left Corner of Empty Board")
    @Tag("fast")
    void testPieceValidMovesAtBottomLeftCornerOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Dog(56, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();
        assertTrue(board.getTerrain(56).isTerrainOccupied());
        assertEquals(0, board.bluePlayer().getActivePieces().size());
        assertEquals(0, board.bluePlayer().getValidMoves().size());
        final Collection<Move> redValidMoves = board.redPlayer().getValidMoves();
        assertEquals(2, redValidMoves.size());
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 56, 49)));
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 56, 57)));
    }

    @Test
    @DisplayName("Test Piece's Valid Moves at Bottom-Right Corner of Empty Board")
    @Tag("fast")
    void testPieceValidMovesAtBottomRightCornerOfEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Wolf(62, PlayerColor.RED));
        builder.setNextMovePlayer(PlayerColor.RED);
        final Board board = builder.build();
        assertTrue(board.getTerrain(62).isTerrainOccupied());
        assertEquals(0, board.bluePlayer().getActivePieces().size());
        assertEquals(0, board.bluePlayer().getValidMoves().size());
        final Collection<Move> redValidMoves = board.redPlayer().getValidMoves();
        assertEquals(2, redValidMoves.size());
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 62, 55)));
        assertTrue(redValidMoves.contains(Move.MoveFactory.createMove(board, 62, 61)));
    }

    @Test
    @DisplayName("Test Piece's Equality")
    @Tag("fast")
    void testPieceEquality() {
        final Board board1 = Board.constructStandardBoard();
        final Board board2 = Board.constructStandardBoard();
        assertAll("Check if two pieces are equal",
                () -> assertEquals(board1.getPiece(0), board2.getPiece(0)),
                () -> assertEquals(board1.getPiece(6), board2.getPiece(6)),
                () -> assertEquals(board1.getPiece(8), board2.getPiece(8)),
                () -> assertEquals(board1.getPiece(12), board2.getPiece(12)),
                () -> assertEquals(board1.getPiece(14), board2.getPiece(14)),
                () -> assertEquals(board1.getPiece(16), board2.getPiece(16)),
                () -> assertEquals(board1.getPiece(18), board2.getPiece(18)),
                () -> assertEquals(board1.getPiece(20), board2.getPiece(20)),
                () -> assertEquals(board1.getPiece(42), board2.getPiece(42)),
                () -> assertEquals(board1.getPiece(44), board2.getPiece(44)),
                () -> assertEquals(board1.getPiece(46), board2.getPiece(46)),
                () -> assertEquals(board1.getPiece(48), board2.getPiece(48)),
                () -> assertEquals(board1.getPiece(50), board2.getPiece(50)),
                () -> assertEquals(board1.getPiece(54), board2.getPiece(54)),
                () -> assertEquals(board1.getPiece(56), board2.getPiece(56)),
                () -> assertEquals(board1.getPiece(62), board2.getPiece(62))
        );
        assertAll("Check if two pieces are unequal",
                () -> assertNotEquals(board1.getPiece(0), board2.getPiece(1)),
                () -> assertNotEquals(board1.getPiece(6), board2.getPiece(56)),
                () -> assertNotEquals(board1.getPiece(8), board2.getPiece(9)),
                () -> assertNotEquals(board1.getPiece(12), board2.getPiece(13)),
                () -> assertNotEquals(board1.getPiece(14), board2.getPiece(15)),
                () -> assertNotEquals(board1.getPiece(16), board2.getPiece(17)),
                () -> assertNotEquals(board1.getPiece(18), board2.getPiece(19)),
                () -> assertNotEquals(board1.getPiece(20), board2.getPiece(6)),
                () -> assertNotEquals(board1.getPiece(42), board2.getPiece(43)),
                () -> assertNotEquals(board1.getPiece(44), board2.getPiece(20)),
                () -> assertNotEquals(board1.getPiece(46), board2.getPiece(47)),
                () -> assertNotEquals(board1.getPiece(48), board2.getPiece(49)),
                () -> assertNotEquals(board1.getPiece(50), board2.getPiece(51)),
                () -> assertNotEquals(board1.getPiece(54), board2.getPiece(55)),
                () -> assertNotEquals(board1.getPiece(56), board2.getPiece(57)),
                () -> assertNotEquals(board1.getPiece(62), board2.getPiece(46))
        );
    }

    @Test
    @DisplayName("Test Hash Code")
    @Tag("fast")
    void testHashCode() {
        final Board board = Board.constructStandardBoard();
        final Set<Piece> pieceSet = Sets.newHashSet(board.getAllPieces());
        final Set<Piece> bluePieceSet = Sets.newHashSet(board.getBluePieces());
        final Set<Piece> redPieceSet = Sets.newHashSet(board.getRedPieces());
        assertEquals(16, pieceSet.size());
        assertEquals(8, bluePieceSet.size());
        assertEquals(8, redPieceSet.size());
    }
}
