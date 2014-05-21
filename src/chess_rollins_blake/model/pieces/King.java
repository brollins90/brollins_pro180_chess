package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceType;

public class King extends Piece {

    private boolean hasBeenInCheck;
    
    public King(boolean isWhite) {
        super(isWhite);
        super.type = PieceType.k;
        this.hasBeenInCheck = false;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest, boolean capturing) {
        boolean isValid = false;

        if ((dest.getColumn() == src.getColumn()) && (Math.abs(dest.getRow() - src.getRow()) == 1)) {
            isValid = true;
        }
        if ((dest.getRow() == src.getRow()) && (Math.abs(dest.getColumn() - src.getColumn()) == 1)) {
            isValid = true;
        }
        if (Math.abs(dest.getColumn() - src.getColumn()) == Math.abs(dest.getRow() - src.getRow()) && ((Math.abs(dest.getColumn() - src.getColumn()) == 1) || ((Math.abs(dest.getRow() - src.getRow()) == 1)))) {
            isValid = true;
        }
        
        return isValid;        
    }

}
