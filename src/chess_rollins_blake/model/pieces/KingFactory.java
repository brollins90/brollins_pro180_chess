package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.PieceColor;

public class KingFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new King(color);
    }

}
