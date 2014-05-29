package chess_rollins_blake.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chess_rollins_blake.controller.GameStatus;
import chess_rollins_blake.exceptions.ChessException;
import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.ChessMove;
import chess_rollins_blake.lib.MoveType;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.ChessModel;

public class GUIView extends ChessView {

    JButton[][] boardSquares = new JButton[8][8];
    JLabel messageLabel, whiteInCheckLabel, blackInCheckLabel;
    public BoardPanel boardPanel;
    

    private Scanner scan;

    public GUIView(ChessModel model) {
        super(model);
        scan = new Scanner(System.in);


        JFrame frame = new JFrame();
        JPanel outerPanel = new JPanel();
//        JPanel outerPanel = new JPanel(new GridBagLayout());
//        GridBagConstraints c = new GridBagConstraints();
        this.boardPanel = new BoardPanel(super.model);
        this.boardPanel.setPreferredSize(new Dimension(600, 600));
        
        //c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 0;
//        c.gridy = 0;
//        c.gridwidth = 3;
        outerPanel.add(boardPanel);
        
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 4;
//        c.gridy = 0;
//        outerPanel.add(new JPanel(), c);
        
        // White in check
        whiteInCheckLabel = new JLabel();
        outerPanel.add(whiteInCheckLabel);

        // Status
        messageLabel = new JLabel();
        messageLabel.setText("POOP");
        outerPanel.add(messageLabel);

        // Black in check
        blackInCheckLabel = new JLabel();
        outerPanel.add(blackInCheckLabel);


        frame.add(outerPanel);


        frame.setSize(840, 800);
        // frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //this.boardPanel.setModel(super.model);
        
    }

    @Override
    public void update(Observable obs, Object obj) {
        if (obj instanceof String) {
            this.messageLabel.setText((String) obj);
        } else {
            this.messageLabel.setText("ERROR: Invalid message.");
        }
        updateBoard();
        if (this.model.isBlackKingInCheck()) {
            blackInCheckLabel.setText("Black King is in check!");
        } else {
            blackInCheckLabel.setText("Black King is not in check!");
        }
        if (this.model.isWhiteKingInCheck()) {
            whiteInCheckLabel.setText("White King is in check!");
        } else {
            whiteInCheckLabel.setText("White King not is in check!");
        }
    }

    public void updateBoard() {
        this.boardPanel.updateUI();
        this.boardPanel.repaint();
    }

    @Override
    public BoardLocation requestSourcePiece() {
        //boardPanel.setAvailableDestinations(new HashSet<BoardLocation>());
        // update();


        boolean curTurnIsWhite = this.model.isWhiteTurn();
        System.out.println("-- " + pieceColorDisplayMap.get(curTurnIsWhite) + " Player's turn--");
        // System.out.println("Enter the which piece you would like to move?");

        HashSet<ChessMove> moves = this.model.getAvailableMoves();
        HashSet<BoardLocation> srcs = new HashSet<BoardLocation>();
        for (ChessMove m : moves) {
            srcs.add(m.getSrcLoc());
        }

        boardPanel.setAvailableSources(srcs);
        update();



        boolean validSource = false;
        BoardLocation loc = null;
        do {
            String locationString = readLine();
            loc = BoardLocation.valueOf(locationString.trim());

            for (BoardLocation l : srcs) {
                if (l == loc) {
                    validSource = true;
                }
            }
            if (!validSource) {
                System.out.println("Location is not valid.  Enter a new location.");
            }
        } while (!validSource);

        ActionEvent event = new ActionEvent(this, MoveType.LOCATION.ordinal(), loc.toString());

        try {
            super.sendRequestToController(event);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
        }
        return loc;



        // return null;
        // TODO Auto-generated method stub

    }

    @Override
    public BoardLocation requestDestinationPiece(BoardLocation srcLoc) {


        // System.out.println("Select destination from: ");

        HashSet<ChessMove> moves = this.model.getAvailableMoves();
        HashSet<ChessMove> movesFromSrc = new HashSet<ChessMove>();
        HashSet<BoardLocation> dests = new HashSet<BoardLocation>();

        for (ChessMove m : moves) {
            if (m.getSrcLoc() == srcLoc) {
                movesFromSrc.add(m);
                dests.add(m.getDestLoc());
            }
        }


//        boardPanel.setAvailableDestinations(dests);
        update();


        // System.out.print("(");
        // for (int i = 0; i < movesFromSrc.size(); i++) {
        // System.out.print(movesFromSrc.get(i).getDestLoc().toString());
        // if (i + 1 < movesFromSrc.size()) {
        // System.out.print(",");
        // }
        // }
        // System.out.println(")");

        BoardLocation loc = null;
        boolean moveIsValidSyntax = false;
        while (!moveIsValidSyntax) {
            String tempCommand = readLine();
            try {
                loc = BoardLocation.valueOf(tempCommand.trim());
            } catch (IllegalArgumentException e) {
                
            }
            for (ChessMove m : movesFromSrc) {
                if (m.getDestLoc() == loc) {
                    moveIsValidSyntax = true;
                }
            }
            if (!moveIsValidSyntax) {
                System.out.println(tempCommand + " is not a valid destination.  Try again.");
            }
        }
        return loc;

    }

    @Override
    public PieceType requestPawnPromotion() {
        // TODO Auto-generated method stub
        return PieceType.q;
    }

    @Override
    public void printGameStatus(GameStatus status) {
        messageLabel.setText(status.toString());

    }
//
//    @Override
//    public void setModel(ChessModel m) {
//        this.model = m;
//        this.boardPanel.setModel(this.model);
//    }

    private String readLine() {
        return scan.nextLine();
    }

    @Override
    public void addBoardListener(MouseListener l) {
        this.boardPanel.addMouseListener(l);
    }

    @Override
    public void addBoardMotionListener(MouseMotionListener l) {
        this.boardPanel.addMouseMotionListener(l);
    }


}
