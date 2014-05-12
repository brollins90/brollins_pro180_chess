package chess_rollins_blake.lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chess_rollins_blake.model.PieceList;

public class ChessMove {

    public BoardLocation srcLoc;
    public BoardLocation destLoc;
    public Piece piece;
    public String moveString;
    public ChessMove subMove;
    public Piece capturedPiece;
    public boolean capturedAPiece;
    public MoveType type;
    public String message;

    public ChessMove(String moveString) {
        moveString = moveString.toLowerCase();
        this.message = moveString + "\n";
        this.capturedAPiece = false;
        this.capturedPiece = null;
        this.moveString = moveString;
        this.piece = null;
        this.subMove = null;


        if (moveString.length() == 4) {
            PieceType type = PieceType.valueOf("" + moveString.charAt(0));
            PieceColor color = PieceColor.valueOf("" + moveString.charAt(1));
            switch (type) {
                case b:
                    this.piece = new Bishop(color, PieceStatus.ALIVE);
                    break;
                case k:
                    this.piece = new King(color, PieceStatus.ALIVE);
                    break;
                case n:
                    this.piece = new Knight(color, PieceStatus.ALIVE);
                    break;
                case p:
                default:
                    this.piece = new Pawn(color, PieceStatus.ALIVE);
                    break;
                case q:
                    this.piece = new Queen(color, PieceStatus.ALIVE);
                    break;
                case r:
                    this.piece = new Rook(color, PieceStatus.ALIVE);
                    break;
            }
            this.srcLoc = BoardLocation.none;
            this.destLoc = BoardLocation.valueOf("" + moveString.substring(2));
            this.type = MoveType.ADD;
            this.message += "Created a new Piece at " + this.destLoc + " color: " + this.piece.getColor() + " type: " + this.piece.getType() + ".\n";
        } else if (moveString.length() == 11) {
            this.srcLoc = BoardLocation.valueOf("" + moveString.substring(0, 2));
            this.destLoc = BoardLocation.valueOf("" + moveString.substring(3, 5));
            this.subMove = new ChessMove(moveString.substring(6));
            this.type = MoveType.MOVE;
            this.message += "Moved a piece from " + this.srcLoc + " to " + this.destLoc + " and includes the next move.\n";
        } else /* if (moveString.length() = 5 or 6 */{
            this.srcLoc = BoardLocation.valueOf("" + moveString.substring(0, 2));
            this.destLoc = BoardLocation.valueOf("" + moveString.substring(3, 5));
            this.type = MoveType.MOVE;
            if (moveString.contains("*")) {
                this.capturedPiece = null;
                this.capturedAPiece = true;
                this.type = MoveType.CAPTURE;
            }
            this.message += "Moved a piece from " + this.srcLoc + " to " + this.destLoc + ".\n";
            if (this.capturedAPiece) {
                this.message += " and captured a piece\n";
            }
        }
    }

    public static boolean validateSyntax(String testString) {

        Pattern p = Pattern.compile("(\\w{4})|(\\w{2} \\w{2}\\*+)|(\\w{2} \\w{2} \\w{2} \\w{2})");
        Matcher m = p.matcher(testString);
        boolean b = m.matches();

        return b;
    }

    public static boolean validateMove(String testString, PieceList pieces) {



        return true;
    }
}
