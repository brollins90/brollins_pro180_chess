package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;

public class Queen extends Piece {

    public Queen(PieceColor color) {
        super(color);
        super.type = PieceType.q;
        this.canCollideOnMove = true;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest) {
        boolean isValid = false;

        if (dest.getColumn() == src.getColumn()) {
            isValid = true;
        }
        if (dest.getRow() == src.getRow()) {
            isValid = true;
        }
        if (Math.abs(dest.getColumn() - src.getColumn()) == Math.abs(dest.getRow() - src.getRow())) {
            isValid = true;
        }
        
        return isValid;        
    }

}