package chess_rollins_blake.lib;

public class KnightFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new Knight(color);
    }

}
