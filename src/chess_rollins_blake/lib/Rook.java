package chess_rollins_blake.lib;

public class Rook extends Piece {

    public Rook(PieceColor color) {
        super(color);
        super.type = PieceType.r;
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
        
        return isValid;        
    }

}
