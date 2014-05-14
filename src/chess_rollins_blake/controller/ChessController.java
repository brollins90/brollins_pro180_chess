package chess_rollins_blake.controller;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.ChessFactory;
import chess_rollins_blake.model.ChessException;
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

    public void loadFromFile(String filePath) {

        if (filePath != null) {
            BufferedReader br = null;
            try {
                String path = filePath;
                // System.out.println(path);
                br = new BufferedReader(new FileReader(path));

                String line = "";
                while ((line = br.readLine()) != null) {
                     //System.out.println(line);
//
//                    try {
//                        if (model.addMove(line)) {
//                            this.view.update();
//                            this.model.clearMessage();
//                        }
//                    } catch (ChessException e) {
//                        this.model.message = "ERROR: " + e.getMessage();
//                        this.view.update();
//                        this.model.message = "";
//                    }
                    
                    model.addMove(line);
                    
                }

                br.close();

            } catch (FileNotFoundException e) {
                System.out.println("Unable to open the file at: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The file is not set...");
        }

    }

}
