package chess_rollins_blake.model;

import java.util.ArrayList;
import java.util.Stack;

import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.exceptions.InvalidMoveException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.CaptureMove;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.MovingMove;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.pieces.Piece;

public class ChessModel extends java.util.Observable {

    public ChessBoard currentBoard;
    protected Stack<ChessMove> movesExecuted;
    protected Stack<ChessMove> movesRedo;
    protected String statusMessage;
    protected boolean currentlyWhitesTurn;
    protected boolean whiteKingInCheck;
    protected boolean blackKingInCheck;


    protected ArrayList<ChessMove> availableMoves;

    /**
     * Creates a ChessModel
     */
    public ChessModel() {
        this.currentBoard = new ChessBoard();
        this.movesExecuted = new Stack<>();
        this.movesRedo = new Stack<>();
        this.statusMessage = "";
        this.currentlyWhitesTurn = true;
        this.whiteKingInCheck = false;
        this.blackKingInCheck = false;

        availableMoves = new ArrayList<ChessMove>();
    }

    public void setAvailableMoves(ArrayList<ChessMove> moves) {
        this.availableMoves = moves;
    }

    public ArrayList<ChessMove> getAvailableMoves() {
        return this.availableMoves;
    }

    /**
     * Returns the locations of the specified player's king
     * 
     * @param player If the player is White
     * @return The Location of the input player's king
     */
    private BoardLocation getKingLoc(boolean player) {
        BoardLocation retLoc = null;

        for (int i = 0; i < this.currentBoard.size(); i++) {
            BoardLocation cur = BoardLocation.values()[i];
            Piece curPiece = this.currentBoard.get(cur);
            if (curPiece != null && curPiece.isWhite() == player && curPiece.getType() == PieceType.k) {
                retLoc = cur;
            }
        }
        return retLoc;
    }

    /**
     * Returns if the White King is in check
     * 
     * @return if the White King is in check
     */
    public boolean isWhiteKingInCheck() {
        return this.whiteKingInCheck;
    }

    /**
     * Returns if the Black King is in check
     * 
     * @return if the Black King is in check
     */
    public boolean isBlackKingInCheck() {
        return this.blackKingInCheck;
    }

    /**
     * Returns if the current player King is in check
     * 
     * @return if the current player King is in check
     */
    public boolean isCurrentInCheck() {
        return (this.currentlyWhitesTurn) ? whiteKingInCheck : blackKingInCheck;
    }

    /**
     * Returns if the not current player King is in check
     * 
     * @return if the not current player King is in check
     */
    public boolean isOtherInCheck() {
        return (this.currentlyWhitesTurn) ? blackKingInCheck : whiteKingInCheck;
    }



    public ArrayList<BoardLocation> getLocationsThatCanMove() {
        ArrayList<BoardLocation> locsThatCanMove = new ArrayList<BoardLocation>();
        // BoardLocation otherKingLocation = getKingLoc(!whiteTurn);

        // get the pieces that can move
        for (int i = 0; i < this.currentBoard.size(); i++) {
            BoardLocation loc = BoardLocation.values()[i];
            Piece curPiece = this.currentBoard.get(loc);
            if (curPiece != null && curPiece.isWhite() == currentlyWhitesTurn) {

                ArrayList<BoardLocation> dests = getAvailableDestinationsFromLocation(loc);

                if (dests.size() > 0) {
                    locsThatCanMove.add(loc);
                }
            }
        }
        return locsThatCanMove;
    }


    public ArrayList<BoardLocation> getAvailableDestinationsFromLocation(BoardLocation loc) {
        ArrayList<BoardLocation> dests = new ArrayList<BoardLocation>();
        Piece p = this.getPiece(loc);

        // TODO iterator stuff
        for (int i = 0; i < this.currentBoard.size(); i++) {
            BoardLocation end = BoardLocation.values()[i];
            String moveString = loc.toString() + " " + end.toString();
            ChessMove currentMovingMove = ChessFactory.CreateMove(MoveType.MOVE, moveString);
            ChessMove currentCapturingMove = ChessFactory.CreateMove(MoveType.CAPTURE, moveString);
            if (validateMove(currentMovingMove, false) || validateMove(currentCapturingMove, false)) {
                dests.add(end);
            }
        }
        return dests;
    }



    public void addMove(ChessMove m) {
        if (validateMove(m, true)) {
            movesExecuted.push(m);
            try {
                executeMove(m);
            } catch (ChessException e) {
                movesExecuted.pop();
            } finally {
                setMessage(m.getMessage());

            }
        } else {
            throw new InvalidMoveException(m.getMessage());
        }
    }

    /**
     * Adds a move to the ChessGame
     * 
     * @param moveString The String representation of the ChessMove
     * @return If the Move was successfully added
     */
    public void addMove(String moveString) {
        MoveType movesType = ChessFactory.ValidateMoveString(moveString);
        if (movesType == null) {
            throw new InvalidMoveException(moveString + " - \nThe move syntax was not valid for '" + moveString + "'");
        }
        ChessMove currentMove = ChessFactory.CreateMove(movesType, moveString);
        try {
            addMove(currentMove);
        } catch (InvalidMoveException e) {
            throw new InvalidMoveException(moveString + " - \nThe move action was not valid for '" + moveString + "'\n" + e.getMessage());
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

        if (currentMove.getType() == MoveType.ADD) {
            this.currentBoard.add(currentMove.getDestLoc(), currentMove.getPiece());
        } else if (currentMove.getType() == MoveType.CAPTURE) {
            CaptureMove currentAsCapture = (CaptureMove) currentMove;
            Piece capturedPiece = this.currentBoard.capturePiece(currentAsCapture.getDestLoc());
            currentAsCapture.setPieceCaptured(capturedPiece);
            currentBoard.move(currentMove.getSrcLoc(), currentMove.getDestLoc());
        } else if (currentMove.getType() == MoveType.MOVE) {
            this.currentBoard.move(currentMove.getSrcLoc(), currentMove.getDestLoc());
        }
        if (currentMove.getSubMove() != null) {
            executeMove(currentMove.getSubMove());
        }


        // Check Check
        this.whiteKingInCheck = isThisKingInCheck(true);
        this.blackKingInCheck = isThisKingInCheck(false);
        // if (checkKingInCheck(currentTurn)) {
        // System.out.println(currentTurn + " king is in check!");
        // } else {
        //
        // System.out.println(currentTurn + " king is not in check!");
        // }

        if (currentMove.getType() != MoveType.ADD) {
            switchTurn();
        }


    }

    public ArrayList<BoardLocation> getPlayersPieces(boolean player) {

        ArrayList<BoardLocation> pieces = new ArrayList<BoardLocation>();

        for (int i = 0; i < currentBoard.size(); i++) {
            BoardLocation currentLocation = BoardLocation.values()[i];
            Piece currentPiece = currentBoard.get(currentLocation);
            if (currentPiece != null && currentPiece.isWhite() == player) {
                pieces.add(currentLocation);
            }
        }

        return pieces;

    }

    public boolean isThisKingInCheck(boolean player) {

        boolean kingIsInCheck = false;
        BoardLocation kingLoc = getKingLoc(player);
        ArrayList<BoardLocation> otherColorPieces = getPlayersPieces(!player);

        if (kingLoc != null) {
            for (BoardLocation l : otherColorPieces) {
                // System.out.println("Piece at: " + l);
                String moveString = l.toString() + " " + kingLoc.toString() + "*";
                if (validateMove(ChessFactory.CreateMove(MoveType.CAPTURE, moveString), false)) {
                    // System.out.println("has checked the king");
                    kingIsInCheck = true;
                } else {
                    // System.out.println("no check");
                }
            }
        }

        return kingIsInCheck;
        // if (player) {
        // whiteKingInCheck = kingIsInCheck;
        // } else {
        // blackKingInCheck = kingIsInCheck;
        // }

    }

    public boolean isWhiteTurn() {
        return this.currentlyWhitesTurn;
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
        return this.statusMessage;
    }

    private void switchTurn() {
        this.currentlyWhitesTurn = !currentlyWhitesTurn;
    }

    public boolean undoMove() {
        ChessMove m = movesExecuted.pop();

        ChessMove undo = null;
        if (m instanceof CaptureMove) {
            undo = ChessFactory.CreateMove(m.getDestLoc() + " " + m.getSrcLoc());
            CaptureMove c = (CaptureMove) m;
            Piece capturedPiece = c.getCapturedPiece();
            String readdString = "";
            readdString += capturedPiece.getType();
            readdString += (capturedPiece.isWhite()) ? "l" : "d";
            readdString += m.getDestLoc();
            undo.setSubmove(ChessFactory.CreateMove(readdString));
        } else if (m instanceof MovingMove) {
            undo = ChessFactory.CreateMove(m.getDestLoc() + " " + m.getSrcLoc());
        }

        executeMove(undo);



        movesRedo.push(m);

        return true;
    }

    public boolean redoMove() {
        ChessMove m = movesRedo.pop();
        movesExecuted.push(m);

        return true;
    }

    public boolean locationHasPiece(BoardLocation loc) {
        return this.currentBoard.get(loc) != null;
    }

    private boolean validateMove(ChessMove m, boolean turnCheck) {
        boolean isValid = true;
        String errorMessage = "";

        // System.out.println(m.moveString);

        if (isValid && m.getType() == MoveType.ADD && locationHasPiece(m.getDestLoc())) {
            isValid = false;
            errorMessage += "ERROR: " + m.getMoveString() + " - The destination already has a piece.\n";
        }

        if (isValid && (m.getType() == MoveType.MOVE || m.getType() == MoveType.CAPTURE) && !locationHasPiece(m.getSrcLoc())) {
            isValid = false;
            errorMessage += "ERROR: " + m.getMoveString() + " - The source is empty.\n";
        }

        if (turnCheck) {
            if (isValid && (m.getType() == MoveType.MOVE || m.getType() == MoveType.CAPTURE) && this.currentBoard.get(m.getSrcLoc()).isWhite() != this.isWhiteTurn()) {
                isValid = false;
                errorMessage += "ERROR: " + m.getMoveString() + " - Wrong player's turn.\n";
            }
        }

        if (isValid && (m.getType() == MoveType.MOVE) && locationHasPiece(m.getDestLoc())) {
            isValid = false;
            errorMessage += "ERROR: " + m.getMoveString() + " - The destination is not empty, cannot Move.\n";
        }

        if (isValid && (m.getType() == MoveType.CAPTURE) && !locationHasPiece(m.getDestLoc())) {
            isValid = false;
            errorMessage += "ERROR: " + m.getMoveString() + " - The destination is empty, cannot Capture.\n";
        }

        if (isValid && (m.getType() == MoveType.CAPTURE) && this.currentBoard.get(m.getSrcLoc()).isWhite() == this.currentBoard.get(m.getDestLoc()).isWhite()) {
            isValid = false;
            errorMessage += "ERROR: " + m.getMoveString() + " - The destination is the same color, cannot Capture.\n";
        }

        if (isValid && (m.getType() == MoveType.MOVE) && !this.currentBoard.get(m.getSrcLoc()).isValidMovement(m.getSrcLoc(), m.getDestLoc(), false)) {
            isValid = false;
            errorMessage += "ERROR: " + m.getMoveString() + " - The move was not valid.\n";
        }

        if (isValid && (m.getType() == MoveType.CAPTURE) && !this.currentBoard.get(m.getSrcLoc()).isValidMovement(m.getSrcLoc(), m.getDestLoc(), true)) {
            isValid = false;
            errorMessage += "ERROR: " + m.getMoveString() + " - The capture was not valid.\n";
        }

        if (isValid && (m.getType() == MoveType.MOVE || m.getType() == MoveType.CAPTURE) && this.currentBoard.get(m.getSrcLoc()).canCollide()) {
            ArrayList<BoardLocation> locsToCheck = getLocationsBetween(m.getSrcLoc(), m.getDestLoc());
            for (BoardLocation l : locsToCheck) {
                // System.out.println("blake- " + l);
                if (isValid && this.currentBoard.get(l) != null) {
                    isValid = false;
                    errorMessage += "ERROR: " + m.getMoveString() + " - The movement collided at " + l.toString() + ".\n";
                    // System.out.println(errorMessage);
                }
            }
        }

        m.setMessage(m.getMessage() + errorMessage);
        // System.out.println(errorMessage);
        if (isValid && m.getSubMove() != null) {
            return validateMove(m.getSubMove(), true);
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

            // Vert
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
            // Horizontal
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
            // Diag
            if ((Math.abs(dest.getColumn() - src.getColumn())) == Math.abs(dest.getRow() - src.getRow())) {
                // TODO
                // need to add the diag collisions
                // int testRow = src.getRow();
                // int testCol = src.getColumn();
                //
                // boolean blake = true;
                //
                // while (blake) {
                // if (dest.getColumn() > src.getColumn()) {
                // // dest is right of src
                // testCol++;
                // } else {
                // // dest is left than src
                // testCol--;
                // }
                // if (dest.getRow() > src.getRow()) {
                // // dest is above src
                // testRow++;
                //
                // } else {
                // // dest is below src
                // testRow--;
                // }
                // BoardLocation tempLoc = rowColumnToLoc(testRow, testCol);
                // if (tempLoc != dest) {
                // locs.add(tempLoc);
                // } else {
                // blake = false;
                // }
                //
                // }



            }
        }
        return locs;

    }

    BoardLocation rowColumnToLoc(int row, int col) {
        int retVal = 0;
        retVal += col * 8;
        retVal += row;
        return BoardLocation.values()[retVal];
    }
}
