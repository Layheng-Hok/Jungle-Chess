package model.board;

import model.piece.Piece;
import model.player.PlayerColor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Terrain {
    protected final int terrainCoordinate;
    private static final Map<Integer, EmptyTerrain> EMPTY_TERRAINS = constructAllPossibleEmptyTerrains();

    private static Map<Integer, EmptyTerrain> constructAllPossibleEmptyTerrains() {
        final Map<Integer, EmptyTerrain> emptyTerrainsMap = new HashMap<>();
        for (int i = 0; i < 63; i++) {
            emptyTerrainsMap.put(i, new EmptyTerrain(i));
        }
        return Collections.unmodifiableMap(emptyTerrainsMap);
    }

    Terrain(int terrainCoordinate) {
        this.terrainCoordinate = terrainCoordinate;
    }

    public abstract boolean isTerrainOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTerrain extends Terrain {
        private EmptyTerrain(final int coordinate) {
            super(coordinate);
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

        private OccupiedTerrain(int coordinate, Piece pieceOnTerrain) {
            super(coordinate);
            this.pieceOnTerrain = pieceOnTerrain;
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
