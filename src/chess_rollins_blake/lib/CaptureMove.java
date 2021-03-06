package chess_rollins_blake.lib;

import chess_rollins_blake.model.pieces.Piece;

public class CaptureMove extends MovingMove {

    protected Piece capturedPiece;
    
    public CaptureMove(String moveString) {
        super(moveString);
        //super.capturedAPiece = true;
        super.type = MoveType.CAPTURE;
        super.message = this.message.substring(0, this.message.length() - 2) + " and captured a piece\n";
    }
    
    public void setPieceCaptured(Piece captured) {
        this.capturedPiece = captured;
    }
    
    public Piece getCapturedPiece() {
        return this.capturedPiece;
    }
}
