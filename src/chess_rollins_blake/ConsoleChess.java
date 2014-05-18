package chess_rollins_blake;

import chess_rollins_blake.controller.ChessController;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.view.ChessView;
import chess_rollins_blake.view.ConsoleView;
import chess_rollins_blake.view.ConsoleViewLarge;

/**
 * This is the entry point into my Chess game
 * The game wsa built for the PRO180 class at Neumont University in the Spring quarter of 2014
 * @author Blake Rollins
 * Instructor: Steve Halladay
 *
 */
public class ConsoleChess {

    /**
     * The first argument is the path / name of the file to start the game from.
     * @param args
     */
    public static void main(String[] args) {
        
        args = new String[1];
        args[0] = "sampleGame1.txt";
        ConsoleChess c = new ConsoleChess();
        c.playChess(args);
    }
    
    private void playChess(String[] args) {
        
        try {
        ChessModel model = new ChessModel();
        //ChessView view = new ConsoleView();
        ChessView view = new ConsoleViewLarge();
        
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
