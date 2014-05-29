package chess_rollins_blake.controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import chess_rollins_blake.ConsoleChess;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.ChessFactory;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.model.pieces.Pawn;
import chess_rollins_blake.model.pieces.Piece;
import chess_rollins_blake.view.ChessView;

public class ChessController implements java.awt.event.ActionListener {

    int VIEW_WIDTH = 75;
    int VIEW_HEIGHT = 75;


    protected ChessModel model;
    protected ChessView view;
    protected GameStatus currentGameStatus;

    public ChessController(ChessModel model, ChessView view) {
        this.model = model;
        this.view = view;

        this.view.addBoardListener(new LocationListener());
        this.view.addBoardMotionListener(new LocationListener());

    }

    class LocationListener implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            ConsoleChess.debugMessage("mouseClicked");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ConsoleChess.debugMessage("mouseEntered");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ConsoleChess.debugMessage("mouseExited");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            ConsoleChess.debugMessage("mousePressed");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ConsoleChess.debugMessage("mouseReleased");
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            ConsoleChess.debugMessage("mouseDragged");
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // ConsoleChess.debugMessage("mouseMoved");
            int mouseX = e.getX();
            int mouseY = e.getY();

            int row = 7 - (mouseY / VIEW_HEIGHT);
            int col = mouseX / VIEW_WIDTH;

            // System.out.println(mouseX + " " + mouseY);
            // System.out.println(row + " " + col);

            BoardLocation mouseLoc = BoardLocation.getLocFromRowAndColumn(row, col);

            updateModelForLocation(mouseLoc);


        }


    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // TODO
        // Add some obj validation

        String theCommand = event.getActionCommand();
        switch (MoveType.values()[event.getID()]) {
            case ADD:
                addMove(theCommand, false);
                break;
            case CAPTURE:
            case MOVE:
                addMove(theCommand, true);
                break;
            case LOCATION: // Check available moves
                // this.model.setAvailableDestinations(BoardLocation.valueOf(theCommand));
                break;
            case PROMOTION:
                break;
        }

    }


    public void updateModelForLocation(BoardLocation loc) {
        this.model.setCurrentModelStateLocation(loc);
        view.update();
    }

    public void addMove(String moveString, boolean updateObservers) {
        this.model.addMove(moveString, updateObservers);
    }

    public void addMove(ChessMove move, boolean updateObservers) {
        this.model.addMove(move, updateObservers);
    }

    public void addMoveWithoutUpdate(ChessMove m) {
        this.model.addMoveWithoutUpdate(m);
    }

    public void addMoveWithoutUpdate(String m) {
        this.model.addMoveWithoutUpdate(m);
    }

    public void loadFromFile(String filePath) {

        if (filePath != null) {
            //boolean isNewBoard = (filePath.equals("newBoard.txt")) ? true : false;
            BufferedReader br = null;
            try {
                String path = filePath;
                br = new BufferedReader(new FileReader(path));

                String line = "";
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    try {
                        actionPerformed(new ActionEvent(this, MoveType.ADD.ordinal(), line));
                        // addMove(line, !isNewBoard);
                    } catch (ChessException e) {
                        this.model.setModelStatusMessage(e.getMessage());
                    }
                }
                br.close();

            } catch (FileNotFoundException e) {
                throw new ChessException("Unable to open the file at: " + filePath);
            } catch (IOException e) {
                throw new ChessException("IOException when trying to read the file at: " + filePath);
            }
        } else {
            throw new ChessException("The filepath is not set");
        }

    }

    public void playChess() {

        this.currentGameStatus = GameStatus.PLAYING;

        // Start the game Loop
        while (this.currentGameStatus == GameStatus.PLAYING) {

            this.model.resetView();

            HashSet<ChessMove> availableMoves = this.model.getAvailableMoves();

            // if there are no moves, then the game is over
            if (availableMoves.size() == 0) {
                this.currentGameStatus = this.model.isWhiteTurn() ? GameStatus.DARKWIN : GameStatus.LIGHTWIN;
            }
            
            // I dont like this logic, but I also dont want to use a break when the game is over
            if (this.currentGameStatus == GameStatus.PLAYING) {

                BoardLocation src = null;
                while (src == null) {
                    src = this.view.requestSourcePiece();
                }
                updateModelForLocation(src);

                BoardLocation dest = null;
                while (dest == null) {
                    dest = this.view.requestDestinationPiece(src);
                }

                String moveString = src + " " + dest;
                Piece destPiece = this.model.getPiece(dest);
                if (destPiece != null && destPiece.isWhite() != this.model.isWhiteTurn()) {
                    moveString += "*";
                }

                Piece srcPiece = this.model.getPiece(src);
                ChessMove thisMove = ChessFactory.CreateMove(moveString);
                
                
                
                
                // Check for pawn promotion
                //Piece newDestPiece = this.model.getPiece(dest);
                if (srcPiece instanceof Pawn) {
                    if (((Pawn) srcPiece).isInEigthRow(dest)) {
                        PieceType pawnPromotionType = this.view.requestPawnPromotion();
                        thisMove.setChangeTurnAfter(false);
                        //"a1 qda1"
                        char ldColor = (srcPiece.isWhite()) ? 'l' : 'd';
                        String promotionString = thisMove.getDestLoc().toString() + " " + pawnPromotionType.toString() + ldColor + thisMove.getDestLoc().toString();
                        thisMove.setSubmove(ChessFactory.CreateMove(promotionString));
                        
                    }
                }
                

                this.addMove(thisMove, true);

                
            } else {
                System.out.println("sakjdfadkjsfjdsa");
            }
            // this.view.requestInput();
        }

        this.view.printGameStatus(currentGameStatus);
    }

    public void loadNewBoard() {
        loadFromFile("newBoard.txt");
    }

}
