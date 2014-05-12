package chess_rollins_blake.model;

import java.util.Stack;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.Piece;
import chess_rollins_blake.lib.PieceColor;

public class ChessModel extends java.util.Observable {

    public PieceList pieces;
    private Stack<ChessMove> moves;
    private Stack<ChessMove> movesRedo;
    public String message;
    public PieceColor currentTurn;

    public ChessModel() {
        // System.out.println("ChessModel()");

        this.pieces = new PieceList();
        this.moves = new Stack<>();
        this.movesRedo = new Stack<>();
        this.message = "";
        this.currentTurn = PieceColor.l;

    }

    private void switchTurn() {
        this.currentTurn = (this.currentTurn == PieceColor.l) ? PieceColor.d : PieceColor.l;
    }

    public boolean addPiece(BoardLocation loc, Piece p) {
        pieces.add(loc, p);
        return true;
    }

    public Piece getPiece(BoardLocation loc) {
        return pieces.get(loc);
    }

    public boolean addMove(ChessMove m) {
        // todo
        this.message += m.message;
        if (validateMove(m)) {
            // System.out.println("Move syntax valid");

            moves.push(m);
            if (m.type == MoveType.ADD) {
                // System.out.println("Adding piece");
                pieces.add(m.destLoc, m.piece);
            } else if (m.type == MoveType.CAPTURE) {
                // System.out.println("captured piece");
                this.pieces.capturePiece(m.destLoc);
                this.pieces.move(m.srcLoc, m.destLoc);
            } else if (m.type == MoveType.MOVE) {
                // System.out.println("moving piece");
                this.pieces.move(m.srcLoc, m.destLoc);
            }
            if (m.subMove != null) {
                addMove(m.subMove);
            }

        } else {

            // System.out.println("Move NOT valid");
        }

        switchTurn();
        return true;
    }

    public boolean undoMove() {
        ChessMove m = moves.pop();
        movesRedo.push(m);

        return true;
    }

    public boolean redoMove() {
        ChessMove m = movesRedo.pop();
        moves.push(m);

        return true;
    }

    private boolean validateMove(ChessMove m) {
        boolean isValid = true;

        if (isValid && m.type == MoveType.ADD && this.pieces.get(m.destLoc) != null) {
            isValid = false;
            message += "ERROR: The destination already has a piece.\n";
        }
        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.pieces.get(m.srcLoc) == null) {
            isValid = false;
            message += "ERROR: The source is empty.\n";
        }
        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.pieces.get(m.srcLoc).getColor() != this.currentTurn) {
            isValid = false;
            message += "ERROR: Wrong player's turn.\n";
        }
        if (isValid && m.type == MoveType.CAPTURE && this.pieces.get(m.destLoc) == null) {
            isValid = false;
            message += "ERROR: The destination is empty.\n";
        }
        if (isValid && m.type == MoveType.MOVE && this.pieces.get(m.destLoc) != null) {
            isValid = false;
            message += "ERROR: The destination is not empty.\n";
        }
        
        if (isValid && m.type == MoveType.MOVE && !this.pieces.get(m.srcLoc).isValidMove(m.srcLoc, m.destLoc, false)) {
            isValid = false;
            message += "ERROR: The move was not valid.\n";
        }
        
        if (isValid && m.type == MoveType.CAPTURE && !this.pieces.get(m.srcLoc).isValidMove(m.srcLoc, m.destLoc, true)) {
            isValid = false;
            message += "ERROR: The move was not valid.\n";
        }
        

        if (isValid && m.subMove != null) {
            return validateMove(m.subMove);
        }
        return isValid;
    }
}
