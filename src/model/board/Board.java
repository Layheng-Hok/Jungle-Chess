package model.board;

import model.piece.Piece;
import model.piece.animal.*;
import model.player.BluePlayer;
import model.player.Player;
import model.player.PlayerColor;
import model.player.RedPlayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private final List<Terrain> chessboard;
    private final Map<Integer, Piece> boardConfig;
    private final Collection<Piece> bluePieces;
    private final Collection<Piece> redPieces;
    private final BluePlayer bluePlayer;
    private final RedPlayer redPlayer;
    private final Player currentPlayer;
    private final Move transitionMove;

    private Board(Builder builder) {
        this.chessboard = constructChessboard(builder);
        this.boardConfig = Collections.unmodifiableMap(builder.boardConfig);
        this.bluePieces = determineActivePieces(this.chessboard, PlayerColor.BLUE);
        this.redPieces = determineActivePieces(this.chessboard, PlayerColor.RED);
        final Collection<Move> blueValidMoves = determineValidMoves(this.bluePieces);
        final Collection<Move> redValidMoves = determineValidMoves(this.redPieces);
        this.bluePlayer = new BluePlayer(this, blueValidMoves, redValidMoves);
        this.redPlayer = new RedPlayer(this, blueValidMoves, redValidMoves);
        this.currentPlayer = getPlayer(builder.nextMovePlayer, this.bluePlayer, this.redPlayer);
        this.transitionMove = builder.transitionMove != null ? builder.transitionMove : Move.MoveFactory.getNullMove();
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
            final String terrainText = this.chessboard.get(i).toString();
            stringBuilder.append(terrainText);
            if ((i + 1) % BoardUtils.NUM_TERRAINS_PER_ROW == 0) {
                stringBuilder.append("\n");
            } else {
                stringBuilder.append("  ");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Board)) {
            return false;
        }
        Board otherBoard = (Board) other;
        System.out.println(this.boardConfig);
        System.out.println(otherBoard.boardConfig);
        System.out.println("Compare chessboard toString(): " + this.chessboard.toString().equals(otherBoard.chessboard.toString()));
        System.out.println("Compare boardConfig: " + this.boardConfig.equals(otherBoard.boardConfig));
        System.out.println("Compare bluePieces: " + this.bluePieces.equals(otherBoard.bluePieces));
        System.out.println("Compare redPieces: " + this.redPieces.equals(otherBoard.redPieces));
        System.out.println("Compare bluePlayer toString(): " + this.bluePlayer.toString().equals(otherBoard.bluePlayer.toString()));
        System.out.println("Compare redPlayer toString(): " + this.redPlayer.toString().equals(otherBoard.redPlayer.toString()));
        System.out.println("Compare currentPlayer toString(): " + this.currentPlayer.toString().equals(otherBoard.currentPlayer.toString()));
        return this.chessboard.toString().equals(otherBoard.chessboard.toString()) &&
              this.boardConfig.equals(otherBoard.boardConfig) &&
                this.bluePieces.equals(otherBoard.bluePieces) &&
                this.redPieces.equals(otherBoard.redPieces) &&
                this.bluePlayer.toString().equals(otherBoard.bluePlayer.toString()) &&
                this.redPlayer.toString().equals(otherBoard.redPlayer.toString()) &&
                this.currentPlayer.toString().equals(otherBoard.currentPlayer.toString());
    }


    public Player bluePlayer() {
        return this.bluePlayer;
    }

    public Player redPlayer() {
        return this.redPlayer;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Collection<Piece> getBluePieces() {
        return this.bluePieces;
    }

    public Collection<Piece> getRedPieces() {
        return this.redPieces;
    }

    public Collection<Piece> getAllPieces() {
        return Stream.concat(this.bluePieces.stream(), this.redPieces.stream()).collect(Collectors.toList());
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
    
    public static Board constructSpecificBoard(ArrayList<String> animalList, ArrayList<Integer> coordinateList, String lastTurn) {
        final Builder builder = new Builder();

        for (int i = 0; i < animalList.size(); i++) {
            String animal = animalList.get(i);
            int coordinate = coordinateList.get(i);
            switch (animal) {
                case "RA" -> builder.setPiece(new Rat(coordinate, PlayerColor.BLUE));
                case "CA" -> builder.setPiece(new Cat(coordinate, PlayerColor.BLUE));
                case "DO" -> builder.setPiece(new Dog(coordinate, PlayerColor.BLUE));
                case "WO" -> builder.setPiece(new Wolf(coordinate, PlayerColor.BLUE));
                case "LE" -> builder.setPiece(new Leopard(coordinate, PlayerColor.BLUE));
                case "TI" -> builder.setPiece(new Tiger(coordinate, PlayerColor.BLUE));
                case "LI" -> builder.setPiece(new Lion(coordinate, PlayerColor.BLUE));
                case "EL" -> builder.setPiece(new Elephant(coordinate, PlayerColor.BLUE));

                case "ra" -> builder.setPiece(new Rat(coordinate, PlayerColor.RED));
                case "ca" -> builder.setPiece(new Cat(coordinate, PlayerColor.RED));
                case "do" -> builder.setPiece(new Dog(coordinate, PlayerColor.RED));
                case "wo" -> builder.setPiece(new Wolf(coordinate, PlayerColor.RED));
                case "le" -> builder.setPiece(new Leopard(coordinate, PlayerColor.RED));
                case "ti" -> builder.setPiece(new Tiger(coordinate, PlayerColor.RED));
                case "li" -> builder.setPiece(new Lion(coordinate, PlayerColor.RED));
                case "el" -> builder.setPiece(new Elephant(coordinate, PlayerColor.RED));
            }
        }

        if (lastTurn.equals("bl")) {
            builder.setNextMovePlayer(PlayerColor.BLUE);
        } else if (lastTurn.equals("re")){
            builder.setNextMovePlayer(PlayerColor.RED);
        }
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

    private Player getPlayer(PlayerColor nextMovePlayer, Player bluePlayer, Player redPlayer) {
        if (nextMovePlayer == PlayerColor.BLUE) {
            return bluePlayer;
        } else {
            return redPlayer;
        }
    }

    public void setBoard(ArrayList<String> readList) {
        for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
            String[] split = readList.get(i).split(" ");
            System.out.println(split[i]);
        }
    }

    public Move getTransitionMove() {
        return this.transitionMove;
    }

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        PlayerColor nextMovePlayer;
        Move transitionMove;

        public Builder() {
            this.boardConfig = new HashMap<>(16, 1.0f);
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPieceCoordinate(), piece);
            if (BoardUtils.isEnemyTrap(piece.getPieceCoordinate(), piece.getPieceColor())) {
                piece.setPieceDefenseRank(0);
            } else {
                piece.setPieceDefenseRank(piece.getPieceAttackRank());
            }
            return this;
        }

        public void setNextMovePlayer(final PlayerColor nextPlayerColor) {
            this.nextMovePlayer = nextPlayerColor;
        }

        public void setMoveTransition(final Move transitionMove) {
            this.transitionMove = transitionMove;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
