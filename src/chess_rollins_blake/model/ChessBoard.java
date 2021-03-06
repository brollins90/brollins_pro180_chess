package chess_rollins_blake.model;

import chess_rollins_blake.exceptions.InvalidMoveException;
import chess_rollins_blake.lib.BoardLocation;
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

    public Piece capturePiece(BoardLocation loc) {
        if (this.get(loc) != null) {
            return remove(loc);
        } else {
            throw new InvalidMoveException("Failed to capture piece, there is no Piece to capture at " + loc);
        }
    }

    public Piece get(BoardLocation loc) {
        return this.boardArray[loc.getRow()][loc.getColumn()];
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

    public void promote(BoardLocation loc, Piece piece) {
        if (this.get(loc) != null) {
            remove(loc);
            add(loc, piece);
        } else {
            throw new InvalidMoveException("Failed to promote piece, ... " + loc);
        }
    }

    public Piece remove(BoardLocation loc) {
        Piece retPiece = this.get(loc);
        this.set(loc, null);
        return retPiece;
    }

    private void set(BoardLocation loc, Piece p) {
        this.boardArray[loc.getRow()][loc.getColumn()] = p;
    }

    public int getBoardRowSize() {
        return BOARD_SIZE;
    }

    public int getBoardSize() {
        return BOARD_SIZE * BOARD_SIZE;
    }
}
