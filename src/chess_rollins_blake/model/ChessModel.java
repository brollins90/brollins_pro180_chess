package chess_rollins_blake.model;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessFactory;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.Piece;
import chess_rollins_blake.lib.PieceColor;

public class ChessModel extends java.util.Observable {

    public ChessBoard pieces;
    private Stack<ChessMove> moves;
    private Stack<ChessMove> movesRedo;
    private String message;
    public PieceColor currentTurn;

    /**
     * Creates a ChessModel
     */
    public ChessModel() {
        this.pieces = new ChessBoard();
        this.moves = new Stack<>();
        this.movesRedo = new Stack<>();
        this.message = "";
        this.currentTurn = PieceColor.l;
    }

    /**
     * Adds a move to the ChessGame
     * 
     * @param moveString The String representation of the ChessMove
     * @return If the Move was successfully added
     */
    public boolean addMove(String moveString) {
        // System.out.println("Adding: " + moveString);
        boolean moveAdded = false;
        MoveType movesType = this.validateSyntax(moveString);
        // System.out.println("type: " + movesType);
        if (movesType != null) {
            ChessMove currentMove = ChessFactory.CreateMove(movesType, moveString);
            // System.out.println("created");
            if (validateMove(currentMove)) {
                moves.push(currentMove);
                boolean executed = executeMove(currentMove);
                setMessage(currentMove.message);
                if (!executed) {
                    moves.pop();
                    moveAdded = !executed;
                } else {         // moveAdded = executed
                    moveAdded = executed;
                }
            } else {
                setMessage(currentMove.message);
            }
        }
        return moveAdded;
    }

    /**
     * Adds a piece to the list
     * 
     * @param loc The location on the board
     * @param p The Piece
     * @return If it was successfully added
     */
    public boolean addPiece(BoardLocation loc, Piece p) {
        return pieces.add(loc, p);
    }

    /**
     * Executes a move
     * 
     * @param currentMove The Move to execute
     * @return If the move was successfully executed
     */
    public boolean executeMove(ChessMove currentMove) {

        // System.out.println("executing: " + currentMove.moveString);
        boolean moveExecuted = false;

        if (currentMove.type == MoveType.ADD) {
            // System.out.println("Adding piece");
            moveExecuted = pieces.add(currentMove.destLoc, currentMove.piece);
        } else if (currentMove.type == MoveType.CAPTURE) {
            // System.out.println("captured piece");
            this.pieces.capturePiece(currentMove.destLoc);
            moveExecuted = this.pieces.move(currentMove.srcLoc, currentMove.destLoc);
        } else if (currentMove.type == MoveType.MOVE) {
            // System.out.println("moving piece");
            moveExecuted = this.pieces.move(currentMove.srcLoc, currentMove.destLoc);
        }
        if (moveExecuted && currentMove.subMove != null) {
            moveExecuted = executeMove(currentMove.subMove);
        }
        if (moveExecuted) {
            switchTurn();
        }
        return moveExecuted;
    }

    /**
     * Returns the piece at the BoardLocation
     * 
     * @param loc The location on the board
     * @return The piece at the location or null if the location is empty
     */
    public Piece getPiece(BoardLocation loc) {
        return pieces.get(loc);
    }

    /**
     * Sends a new status message to the observers
     * 
     * @param newMessage The message to send
     */
    private void setMessage(String newMessage) {
        // this.message = newMessage;
        setChanged();
        notifyObservers(newMessage);
    }



    public String getMessage() {
        return this.message;
    }

    private void switchTurn() {
        this.currentTurn = (this.currentTurn == PieceColor.l) ? PieceColor.d : PieceColor.l;
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
        String errorMessage = "";


        if (isValid && m.type == MoveType.ADD && this.pieces.get(m.destLoc) != null) {
            isValid = false;
            errorMessage += "ERROR: The destination already has a piece.\n";
        }
        
        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.pieces.get(m.srcLoc) == null) {
            isValid = false;
            errorMessage += "ERROR: The source is empty.\n";
        }
        
        // if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.pieces.get(m.srcLoc).getColor() != this.currentTurn) {
        // isValid = false;
        // errorMessage += "ERROR: Wrong player's turn.\n";
        // }

        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.pieces.get(m.destLoc) != null) {
            isValid = false;
            errorMessage += "ERROR: The destination is not empty.\n";
        }

        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && !this.pieces.get(m.srcLoc).isValidMovement(m.srcLoc, m.destLoc/* , false */)) {
            isValid = false;
            errorMessage += "ERROR: The move was not valid.\n";
        }

        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.pieces.get(m.srcLoc).canCollide()) {
            ArrayList<BoardLocation> locsToCheck = getLocationsBetween(m.srcLoc, m.destLoc);
            for (BoardLocation l : locsToCheck) {
                System.out.println(l);
                if (isValid && this.pieces.get(l) != null) {
                    isValid = false;
                    errorMessage += "ERROR: The movement collided.\n";
                }
            }
        }

        m.message += errorMessage;
        // System.out.println(errorMessage);
        if (isValid && m.subMove != null) {
            return validateMove(m.subMove);
        }
        return isValid;
    }

    public ArrayList<BoardLocation> getLocationsBetween(BoardLocation src, BoardLocation dest) {
        ArrayList<BoardLocation> locs = new ArrayList<>();
        
        int diff = 0;
        
        if (src.getColumn() == dest.getColumn()) {
            diff = dest.getRow() - src.getRow();  
            if (diff > 0) {
                for (int j = 1; j < diff; j++) {
                    locs.add(BoardLocation.values()[(src.getColumn() * this.pieces.getBoardSize()) + (src.getRow() + j)]);
                }
            } else {
                for (int j = -1; j > diff; j--) {
                    locs.add(BoardLocation.values()[(src.getColumn() * this.pieces.getBoardSize()) + (src.getRow() + j)]);
                }
            }                   
        }
        if (src.getRow() == dest.getRow()) {
            diff = dest.getColumn() - src.getColumn();
            if (diff > 0) {
                for (int j = 1; j < diff; j++) {
                    locs.add(BoardLocation.values()[((src.getColumn() + j) * this.pieces.getBoardSize()) + (src.getRow())]);
                }
            } else {
                for (int j = -1; j > diff; j--) {
                    locs.add(BoardLocation.values()[((src.getColumn() + j) * this.pieces.getBoardSize()) + (src.getRow())]);
                }
            }
        }
        return locs;
        
    }

    public MoveType validateSyntax(String moveString) {

        MoveType returnType = null;
        // Force lower case
        moveString = moveString.toLowerCase();

        Pattern ADD_REGEX = Pattern.compile("[kqrbnp][ld][a-h][1-8]");
        Pattern MOVE_REGEX = Pattern.compile("[a-h][1-8] [a-h][1-8]\\*?");
        Pattern MOVE2_REGEX = Pattern.compile("[a-h][1-8] [a-h][1-8] [a-h][1-8] [a-h][1-8]");
        Matcher m;

        // Check the Add regex
        m = ADD_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = MoveType.ADD;
        }

        // Check the move regex
        m = MOVE_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = (moveString.contains("*")) ? MoveType.CAPTURE : MoveType.MOVE;
        }

        // Check the double move regex
        m = MOVE2_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = MoveType.MOVE;
        }

        // None of them are valid to return null
        return returnType;

    }
}
