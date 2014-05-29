package chess_rollins_blake.view;

import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;
import java.util.TreeSet;

import chess_rollins_blake.controller.GameStatus;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.model.pieces.Piece;

public class ConsoleViewLarge extends ChessView {

    private Scanner scan;

    public ConsoleViewLarge(ChessModel model) {
        super(model);
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
        System.out.println("");
        System.out.println("");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("");

    }

    public void printGameStatus(GameStatus status) {
        System.out.println(gameStatusDisplayMap.get(status));
    }

    public void printMessage() {
        if (this.model.getMessage() != null) {
            System.out.println(this.model.getMessage());
        }

    }

    public void printBoard() {
        // Create the column numbers

        BoardLocation tempSource = this.model.getCurrentModelStateLocation();
        HashSet<BoardLocation> tempDests = this.model.getAvailableDestinationsFromLocation(tempSource);

        String retString = "";
        retString += "--|----A---------B---------C---------D---------E---------F---------G---------H----|--\n";
        int numberOfRows = this.model.getBoardRowSize();
        int numberOfCols = this.model.getBoardRowSize();
        for (int rowIndex = numberOfRows - 1; rowIndex > -1; rowIndex--) {

            // Create the row numbers
            retString += (rowIndex + 1) + " | ";

            // Print each row
            for (int colIndex = 0; colIndex < numberOfCols; colIndex++) {
                int currentPieceLocation = colIndex * numberOfCols + rowIndex;
                retString += pieceString(this.model.getPiece(BoardLocation.values()[currentPieceLocation]));
                if (tempDests.contains(currentPieceLocation)) {
                    retString += "&";
                } else {
                    retString += " ";
                }
                retString += " | ";
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
            retString += "------";
        }
        return retString;
    }



    @Override
    public BoardLocation requestSourcePiece() {
        printBoard();



        boolean curTurnIsWhite = this.model.isWhiteTurn();
        System.out.println("-- " + pieceColorDisplayMap.get(curTurnIsWhite) + " Player's turn--");
        System.out.println("Enter the which piece you would like to move?");

        HashSet<ChessMove> moves = this.model.getAvailableMoves();
        TreeSet<BoardLocation> srcs = new TreeSet<BoardLocation>();
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

        HashSet<ChessMove> moves = this.model.getAvailableMoves();
        //ArrayList<ChessMove> movesFromSrc = new ArrayList<ChessMove>();
        TreeSet<BoardLocation> dests = this.model.getDestsFromMoveCache(srcLoc);
//        for (ChessMove m : moves) {
//            srcs.add(m.getSrcLoc());
//        }

        System.out.print("(");

        int i = 0;
        for (BoardLocation s : dests) {
            System.out.print(s.toString());
            if (i + 1 < dests.size()) {
                System.out.print(",");
            }
            i++;
        }
        System.out.println(")");
        
        
//
//        for (ChessMove m : moves) {
//            if (m.getSrcLoc() == srcLoc) {
//                movesFromSrc.add(m);
//            }
//        }
//        movesFromSrc.sort(null);
//
//        System.out.print("(");
//        for (int i = 0; i < movesFromSrc.size(); i++) {
//            System.out.print(movesFromSrc.get(i).getDestLoc().toString());
//            if (i + 1 < movesFromSrc.size()) {
//                System.out.print(",");
//            }
//        }
//        System.out.println(")");

        BoardLocation loc = null;
        boolean moveIsValidSyntax = false;
        while (!moveIsValidSyntax) {
            String tempCommand = readLine();
            loc = BoardLocation.valueOf(tempCommand.trim());

            for (BoardLocation m : dests) {
                if (m == loc) {
                    moveIsValidSyntax = true;
                }
            }
            if (!moveIsValidSyntax) {
                System.out.println(loc.toString() + " is not a valid destination.  Try again.");
            }
        }
        return loc;
    }

    @Override
    public PieceType requestPawnPromotion() {
        System.out.println("YAY PAWN PROMOTION!!! You get a queen.");
        return PieceType.q;
    }


    private String readLine() {
        return scan.nextLine();
    }


    @Override
    public void addBoardListener(MouseListener l) {

    }


    @Override
    public void addBoardMotionListener(MouseMotionListener l) {
        // TODO Auto-generated method stub

    }



}
