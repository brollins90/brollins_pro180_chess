package chess_rollins_blake.lib;

public class Queen extends Piece {

    public Queen(PieceColor color, PieceStatus status) {
        super(color, status);
        super.type = PieceType.q;
    }

    @Override
    public boolean isValidMove(BoardLocation src, BoardLocation dest, boolean willCapture) {
        // TODO Auto-generated method stub
        return false;
    }

}
