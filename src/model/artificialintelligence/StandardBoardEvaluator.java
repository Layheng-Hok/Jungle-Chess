package model.artificialintelligence;

import model.board.Board;
import model.piece.Piece;
import model.player.Player;

public final class StandardBoardEvaluator implements GameEvaluator {

    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.getCurrentPlayer(), depth) -
                scorePlayer(board, board.getCurrentPlayer().getEnemyPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) {
        return pieceValue(player);
    }

    private static int pieceValue (final Player player) {
       int pieceValueScore = 0;
       for (final Piece piece : player.getActivePieces()) {
           pieceValueScore += piece.getPiecePower();
       }
       return pieceValueScore;
    }
}
