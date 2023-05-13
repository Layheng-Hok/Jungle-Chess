package model.piece.animal;

public enum Animal {
    TRAPPED_ANIMAL (0, "Trapped Animal"),
    RAT (600, "Rat"),
    CAT (100, "Cat"),
    DOG (200,  "Dog"),
    WOLF (300, "Wolf"),
    LEOPARD (500, "Leopard"),
    TIGER (700, "Tiger"),
    LION(800, "Lion"),
    ELEPHANT(800, "Elephant");

    private final int piecePower;
    private final String pieceName;

    Animal(final int piecePower, final String pieceName) {
        this.piecePower = piecePower;
        this.pieceName = pieceName;
    }

    @Override
    public String toString() {
        return this.pieceName;
    }

    public int getPiecePower() {
        return this.piecePower;
    }
}
