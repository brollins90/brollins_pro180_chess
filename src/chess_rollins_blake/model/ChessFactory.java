package chess_rollins_blake.model;

import java.util.HashMap;

import chess_rollins_blake.lib.AddMoveFactory;
import chess_rollins_blake.lib.CaptureMoveFactory;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveCreator;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.MovingMoveFactory;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.pieces.BishopFactory;
import chess_rollins_blake.model.pieces.KingFactory;
import chess_rollins_blake.model.pieces.KnightFactory;
import chess_rollins_blake.model.pieces.PawnFactory;
import chess_rollins_blake.model.pieces.Piece;
import chess_rollins_blake.model.pieces.PieceCreator;
import chess_rollins_blake.model.pieces.QueenFactory;
import chess_rollins_blake.model.pieces.RookFactory;

public class ChessFactory {

    static HashMap<MoveType, MoveCreator> moveMap = new HashMap<>();
    static {
        moveMap.put(MoveType.ADD, new AddMoveFactory());
        moveMap.put(MoveType.CAPTURE, new CaptureMoveFactory());
        moveMap.put(MoveType.MOVE, new MovingMoveFactory());
    }

    static HashMap<PieceType, PieceCreator> pieceMap = new HashMap<>();
    static {
        pieceMap.put(PieceType.b, new BishopFactory());
        pieceMap.put(PieceType.k, new KingFactory());
        pieceMap.put(PieceType.n, new KnightFactory());
        pieceMap.put(PieceType.p, new PawnFactory());
        pieceMap.put(PieceType.q, new QueenFactory());
        pieceMap.put(PieceType.r, new RookFactory());
    }

    public static ChessMove CreateMove(MoveType type, String moveString) {
        return moveMap.get(type).create(moveString);
    }

    public static Piece CreatePiece(PieceType type, PieceColor color) {
        return pieceMap.get(type).create(color);
    }
}
