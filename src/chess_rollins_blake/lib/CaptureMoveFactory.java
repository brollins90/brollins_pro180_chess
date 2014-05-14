package chess_rollins_blake.lib;

public class CaptureMoveFactory implements MoveCreator {

    @Override
    public ChessMove create(String moveString) {
        return new CaptureMove(moveString);
    }

}
