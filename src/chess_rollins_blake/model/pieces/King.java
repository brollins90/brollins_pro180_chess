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

        if (isValidCastlingMove(src, dest)) {
            isValid = true;
        } else {

            // Row changes in the same column
            if ((dest.getColumn() == src.getColumn()) && (Math.abs(dest.getRow() - src.getRow()) == 1)) {
                isValid = true;
            }

            // Column changes in the same row
            if ((dest.getRow() == src.getRow()) && (Math.abs(dest.getColumn() - src.getColumn()) == 1)) {
                isValid = true;
            }

            // diagonal change && change is only 1 space
            if (Math.abs(dest.getColumn() - src.getColumn()) == Math.abs(dest.getRow() - src.getRow()) && ((Math.abs(dest.getColumn() - src.getColumn()) == 1) || ((Math.abs(dest.getRow() - src.getRow()) == 1)))) {
                isValid = true;
            }
        }

        return isValid;
    }

    public boolean isValidCastlingMove(BoardLocation src, BoardLocation dest) {
        boolean isValid = false;

        if (!hasBeenInCheck) {

            if (isWhite()) {
                // Check if in start loc
                if (src == BoardLocation.e1) {

                    // check if dest is one of the valid locs
                    if (dest == BoardLocation.g1 || dest == BoardLocation.c1) {
                        isValid = true;
                    } else {
                        // dest is not a valid castling loc
                    }
                } else {
                    // src is not a valid castling loc
                }
            }
            // is black
            else {
                if (src == BoardLocation.e8) {
                    if (dest == BoardLocation.g8 || dest == BoardLocation.c8) {
                        isValid = true;
                    }
                }
            }

        } else {
            // Has been in check, so cannot castle
        }
        return isValid;
    }
}
