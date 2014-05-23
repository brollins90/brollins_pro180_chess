package chess_rollins_blake;

import chess_rollins_blake.controller.ChessController;
import chess_rollins_blake.controller.GameStatus;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.view.ChessView;
import chess_rollins_blake.view.ConsoleViewLarge;

/**
 * This is the entry point into my Chess game The game was built for the PRO180 class at Neumont University in the Spring quarter of 2014
 * 
 * @author Blake Rollins Instructor: Steve Halladay
 * 
 */
public class ConsoleChess {

    /**
     * The first argument is the path / name of the file to start the game from.
     * 
     * @param args The first String argument is the name of the file from which to load the game.
     */
    public static void main(String[] args) {

        args = new String[1];
        //args[0] = "Donald Byrne vs Robert James Fischer.txt";
        args[0] = "fourMoveCheckMate.txt";
        ConsoleChess c = new ConsoleChess();
        c.playChess(args);
    }

    /**
     * Called after validating the starting information. Initiates the Chess game.
     * 
     * @param args
     */
    private void playChess(String[] args) {

        try {

            // TODO
            // Validate the input file
            // Possible take in the view type


            ChessModel model = new ChessModel();
            // ChessView view = new ConsoleView();
            ChessView view = new ConsoleViewLarge();
            // ChessView view = new GUIView();

            view.setModel(model);
            model.addObserver(view);

            ChessController controller = new ChessController();
            controller.addModel(model);
            controller.addView(view);

            view.addController(controller);

            // controller.loadFromFile("newBoard.txt");
            controller.loadNewBoard();
            controller.loadFromFile(args[0]);
            controller.playChess();
            
            
            

            // view.printBoard();
        } catch (ChessException e) {
            System.err.println("There was a problem with the chess game... Sorry");
            e.printStackTrace();
        }

    }
}
