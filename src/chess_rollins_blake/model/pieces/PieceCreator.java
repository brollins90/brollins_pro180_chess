package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.PieceColor;

public interface PieceCreator {

    public Piece create(PieceColor color);
}
