package model.piece.animal;

public enum Animal {
    TRAPPED_ANIMAL ("0", "A"),
    RAT ("1", "R"),
    CAT ("2", "C"),
    DOG ("3",  "D"),
    WOLF ("4", "W"),
    LEOPARD ("5", "P"),
    TIGER ("6", "T"),
    LION("7", "L"),
    ELEPHANT("8", "E");

    private final String pieceRank;
    private final String pieceName;

    Animal(final String pieceRank, final String pieceName) {
        this.pieceRank = pieceRank;
        this.pieceName = pieceName;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }

    public String getPieceRank() {
        return this.pieceRank;
    }
}
