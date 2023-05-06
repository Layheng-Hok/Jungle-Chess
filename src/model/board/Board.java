package model.board;

import model.piece.Piece;
import model.piece.animal.*;
import model.player.BluePlayer;
import model.player.Player;
import model.player.PlayerColor;
import model.player.RedPlayer;

import java.util.*;

public class Board {
    private final List<Terrain> chessboard;
    private final Map<Integer, Piece> boardConfig;
    private final Collection<Piece> bluePieces;
    private final Collection<Piece> redPieces;
    private final BluePlayer bluePlayer;
    private final RedPlayer redPlayer;
    private final Player currentPlayer;

    private Board(Builder builder) {
        this.chessboard = constructChessboard(builder);
        this.boardConfig = Collections.unmodifiableMap(builder.boardConfig);
        this.bluePieces = determineActivePieces(this.chessboard, PlayerColor.BLUE);
        this.redPieces = determineActivePieces(this.chessboard, PlayerColor.RED);
        final Collection<Move> blueStandardValidMoves = determineValidMoves(this.bluePieces);
        final Collection<Move> redStandardValidMoves = determineValidMoves(this.redPieces);
        this.bluePlayer = new BluePlayer(this, blueStandardValidMoves, redStandardValidMoves);
        this.redPlayer = new RedPlayer(this, blueStandardValidMoves, redStandardValidMoves);
        this.currentPlayer = builder.nextMovePlayer.choosePlayer(this.bluePlayer, this.redPlayer);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
            final String terrainText = this.chessboard.get(i).toString();
            stringBuilder.append(String.format("%3s", terrainText));
            if ((i + 1) % BoardUtils.NUM_TERRAINS_PER_ROW == 0) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public Player bluePlayer() {
        return this.bluePlayer;
    }

    public Player redPlayer() {
        return this.redPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Collection<Piece> getBluePieces() {
        return this.bluePieces;
    }

    public Collection<Piece> getRedPieces() {
        return this.redPieces;
    }

    private Collection<Move> determineValidMoves(Collection<Piece> pieces) {
        final List<Move> validMoves = new ArrayList<>();
        for (final Piece piece : pieces) {
            validMoves.addAll(piece.determineValidMoves(this));
        }
        return Collections.unmodifiableList(validMoves);
    }

    private static Collection determineActivePieces(List<Terrain> chessboard, PlayerColor playerColor) {
        final List<Piece> activePieces = new ArrayList<>();
        for (final Terrain terrain : chessboard) {
            if (terrain.isTerrainOccupied()) {
                final Piece piece = terrain.getPiece();
                if (piece.getPieceColor() == playerColor) {
                    activePieces.add(piece);
                }
            }
        }
        return Collections.unmodifiableList(activePieces);
    }

    public Terrain getTerrain(int terrainCoordinate) {
        return chessboard.get(terrainCoordinate);
    }

    private static List<Terrain> constructChessboard(final Builder builder) {
        final Terrain[] terrains = new Terrain[BoardUtils.NUM_TERRAINS];
        for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
            terrains[i] = Terrain.constructTerrain(i, builder.boardConfig.get(i));
        }
        return Collections.unmodifiableList(Arrays.asList(terrains));
    }

    public static Board constructStandardBoard() {
        final Builder builder = new Builder();

        builder.setPiece(new Rat(48, PlayerColor.BLUE));
        builder.setPiece(new Cat(50, PlayerColor.BLUE));
        builder.setPiece(new Dog(54, PlayerColor.BLUE));
        builder.setPiece(new Wolf(44, PlayerColor.BLUE));
        builder.setPiece(new Leopard(46, PlayerColor.BLUE));
        builder.setPiece(new Tiger(56, PlayerColor.BLUE));
        builder.setPiece(new Lion(62, PlayerColor.BLUE));
        builder.setPiece(new Elephant(42, PlayerColor.BLUE));

        builder.setPiece(new Rat(14, PlayerColor.RED));
        builder.setPiece(new Cat(12, PlayerColor.RED));
        builder.setPiece(new Dog(8, PlayerColor.RED));
        builder.setPiece(new Wolf(18, PlayerColor.RED));
        builder.setPiece(new Leopard(16, PlayerColor.RED));
        builder.setPiece(new Tiger(6, PlayerColor.RED));
        builder.setPiece(new Lion(0, PlayerColor.RED));
        builder.setPiece(new Elephant(20, PlayerColor.RED));

        builder.setNextMovePlayer(PlayerColor.BLUE);
        return builder.build();
    }

    public Iterable<Move> getAllValidMoves() {
        List<Move> allMoves = new ArrayList<>();
        allMoves.addAll(this.bluePlayer.getValidMoves());
        allMoves.addAll(this.redPlayer.getValidMoves());
        return Collections.unmodifiableList(allMoves);
    }

    public Piece getPiece(final int coordinate) {
        return this.boardConfig.get(coordinate);
    }

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        PlayerColor nextMovePlayer;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPieceCoordinate(), piece);
            return this;
        }

        public Builder setNextMovePlayer(final PlayerColor nextPlayerColor) {
            this.nextMovePlayer = nextPlayerColor;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
