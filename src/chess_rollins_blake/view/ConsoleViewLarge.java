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

    HashMap<PieceType, String> pieceDisplayMap;
    HashMap<PieceColor, String> pieceColorDisplayMap;
    Scanner scan;

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
        System.out.println(obj);
        printBoard();
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

            if (tempType == MoveType.LOCATION) {
                event = new ActionEvent(this, 2, tempCommand); // 1- addmove, 2- check loc valid
                moveIsValidSyntax = true;

            } else if (tempType == MoveType.MOVE) {
                event = new ActionEvent(this, 1, tempCommand);
                moveIsValidSyntax = true;
            } else if (tempType == MoveType.CAPTURE) {
                event = new ActionEvent(this, 3, tempCommand);
                moveIsValidSyntax = true;
            } else {
                System.out.println("The command was not valid.  Try again.");
            }
        }

        try {
            super.sendRequestToController(event);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
        }

        if (tempType == MoveType.LOCATION) {
            ArrayList<BoardLocation> aMoves = this.model.getAvailableMoves();
            System.out.println("There are " + aMoves.size() + " vailable moves from " + tempCommand + ":");
            for (BoardLocation l : aMoves) {
                System.out.println(l);
            }
        }

    }

    private String readLine() {
        return scan.nextLine();
    }

}
