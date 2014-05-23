package chess_rollins_blake.lib;

public class MovingMove extends ChessMove {

    public MovingMove(String moveString) {
        super(moveString);
        super.srcLoc = BoardLocation.valueOf("" + moveString.substring(0, 2));
        super.destLoc = BoardLocation.valueOf("" + moveString.substring(3, 5));
        super.message += "Moved a piece from " + this.srcLoc + " to " + this.destLoc;
        if (moveString.length() > 6) {
            this.subMove = new MovingMove(moveString.substring(6));
            this.message += " and includes the next move";
            super.changeTurnAfter = false;
        }
        super.message += ".\n";
        super.type = MoveType.MOVE;
    }

}
