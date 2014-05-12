package chess_rollins_blake.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.Piece;
import chess_rollins_blake.lib.PieceStatus;

public class PieceList implements Iterable<Map.Entry<BoardLocation, Piece>> {

    Map<BoardLocation, Piece> pieces;
    
    public PieceList() {
        pieces = new TreeMap<BoardLocation, Piece>();
    }
    
    public boolean add(BoardLocation loc, Piece p) {
        this.pieces.put(loc, p);
        return true;
    }
    
    public Piece get(BoardLocation loc) {
        return this.pieces.get(loc);
    }
    
    public boolean capturePiece(BoardLocation loc) {
        pieces.get(loc).setStatus(PieceStatus.CAPTURED);
        return true;
    }
    
    public boolean move(BoardLocation src, BoardLocation dest) {
        Piece temp = pieces.get(src);
        pieces.remove(src);
        pieces.put(dest, temp);
        return true;
    }
    
    public int size() {
        return this.pieces.size();
    }

    @Override
    public Iterator<Entry<BoardLocation, Piece>> iterator() {
        return this.pieces.entrySet().iterator();
    }
}
