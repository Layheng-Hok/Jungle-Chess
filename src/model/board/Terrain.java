package model.board;

import model.piece.Piece;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Terrain {
    protected final int terrainCoordinate;
    private static final Map<Integer, EmptyTerrain> EMPTY_TERRAINS = constructAllPossibleEmptyTerrains();

    private static Map<Integer, EmptyTerrain> constructAllPossibleEmptyTerrains() {
        final Map<Integer, EmptyTerrain> emptyTerrainsMap = new HashMap<>();
        for (int i = 0; i < BoardUtils.NUM_TERRAINS; i++) {
            emptyTerrainsMap.put(i, new EmptyTerrain(i));
        }
        return Collections.unmodifiableMap(emptyTerrainsMap);
    }

    private Terrain(final int terrainCoordinate) {
        this.terrainCoordinate = terrainCoordinate;
    }

    public static Terrain constructTerrain(int terrainCoordinate, Piece piece) {
        return piece != null ? new OccupiedTerrain(terrainCoordinate, piece) : EMPTY_TERRAINS.get(terrainCoordinate);
    }

    public abstract boolean isTerrainOccupied();

    public abstract Piece getPiece();

    public int getTerrainCoordinate() {
        return this.terrainCoordinate;
    }

    public static final class EmptyTerrain extends Terrain {
        private EmptyTerrain(final int terrainCoordinate) {
            super(terrainCoordinate);
        }

        @Override
        public String toString() {
            return "*";
        }

        @Override
        public boolean isTerrainOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTerrain extends Terrain {
        private final Piece pieceOnTerrain;

        private OccupiedTerrain(final int terrainCoordinate, final Piece pieceOnTerrain) {
            super(terrainCoordinate);
            this.pieceOnTerrain = pieceOnTerrain;
        }

        @Override
        public String toString() {
            return getPiece().getPieceColor().isRed() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }

        @Override
        public boolean isTerrainOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTerrain;
        }
    }
}
