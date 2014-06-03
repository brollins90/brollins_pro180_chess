package chess_rollins_blake.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
    JLabel messageLabel, whiteInCheckLabel, blackInCheckLabel, turnLabel;
    public BoardPanel boardPanel;
    protected JList moveList;
    BoardLocation previousModelStateLocation;
    JFrame frame;
    private GameStatus currentGameStatus;


    private Scanner scan;

    public GUIView(ChessModel model) {
        super(model);
        currentGameStatus = GameStatus.PLAYING;
        scan = new Scanner(System.in);
        previousModelStateLocation = BoardLocation.none;

        frame = new JFrame();
        JPanel outerPanel = new JPanel(new BorderLayout());

        JPanel upperPanel = new JPanel(new GridLayout(1, 3));
        // White in check
        whiteInCheckLabel = new JLabel(null, null, JLabel.CENTER);
        whiteInCheckLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        whiteInCheckLabel.setHorizontalTextPosition(JLabel.CENTER);
        upperPanel.add(whiteInCheckLabel);

        upperPanel.add(new JLabel(null, null, JLabel.CENTER));
        // Black in check
        blackInCheckLabel = new JLabel(null, null, JLabel.CENTER);
        blackInCheckLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        blackInCheckLabel.setHorizontalTextPosition(JLabel.CENTER);
        upperPanel.add(blackInCheckLabel);
        outerPanel.add(upperPanel, BorderLayout.NORTH);


        JPanel midPanel = new JPanel();
        this.boardPanel = new BoardPanel(super.model);
        this.boardPanel.setPreferredSize(new Dimension(600, 600));

        midPanel.add(boardPanel);
        outerPanel.add(midPanel);



        JPanel lowerPanel = new JPanel(new GridLayout(2, 1));
        // Status
        turnLabel = new JLabel(null, null, JLabel.CENTER);
        turnLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        turnLabel.setHorizontalTextPosition(JLabel.CENTER);
        lowerPanel.add(turnLabel);
        // Status
        messageLabel = new JLabel(null, null, JLabel.CENTER);
        messageLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        messageLabel.setHorizontalTextPosition(JLabel.CENTER);
        lowerPanel.add(messageLabel);
        outerPanel.add(lowerPanel, BorderLayout.SOUTH);
        // this.moveList = new JList<ChessMove>();
        // upperPanel.add(moveList);
        // outerPanel.add(upperPanel);

        // c.fill = GridBagConstraints.HORIZONTAL;
        // c.gridx = 4;
        // c.gridy = 0;
        // outerPanel.add(new JPanel(), c);


        frame.add(outerPanel);


        frame.setSize(840, 800);
        // frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // this.boardPanel.setModel(super.model);

    }

    @Override
    public void update(Observable obs, Object obj) {
        // BoardLocation currentModelStateLocation = this.model.getCurrentModelStateLocation();
        if (obj instanceof GameStatus) {
            this.currentGameStatus = (GameStatus) obj;
            if (currentGameStatus != GameStatus.PLAYING) {
                System.out.println("observed not playing");
                // this.boardPanel.setGameStatus(currentGameStatus);

                String messageString = "";

                if (currentGameStatus == GameStatus.DARKWIN) {
                    messageString += "Black Player wins.";
                } else if (currentGameStatus == GameStatus.DARKFORFEIT) {
                    messageString += "White Player wins, Black forfeit";
                } else if (currentGameStatus == GameStatus.LIGHTWIN) {
                    messageString += "White Player wins.";
                } else if (currentGameStatus == GameStatus.LIGHTFORFEIT) {
                    messageString += "Black Player wins, White forfeit";
                } else if (currentGameStatus == GameStatus.STALEMATE) {
                    messageString += "Stalemate";
                }

                // g.setColor(Color.black);
                // g.drawString(messageString, 200, 200);
                messageLabel.setFont(new Font("Serif", Font.BOLD, 20));
                messageLabel.setText(messageString);
            }

        } else if (obj instanceof String) {
            this.messageLabel.setText((String) obj);
        } else {
            // this.messageLabel.setText("ERROR: Invalid message.");
        }
        if (currentGameStatus == GameStatus.PLAYING) {
            updateBoard();
        }
        // }
        if (this.model.isBlackKingInCheck()) {
            blackInCheckLabel.setText("Black King is in check!");
            blackInCheckLabel.setFont(new Font("Serif", Font.BOLD, 20));
        } else {
            blackInCheckLabel.setText("Black King is not in check!");
            blackInCheckLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        }
        if (this.model.isWhiteKingInCheck()) {
            whiteInCheckLabel.setText("White King is in check!");
            whiteInCheckLabel.setFont(new Font("Serif", Font.BOLD, 20));
        } else {
            whiteInCheckLabel.setText("White King not is in check!");
            whiteInCheckLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        }
        
        if (this.model.isWhiteTurn()) {
            turnLabel.setText("White Turn");
        } else {
            turnLabel.setText("Black Turn");
        }

        // this.moveList
    }

    public void updateBoard() {
        this.boardPanel.updateUI();
        this.boardPanel.repaint();
    }

    @Override
    public BoardLocation requestSourcePiece() {
        // boardPanel.setAvailableDestinations(new HashSet<BoardLocation>());
        // update();


        boolean curTurnIsWhite = this.model.isWhiteTurn();
        System.out.println("-- " + pieceColorDisplayMap.get(curTurnIsWhite) + " Player's turn--");
        // System.out.println("Enter the which piece you would like to move?");

        HashSet<ChessMove> moves = this.model.getAvailableMoves();
        HashSet<BoardLocation> srcs = new HashSet<BoardLocation>();
        for (ChessMove m : moves) {
            srcs.add(m.getSrcLoc());
        }

        // boardPanel.setAvailableSources(srcs);
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


        // boardPanel.setAvailableDestinations(dests);
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
    // @Override
    // public void setModel(ChessModel m) {
    // this.model = m;
    // this.boardPanel.setModel(this.model);
    // }

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
