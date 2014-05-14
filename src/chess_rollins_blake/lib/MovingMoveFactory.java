package chess_rollins_blake.lib;

public class MovingMoveFactory implements MoveCreator {

    @Override
    public ChessMove create(String moveString) {
        return new MovingMove(moveString);
    }

}
