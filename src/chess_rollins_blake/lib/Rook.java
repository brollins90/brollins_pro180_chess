package chess_rollins_blake.lib;

public class Rook extends Piece {

    public Rook(PieceColor color, PieceStatus status) {
        super(color, status);
        super.type = PieceType.r;
    }

    @Override
    public boolean isValidMove(BoardLocation src, BoardLocation dest, boolean willCapture) {
        // TODO Auto-generated method stub
        return false;
    }

}
