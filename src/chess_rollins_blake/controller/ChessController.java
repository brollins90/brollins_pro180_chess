package chess_rollins_blake.controller;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveFactory;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.view.ChessView;

public class ChessController implements java.awt.event.ActionListener {

    private ChessModel model;
    protected ChessView view;

    public ChessController() {
//        System.out.println("ChessController()");
    }

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

    public void loadModelFromFile(String filePath) {
                
        if (filePath != null) {
            BufferedReader br = null;
            try {
                String path = filePath;
                //System.out.println(path);
                br = new BufferedReader(new FileReader(path));

                String line = "";
                while ((line = br.readLine()) != null) {
                    // System.out.println(line);
                    ChessMove thisMove = MoveFactory.CreateMove(line);
                    if (thisMove != null) {
                        model.addMove(thisMove);
                        this.view.update();
                        this.model.message = "";
                    }
                    //
                    // String[] parts = line.split(",");
                    // if (parts.length == 4) {
                    // this.add(parts[0], parts[1], parts[2], parts[3], false);
                    // } else {
                    // // System.out.println("Bad line.  Skipping");
                    // }
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
