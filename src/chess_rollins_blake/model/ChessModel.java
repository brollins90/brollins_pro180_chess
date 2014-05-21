package chess_rollins_blake.model;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    protected ArrayList<ChessMove> availableMoves;
    protected Stack<ChessMove> moves;
    protected Stack<ChessMove> movesRedo;
    protected String message;
    protected boolean whiteTurn;
//    protected ArrayList<BoardLocation> availableSources;
//    protected ArrayList<BoardLocation> availableDestinations;
    protected boolean whiteKingInCheck, blackKingInCheck;

    /**
     * Creates a ChessModel
     */
    public ChessModel() {
        this.currentBoard = new ChessBoard();
        this.moves = new Stack<>();
        this.movesRedo = new Stack<>();
        this.message = "";
        this.whiteTurn = true;
//        this.availableSources = new ArrayList<>();
//        this.availableDestinations = new ArrayList<>();
        whiteKingInCheck = false;
        blackKingInCheck = false;
        availableMoves = new ArrayList<ChessMove>();
    }
    
    public void setAvailableMoves(ArrayList<ChessMove> moves) {
        this.availableMoves = moves;
    }
    
    public ArrayList<ChessMove> getAvailableMoves() {
        return this.availableMoves;
    }

    public boolean isWhiteKingInCheck() {
        return this.whiteKingInCheck;
    }

    public boolean isBlackKingInCheck() {
        return this.blackKingInCheck;
    }

    public boolean isCurrentInCheck() {
        return (this.whiteTurn) ? whiteKingInCheck : blackKingInCheck;
    }

    public boolean isOtherInCheck() {
        return (this.whiteTurn) ? blackKingInCheck : whiteKingInCheck;
    }
//    public ArrayList<BoardLocation> getAvailableSources() {
//        return this.availableSources;
//    }
    
    
    public void doturn() {

        // check if current is in check
        if (isCurrentInCheck()) {
            
            // Get out of check
            
            
        }

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
    
    public ArrayList<BoardLocation> getLocationsThatCanMove() {
        ArrayList<BoardLocation> locsThatCanMove = new ArrayList<BoardLocation>();
        //BoardLocation otherKingLocation = getKingLoc(!whiteTurn);
        
        // get the pieces that can move
        for (int i = 0; i < this.currentBoard.size(); i++) {
            BoardLocation loc = BoardLocation.values()[i];
            Piece curPiece = this.currentBoard.get(loc);
            if (curPiece != null && curPiece.isWhite() == whiteTurn) {
                
                ArrayList<BoardLocation> dests = getAvailableDestinationsFromLocation(loc);
                
                if (dests.size() > 0) {
                    locsThatCanMove.add(loc);
//                    if (whiteTurn) { // white turn
//                        if (blackKingInCheck) { // and black king is in check, have to kill it
//                            if (this.availableDestinations.contains(otherKingLocation)) {
//                                availableSources.add(loc);
//                            } else {
//                                // other king is in check, but this piece cannot get to king
//                            }
//                        } else {
//                            // king not in check so we can add
//                            availableSources.add(loc);
//                        }
//                        if (whiteKingInCheck) {
//                            // if own king is in check, have to stop it.
//                            // Clone board attempt move and recheck for check
//                            
//                        }
//                    } else {// black turn
//                        if (whiteKingInCheck) {// and white king is in check, have to kill it
//                            if (this.availableDestinations.contains(otherKingLocation)) {
//                                availableSources.add(loc);
//                            } else {
//                                // other king is in check, but this piece cannot get to king
//                            }
//                        } else {
//                            // king not in check so we can add
//                            availableSources.add(loc);
//                        }
//                        if (blackKingInCheck) {
//                            // if own king is in check, have to stop it.
//                            // Clone board attempt move and recheck for check
//                            
//                        }
//                    }
                    //availableSources.add(loc);
                }
            }
        }        
        return locsThatCanMove;
    }
    

//    public void setAvailableSources(boolean turnColor) {
//        availableSources.clear();
//        BoardLocation otherPlayerKingLocation = getKingLoc(!turnColor);
//        // get the pieces that can move
//        for (int i = 0; i < this.currentBoard.size(); i++) {
//            BoardLocation loc = BoardLocation.values()[i];
//            Piece curPiece = this.currentBoard.get(loc);
//            if (curPiece != null && curPiece.isWhite() == turnColor) {
//                this.setAvailableDestinations(loc);
//                if (this.availableDestinations.size() > 0) {
//                    if (turnColor) { // white turn
//                        if (blackKingInCheck) { // and black king is in check, have to kill it
//                            if (this.availableDestinations.contains(otherPlayerKingLocation)) {
//                                availableSources.add(loc);
//                            } else {
//                                // other king is in check, but this piece cannot get to king
//                            }
//                        } else {
//                            // king not in check so we can add
//                            availableSources.add(loc);
//                        }
//                        if (whiteKingInCheck) {
//                            // if own king is in check, have to stop it.
//                            // Clone board attempt move and recheck for check
//                            
//                        }
//                    } else {// black turn
//                        if (whiteKingInCheck) {// and white king is in check, have to kill it
//                            if (this.availableDestinations.contains(otherPlayerKingLocation)) {
//                                availableSources.add(loc);
//                            } else {
//                                // other king is in check, but this piece cannot get to king
//                            }
//                        } else {
//                            // king not in check so we can add
//                            availableSources.add(loc);
//                        }
//                        if (blackKingInCheck) {
//                            // if own king is in check, have to stop it.
//                            // Clone board attempt move and recheck for check
//                            
//                        }
//                    }
//                    //availableSources.add(loc);
//                }
//            }
//        }
//    }

    private BoardLocation getKingLoc(boolean b) {
        BoardLocation retLoc = null;

        for (int i = 0; i < this.currentBoard.size(); i++) {
            BoardLocation cur = BoardLocation.values()[i];
            Piece curPiece = this.currentBoard.get(cur);
            if (curPiece != null && curPiece.isWhite() == b && curPiece.getType() == PieceType.k) {
                retLoc = cur;
            }
        }

        return retLoc;
    }

//    public ArrayList<BoardLocation> getAvailableDestinations() {
//        return this.availableDestinations;
//    }
//
//    public void setAvailableDestinations(BoardLocation srcLoc) {
//        availableDestinations.clear();
//        Piece p = this.getPiece(srcLoc);
//
//        // TODO iterator stuff
//        for (int i = 0; i < this.currentBoard.size(); i++) {
//            BoardLocation end = BoardLocation.values()[i];
//            String moveString = srcLoc.toString() + " " + end.toString();
//            ChessMove currentMovingMove = ChessFactory.CreateMove(MoveType.MOVE, moveString);
//            ChessMove currentCapturingMove = ChessFactory.CreateMove(MoveType.CAPTURE, moveString);
//            if (validateMove(currentMovingMove, false) || validateMove(currentCapturingMove, false)) {
//                availableDestinations.add(end);
//            }
//        }
//        // availableMoves.add(BoardLocation.a4);
//        // availableMoves.add(BoardLocation.b4);
//    }

    public void addMove(ChessMove m) {
        if (validateMove(m, true)) {
            moves.push(m);
            try {
                executeMove(m);
            } catch (ChessException e) {
                moves.pop();
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
            this.currentBoard.capturePiece(currentMove.getDestLoc());
            currentBoard.move(currentMove.getSrcLoc(), currentMove.getDestLoc());
        } else if (currentMove.getType() == MoveType.MOVE) {
            this.currentBoard.move(currentMove.getSrcLoc(), currentMove.getDestLoc());
        }
        if (currentMove.getSubMove() != null) {
            executeMove(currentMove.getSubMove());
        }


        // Check Check
        checkKingInCheck(true);
        checkKingInCheck(false);
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

    public void checkKingInCheck(boolean kColor) {

        boolean kingIsInCheck = false;
        BoardLocation kingLoc = null;

        ArrayList<BoardLocation> otherColorPieces = new ArrayList<>();
        for (int i = 0; i < currentBoard.size(); i++) {
            BoardLocation cur = BoardLocation.values()[i];
            Piece curPiece = currentBoard.get(cur);
            if (curPiece != null) {
                if (curPiece.isWhite() != kColor) {
                    otherColorPieces.add(cur);
                } else {
                    if (curPiece.getType() == PieceType.k) {
                        kingLoc = cur;
                    }
                }
            }
        }
        if (kingLoc != null) {
            System.out.println("Checking for check");
            System.out.println("King color is: " + kColor);
            System.out.println("There are " + otherColorPieces.size() + " piece for the other player");
            for (BoardLocation l : otherColorPieces) {
                System.out.println("Piece at: " + l);
                String moveString = l.toString() + " " + kingLoc.toString() + "*";
                if (validateMove(ChessFactory.CreateMove(MoveType.CAPTURE, moveString), false)) {
                    System.out.println("has checked the king");
                    kingIsInCheck = true;
                } else {
                    System.out.println("no check");
                }
            }
        }

        if (kColor == true) {
            whiteKingInCheck = kingIsInCheck;
        } else {
            blackKingInCheck = kingIsInCheck;
        }

    }

    public boolean isWhiteTurn() {
        return this.whiteTurn;
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
        this.whiteTurn = !whiteTurn;
    }

    public boolean undoMove() {
        ChessMove m = moves.pop();
        
        ChessMove undo = null;
        if (m instanceof MovingMove) {
            undo = ChessFactory.CreateMove(m.getDestLoc() + " " + m.getSrcLoc());
        } else if (m instanceof CaptureMove) {
            undo = ChessFactory.CreateMove(m.getDestLoc() + " " + m.getSrcLoc());
            CaptureMove c = (CaptureMove) undo;
            Piece capturedPiece = c.getCapturedPiece();
            String readdString = "";
            readdString += capturedPiece.getType();
            readdString += (capturedPiece.isWhite()) ? "l" : "d";
            readdString += m.getDestLoc();
            undo.setSubmove(ChessFactory.CreateMove(readdString));
        }
        
        executeMove(undo);  
        
        
        
        movesRedo.push(m);

        return true;
    }

    public boolean redoMove() {
        ChessMove m = movesRedo.pop();
        moves.push(m);

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
            }
        }
        return locs;

    }
}
