package chess_rollins_blake.lib;

public class Queen extends Piece {

    public Queen(PieceColor color) {
        super(color);
        super.type = PieceType.q;
    }

    @Override
    public boolean isValidMove(BoardLocation src, BoardLocation dest, boolean willCapture) {
        // TODO Auto-generated method stub
        return false;
    }

}
