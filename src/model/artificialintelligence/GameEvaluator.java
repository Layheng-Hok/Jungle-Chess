package model.artificialintelligence;

import model.board.Board;

public interface GameEvaluator {
    int evaluate (Board board, int depth);
}
