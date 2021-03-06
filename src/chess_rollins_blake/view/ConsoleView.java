//package chess_rollins_blake.view;
//
//import java.util.Map;
//import java.util.Observable;
//
//import chess_rollins_blake.lib.BoardLocation;
//import chess_rollins_blake.lib.PieceColor;
//import chess_rollins_blake.model.pieces.Piece;
//
//public class ConsoleView extends ChessView {
//
//    public ConsoleView() {
//        System.out.println("Welcome to the Console Chess.\n");
//    }
//
//    //
//    // @Override
//    // public void update() {
//    // printMessage();
//    // printBoard();
//    // }
//
//    public void update(Observable obs, Object obj) {
//        System.out.println(obj);
//        printBoard();
//    }
//
//    public void printMessage() {
//        if (this.model.getMessage() != null) {
//            System.out.println(this.model.getMessage());
//        }
//
//    }
//
//    public void printBoard() {
//        String retString = "  ____________________\n";
//        for (int rowIndex = this.model.currentBoard.getBoardSize() - 1; rowIndex > -1; rowIndex--) {
//
//            retString += (rowIndex + 1) + " | ";
//            for (int colIndex = 0; colIndex < this.model.currentBoard.getBoardSize(); colIndex++) {
//                retString += pieceString(this.model.getPiece(BoardLocation.values()[colIndex * this.model.currentBoard.getBoardSize() + rowIndex])) + " ";
//
//            }
//            retString += " |\n";
//        }
//        retString += "  ____________________\n";
//        retString += "    a b c d e f g h\n";
//        //
//        // for (Map.Entry<BoardLocation, Piece> kv : this.model.pieces) {
//        // retString += kv.getKey() + " " + "\n";
//        // }
//
//        System.out.println(retString);
//
//    }
//
//    private String pieceString(Piece p) {
//        String retString = "";
//        if (p != null) {
//            if (p.getColor() == PieceColor.l) {
//                retString += p.toString().substring(0, 1).toUpperCase();
//            } else {
//                retString += p.toString().substring(0, 1).toLowerCase();
//            }
//        } else {
//            retString += "-";
//        }
//        return retString;
//    }
//
//    @Override
//    public void requestInput() {
//        // TODO Auto-generated method stub
//        // Copy this from large since that is what I want to do first.
//    }
//
//    @Override
//    public BoardLocation requestSourcePiece() {
//        return null;
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public BoardLocation requestDestinationPiece(BoardLocation srcLoc) {
//        return null;
//        // TODO Auto-generated method stub
//        
//    }
//
//}
