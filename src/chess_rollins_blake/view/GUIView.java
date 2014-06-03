package chess_rollins_blake.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import chess_rollins_blake.controller.GameStatus;
import chess_rollins_blake.lib.BoardLocation;
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

    public GUIView(ChessModel model) {
        super(model);
        currentGameStatus = GameStatus.PLAYING;
        previousModelStateLocation = BoardLocation.none;


        // Create the Frame
        frame = new JFrame();
        JPanel outerPanel = new JPanel(new BorderLayout());
        JPanel upperPanel = new JPanel(new GridLayout(1, 2));

        // Create the top panel
        // White in check
        whiteInCheckLabel = new JLabel(null, null, JLabel.CENTER);
        whiteInCheckLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        whiteInCheckLabel.setHorizontalTextPosition(JLabel.CENTER);
        upperPanel.add(whiteInCheckLabel);
        // Black in check
        blackInCheckLabel = new JLabel(null, null, JLabel.CENTER);
        blackInCheckLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        blackInCheckLabel.setHorizontalTextPosition(JLabel.CENTER);
        upperPanel.add(blackInCheckLabel);
        outerPanel.add(upperPanel, BorderLayout.NORTH);


        // Create the middle panel
        JPanel midPanel = new JPanel();
        this.boardPanel = new BoardPanel(super.model);
        this.boardPanel.setPreferredSize(new Dimension(600, 600));

        midPanel.add(boardPanel);
        outerPanel.add(midPanel);


        // Create the bottom panel
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


        frame.add(outerPanel);
        frame.setSize(840, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void update(Observable obs, Object obj) {

        if (obj instanceof GameStatus) {
            this.currentGameStatus = (GameStatus) obj;
            if (currentGameStatus != GameStatus.PLAYING) {

                messageLabel.setText(super.gameStatusDisplayMap.get(currentGameStatus));
                messageLabel.setFont(new Font("Serif", Font.BOLD, 20));
                // String messageString = "";
                //
                // if (currentGameStatus == GameStatus.DARKWIN) {
                // messageString += "Black Player wins.";
                // } else if (currentGameStatus == GameStatus.DARKFORFEIT) {
                // messageString += "White Player wins, Black forfeit";
                // } else if (currentGameStatus == GameStatus.LIGHTWIN) {
                // messageString += "White Player wins.";
                // } else if (currentGameStatus == GameStatus.LIGHTFORFEIT) {
                // messageString += "Black Player wins, White forfeit";
                // } else if (currentGameStatus == GameStatus.STALEMATE) {
                // messageString += "Stalemate";
                // }
                // messageLabel.setFont(new Font("Serif", Font.BOLD, 20));
                // messageLabel.setText(messageString);
            }

        } else if (obj instanceof String) {
            this.messageLabel.setText((String) obj);
        }

        if (currentGameStatus == GameStatus.PLAYING) {
            this.boardPanel.updateUI();
            this.boardPanel.repaint();
        }

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
    }


    @Override
    public BoardLocation requestSourcePiece() {
        return null;
    }

    @Override
    public BoardLocation requestDestinationPiece(BoardLocation srcLoc) {
        return null;
    }

    @Override
    public PieceType requestPawnPromotion() {
        return PieceType.q;
    }

    @Override
    public void printGameStatus(GameStatus status) {}

    @Override
    public void addBoardListener(MouseListener l) {
        this.boardPanel.addMouseListener(l);
    }

    @Override
    public void addBoardMotionListener(MouseMotionListener l) {
        this.boardPanel.addMouseMotionListener(l);
    }


}
