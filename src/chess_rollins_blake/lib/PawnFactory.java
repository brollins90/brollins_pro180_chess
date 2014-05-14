package chess_rollins_blake.lib;

public class PawnFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new Pawn(color);
    }

}
