package model.board;

import model.piece.Piece;

public abstract class Move {
    protected Board board;
    protected final Piece movedPiece;
    protected int destinationCoordinate;
    protected final boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(final Board board, final int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPieceCoordinate();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return this.getCurrentCoordinate() == otherMove.getCurrentCoordinate()
                && this.getDestinationCoordinate() == otherMove.getDestinationCoordinate()
                && this.getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPieceCoordinate();
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public boolean isCaptureMove() {
        return false;
    }

    public Piece getCapturedPiece() {
        return null;
    }

    public Board execute() {
        final Board.Builder builder = new Board.Builder();
        for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.getCurrentPlayer().getEnemyPlayer().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setNextMovePlayer(this.board.getCurrentPlayer().getEnemyPlayer().getAllyColor());
        return builder.build();
    }

    String disambiguationFile() {
        for (final Move move : this.board.getCurrentPlayer().getValidMoves()) {
            if (move.getDestinationCoordinate() == this.destinationCoordinate && !this.equals(move) &&
                    this.movedPiece.getPieceType().equals(move.getMovedPiece().getPieceType())) {
                return Utilities.getPositionAtCoordinate(this.movedPiece.getPieceCoordinate()).substring(0, 1);
            }
        }
        return "";
    }

    public Board undo() {
        final Board.Builder builder = new Board.Builder();
        for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            builder.setPiece(piece);
        }
        for (final Piece piece : this.board.getCurrentPlayer().getEnemyPlayer().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.undoMove(this));
        builder.setNextMovePlayer(this.board.getCurrentPlayer().getAllyColor());
        return builder.build();
    }

    public static final class StandardMove extends Move {
        public StandardMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof StandardMove && super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceColor().toString().toLowerCase().substring(0, 2) + " "
                    + movedPiece.getPieceCoordinate() + " "
                    + destinationCoordinate;
        }
    }

    public static class CaptureMove extends Move {
        final Piece capturedPiece;

        public CaptureMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece capturedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.capturedPiece = capturedPiece;
        }

        @Override
        public int hashCode() {
            return this.capturedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CaptureMove)) {
                return false;
            }
            final CaptureMove otherCaptureMove = (CaptureMove) other;
            return super.equals(otherCaptureMove)
                    && this.getCapturedPiece().equals(otherCaptureMove.getCapturedPiece());
        }

        @Override
        public boolean isCaptureMove() {
            return true;
        }

        @Override
        public Piece getCapturedPiece() {
            return this.capturedPiece;
        }

        @Override
        public String toString() {
            return movedPiece.getPieceColor().toString().toLowerCase().substring(0, 2) + " "
                    + movedPiece.getPieceCoordinate() + " "
                    + destinationCoordinate;
        }
    }

    static class NullMove extends Move {

        NullMove() {
            super(null, -1);
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }

        @Override
        public int getDestinationCoordinate() {
            return -1;
        }

        @Override
        public Board execute() {
            throw new RuntimeException("You cannot execute a null move.");
        }

        @Override
        public String toString() {
            return "Null Move";
        }
    }

    public static class MoveCreator {
        private MoveCreator() {
            throw new RuntimeException("You cannot instantiate an object of \"MoveFactory\".");
        }

        public static Move getNullMove() {
            return NULL_MOVE;
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllValidMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
