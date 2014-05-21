package chess_rollins_blake.controller;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.model.ChessFactory;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.model.pieces.Piece;
import chess_rollins_blake.view.ChessView;

public class ChessController implements java.awt.event.ActionListener {

    protected ChessModel model;
    protected ChessView view;
    protected boolean gameIsPlaying;

    public ChessController() {
        this.gameIsPlaying = true;

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // TODO
        // Add some obj validation

        String theCommand = event.getActionCommand();
        switch (MoveType.values()[event.getID()]) {
            case ADD:
                break;
            case MOVE:
            case CAPTURE:
                addMove(theCommand);
                break;
            case LOCATION: // Check available moves
                // this.model.setAvailableDestinations(BoardLocation.valueOf(theCommand));
                break;
        }

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

    public void addMove(ChessMove m) {
        this.model.addMove(m);
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

    public void startGameLoop() {


        // Start the game Loop
        while (this.gameIsPlaying) {


            // Get all the pieces for this player that have moves.
            ArrayList<BoardLocation> piecesThatCanMove = this.model.getLocationsThatCanMove();
            ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

            boolean wasKingInCheck = this.model.isCurrentInCheck();
            // if (this.model.isCurrentInCheck()) {
            ArrayList<ChessMove> movesThatCanGetOutOfCheck = new ArrayList<ChessMove>();
            // current player is in check, we need to get out of it
            for (BoardLocation pieceThatCanMove : piecesThatCanMove) {
                ArrayList<BoardLocation> destinationsForThisPiece = this.model.getAvailableDestinationsFromLocation(pieceThatCanMove);

                for (BoardLocation destination : destinationsForThisPiece) {

                    String moveString = pieceThatCanMove + " " + destination;
                    if (this.model.locationHasPiece(destination)) {
                        moveString += "*";
                    }
                    ChessMove testMove = ChessFactory.CreateMove(moveString);
                    try {
                        this.addMove(testMove);
                        this.model.checkKingInCheck(this.model.isWhiteTurn());
                        if (wasKingInCheck && !this.model.isOtherInCheck()) {
                            movesThatCanGetOutOfCheck.add(testMove);
                        }
                        moves.add(testMove);
                        this.model.undoMove();
                    } catch (ChessException e) {
                        System.out.println("ERROR");
                    }

                }

            }



            this.model.setAvailableMoves(moves);
            if (wasKingInCheck) {

                if (movesThatCanGetOutOfCheck.size() == 0) {
                    System.out.println("Stalemate or checkmate");
                }
                for (ChessMove m : movesThatCanGetOutOfCheck) {
                    System.out.println(m.getMoveString());

                }

                this.model.setAvailableMoves(movesThatCanGetOutOfCheck);
            }



            // } // current is not in check
            // else {
            // }



            // this.model.setAvailableSources(this.model.isWhiteTurn());
            BoardLocation src = this.view.requestSourcePiece();

            BoardLocation dest = this.view.requestDestinationPiece(src);

            String moveString = src + " " + dest;
            Piece destPiece = this.model.currentBoard.get(dest);
            if (destPiece != null && destPiece.isWhite() != this.model.isWhiteTurn()) {
                moveString += "*";
            }

            ChessMove thisMove = ChessFactory.CreateMove(moveString);
            this.addMove(moveString);


            // this.view.requestInput();
        }
    }

}
