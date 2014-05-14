package chess_rollins_blake.lib;

import java.util.HashMap;

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
