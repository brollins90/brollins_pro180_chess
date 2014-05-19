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
    public boolean isValidMovement(BoardLocation src, BoardLocation dest, boolean capturing) {
        boolean isValid = false;
        int pawnMoveDirection = (super.color == PieceColor.l) ? 1 : -1;
        int moveVal = (dest.ordinal() - src.ordinal()) * pawnMoveDirection;

        if (capturing) {
            if (super.color == PieceColor.l) {
                if (src.ordinal() + 9 == dest.ordinal() || src.ordinal() - 7 == dest.ordinal()) {
                    isValid = true;
                }
            } else {
                if (src.ordinal() - 9 == dest.ordinal() || src.ordinal() + 7 == dest.ordinal()) {
                    isValid = true;
                }
            }
        } else {

            int pawnStartRow = (super.color == PieceColor.l) ? 1 : 6;
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

}
