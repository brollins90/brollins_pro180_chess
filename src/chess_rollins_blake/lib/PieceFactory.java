package chess_rollins_blake.lib;

import chess_rollins_blake.model.pieces.Bishop;
import chess_rollins_blake.model.pieces.King;
import chess_rollins_blake.model.pieces.Knight;
import chess_rollins_blake.model.pieces.Pawn;
import chess_rollins_blake.model.pieces.Piece;
import chess_rollins_blake.model.pieces.Queen;
import chess_rollins_blake.model.pieces.Rook;

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
