package chess_rollins_blake.view;

import java.util.HashMap;
import java.util.Observable;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceColor;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.pieces.Piece;

public class ConsoleViewLarge extends ChessView {
    
    HashMap<PieceType, String> pieceDisplayMap;

    public ConsoleViewLarge() {
        pieceDisplayMap = new HashMap<>();
        pieceDisplayMap.put(PieceType.b, "Bishop ");
        pieceDisplayMap.put(PieceType.k, " King  ");
        pieceDisplayMap.put(PieceType.n, "Knight ");
        pieceDisplayMap.put(PieceType.p, " Pawn  ");
        pieceDisplayMap.put(PieceType.q, "Queen  ");
        pieceDisplayMap.put(PieceType.r, " Rook  ");
        System.out.println("Welcome to the Console Chess.\n");
    }
//
//    @Override
//    public void update() {
//        printMessage();
//        printBoard();
//    }
    
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

}
