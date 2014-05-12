package chess_rollins_blake.lib;

public class PieceFactory {
    
    public static Piece CreatePiece(PieceType type, PieceColor color) {
        switch (type) {
            case b:
                return new Bishop(color, PieceStatus.ALIVE);
            case k:
                return new King(color, PieceStatus.ALIVE);
            case n:
                return new Knight(color, PieceStatus.ALIVE);
            case p:
            default:
                return new Pawn(color, PieceStatus.ALIVE);
            case q:
                return new Queen(color, PieceStatus.ALIVE);
            case r:
                return new Rook(color, PieceStatus.ALIVE);
        }
    }
}
