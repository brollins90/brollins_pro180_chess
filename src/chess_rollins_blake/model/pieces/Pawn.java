package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceType;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
        super.type = PieceType.p;
        this.canCollideOnMove = true;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest, boolean capturing) {
        boolean isValid = false;
        int pawnMoveDirection = (super.isWhite()) ? 1 : -1;
        int moveVal = (dest.ordinal() - src.ordinal()) * pawnMoveDirection;

        if (capturing) {
            if (super.isWhite()) {
                if (src.ordinal() + 9 == dest.ordinal() || src.ordinal() - 7 == dest.ordinal()) {
                    isValid = true;
                }
            } else {
                if (src.ordinal() - 9 == dest.ordinal() || src.ordinal() + 7 == dest.ordinal()) {
                    isValid = true;
                }
            }
        } else {

            int pawnStartRow = (super.isWhite()) ? 1 : 6;
            int pawnCurRow = src.getRow();

            if (moveVal == 1) {
                isValid = true;
            }

            if (pawnCurRow == pawnStartRow && moveVal == 2) {
                isValid = true;
            }
        }
        return isValid;
    }

    public boolean isInEigthRow(BoardLocation src) {       

        int pawnEndRow = (super.isWhite()) ? 7 : 0;
        int pawnCurRow = src.getRow();
        return pawnCurRow == pawnEndRow;
    }

}
