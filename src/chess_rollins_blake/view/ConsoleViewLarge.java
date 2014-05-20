package chess_rollins_blake.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.pieces.Piece;

public class ConsoleViewLarge extends ChessView {

    private HashMap<PieceType, String> pieceDisplayMap;
    private HashMap<PieceColor, String> pieceColorDisplayMap;
    private Scanner scan;

    public ConsoleViewLarge() {
        pieceDisplayMap = new HashMap<>();
        pieceDisplayMap.put(PieceType.b, "Bishop ");
        pieceDisplayMap.put(PieceType.k, " King  ");
        pieceDisplayMap.put(PieceType.n, "Knight ");
        pieceDisplayMap.put(PieceType.p, " Pawn  ");
        pieceDisplayMap.put(PieceType.q, "Queen  ");
        pieceDisplayMap.put(PieceType.r, " Rook  ");

        pieceColorDisplayMap = new HashMap<>();
        pieceColorDisplayMap.put(PieceColor.d, "Black");
        pieceColorDisplayMap.put(PieceColor.l, "White");

        scan = new Scanner(System.in);

        System.out.println("Welcome to the Console Chess.\n");
    }


    //
    // @Override
    // public void update() {
    // printMessage();
    // printBoard();
    // }

    public void update(Observable obs, Object obj) {
        if (obj instanceof String) {
            System.out.println(obj);
        } else {
            System.out.println("ERROR: Invalid message.");
        }
        printBoard();
        if (this.model.getDKingCheck()) {
            System.out.println("Black King is in check!");
        } else {
            System.out.println("Black King is not in check!");
        }
        if (this.model.getLKingCheck()) {
            System.out.println("White King is in check!");
        } else {
            System.out.println("White King is not in check!");
        }

    }

    public void printMessage() {
        if (this.model.getMessage() != null) {
            System.out.println(this.model.getMessage());
        }

    }

    public void printBoard() {
        // Create the column numbers
        String retString = "";
        retString += "--|----A---------B---------C---------D---------E---------F---------G---------H----|--\n";
        int numberOfRows = this.model.currentBoard.getBoardSize();
        int numberOfCols = this.model.currentBoard.getBoardSize();
        for (int rowIndex = numberOfRows - 1; rowIndex > -1; rowIndex--) {

            // Create the row numbers
            retString += (rowIndex + 1) + " | ";

            // Print each row
            for (int colIndex = 0; colIndex < numberOfCols; colIndex++) {
                int currentPieceLocation = colIndex * numberOfCols + rowIndex;
                retString += pieceString(this.model.getPiece(BoardLocation.values()[currentPieceLocation])) + " | ";

            }
            // Create the row numbers
            retString += (rowIndex + 1) + "\n";
        }
        retString += "--|----A---------B---------C---------D---------E---------F---------G---------H----|--\n";

        System.out.println(retString);

    }

    private String pieceString(Piece p) {
        String retString = "";
        if (p != null) {
            retString = (p.getColor() == PieceColor.l) ? pieceDisplayMap.get(p.getType()).toUpperCase() : pieceDisplayMap.get(p.getType()).toLowerCase();
        } else {
            retString += "-------";
        }
        return retString;
    }

    @Override
    public void requestInput() {
        PieceColor curTurn = this.model.getCurrentTurn();
        System.out.println("-- " + pieceColorDisplayMap.get(curTurn) + " Player's turn--");
        System.out.println("Please enter the location of the piece you would like to move: ");
        boolean moveIsValidSyntax = false;
        String tempCommand = "";
        ActionEvent event = null;
        MoveType tempType = null;
        while (!moveIsValidSyntax) {
            tempCommand = readLine();
            tempType = this.model.validateSyntax(tempCommand);
            event = new ActionEvent(this, tempType.ordinal(), tempCommand);
        }

        try {
            super.sendRequestToController(event);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
        }

        if (tempType == MoveType.LOCATION) {
            ArrayList<BoardLocation> aMoves = this.model.getAvailableDestinations();
            System.out.println("There are " + aMoves.size() + " vailable moves from " + tempCommand + ":");
            for (BoardLocation l : aMoves) {
                System.out.println(l);
            }
        }
    }


    @Override
    public BoardLocation requestSourcePiece() {
        PieceColor curTurn = this.model.getCurrentTurn();
        System.out.println("-- " + pieceColorDisplayMap.get(curTurn) + " Player's turn--");
        System.out.println("Enter the which piece you would like to move?");
        System.out.print("(");
        for (int i = 0; i < this.model.getAvailableSources().size(); i++) {
            System.out.print(this.model.getAvailableSources().get(i));
            if (i + 1 < this.model.getAvailableSources().size()) {
                System.out.print(",");
            }
        }
        System.out.println(")");

        boolean validSource = false;
        BoardLocation loc = null;
        do {
            String locationString = readLine();
            loc = BoardLocation.valueOf(locationString);
            if (this.model.getAvailableSources().contains(loc)) {
                validSource = true;
            } else {
                System.out.println("Location is not valid.  Enter a new location.");
            }
        } while (!validSource);

        ActionEvent event = new ActionEvent(this, MoveType.LOCATION.ordinal(), loc.toString());

        try {
            super.sendRequestToController(event);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
        }
        return loc;


    }

    @Override
    public BoardLocation requestDestinationPiece(BoardLocation srcLoc) {

        System.out.println("Select destination from: ");
        System.out.print("(");
        for (int i = 0; i < this.model.getAvailableDestinations().size(); i++) {
            System.out.print(this.model.getAvailableDestinations().get(i));
            if (i + 1 < this.model.getAvailableDestinations().size()) {
                System.out.print(",");
            }
        }
        System.out.println(")");

        BoardLocation loc = null;
        boolean moveIsValidSyntax = false;
        while (!moveIsValidSyntax) {
            String tempCommand = readLine();
            loc = BoardLocation.valueOf(tempCommand);
            if (this.model.getAvailableDestinations().contains(loc)) {
                moveIsValidSyntax = true;
            } else {
                System.out.println(loc.toString() + " is not a valid destination.  Try again.");
            }
        }
        return loc;
    }

    //
    // {
    //
    // String moveString = srcLoc.toString() + " " + loc.toString();
    // if (this.model.getPiece(loc) != null) {
    // moveString += "*";
    // }
    // MoveType moveType = this.model.validateSyntax(moveString);
    // event = new ActionEvent(this, moveType.ordinal(), moveString);
    //
    // try {
    // super.sendRequestToController(event);
    // } catch (ChessException e) {
    // System.out.println(e.getMessage());
    // }
    // }

    private String readLine() {
        return scan.nextLine();
    }

}
