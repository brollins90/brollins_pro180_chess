package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;

public class Bishop extends Piece {

    public Bishop(PieceColor color) {
        super(color);
        super.type = PieceType.b;
        this.canCollideOnMove = true;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest, boolean capturing) {
        boolean isValid = true;

        if (isValid && (src == dest)) {
            isValid = false;
        }
        if (isValid && (Math.abs(dest.getColumn() - src.getColumn())) != Math.abs(dest.getRow() - src.getRow())) {
            isValid = false;
        }
        return isValid;
    }

}
