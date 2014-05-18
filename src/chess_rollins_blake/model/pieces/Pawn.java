package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
        super.type = PieceType.p;
        this.canCollideOnMove = true;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest) {
        boolean isValid = false;
        
        int pawnStartRow = (super.color == PieceColor.l) ? 1 : 6;
        int pawnCurRow = src.ordinal() % 8;
//        int pawnCurCol = src.ordinal() - (pawnCurRow * 8);
        int pawnMoveDirection = (super.color == PieceColor.l) ? 1 : -1;
        
        int moveVal = (dest.ordinal() - src.ordinal()) * pawnMoveDirection;
        
        if (moveVal == 1) {
            isValid = true;
        }
        
        if (pawnCurRow == pawnStartRow && moveVal == 2) {
            isValid = true;
        }
//        
//        if (willCapture) {
//            // check for the diag
//        }
        
        isValid = true;
        return isValid;
    }

}
