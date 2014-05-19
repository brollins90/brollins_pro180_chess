package chess_rollins_blake.lib;

import chess_rollins_blake.model.pieces.Piece;

public class ChessMove {

    protected boolean capturedAPiece;
    protected BoardLocation destLoc;
    protected String message;
    protected String moveString;
    protected Piece piece;
    protected BoardLocation srcLoc;
    protected ChessMove subMove;
    protected MoveType type;

    public ChessMove(String moveString) {
        moveString = moveString.toLowerCase();
        this.message = moveString + " - ";
        this.capturedAPiece = false;
        this.moveString = moveString;
        this.piece = null;
        this.subMove = null;
    }
    
    public BoardLocation getDestLoc() {
        return this.destLoc;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getMoveString() {
        return this.moveString;
    }
    
    public Piece getPiece() {
        return this.piece;
    }
    
    public BoardLocation getSrcLoc() {
        return this.srcLoc;
    }
    
    public ChessMove getSubMove() {
        return this.subMove;
    }
    
    public MoveType getType() {
        return this.type;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
