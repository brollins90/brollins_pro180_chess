package chess_rollins_blake.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import chess_rollins_blake.controller.GameStatus;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.model.ChessModel;

public abstract class ChessView implements java.util.Observer {

    protected ChessModel model;

    private ActionListener c;

    public ChessView() {
        // System.out.println("ChessView()");
    }

    public void update() {

    }

    @Override
    public void update(Observable obs, Object obj) {
        // TODO Auto-generated method stub

    }

    public void addController(ActionListener c) {
        this.c = c;
    }

    public void setModel(ChessModel m) {
        this.model = m;
    }

//    public abstract void requestInput();

    public void sendRequestToController(ActionEvent e) {

        c.actionPerformed(e);
    }

    public abstract void printGameStatus(GameStatus status);
    public abstract BoardLocation requestSourcePiece();
    public abstract BoardLocation requestDestinationPiece(BoardLocation srcLoc);
    //public abstract void requestContinue();
}
