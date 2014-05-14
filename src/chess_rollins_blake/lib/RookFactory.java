package chess_rollins_blake.lib;

public class RookFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new Rook(color);
    }

}
