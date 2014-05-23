package chess_rollins_blake.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import chess_rollins_blake.model.pieces.Piece;

public class GUIView extends ChessView {

    JButton[][] boardSquares = new JButton[8][8];
    BufferedImage[][] images = new BufferedImage[2][6];
    JLabel messageLabel, whiteInCheckLabel, blackInCheckLabel;


    public GUIView() {
        JFrame frame = new JFrame();
        JPanel outerPanel = new JPanel();
        JPanel boardPanel = new JPanel((new GridLayout(8, 8)));

        for (int rowIndex = 7; rowIndex >= 0; rowIndex--) {
            for (int colindex = 0; colindex < 8; colindex++) {
                JButton temp = new JButton();
                temp.setMinimumSize(new Dimension(50, 50));
                boardSquares[rowIndex][colindex] = temp;
                boardPanel.add(temp);
                // cBoard[i][j] = new JPanel();
                // cBoard[i][j].setMinimumSize(new Dimension(50, 50));
                // outerPanel.add(cBoard[i][j]);
            }
        }

        try {
            BufferedImage bImage = ImageIO.read(new File("Chess_symbols2.PNG"));
            int colWidth = 63;
            int rowHeight = 72;
            int rowNum = 2;
            int colNum = 6;

            for (int i = 0; i < rowNum; i++) {
                //System.out.println("i: " + i);
                for (int j = 0; j < colNum; j++) {
                    //System.out.println("j: " + j);
                    int x = j * colWidth;
                    int y = i * rowHeight;
                    int w = colWidth;
                    int h = rowHeight;
                    //System.out.println("x: " + x + ",y: " + y + ",w: " + w + ",h: " + h);
                    images[i][j] = bImage.getSubimage(x, y, w, h);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        outerPanel.add(new JLabel());
        outerPanel.add(new JLabel());
        outerPanel.add(new JLabel());
        outerPanel.add(new JLabel());
        outerPanel.add(boardPanel);
        outerPanel.add(new JLabel());

        whiteInCheckLabel = new JLabel();
        outerPanel.add(whiteInCheckLabel);

        messageLabel = new JLabel();
        messageLabel.setText("POOP");
        outerPanel.add(messageLabel);
        
        blackInCheckLabel = new JLabel();
        outerPanel.add(blackInCheckLabel);

        frame.add(outerPanel);


        frame.setSize(840, 800);
        // frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private ImageIcon getIconFromTypeAndColor(PieceType t, boolean c) {
        ImageIcon retVal = null;

        int colorInt = (c) ? 0 : 1;
        switch (t) {
            case k:
                retVal = new ImageIcon(images[colorInt][0]);
                break;
            case q:
                retVal = new ImageIcon(images[colorInt][1]);
                break;
            case b:
                retVal = new ImageIcon(images[colorInt][2]);
                break;
            case n:
                retVal = new ImageIcon(images[colorInt][3]);
                break;
            case r:
                retVal = new ImageIcon(images[colorInt][4]);
                break;
            case p:
                retVal = new ImageIcon(images[colorInt][5]);
                break;
        }


        return retVal;

    }

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
        // Create the column numbers
        // String retString = "";
        // retString += "--|----A---------B---------C---------D---------E---------F---------G---------H----|--\n";
        int numberOfRows = this.model.currentBoard.getBoardSize();
        int numberOfCols = this.model.currentBoard.getBoardSize();

        int blackBack = 1;
        for (int rowIndex = numberOfRows - 1; rowIndex > -1; rowIndex--) {
            blackBack++;
            // Print each row
            for (int colIndex = 0; colIndex < numberOfCols; colIndex++) {
                blackBack++;
                int currentPieceLocation = colIndex * numberOfCols + rowIndex;
                Piece temp = this.model.getPiece(BoardLocation.values()[currentPieceLocation]);

                if (temp != null) {
                    boardSquares[rowIndex][colIndex].setIcon(getIconFromTypeAndColor(temp.getType(), temp.isWhite()));
                }
                if (blackBack % 2 == 0) {
                    boardSquares[rowIndex][colIndex].setBackground(Color.GRAY);
                }
            }
        }
    }

    @Override
    public BoardLocation requestSourcePiece() {
        update();
        
        
        boolean curTurnIsWhite = this.model.isWhiteTurn();
        System.out.println("-- " + pieceColorDisplayMap.get(curTurnIsWhite) + " Player's turn--");
        System.out.println("Enter the which piece you would like to move?");

        ArrayList<ChessMove> moves = this.model.getAvailableMoves();
        HashSet<BoardLocation> srcs = new HashSet<BoardLocation>();
        for (ChessMove m : moves) {
            srcs.add(m.getSrcLoc());
        }

        System.out.print("(");

        int i = 0;
        for (BoardLocation s : srcs) {
            System.out.print(s.toString());
            if (i + 1 < srcs.size()) {
                System.out.print(",");
            }
            i++;
        }

        // for (int i = 0; i < srcs.size(); i++) {
        // System.out.print(srcs..get(i).getSrcLoc().toString());
        // if (i + 1 < srcs.size()) {
        // System.out.print(",");
        // }
        // }
        System.out.println(")");

        boolean validSource = false;
        BoardLocation loc = null;
        do {
//            String locationString = readLine();
//            loc = BoardLocation.valueOf(locationString.trim());
//
//            for (BoardLocation l : srcs) {
//                if (l == loc) {
//                    validSource = true;
//                }
//            }
//            if (!validSource) {
//                System.out.println("Location is not valid.  Enter a new location.");
//            }
        } while (!validSource);

        ActionEvent event = new ActionEvent(this, MoveType.LOCATION.ordinal(), loc.toString());

        try {
            super.sendRequestToController(event);
        } catch (ChessException e) {
            System.out.println(e.getMessage());
        }
        return loc;

        
        
//        return null;
        // TODO Auto-generated method stub

    }

    @Override
    public BoardLocation requestDestinationPiece(BoardLocation srcLoc) {
        return null;
        // TODO Auto-generated method stub

    }

    @Override
    public void printGameStatus(GameStatus status) {
        // TODO Auto-generated method stub

    }

}
