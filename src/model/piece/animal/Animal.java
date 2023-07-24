package model.piece.animal;

public enum Animal {
    TRAPPED_ANIMAL (0, "Trapped Animal"),
    RAT (500, "Rat"),
    CAT (200, "Cat"),
    DOG (300,  "Dog"),
    WOLF (400, "Wolf"),
    LEOPARD (500, "Leopard"),
    TIGER (800, "Tiger"),
    LION(900, "Lion"),
    ELEPHANT(1000, "Elephant");

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
