package chess_rollins_blake.lib;

public class King extends Piece {

    public King(PieceColor color) {
        super(color);
        super.type = PieceType.k;
    }

    @Override
    public boolean isValidMove(BoardLocation src, BoardLocation dest, boolean willCapture) {
        // TODO Auto-generated method stub
        return false;
    }

}
