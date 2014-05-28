package chess_rollins_blake.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import chess_rollins_blake.ConsoleChess;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.exceptions.InvalidMoveException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.CaptureMove;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.MovingMove;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.pieces.King;
import chess_rollins_blake.model.pieces.Piece;

public class ChessModel extends java.util.Observable {

    protected ChessBoard currentBoard;
    protected Stack<ChessMove> movesExecuted;
    protected Stack<ChessMove> movesRedo;
    protected String statusMessage;
    protected boolean currentlyWhitesTurn;

    protected BoardLocation currentModelState = BoardLocation.none;
    protected HashSet<ChessMove> availableMoves;
    protected HashSet<BoardLocation> availableSources;
    protected HashSet<BoardLocation> thisPlayersPieces;

    protected boolean whiteKingInCheck;
    protected boolean blackKingInCheck;

    // protected int whitePiecesTaken;
    // protected int blackPiecesTaken;



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
        // this.whitePiecesTaken = 0;
        // this.blackPiecesTaken = 0;

        availableMoves = new HashSet<ChessMove>();
    }

    public BoardLocation getCurrentModelState() {
        return this.currentModelState;
    }

    public void setCurrentModelState(BoardLocation loc) {
        this.currentModelState = loc;
        resetView();

        // TODO

        // reset everything
    }

    public void resetView() {
        System.out.println("Reset view");
        setCurrentModelState(BoardLocation.none);
        this.thisPlayersPieces = getPlayersPieces(currentlyWhitesTurn);
        
        this.availableMoves = null;
        this.availableSources = getAvailableSources();
    }
    
    private void getAllMovesForPlayer() {
        for (BoardLocation sourceLocation : this.thisPlayersPieces) {
            
        }
    }

    private void switchTurn() {
        System.out.println("SWITCHING TURNS: new turn is " + !this.currentlyWhitesTurn);
        this.currentlyWhitesTurn = !currentlyWhitesTurn;
        resetView();
    }

    private HashSet<ChessMove> findMovesFromLocation(BoardLocation loc) {

        HashSet<BoardLocation> sources = getAvailableSources();

        for (BoardLocation pieceThatCanMove : sources) {
            HashSet<BoardLocation> destinationsForThisPiece = this.getAvailableDestinationsFromLocation(pieceThatCanMove);

            for (BoardLocation destination : destinationsForThisPiece) {

                String moveString = pieceThatCanMove + " " + destination;
                if (this.locationHasPiece(destination)) {
                    moveString += "*";
                }
                ChessMove testMove = ChessFactory.CreateMove(moveString);
                try {
                    testMove.setChangeTurnAfter(false);
                    this.addMoveWithoutUpdate(testMove);
                    //
                    // this.isThisKingInCheck(this.isWhiteTurn());
                    // if (wasKingInCheck && !this.isOtherInCheck()) {
                    // movesThatCanGetOutOfCheck.add(testMove);
                    // }
                    moves.add(testMove);
                    this.model.undoMove();
                } catch (ChessException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }

            }

        }



    }

    public void setAvailableMoves(HashSet<ChessMove> moves) {
        ConsoleChess.debugMessage("ChessModel.setAvailableMoves()");
        this.availableMoves = moves;

        this.availableSources = new HashSet<BoardLocation>();
        for (ChessMove m : moves) {
            this.availableSources.add(m.getSrcLoc());
        }
    }

    public HashSet<ChessMove> getAvailableMoves() {
        return this.availableMoves;
    }

    public HashSet<BoardLocation> getAvailableSources() {
        if (this.availableSources == null) {
            this.availableSources = getLocationsThatCanMove();
        }
        return this.availableSources;
    }

    public void setAvailableSources() {

        this.availableSources = new HashSet<BoardLocation>();
        for (ChessMove m : this.availableMoves) {
            this.availableSources.add(m.getSrcLoc());
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
        ConsoleChess.debugMessage("ChessModel.addPiece()");
        this.currentBoard.add(loc, p);
    }

    /**
     * Returns the locations of the specified player's king
     * 
     * @param player If the player is White
     * @return The Location of the input player's king
     */
    private BoardLocation getKingLoc(boolean player) {
        ConsoleChess.debugMessage("ChessModel.getKingLoc()");
        BoardLocation retLoc = null;

        for (int i = 0; i < this.currentBoard.getBoardSize(); i++) {
            BoardLocation cur = BoardLocation.values()[i];
            Piece curPiece = this.currentBoard.get(cur);
            if (curPiece != null && curPiece.isWhite() == player && curPiece.getType() == PieceType.k) {
                retLoc = cur;
            }
        }
        return retLoc;
    }

    /**
     * Returns a Collection of all the Locations that contain Pieces for the specified player
     * 
     * @param player If the player is White
     * @return a Collection of all the Locations that contain Pieces for the specified player
     */
    public HashSet<BoardLocation> getPlayersPieces(boolean player) {
        ConsoleChess.debugMessage("ChessModel.getPlayersPieces()");

        HashSet<BoardLocation> pieces = new HashSet<BoardLocation>();

        for (int i = 0; i < currentBoard.getBoardSize(); i++) {
            BoardLocation currentLocation = BoardLocation.values()[i];
            Piece currentPiece = currentBoard.get(currentLocation);
            if (currentPiece != null && currentPiece.isWhite() == player) {
                pieces.add(currentLocation);
            }
        }
        return pieces;
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

    /**
     * Returns true if the input player's king is in check
     * 
     * @param player The player that we want to to see if is in check
     * @return if the input player's king is in check
     */
    public boolean isThisKingInCheck(boolean player) {
        ConsoleChess.debugMessage("ChessModel.isThisKingInCheck()");

        boolean kingIsInCheck = false;
        BoardLocation kingLoc = getKingLoc(player);
        HashSet<BoardLocation> otherColorPieces = getPlayersPieces(!player);

        if (kingLoc != null) {
            for (BoardLocation l : otherColorPieces) {
                String moveString = l.toString() + " " + kingLoc.toString() + "*";
                if (isMoveValid(ChessFactory.CreateMove(MoveType.CAPTURE, moveString), false)) {
                    kingIsInCheck = true;
                }
            }
        }
        return kingIsInCheck;
    }

    /**
     * Returns if the current turn is White
     * 
     * @return if the current turn is White
     */
    public boolean isWhiteTurn() {
        return this.currentlyWhitesTurn;
    }

    public int getBoardRowSize() {
        return this.currentBoard.getBoardRowSize();
    }

    public int getBoardSize() {
        return this.currentBoard.getBoardSize();
    }



    public HashSet<BoardLocation> getLocationsThatCanMove() {
        ConsoleChess.debugMessage("ChessModel.getLocationsThatCanMove()");

        HashSet<BoardLocation> locsThatCanMove = new HashSet<BoardLocation>();
        // BoardLocation otherKingLocation = getKingLoc(!whiteTurn);

        // get the pieces that can move
        for (int i = 0; i < this.currentBoard.getBoardSize(); i++) {
            BoardLocation loc = BoardLocation.values()[i];
            Piece curPiece = this.currentBoard.get(loc);
            // If the Piece is not null AND the piece is the same color as the current turn
            if (curPiece != null && curPiece.isWhite() == currentlyWhitesTurn) {

                HashSet<BoardLocation> dests = getAvailableDestinationsFromLocation(loc);

                if (dests.size() > 0) {
                    locsThatCanMove.add(loc);
                }
            }
        }
        return locsThatCanMove;
    }


    public HashSet<BoardLocation> getAvailableDestinationsFromLocation(BoardLocation loc) {
        ConsoleChess.debugMessage("ChessModel.getAvailableDestinationsFromLocation()");
        HashSet<BoardLocation> dests = new HashSet<BoardLocation>();

        if (loc != BoardLocation.none) {
            Piece p = this.getPiece(loc);

            for (ChessMove m : this.availableMoves) {
                if (loc == m.getSrcLoc()) {
                    dests.add(m.getDestLoc());
                }
            }

            // // TODO iterator stuff
            // for (int i = 0; i < this.currentBoard.getBoardSize(); i++) {
            // BoardLocation end = BoardLocation.values()[i];
            // String moveString = loc.toString() + " " + end.toString();
            // ChessMove currentMovingMove = ChessFactory.CreateMove(MoveType.MOVE, moveString);
            // ChessMove currentCapturingMove = ChessFactory.CreateMove(MoveType.CAPTURE, moveString);
            // if (isMoveValid(currentMovingMove, false) || isMoveValid(currentCapturingMove, false)) {
            // dests.add(end);
            // }

        }
        return dests;
    }



    public void addMoveWithoutUpdate(ChessMove m) {
        addMove(m, false);
    }

    public void addMoveWithoutUpdate(String m) {
        addMove(m, false);
    }

    public void addMove(ChessMove m, boolean updateObservers) {
        if (isMoveValid(m, updateObservers)) {
            movesExecuted.push(m);
            try {
                executeMove(m);
            } catch (ChessException e) {
                movesExecuted.pop();
            } finally {
                if (updateObservers) {
                    setModelStatusMessage(m.getMessage());
                }

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
    public void addMove(String moveString, boolean updateObservers) {
        MoveType movesType = ChessFactory.ValidateMoveString(moveString);
        if (movesType == null) {
            throw new InvalidMoveException(moveString + " - \nThe move syntax was not valid for '" + moveString + "'");
        }
        ChessMove currentMove = ChessFactory.CreateMove(movesType, moveString);
        try {
            addMove(currentMove, updateObservers);
        } catch (InvalidMoveException e) {
            throw new InvalidMoveException(moveString + " - \nThe move action was not valid for '" + moveString + "'\n" + e.getMessage());
        }
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
            // switchTurn();
        }


        // Check Check
        this.whiteKingInCheck = isThisKingInCheck(true);
        this.blackKingInCheck = isThisKingInCheck(false);

        if (currentMove.shouldChangeTurn()) {
            switchTurn();
        }
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
    public void setModelStatusMessage(String newMessage) {
        // this.message = newMessage;
        setChanged();
        notifyObservers(newMessage);
    }



    public String getMessage() {
        return this.statusMessage;
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
        undo.setChangeTurnAfter(false);
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

    private boolean isMoveValid(ChessMove m, boolean turnCheck) {
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

        // // Check for a submove, normally used when castling
        // if (isValid && (m.getType() == MoveType.MOVE) && m.getSubMove() != null) {
        //
        // // If it is a king
        // if (this.currentBoard.get(m.getSrcLoc()).getType() == PieceType.k) {
        // King currentPiece = (King) this.currentBoard.get(m.getSrcLoc());
        //
        // // could be a valid castling move, YAY
        // if (currentPiece.isValidCastlingMove(m.getSrcLoc(), m.getDestLoc())) {
        //
        // } else {
        // isValid = false;
        // errorMessage += "ERROR: " + m.getMoveString() + " - the move was not a valid castling move, or my test logic is wrong....\n";
        // }
        // } else {
        // isValid = false;
        // errorMessage += "ERROR: " + m.getMoveString() + " - I am not sure how I would get here.\n";
        //
        // }
        // }

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
            return isMoveValid(m.getSubMove(), true);
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
                        locs.add(BoardLocation.values()[(src.getColumn() * this.currentBoard.getBoardRowSize()) + (src.getRow() + j)]);
                    }
                } else {
                    for (int j = -1; j > diff; j--) {
                        locs.add(BoardLocation.values()[(src.getColumn() * this.currentBoard.getBoardRowSize()) + (src.getRow() + j)]);
                    }
                }
            }
            // Horizontal
            if (src.getRow() == dest.getRow()) {
                diff = dest.getColumn() - src.getColumn();
                if (diff > 0) {
                    for (int j = 1; j < diff; j++) {
                        locs.add(BoardLocation.values()[((src.getColumn() + j) * this.currentBoard.getBoardRowSize()) + (src.getRow())]);
                    }
                } else {
                    for (int j = -1; j > diff; j--) {
                        locs.add(BoardLocation.values()[((src.getColumn() + j) * this.currentBoard.getBoardRowSize()) + (src.getRow())]);
                    }
                }
            }
            // Diag
            if ((Math.abs(dest.getColumn() - src.getColumn())) == Math.abs(dest.getRow() - src.getRow())) {
                // TODO
                // need to add the diag collisions
                int testRow = src.getRow();
                int testCol = src.getColumn();

                boolean reachedDestination = true;

                while (reachedDestination) {
                    if (dest.getColumn() > src.getColumn()) { // dest is right of src
                        testCol++;
                    } else { // dest is left than src
                        testCol--;
                    }
                    if (dest.getRow() > src.getRow()) { // dest is above src
                        testRow++;
                    } else { // dest is below src
                        testRow--;
                    }
                    BoardLocation tempLoc = BoardLocation.getLocFromRowAndColumn(testRow, testCol);
                    if (tempLoc != dest) {
                        locs.add(tempLoc);
                    } else {
                        reachedDestination = false;
                    }
                }
            }
        }
        return locs;
    }
}
