package model.artificialintelligence;

import model.board.Board;
import model.board.Move;

public interface MoveStrategy {
    Move execute(Board board);
    long getNumEvaluatedBoards();
}
