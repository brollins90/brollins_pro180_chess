package chess_rollins_blake.view;

import java.awt.event.ActionListener;
import java.util.Observable;

import chess_rollins_blake.controller.ChessController;
import chess_rollins_blake.model.ChessModel;

public class ChessView implements java.util.Observer {
    
    protected ChessModel model;

    private ActionListener c;
    
    public ChessView() {
//        System.out.println("ChessView()");
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
}
