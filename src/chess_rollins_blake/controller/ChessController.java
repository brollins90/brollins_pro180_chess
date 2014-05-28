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
import chess_rollins_blake.model.ChessFactory;
import chess_rollins_blake.model.ChessModel;
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
            //ConsoleChess.debugMessage("mouseMoved");  
            int mouseX = e.getX();
            int mouseY = e.getY();
            
            int row = 7 - (mouseY / VIEW_HEIGHT);
            int col = mouseX / VIEW_WIDTH;

//            System.out.println(mouseX + " " + mouseY);
//            System.out.println(row + " " + col);
            
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
            case MOVE:
            case CAPTURE:
                addMove(theCommand, true);
                break;
            case LOCATION: // Check available moves
                //this.model.setAvailableDestinations(BoardLocation.valueOf(theCommand));
                break;
        }

    }

//    public void addModel(ChessModel m) {
//        this.model = m;
//    }
//
//    public void addView(ChessView v) {
//        this.view = v;
//    }
    
    
    public void updateModelForLocation(BoardLocation loc) {
        
        //System.out.println("mouse at " + loc);
        this.model.setCurrentModelState(loc);
//
//        GUIView gview = (GUIView) view;
//        //gview.boardPanel.setAvailableDestinations(new HashSet<BoardLocation>());
//        // update();
//
//
//        boolean curTurnIsWhite = model.isWhiteTurn();
////        System.out.println("-- " + pieceColorDisplayMap.get(curTurnIsWhite) + " Player's turn--");
////        // System.out.println("Enter the which piece you would like to move?");
//
//        ArrayList<ChessMove> moves = model.getAvailableMoves();
//        HashSet<BoardLocation> srcs = model.getAvailableSources();
//
//        gview.boardPanel.setAvailableSources(srcs);
        view.update();


//
//        boolean validSource = false;
//        BoardLocation loc = null;
//        do {
//            String locationString = readLine();
//            loc = BoardLocation.valueOf(locationString.trim());
//
//            for (BoardLocation l : srcs) {
//                if (l == loc) {
//                    validSource = true;
//                }
//            }
//            if (!validSource) {
//                System.out.println("Location is not valid.  Enter a new location.");
//            }
//        } while (!validSource);
//
//        ActionEvent event = new ActionEvent(this, MoveType.LOCATION.ordinal(), loc.toString());
//
//        try {
//            super.sendRequestToController(event);
//        } catch (ChessException e) {
//            System.out.println(e.getMessage());
//        }
//        return loc;
//
//        
//        
    }
    
    

    public void addMove(String moveString, boolean updateObservers) {
        this.model.addMove(moveString, updateObservers);
    }

    public void addMoveWithoutUpdate(ChessMove m) {
        this.model.addMoveWithoutUpdate(m);
    }

    public void addMoveWithoutUpdate(String m) {
        this.model.addMoveWithoutUpdate(m);
    }

    public void loadFromFile(String filePath) {

        if (filePath != null) {
            boolean isNewBoard = (filePath.equals("newBoard.txt")) ? true : false;
            BufferedReader br = null;
            try {
                String path = filePath;
                br = new BufferedReader(new FileReader(path));

                String line = "";
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    try {
                        actionPerformed(new ActionEvent(this, MoveType.ADD.ordinal(), line));
                        //addMove(line, !isNewBoard);
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


            // Get all the pieces for this player that have moves.
            HashSet<BoardLocation> piecesThatCanMove = this.model.getLocationsThatCanMove();
            HashSet<ChessMove> moves = new HashSet<ChessMove>();

            boolean wasKingInCheck = this.model.isCurrentInCheck();
            // if (this.model.isCurrentInCheck()) {
            HashSet<ChessMove> movesThatCanGetOutOfCheck = new HashSet<ChessMove>();
            // current player is in check, we need to get out of it
            for (BoardLocation pieceThatCanMove : piecesThatCanMove) {
                HashSet<BoardLocation> destinationsForThisPiece = this.model.getAvailableDestinationsFromLocation(pieceThatCanMove);

                for (BoardLocation destination : destinationsForThisPiece) {

                    String moveString = pieceThatCanMove + " " + destination;
                    if (this.model.locationHasPiece(destination)) {
                        moveString += "*";
                    }
                    ChessMove testMove = ChessFactory.CreateMove(moveString);
                    try {
                        testMove.setChangeTurnAfter(false);
                        this.addMoveWithoutUpdate(testMove);
                        this.model.isThisKingInCheck(this.model.isWhiteTurn());
                        if (wasKingInCheck && !this.model.isOtherInCheck()) {
                            movesThatCanGetOutOfCheck.add(testMove);
                        }
                        moves.add(testMove);
                        this.model.undoMove();
                    } catch (ChessException e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }

                }

            }



            this.model.setAvailableMoves(moves);
            if (wasKingInCheck) {

                if (movesThatCanGetOutOfCheck.size() == 0) {
                    this.currentGameStatus = this.model.isWhiteTurn() ? GameStatus.DARKWIN : GameStatus.LIGHTWIN;
                    // System.out.println("Stalemate or checkmate");
                }
                for (ChessMove m : movesThatCanGetOutOfCheck) {
                    System.out.println(m.getMoveString());

                }

                this.model.setAvailableMoves(movesThatCanGetOutOfCheck);
            }



            // } // current is not in check
            // else {
            // }

            if (this.currentGameStatus == GameStatus.PLAYING) {

                // this.model.setAvailableSources(this.model.isWhiteTurn());
                BoardLocation src = null;
                while (src == null) {
                    src = this.view.requestSourcePiece();
                }

                BoardLocation dest = null;
                while (dest == null) {
                    dest = this.view.requestDestinationPiece(src);
                }

                String moveString = src + " " + dest;
                Piece destPiece = this.model.getPiece(dest);
                if (destPiece != null && destPiece.isWhite() != this.model.isWhiteTurn()) {
                    moveString += "*";
                }

                ChessMove thisMove = ChessFactory.CreateMove(moveString);
                this.addMove(moveString, true);

            }
            // this.view.requestInput();
        }

        this.view.printGameStatus(currentGameStatus);
    }

    public void loadNewBoard() {
        loadFromFile("newBoard.txt");
    }

}
