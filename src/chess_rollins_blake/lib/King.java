package chess_rollins_blake.lib;

public class King extends Piece {

    private boolean hasBeenInCheck;
    
    public King(PieceColor color) {
        super(color);
        super.type = PieceType.k;
        this.hasBeenInCheck = false;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest) {
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
