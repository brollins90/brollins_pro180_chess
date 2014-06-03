package chess_rollins_blake.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Observable;

import chess_rollins_blake.controller.GameStatus;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.ChessModel;

public abstract class ChessView implements java.util.Observer {

    protected ChessModel model;
    protected HashMap<PieceType, String> pieceDisplayMap;
    protected HashMap<Boolean, String> pieceColorDisplayMap;
    protected HashMap<GameStatus, String> gameStatusDisplayMap;

    protected ActionListener theController;

    public ChessView(ChessModel model) {
        this.model = model;
        pieceDisplayMap = new HashMap<>();
        pieceDisplayMap.put(PieceType.b, "Bishop");
        pieceDisplayMap.put(PieceType.k, " King ");
        pieceDisplayMap.put(PieceType.n, "Knight");
        pieceDisplayMap.put(PieceType.p, " Pawn ");
        pieceDisplayMap.put(PieceType.q, "Queen ");
        pieceDisplayMap.put(PieceType.r, " Rook ");

        pieceColorDisplayMap = new HashMap<>();
        pieceColorDisplayMap.put(false, "Black");
        pieceColorDisplayMap.put(true, "White");

        gameStatusDisplayMap = new HashMap<GameStatus, String>();
        gameStatusDisplayMap.put(GameStatus.DARKFORFEIT, pieceColorDisplayMap.get(true) + " player wins because " + pieceColorDisplayMap.get(false) + " player forfeit.");
        gameStatusDisplayMap.put(GameStatus.DARKWIN, "Checkmate.\n" + pieceColorDisplayMap.get(false) + " player wins.");
        gameStatusDisplayMap.put(GameStatus.LIGHTFORFEIT, pieceColorDisplayMap.get(false) + " player wins because " + pieceColorDisplayMap.get(true) + " player forfeit.");
        gameStatusDisplayMap.put(GameStatus.LIGHTWIN, "Checkmate.\n" + pieceColorDisplayMap.get(true) + " player wins.");
        gameStatusDisplayMap.put(GameStatus.PLAYING, "Game is in progress.");
        gameStatusDisplayMap.put(GameStatus.STALEMATE, "Tie, Stalemate");

    }

    public void update() {
        update(this.model, null);
    }

    @Override
    public void update(Observable obs, Object obj) {
        // TODO Auto-generated method stub

    }

    public void addController(ActionListener c) {
        this.theController = c;
    }
//
//    public void setModel(ChessModel m) {
//        this.model = m;
//    }

//    public abstract void requestInput();

    public void sendRequestToController(ActionEvent e) {

        theController.actionPerformed(e);
    }

    public abstract void addBoardListener(MouseListener l);
    public abstract void addBoardMotionListener(MouseMotionListener l);
    public abstract void printGameStatus(GameStatus status);
    public abstract BoardLocation requestSourcePiece();
    public abstract BoardLocation requestDestinationPiece(BoardLocation srcLoc);
    //public abstract void requestContinue();

    public abstract PieceType requestPawnPromotion(); 
}
