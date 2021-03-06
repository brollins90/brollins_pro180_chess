package chess_rollins_blake.model;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chess_rollins_blake.ConsoleChess;
import chess_rollins_blake.exceptions.InvalidMoveException;
import chess_rollins_blake.lib.AddMove;
import chess_rollins_blake.lib.CaptureMove;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.MovingMove;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.lib.PromotionMove;
import chess_rollins_blake.model.pieces.Bishop;
import chess_rollins_blake.model.pieces.King;
import chess_rollins_blake.model.pieces.Knight;
import chess_rollins_blake.model.pieces.Pawn;
import chess_rollins_blake.model.pieces.Piece;
import chess_rollins_blake.model.pieces.Queen;
import chess_rollins_blake.model.pieces.Rook;

public class ChessFactory {
    public interface MoveCreator {

        public ChessMove create(String moveString);
    };
    public interface PieceCreator {

        public Piece create(boolean isWhite);
    };

    static HashMap<MoveType, MoveCreator> moveMap = new HashMap<>();
    static {
        moveMap.put(MoveType.ADD, new MoveCreator() {
            @Override
            public ChessMove create(String moveString) {
                return new AddMove(moveString);
            }
        });
        moveMap.put(MoveType.CAPTURE, new MoveCreator() {
            @Override
            public ChessMove create(String moveString) {
                return new CaptureMove(moveString);
            }
        });
        moveMap.put(MoveType.MOVE, new MoveCreator() {
            @Override
            public ChessMove create(String moveString) {
                return new MovingMove(moveString);
            }
        });
        moveMap.put(MoveType.PROMOTION, new MoveCreator() {
            @Override
            public ChessMove create(String moveString) {
                return new PromotionMove(moveString);
            }
        });
    }

    static HashMap<PieceType, PieceCreator> pieceMap = new HashMap<>();
    static {
        pieceMap.put(PieceType.b, new PieceCreator() {
            @Override
            public Piece create(boolean isWhite) {
                return new Bishop(isWhite);
            }
        });
        pieceMap.put(PieceType.k, new PieceCreator() {
            @Override
            public Piece create(boolean isWhite) {
                return new King(isWhite);
            }
        });
        pieceMap.put(PieceType.n, new PieceCreator() {
            @Override
            public Piece create(boolean isWhite) {
                return new Knight(isWhite);
            }
        });
        pieceMap.put(PieceType.p, new PieceCreator() {
            @Override
            public Piece create(boolean isWhite) {
                return new Pawn(isWhite);
            }
        });
        pieceMap.put(PieceType.q, new PieceCreator() {
            @Override
            public Piece create(boolean isWhite) {
                return new Queen(isWhite);
            }
        });
        pieceMap.put(PieceType.r, new PieceCreator() {
            @Override
            public Piece create(boolean isWhite) {
                return new Rook(isWhite);
            }
        });
    }

    public static ChessMove CreateMove(MoveType type, String moveString) {
        ConsoleChess.debugMessage("ChessFactory.ChessMove(" + type + "," + moveString + ")");
        return moveMap.get(type).create(moveString);
    }
    
    public static ChessMove CreateMove(String moveString) {
        ConsoleChess.debugMessage("ChessFactory.ChessMove(" + moveString + ")");
        return moveMap.get(ValidateMoveString(moveString)).create(moveString);
    }

    public static Piece CreatePiece(PieceType type, boolean isWhite) {
        return pieceMap.get(type).create(isWhite);
    }

    public static MoveType ValidateMoveString(String moveString) {

        MoveType returnType = null;
        // Force lower case
        moveString = moveString.toLowerCase();

        Pattern ADD_REGEX = Pattern.compile("[kqrbnp][ld][a-h][1-8]");
        Pattern MOVE_REGEX = Pattern.compile("[a-h][1-8] [a-h][1-8]\\*?");
        Pattern MOVE2_REGEX = Pattern.compile("[a-h][1-8] [a-h][1-8] [a-h][1-8] [a-h][1-8]");
        Pattern LOCATION_REGEX = Pattern.compile("[a-h][1-8]");
        Pattern PROMOTION_REGEX = Pattern.compile("[a-h][1-8] [kqrbnp][ld][a-h][1-8]");
        Matcher m;

        // Check the Add regex
        m = ADD_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = MoveType.ADD;
        }

        // Check the move regex
        m = MOVE_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = (moveString.contains("*")) ? MoveType.CAPTURE : MoveType.MOVE;
        }

        // Check the double move regex
        m = MOVE2_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = MoveType.MOVE;
        }

        // Check if it is a valid location request
        m = LOCATION_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = MoveType.LOCATION;
        }

        // Check if it is a valid promotion move
        m = PROMOTION_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = MoveType.PROMOTION;
        }

        // None of them are valid to return null
        
        if (returnType == null) {
            throw new InvalidMoveException(moveString + " - \nThe move syntax was not valid for '" + moveString + "'");
        }
        
        return returnType;

    }
}
