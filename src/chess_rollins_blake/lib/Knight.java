package chess_rollins_blake.lib;

public class Knight extends Piece {

    public Knight(PieceColor color) {
        super(color);
        super.type = PieceType.n;
    }

    @Override
    public boolean isValidMove(BoardLocation src, BoardLocation dest, boolean willCapture) {
        // TODO Auto-generated method stub
        return false;
    }

}
