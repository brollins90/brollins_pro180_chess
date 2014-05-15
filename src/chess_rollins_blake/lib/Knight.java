package chess_rollins_blake.lib;

public class Knight extends Piece {

    public Knight(PieceColor color) {
        super(color);
        super.type = PieceType.n;
    }

    @Override
    public boolean isValidMovement(BoardLocation src, BoardLocation dest) {
        boolean isValid = false;

        switch (Math.abs(dest.getColumn() - src.getColumn())) {
            case 1:
                isValid = (Math.abs(dest.getRow() - src.getRow())) == 2 ? true : false;
                break;
            case 2:
                isValid = (Math.abs(dest.getRow() - src.getRow())) == 1 ? true : false;
                break;
        }
        
        return isValid;        
    }

}
