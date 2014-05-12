package chess_rollins_blake;

import chess_rollins_blake.controller.ChessController;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.view.ConsoleView;

public class ConsoleChess {

    public static void main(String[] args) {
        
        ConsoleChess c = new ConsoleChess();
        c.playChess();
    }
    
    public void playChess() {
        
        ChessModel model = new ChessModel();
        ConsoleView view = new ConsoleView();
        
        view.setModel(model);
        model.addObserver(view);
        
        ChessController controller = new ChessController();
        controller.addModel(model);
        controller.addView(view);
        controller.loadModelFromFile("chess03.txt");
        
        view.addController(controller);
        
        //view.printBoard();
        
    }
}
