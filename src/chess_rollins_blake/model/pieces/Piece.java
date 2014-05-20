package chess_rollins_blake.model.pieces;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;

public abstract class Piece {

    protected PieceColor color;
    protected PieceType type;
    protected boolean canCollideOnMove;
    protected boolean hasMoved;

    public Piece(PieceColor color) {
        this.color = color;
        this.canCollideOnMove = false;
        this.hasMoved = false;
    }

    public PieceColor getColor() {
        return this.color;
    }

    public PieceType getType() {
        return this.type;
    }

    public String toString() {
        return this.type.name();
    }

    public boolean canCollide() {
        return this.canCollideOnMove;
    }

    public abstract boolean isValidMovement(BoardLocation src, BoardLocation dest, boolean capturing);
}
