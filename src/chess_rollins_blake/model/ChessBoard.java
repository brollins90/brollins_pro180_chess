package chess_rollins_blake.model;

import java.util.Iterator;

import chess_rollins_blake.exceptions.InvalidMoveException;
import chess_rollins_blake.lib.BoardLocation;
//import chess_rollins_blake.lib.PieceStatus;
import chess_rollins_blake.model.pieces.Piece;

public class ChessBoard {

    final int BOARD_SIZE = 8;
    private Piece boardArray[][];

    public ChessBoard() {
        boardArray = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    public void add(BoardLocation loc, Piece p) {
        if (this.get(loc) == null) {
            set(loc, p);
        } else {
            throw new InvalidMoveException("Failed to add piece, there is already a Piece at " + loc);
        }
    }

    public void capturePiece(BoardLocation loc) {
        // get(loc).setStatus(PieceStatus.CAPTURED);
        if (this.get(loc) != null) {
            remove(loc);
        } else {
            throw new InvalidMoveException("Failed to capture piece, there is no Piece to capture at " + loc);
        }
    }

    public Piece get(BoardLocation loc) {
        return this.boardArray[loc.getRow()][loc.getColumn()];
    }

    private void set(BoardLocation loc, Piece p) {
        // if (this.get(loc) == null) {
        this.boardArray[loc.getRow()][loc.getColumn()] = p;
        // } else {
        // throw new InvalidMoveException("Failed to set piece, there is already a Piece at " + loc);
        // }
    }

    public void remove(BoardLocation loc) {
        this.set(loc, null);
    }

    public void move(BoardLocation src, BoardLocation dest) {
        if (get(src) != null) {
            Piece tempPiece = get(src);
            remove(src);
            set(dest, tempPiece);
        } else {
            throw new InvalidMoveException("Failed to move piece, there is no Piece at " + src);
        }
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int size() {
        return BOARD_SIZE * BOARD_SIZE;
    }
}
