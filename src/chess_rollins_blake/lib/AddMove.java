package chess_rollins_blake.lib;

import chess_rollins_blake.model.ChessFactory;

public class AddMove extends ChessMove {

    public AddMove(String moveString) {
        super(moveString);
        PieceType type = PieceType.valueOf("" + moveString.charAt(0));
        boolean pieceIsWhite = (moveString.charAt(1) == 'l');
        super.piece = ChessFactory.CreatePiece(type, pieceIsWhite);
        super.srcLoc = BoardLocation.none;
        super.destLoc = BoardLocation.valueOf("" + moveString.substring(2));
        super.type = MoveType.ADD;
        super.message += "Created a new Piece at " + this.destLoc + " color: " + this.piece.isWhite() + " type: " + this.piece.getType() + ".\n";
        super.changeTurnAfter = false;
    }

}
