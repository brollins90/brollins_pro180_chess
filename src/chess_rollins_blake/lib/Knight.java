package chess_rollins_blake.lib;

public class Knight extends Piece {

    public Knight(PieceColor color, PieceStatus status) {
        super(color, status);
        super.type = PieceType.n;
    }

    @Override
    public boolean isValidMove(BoardLocation src, BoardLocation dest, boolean willCapture) {
        // TODO Auto-generated method stub
        return false;
    }

}
