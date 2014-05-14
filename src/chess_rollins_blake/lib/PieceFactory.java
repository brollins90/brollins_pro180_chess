package chess_rollins_blake.lib;

public class PieceFactory {
    
    public static Piece CreatePiece(PieceType type, PieceColor color) {
        switch (type) {
            case b:
                return new Bishop(color);
            case k:
                return new King(color);
            case n:
                return new Knight(color);
            case p:
            default:
                return new Pawn(color);
            case q:
                return new Queen(color);
            case r:
                return new Rook(color);
        }
    }
}
