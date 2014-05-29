package chess_rollins_blake.lib;

import chess_rollins_blake.model.ChessFactory;

public class PromotionMove extends ChessMove {

    public PromotionMove(String moveString) {
        super(moveString);
        PieceType type = PieceType.valueOf("" + moveString.charAt(3));
        boolean pieceIsWhite = (moveString.charAt(4) == 'l');
        super.piece = ChessFactory.CreatePiece(type, pieceIsWhite);
//        super.changeTurnAfter = false;
        super.srcLoc = BoardLocation.valueOf("" + moveString.substring(0, 2));
        super.destLoc = BoardLocation.valueOf("" + moveString.substring(0, 2));
        super.type = MoveType.PROMOTION;
        super.message = this.message.substring(0, this.message.length() - 2) + " and promoted the piece to a " + type + "\n";
        super.message += "Promoted the Pawn at " + this.srcLoc + " to " + this.type;
        
    }
    
}
