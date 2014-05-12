package chess_rollins_blake.view;

import java.util.Map;
import java.util.Observable;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.Piece;
import chess_rollins_blake.lib.PieceColor;

public class ConsoleView extends ChessView {

    public ConsoleView() {
        System.out.println("Welcome to the Console Chess.\n");
    }

    @Override
    public void update() {
        printBoard();
    }

    public void printBoard() {
        if (this.model.message != null) {
            System.out.println(this.model.message);
        }
        String retString = "  ____________________\n";
        for (int rowIndex = 7; rowIndex > -1; rowIndex--) {

            retString += (rowIndex + 1) + " | ";
            for (int colIndex = 0; colIndex < 8; colIndex++) {
                retString += pieceString(this.model.getPiece(BoardLocation.values()[colIndex * 8 + rowIndex])) + " ";

            }
            retString += " |\n";
        }
        retString += "  ____________________\n";
        retString += "    a b c d e f g h\n";
        //
        // for (Map.Entry<BoardLocation, Piece> kv : this.model.pieces) {
        // retString += kv.getKey() + " " + "\n";
        // }

        System.out.println(retString);

    }

    private String pieceString(Piece p) {
        String retString = "";
        if (p != null) {
            if (p.getColor() == PieceColor.l) {
                retString += p.toString().substring(0, 1).toUpperCase();
            } else {
                retString += p.toString().substring(0, 1).toLowerCase();
            }
        } else {
            retString += "-";
        }
        return retString;
    }

}
