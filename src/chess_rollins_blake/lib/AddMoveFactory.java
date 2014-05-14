package chess_rollins_blake.lib;

public class AddMoveFactory implements MoveCreator {

    @Override
    public ChessMove create(String moveString) {
        return new AddMove(moveString);
    }

}
