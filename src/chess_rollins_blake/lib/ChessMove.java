package chess_rollins_blake.lib;

public class ChessMove {

    public BoardLocation srcLoc;
    public BoardLocation destLoc;
    public Piece piece;
    public String moveString;
    public ChessMove subMove;
    public boolean capturedAPiece;
    public MoveType type;
    public String message;

    public ChessMove(String moveString) {
        moveString = moveString.toLowerCase();
        this.message = moveString + " - ";
        this.capturedAPiece = false;
        this.moveString = moveString;
        this.piece = null;
        this.subMove = null;
    }
}
