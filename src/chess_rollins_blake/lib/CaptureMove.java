package chess_rollins_blake.lib;

public class CaptureMove extends MovingMove {

    public CaptureMove(String moveString) {
        super(moveString);
        this.capturedAPiece = true;
        this.type = MoveType.CAPTURE;
        this.message += " and captured a piece\n";
    }
}
