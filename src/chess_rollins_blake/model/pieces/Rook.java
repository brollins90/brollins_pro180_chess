package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceType;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
        super.type = PieceType.r;
        this.canCollideOnMove = true;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest, boolean capturing) {
        boolean isValid = false;

        if (dest.getColumn() == src.getColumn()) {
            isValid = true;
        }
        if (dest.getRow() == src.getRow()) {
            isValid = true;
        }

        return isValid;
    }

}
