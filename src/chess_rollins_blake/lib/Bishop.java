package chess_rollins_blake.lib;

public class Bishop extends Piece {

    public Bishop(PieceColor color, PieceStatus status) {
        super(color, status);
        super.type = PieceType.b;
    }

    @Override
    public boolean isValidMove(BoardLocation src, BoardLocation dest, boolean willCapture) {

        if ((dest.ordinal() - src.ordinal() == 9) || (dest.ordinal() - src.ordinal() == 7)) {
            return true;
        } else {
            return false;
        }
    }

}
