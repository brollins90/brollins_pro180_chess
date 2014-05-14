package chess_rollins_blake.lib;

public class KingFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new King(color);
    }

}
