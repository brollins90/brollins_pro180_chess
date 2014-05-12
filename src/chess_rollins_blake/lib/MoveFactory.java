package chess_rollins_blake.lib;

public class MoveFactory {

    public static ChessMove CreateMove(String moveString) {

        ChessMove m = null;
        
        //if (ChessMove.validateSyntax(moveString)) {
            m = new ChessMove(moveString);
            
        //}
        return m;
    }
}
