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
import chess_rollins_blake.lib.MovingMove;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.lib.PromotionMove;
import chess_rollins_blake.model.ChessFactory;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.model.pieces.Pawn;
import chess_rollins_blake.model.pieces.Piece;
import chess_rollins_blake.view.ChessView;
import chess_rollins_blake.view.GUIView;

public class ChessController implements java.awt.event.ActionListener {

    int VIEW_WIDTH = 75;
    int VIEW_HEIGHT = 75;

    protected ChessModel model;
    protected ChessView view;
    protected GameStatus currentGameStatus;
    protected BoardLocation previousModelStateLocation;


    public ChessController(ChessModel model, ChessView view) {
        this.model = model;
        this.view = view;

        previousModelStateLocation = BoardLocation.none;
        this.view.addBoardListener(new LocationListener());
        this.view.addBoardMotionListener(new LocationListener());
    }

    class LocationListener implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            BoardLocation mouseLoc = getMouseLoc(e);

            if (!model.getCurrentTurnSourceSet()) {
                if (model.getAvailableSources().contains(mouseLoc)) {
                    model.setCurrentTurnSourceSet();
                } else {
                    // System.out.println("cur mouse loc is NOT in the list of srcs");
                }
            } else {
                // set the dest and perform the action
                if (model.getAvailableDestinationsFromLocationInMoveCache(previousModelStateLocation).contains(mouseLoc)) {
                    String moveString = previousModelStateLocation + " " + mouseLoc;

                    boolean currentTurn = model.isWhiteTurn();
                    HashSet<BoardLocation> otherTeamsPieces = model.getPlayersPieces(!currentTurn);

                    if (otherTeamsPieces.contains(mouseLoc)) {
                        moveString += "*";
                    }


                    Piece srcPiece = model.getPiece(previousModelStateLocation);
                    ChessMove thisMove = ChessFactory.CreateMove(moveString);


                    if (srcPiece instanceof Pawn) {
                        if (((Pawn) srcPiece).isInEigthRow(mouseLoc)) {
                            PieceType pawnPromotionType = view.requestPawnPromotion();
                            thisMove.setChangeTurnAfter(false);
                            // "a1 qda1"
                            char ldColor = (srcPiece.isWhite()) ? 'l' : 'd';
                            String promotionString = thisMove.getDestLoc().toString() + " " + pawnPromotionType.toString() + ldColor + thisMove.getDestLoc().toString();
                            thisMove.setSubmove(ChessFactory.CreateMove(promotionString));

                        }
                    }
                    
                    
                    addMove(thisMove, true);
                    
                    String outString = moveString;
                    if (thisMove.getSubMove() != null) {
                        if (thisMove.getSubMove() instanceof MovingMove) {
                            // castling so print it out
                            outString += " " + thisMove.getSubMove().getMoveString();
                        } else if (thisMove.getSubMove() instanceof PromotionMove) {
                            // promotion, dont print cause it is made up
//                            outString += " " + thisMove.getSubMove().getMoveString();
                        }
                    }
                    
                    System.out.println(outString);

                    // Check for game over
                    if (model.getAvailableMoves().size() == 0) {
                        System.out.println("GAME OVER");
                    }


                } else {
                    // System.out.println("cur mouse loc is NOT in the list of dests");
                }
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            BoardLocation mouseLoc = getMouseLoc(e);

            if (previousModelStateLocation != mouseLoc && !model.getCurrentTurnSourceSet()) {
                previousModelStateLocation = mouseLoc;
                updateModelForLocation(mouseLoc);
            }
        }

        private BoardLocation getMouseLoc(MouseEvent e) {

            int mouseX = e.getX();
            int mouseY = e.getY();

            int row = 7 - (mouseY / VIEW_HEIGHT);
            int col = mouseX / VIEW_WIDTH;

            return BoardLocation.getLocFromRowAndColumn(row, col);
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
        ConsoleChess.debugMessage("ChessController.updateModelForLocation(" + loc + ")");
        this.model.setCurrentModelStateLocation(loc);
        view.update();
    }

    public void addMove(String moveString, boolean updateObservers) {
        ConsoleChess.debugMessage("ChessController.addMove(" + moveString + "," + updateObservers + ")");
        this.model.addMove(moveString, updateObservers);
    }

    public void addMove(ChessMove move, boolean updateObservers) {
        ConsoleChess.debugMessage("ChessController.addMove(" + move.getMoveString() + "," + updateObservers + ")");
        this.model.addMove(move, updateObservers);
    }

    public void addMoveWithoutUpdate(ChessMove m) {
        ConsoleChess.debugMessage("ChessController.addMoveWithoutUpdate(" + m.getMoveString() + ")");
        this.model.addMoveWithoutUpdate(m);
    }

    public void addMoveWithoutUpdate(String m) {
        ConsoleChess.debugMessage("ChessController.addMoveWithoutUpdate(" + m + ")");
        this.model.addMoveWithoutUpdate(m);
    }

    public void loadFromFile(String filePath) {
        ConsoleChess.debugMessage("ChessController.loadFromFile(" + filePath + ")");

        if (filePath != null) {
            // boolean isNewBoard = (filePath.equals("newBoard.txt")) ? true : false;
            BufferedReader br = null;
            try {
                String path = filePath;
                br = new BufferedReader(new FileReader(path));

                String line = "";
                while ((line = br.readLine()) != null) {
                    ConsoleChess.debugMessage("line: " + line);
                    try {
                        actionPerformed(new ActionEvent(this, MoveType.ADD.ordinal(), line));
                        // addMove(line, !isNewBoard);
                    } catch (ChessException e) {
                        this.model.setModelStatusMessage(e.getMessage());
                    }
                }
                br.close();

            } catch (FileNotFoundException e) {

                ConsoleChess.debugMessage("file not found: " + e.getMessage());
                throw new ChessException("Unable to open the file at: " + filePath);
            } catch (IOException e) {
                ConsoleChess.debugMessage("io exception: " + e.getMessage());
                throw new ChessException("IOException when trying to read the file at: " + filePath);
            }
        } else {
            throw new ChessException("The filepath is not set");
        }

    }

    public void playChess() {
        ConsoleChess.debugMessage("ChessController.playChess()");

        this.currentGameStatus = GameStatus.PLAYING;

        this.model.resetView();

        // Start the game Loop
        if (this.view instanceof GUIView) {

            while (this.currentGameStatus == GameStatus.PLAYING) {
                //HashSet<ChessMove> availableMoves = this.model.getAvailableMoves();
                
                if (this.model.getAvailableMoves().size() == 0) {
                    this.currentGameStatus = (!model.isCurrentInCheck()) ? GameStatus.STALEMATE : (this.model.isWhiteTurn()) ? GameStatus.DARKWIN : GameStatus.LIGHTWIN;
                }
                
                if (this.view instanceof GUIView) {
                    this.view.update(null, currentGameStatus);
                
                } else {

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
                    // Piece newDestPiece = this.model.getPiece(dest);
                    if (srcPiece instanceof Pawn) {
                        if (((Pawn) srcPiece).isInEigthRow(dest)) {
                            PieceType pawnPromotionType = this.view.requestPawnPromotion();
                            thisMove.setChangeTurnAfter(false);
                            // "a1 qda1"
                            char ldColor = (srcPiece.isWhite()) ? 'l' : 'd';
                            String promotionString = thisMove.getDestLoc().toString() + " " + pawnPromotionType.toString() + ldColor + thisMove.getDestLoc().toString();
                            thisMove.setSubmove(ChessFactory.CreateMove(promotionString));

                        }
                    }


                    this.addMove(thisMove, true);


                }
            }
        }

        this.view.printGameStatus(currentGameStatus);
    }

    public void loadNewBoard() {
        loadFromFile("newBoard.txt");
    }

}
