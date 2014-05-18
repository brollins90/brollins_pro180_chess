package chess_rollins_blake.controller;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.model.ChessFactory;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.view.ChessView;

public class ChessController implements java.awt.event.ActionListener {

    private ChessModel model;
    protected ChessView view;

    public ChessController() {}

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void addModel(ChessModel m) {
        this.model = m;
    }

    public void addView(ChessView v) {
        this.view = v;
    }

    public void addMove(String moveString) {
        this.model.addMove(moveString);
    }

    public void loadFromFile(String filePath) {

        if (filePath != null) {
            BufferedReader br = null;
            try {
                String path = filePath;
                br = new BufferedReader(new FileReader(path));

                String line = "";
                while ((line = br.readLine()) != null) {
                    // System.out.println(line);
                    try {
                        addMove(line);
                    } catch (ChessException e) {
                        this.model.setMessage(e.getMessage());
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

}
