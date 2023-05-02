package model.board;

import model.piece.Piece;
import model.piece.animal.Cat;
import model.piece.animal.Dog;
import model.piece.animal.Rat;
import model.piece.animal.Wolf;
import model.player.PlayerColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Board {
    private final List<Terrain> gameBoard;

    private Board(Builder builder) {
        this.gameBoard = constructGameBoard(builder);
    }

    public Terrain getTerrain(int terrainCoordinate) {
        return null;
    }

    private static List<Terrain> constructGameBoard(final Builder builder) {
        final Terrain[] terrains = new Terrain[BoardUtils.NUM_TERRAINS];
        for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
            terrains[i] = Terrain.constructTerrain(i, builder.boardConfig.get(i));
        }
        return Collections.unmodifiableList(Arrays.asList(terrains));
    }

    public static Board constructStandardBoard() {
        final Builder builder = new Builder();
        // blue layout
        builder.setPiece(new Rat(48, PlayerColor.BLUE));
        builder.setPiece(new Cat(50, PlayerColor.BLUE));
        builder.setPiece(new Dog(59, PlayerColor.BLUE));
        builder.setPiece(new Wolf(44, PlayerColor.BLUE));
        builder.set
    }

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        PlayerColor nextMovePlayer;

        public Builder() {
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPieceCoordinate(), piece);
            return this;
        }

        public Builder setMovePlayer(final PlayerColor nextPlayerColor) {
            this.nextMovePlayer = nextPlayerColor;
            return this;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
