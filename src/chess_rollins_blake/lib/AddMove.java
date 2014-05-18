package chess_rollins_blake.lib;

import chess_rollins_blake.model.ChessFactory;

public class AddMove extends ChessMove {

    public AddMove(String moveString) {
        super(moveString);
        PieceType type = PieceType.valueOf("" + moveString.charAt(0));
        PieceColor color = PieceColor.valueOf("" + moveString.charAt(1));
        this.piece = ChessFactory.CreatePiece(type, color);
        this.srcLoc = BoardLocation.none;
        this.destLoc = BoardLocation.valueOf("" + moveString.substring(2));
        this.type = MoveType.ADD;
        this.message += "Created a new Piece at " + this.destLoc + " color: " + this.piece.getColor() + " type: " + this.piece.getType() + ".\n";
    }

}
