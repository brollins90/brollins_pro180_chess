package chess_rollins_blake.lib;

public class QueenFactory implements PieceCreator {

    @Override
    public Piece create(PieceColor color) {
        return new Queen(color);
    }

}
