package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;

public abstract class Piece {

    protected PieceColor color;
    // protected PieceStatus status;
    protected PieceType type;
    protected boolean canCollideOnMove;
    protected boolean hasMoved;

    public Piece(PieceColor color) {
        this.color = color;
        // this.status = PieceStatus.ALIVE;
        this.canCollideOnMove = false;
        this.hasMoved = false;
    }

    public PieceColor getColor() {
        return this.color;
    }

    // public PieceStatus getStatus() {
    // return this.status;
    // }
    //
    public PieceType getType() {
        return this.type;
    }

    // public void setStatus(PieceStatus status) {
    // this.status = status;
    // }
    //
    public String toString() {
        return this.type.name();
    }

    public boolean canCollide() {
        return this.canCollideOnMove;
    }

    public abstract boolean isValidMovement(BoardLocation src, BoardLocation dest, boolean capturing);
}
