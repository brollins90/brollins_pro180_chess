package chess_rollins_blake.lib;

public class MovingMove extends ChessMove {

    public MovingMove(String moveString) {
        super(moveString);
        this.srcLoc = BoardLocation.valueOf("" + moveString.substring(0, 2));
        this.destLoc = BoardLocation.valueOf("" + moveString.substring(3, 5));
        this.message += "Moved a piece from " + this.srcLoc + " to " + this.destLoc;
        if (moveString.length() > 6) {
            this.subMove = new MovingMove(moveString.substring(6));
            this.message += " and includes the next move";
        }
        this.message += ".\n";
        this.type = MoveType.MOVE;
    }

}
