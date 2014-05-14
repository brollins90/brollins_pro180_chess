package chess_rollins_blake.lib;

public class BishopFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new Bishop(color);
    }

}
