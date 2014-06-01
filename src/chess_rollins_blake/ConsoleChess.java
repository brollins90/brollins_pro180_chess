package chess_rollins_blake;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import chess_rollins_blake.controller.ChessController;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.view.GUIView;

/**
 * This is the entry point into my Chess game The game was built for the PRO180 class at Neumont University in the Spring quarter of 2014
 * 
 * @author Blake Rollins Instructor: Steve Halladay
 * 
 */
public class ConsoleChess {

    static boolean DEBUG_ON = false;
    static boolean DEBUG_TO_FILE = false;
    static PrintStream ps;

    public static void debugMessage(String s) {
        if (DEBUG_ON) {
            s = Thread.currentThread().getName() + "--" + s;
            System.out.println(s);
            if (DEBUG_TO_FILE) {
                ps.append(s + "\n");
            }
        }
    }

    public ConsoleChess() {
        if (DEBUG_TO_FILE) {
            try {
                ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("C:\\_\\debug.txt")));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * The first argument is the path / name of the file to start the game from.
     * 
     * @param args The first String argument is the name of the file from which to load the game.
     */
    public static void main(String[] args) {

        args = new String[1];
        // args[0] = "Donald Byrne vs Robert James Fischer.txt";
        // args[0] = "Garry Kasparov vs Deep Blue_1996.02.10_r1.txt";
        args[0] = "chess08.txt";
        // testPawnPromotion
        // args[0] = "Pratts Trap.txt";
        // args[0] = "Kostics Trap.txt";
        ConsoleChess c = new ConsoleChess();
        c.playChess(args, true);
    }

    /**
     * Called after validating the starting information. Initiates the Chess game.
     * 
     * @param args
     */
    private void playChess(String[] args, boolean loadNewBoardFirst) {

        try {

            // TODO
            // Validate the input file
            // Possible take in the view type


            ChessModel model = new ChessModel();
            // ChessView view = new ConsoleView(model);
            // ChessView view = new ConsoleViewLarge(model);
            GUIView view = new GUIView(model);

            model.addObserver(view);

            ChessController controller = new ChessController(model, view);

            view.addController(controller);

            if (loadNewBoardFirst) {
                controller.loadNewBoard();
            }
            controller.loadFromFile(args[0]);

            view.update();
            controller.playChess();



            // view.printBoard();
        } catch (ChessException e) {
            System.err.println("There was a problem with the chess game... Sorry");
            e.printStackTrace();
        }

    }
}
