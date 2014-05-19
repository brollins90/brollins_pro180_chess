package chess_rollins_blake.model;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.exceptions.InvalidMoveException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.model.pieces.Piece;

public class ChessModel extends java.util.Observable {

    public ChessBoard currentBoard;
    protected Stack<ChessMove> moves;
    protected Stack<ChessMove> movesRedo;
    protected String message;
    protected PieceColor currentTurn;
    protected ArrayList<BoardLocation> availableMoves;

    /**
     * Creates a ChessModel
     */
    public ChessModel() {
        this.currentBoard = new ChessBoard();
        this.moves = new Stack<>();
        this.movesRedo = new Stack<>();
        this.message = "";
        this.currentTurn = PieceColor.l;
        this.availableMoves = new ArrayList<>();
    }

    public void setAvailableMoves(BoardLocation loc) {
        availableMoves.clear();
        Piece p = this.getPiece(loc);

        // TODO iterator stuff
        for (int i = 0; i < this.currentBoard.size(); i++) {
            BoardLocation end = BoardLocation.values()[i];
            String moveString = loc.toString() + " " + end.toString();
            ChessMove currentMovingMove = ChessFactory.CreateMove(MoveType.MOVE, moveString);
            ChessMove currentCapturingMove = ChessFactory.CreateMove(MoveType.CAPTURE, moveString);
            if (validateMove(currentMovingMove) || validateMove(currentCapturingMove)) {
                availableMoves.add(end);
            }
        }
        // availableMoves.add(BoardLocation.a4);
        // availableMoves.add(BoardLocation.b4);
    }

    public ArrayList<BoardLocation> getAvailableMoves() {
        return this.availableMoves;
    }

    /**
     * Adds a move to the ChessGame
     * 
     * @param moveString The String representation of the ChessMove
     * @return If the Move was successfully added
     */
    public void addMove(String moveString) {
        MoveType movesType = this.validateSyntax(moveString);
        if (movesType == null) {
            throw new InvalidMoveException(moveString + " - \nThe move syntax was not valid for '" + moveString + "'");
        }
        ChessMove currentMove = ChessFactory.CreateMove(movesType, moveString);
        if (validateMove(currentMove)) {
            moves.push(currentMove);
            try {
                executeMove(currentMove);
            } catch (ChessException e) {
                moves.pop();
            } finally {
                setMessage(currentMove.message);

            }
        } else {
            throw new InvalidMoveException(moveString + " - \nThe move action was not valid for '" + moveString + "'\n" + currentMove.message);
        }

    }

    /**
     * Adds a piece to the list
     * 
     * @param loc The location on the board
     * @param p The Piece
     * @return If it was successfully added
     */
    public void addPiece(BoardLocation loc, Piece p) {
        this.currentBoard.add(loc, p);
    }

    /**
     * Executes a move
     * 
     * @param currentMove The Move to execute
     * @return If the move was successfully executed
     */
    public void executeMove(ChessMove currentMove) {

        if (currentMove.type == MoveType.ADD) {
            this.currentBoard.add(currentMove.destLoc, currentMove.piece);
        } else if (currentMove.type == MoveType.CAPTURE) {
            this.currentBoard.capturePiece(currentMove.destLoc);
            currentBoard.move(currentMove.srcLoc, currentMove.destLoc);
        } else if (currentMove.type == MoveType.MOVE) {
            this.currentBoard.move(currentMove.srcLoc, currentMove.destLoc);
        }
        if (currentMove.subMove != null) {
            executeMove(currentMove.subMove);
        }
        if (currentMove.type != MoveType.ADD) {
            switchTurn();
        }
    }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Returns the piece at the BoardLocation
     * 
     * @param loc The location on the board
     * @return The piece at the location or null if the location is empty
     */
    public Piece getPiece(BoardLocation loc) {
        return currentBoard.get(loc);
    }

    /**
     * Sends a new status message to the observers
     * 
     * @param newMessage The message to send
     */
    public void setMessage(String newMessage) {
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

        // System.out.println(m.moveString);

        if (isValid && m.type == MoveType.ADD && this.currentBoard.get(m.destLoc) != null) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - The destination already has a piece.\n";
        }

        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.currentBoard.get(m.srcLoc) == null) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - The source is empty.\n";
        }

        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.currentBoard.get(m.srcLoc).getColor() != this.currentTurn) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - Wrong player's turn.\n";
        }

        if (isValid && (m.type == MoveType.MOVE) && this.currentBoard.get(m.destLoc) != null) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - The destination is not empty, cannot Move.\n";
        }

        if (isValid && (m.type == MoveType.CAPTURE) && this.currentBoard.get(m.destLoc) == null) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - The destination is empty, cannot Capture.\n";
        }

        if (isValid && (m.type == MoveType.CAPTURE) && this.currentBoard.get(m.destLoc).getColor() == currentTurn) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - The destination is the same color, cannot Capture.\n";
        }

        if (isValid && (m.type == MoveType.MOVE) && !this.currentBoard.get(m.srcLoc).isValidMovement(m.srcLoc, m.destLoc, false)) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - The move was not valid.\n";
        }

        if (isValid && (m.type == MoveType.CAPTURE) && !this.currentBoard.get(m.srcLoc).isValidMovement(m.srcLoc, m.destLoc, true)) {
            isValid = false;
            errorMessage += "ERROR: " + m.moveString + " - The capture was not valid.\n";
        }

        if (isValid && (m.type == MoveType.MOVE || m.type == MoveType.CAPTURE) && this.currentBoard.get(m.srcLoc).canCollide()) {
            ArrayList<BoardLocation> locsToCheck = getLocationsBetween(m.srcLoc, m.destLoc);
            for (BoardLocation l : locsToCheck) {
                // System.out.println("blake- " + l);
                if (isValid && this.currentBoard.get(l) != null) {
                    isValid = false;
                    errorMessage += "ERROR: " + m.moveString + " - The movement collided at " + l.toString() + ".\n";
                    // System.out.println(errorMessage);
                }
            }
        }

        m.message += errorMessage;
        // System.out.println(errorMessage);
        if (isValid && m.subMove != null) {
            return validateMove(m.subMove);
        }
        // if (!isValid) {
        // throw new InvalidMoveException(errorMessage);
        // }
        // setMessage(m.message);
        return isValid;
    }

    public ArrayList<BoardLocation> getLocationsBetween(BoardLocation src, BoardLocation dest) {
        ArrayList<BoardLocation> locs = new ArrayList<>();

        if (src != dest) {

            int diff = 0;

            if (src.getColumn() == dest.getColumn()) {
                diff = dest.getRow() - src.getRow();
                if (diff > 0) {
                    for (int j = 1; j < diff; j++) {
                        locs.add(BoardLocation.values()[(src.getColumn() * this.currentBoard.getBoardSize()) + (src.getRow() + j)]);
                    }
                } else {
                    for (int j = -1; j > diff; j--) {
                        locs.add(BoardLocation.values()[(src.getColumn() * this.currentBoard.getBoardSize()) + (src.getRow() + j)]);
                    }
                }
            }
            if (src.getRow() == dest.getRow()) {
                diff = dest.getColumn() - src.getColumn();
                if (diff > 0) {
                    for (int j = 1; j < diff; j++) {
                        locs.add(BoardLocation.values()[((src.getColumn() + j) * this.currentBoard.getBoardSize()) + (src.getRow())]);
                    }
                } else {
                    for (int j = -1; j > diff; j--) {
                        locs.add(BoardLocation.values()[((src.getColumn() + j) * this.currentBoard.getBoardSize()) + (src.getRow())]);
                    }
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
        Pattern LOCATION_REGEX = Pattern.compile("[a-h][1-8]");
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

        // Check if it is a valid location request
        m = LOCATION_REGEX.matcher(moveString);
        if (m.matches()) {
            returnType = MoveType.LOCATION;
        }

        // None of them are valid to return null
        return returnType;

    }
}
