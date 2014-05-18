package chess_rollins_blake.lib;

import chess_rollins_blake.model.pieces.Piece;

public interface PieceCreator {

    public Piece create(PieceColor color);
}
