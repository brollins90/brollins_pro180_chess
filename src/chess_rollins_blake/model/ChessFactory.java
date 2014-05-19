package chess_rollins_blake.model;

import java.util.HashMap;

import chess_rollins_blake.lib.AddMove;
import chess_rollins_blake.lib.CaptureMove;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveCreator;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.MovingMove;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.pieces.Bishop;
import chess_rollins_blake.model.pieces.King;
import chess_rollins_blake.model.pieces.Knight;
import chess_rollins_blake.model.pieces.Pawn;
import chess_rollins_blake.model.pieces.Piece;
import chess_rollins_blake.model.pieces.PieceCreator;
import chess_rollins_blake.model.pieces.Queen;
import chess_rollins_blake.model.pieces.Rook;

public class ChessFactory {

    static HashMap<MoveType, MoveCreator> moveMap = new HashMap<>();
    static {
        moveMap.put(MoveType.ADD, new MoveCreator() {
            @Override
            public ChessMove create(String moveString) {
                return new AddMove(moveString);
            }});
        moveMap.put(MoveType.CAPTURE, new MoveCreator() {
            @Override
            public ChessMove create(String moveString) {
                return new CaptureMove(moveString);
            }});
        moveMap.put(MoveType.MOVE, new MoveCreator() {
            @Override
            public ChessMove create(String moveString) {
                return new MovingMove(moveString);
            }});
    }

    static HashMap<PieceType, PieceCreator> pieceMap = new HashMap<>();
    static {
        pieceMap.put(PieceType.b, new PieceCreator() {
            @Override
            public Piece create(PieceColor color) {
                return new Bishop(color);
            }
        });
        pieceMap.put(PieceType.k, new PieceCreator() {
            @Override
            public Piece create(PieceColor color) {
                return new King(color);
            }
        });
        pieceMap.put(PieceType.n, new PieceCreator() {
            @Override
            public Piece create(PieceColor color) {
                return new Knight(color);
            }
        });
        pieceMap.put(PieceType.p, new PieceCreator() {
            @Override
            public Piece create(PieceColor color) {
                return new Pawn(color);
            }
        });
        pieceMap.put(PieceType.q, new PieceCreator() {
            @Override
            public Piece create(PieceColor color) {
                return new Queen(color);
            }
        });
        pieceMap.put(PieceType.r, new PieceCreator() {
            @Override
            public Piece create(PieceColor color) {
                return new Rook(color);
            }
        });
    }

    public static ChessMove CreateMove(MoveType type, String moveString) {
        return moveMap.get(type).create(moveString);
    }

    public static Piece CreatePiece(PieceType type, PieceColor color) {
        return pieceMap.get(type).create(color);
    }
}
