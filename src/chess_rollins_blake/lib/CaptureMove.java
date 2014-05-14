package chess_rollins_blake.lib;

public class CaptureMove extends MovingMove {

    public CaptureMove(String moveString) {
        super(moveString);
        this.capturedAPiece = true;
        this.type = MoveType.CAPTURE;
        this.message = this.message.substring(0,this.message.length() -2) + " and captured a piece\n";
    }
}
