package model.artificialintelligence;

import model.board.Board;
import model.board.Move;

public interface Strategy {
    Move execute(Board board);
}
