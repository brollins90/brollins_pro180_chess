package chess_rollins_blake;

import chess_rollins_blake.controller.ChessController;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.view.ConsoleView;

public class ConsoleChess {

    public static void main(String[] args) {
        
        args = new String[1];
        args[0] = "chess03.txt";
        ConsoleChess c = new ConsoleChess();
        c.playChess(args);
    }
    
    public void playChess(String[] args) {
        
        try {
        ChessModel model = new ChessModel();
        ConsoleView view = new ConsoleView();
        
        view.setModel(model);
        model.addObserver(view);
        
        ChessController controller = new ChessController();
        controller.addModel(model);
        controller.addView(view);
        controller.loadFromFile(args[0]);
        
        view.addController(controller);
        
        //view.printBoard();
        } catch (ChessException e) {
            System.out.println("There was a problem with the chess game... Sorry");
            e.printStackTrace();
        }
        
    }
}
