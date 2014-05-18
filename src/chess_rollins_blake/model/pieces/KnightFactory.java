package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.PieceColor;

public class KnightFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new Knight(color);
    }

}
