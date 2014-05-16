package chess_rollins_blake.model;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.Piece;
import chess_rollins_blake.lib.PieceStatus;

public class ChessBoard{
   
    final int BOARD_SIZE = 8;
    
    Piece boardArray[][];
    
    public ChessBoard() {
        //pieces = new TreeMap<BoardLocation, Piece>();
        boardArray = new Piece[BOARD_SIZE][BOARD_SIZE];
    }
    
    public boolean add(BoardLocation loc, Piece p) {
        this.boardArray[loc.getRow()][loc.getColumn()] = p;
        return true;
    }
    
    public boolean capturePiece(BoardLocation loc) {
        get(loc).setStatus(PieceStatus.CAPTURED);
        return true;
    }
    
    public Piece get(BoardLocation loc) {
        return this.boardArray[loc.getRow()][loc.getColumn()];
    }
    
    public boolean remove(BoardLocation loc) {
        this.boardArray[loc.getRow()][loc.getColumn()] = null;
        return true;
    }
    
    public boolean move(BoardLocation src, BoardLocation dest) {
        Piece temp = get(src);
        remove(src);
        add(dest, temp);
        return true;
    }
    
    public int getBoardSize() {
        return BOARD_SIZE;
    }
    
    public int size() {
        return BOARD_SIZE * BOARD_SIZE;
//        return this.pieces.size();
    }
//
//    @Override
//    public Iterator<Entry<BoardLocation, Piece>> iterator() {
//        return this.pieces.entrySet().iterator();
//    }
}
