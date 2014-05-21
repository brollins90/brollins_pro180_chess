package chess_rollins_blake.view;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;

import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.pieces.Piece;

public class ConsoleViewLarge extends ChessView {

    private HashMap<PieceType, String> pieceDisplayMap;
    private HashMap<Boolean, String> pieceColorDisplayMap;
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
        pieceColorDisplayMap.put(false, "Black");
        pieceColorDisplayMap.put(true, "White");

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
        if (this.model.isBlackKingInCheck()) {
            System.out.println("Black King is in check!");
        } else {
            System.out.println("Black King is not in check!");
        }
        if (this.model.isWhiteKingInCheck()) {
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
            retString = (p.isWhite()) ? pieceDisplayMap.get(p.getType()).toUpperCase() : pieceDisplayMap.get(p.getType()).toLowerCase();
        } else {
            retString += "-------";
        }
        return retString;
    }

//    @Override
//    public void requestInput() {
//        boolean curTurnIsWhite = this.model.isWhiteTurn();
//        System.out.println("-- " + pieceColorDisplayMap.get(curTurnIsWhite) + " Player's turn--");
//        System.out.println("Please enter the location of the piece you would like to move: ");
//        boolean moveIsValidSyntax = false;
//        String tempCommand = "";
//        ActionEvent event = null;
//        MoveType tempType = null;
//        while (!moveIsValidSyntax) {
//            tempCommand = readLine();
//            tempType = ChessFactory.ValidateMoveString(tempCommand);
//            event = new ActionEvent(this, tempType.ordinal(), tempCommand);
//        }
//
//        try {
//            super.sendRequestToController(event);
//        } catch (ChessException e) {
//            System.out.println(e.getMessage());
//        }
//
//        if (tempType == MoveType.LOCATION) {
//            ArrayList<BoardLocation> aMoves = this.model.getAvailableDestinations();
//            System.out.println("There are " + aMoves.size() + " vailable moves from " + tempCommand + ":");
//            for (BoardLocation l : aMoves) {
//                System.out.println(l);
//            }
//        }
//    }


    @Override
    public BoardLocation requestSourcePiece() {
        printBoard();
        
        
        
        boolean curTurnIsWhite = this.model.isWhiteTurn();
        System.out.println("-- " + pieceColorDisplayMap.get(curTurnIsWhite) + " Player's turn--");
        System.out.println("Enter the which piece you would like to move?");
        
        ArrayList<ChessMove> moves = this.model.getAvailableMoves();
        HashSet<BoardLocation> srcs = new HashSet<BoardLocation>();
        for (ChessMove m : moves) {
            srcs.add(m.getSrcLoc());
        }
         
        System.out.print("(");
        
        int i = 0;
        for (BoardLocation s : srcs) {
            System.out.print(s.toString());
            if (i + 1 < srcs.size()) {
                System.out.print(",");
            }
            i++;
        }
        
//        for (int i = 0; i < srcs.size(); i++) {
//            System.out.print(srcs..get(i).getSrcLoc().toString());
//            if (i + 1 < srcs.size()) {
//                System.out.print(",");
//            }
//        }
        System.out.println(")");

        boolean validSource = false;
        BoardLocation loc = null;
        do {
            String locationString = readLine();
            loc = BoardLocation.valueOf(locationString.trim());

            for (BoardLocation l : srcs) {
                if (l == loc) {
                    validSource = true;
                }
            }
            if (!validSource) {
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

        ArrayList<ChessMove> moves = this.model.getAvailableMoves();
        ArrayList<ChessMove> movesFromSrc = new ArrayList<ChessMove>();
        
        for (ChessMove m : moves) {
            if (m.getSrcLoc() == srcLoc) {
                movesFromSrc.add(m);
            }
        }
        
        
        System.out.print("(");
        for (int i = 0; i < movesFromSrc.size(); i++) {
            System.out.print(movesFromSrc.get(i).getDestLoc().toString());
            if (i + 1 < movesFromSrc.size()) {
                System.out.print(",");
            }
        }
        System.out.println(")");

        BoardLocation loc = null;
        boolean moveIsValidSyntax = false;
        while (!moveIsValidSyntax) {
            String tempCommand = readLine();
            loc = BoardLocation.valueOf(tempCommand.trim());
            
            for (ChessMove m : movesFromSrc) {
                if (m.getDestLoc() == loc) {
                    moveIsValidSyntax = true;
                }
            }
            if (!moveIsValidSyntax) {
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
