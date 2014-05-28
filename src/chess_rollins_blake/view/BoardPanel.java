package chess_rollins_blake.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import chess_rollins_blake.lib.BoardLocation;
import chess_rollins_blake.lib.PieceType;
import chess_rollins_blake.model.ChessModel;
import chess_rollins_blake.model.pieces.Piece;

public class BoardPanel extends JPanel {

    BoardLocation prevLoc = null;

    private static final long serialVersionUID = 1L;

    private Color SQUARE_COLOR_LIGHT = Color.WHITE;
    private Color SQUARE_COLOR_DARK = Color.GRAY;
    private Color SQUARE_HAS_MOVE = Color.GREEN;
    private Color SQUARE_IS_DEST = Color.BLUE;

    private int SQUARE_HEIGHT = 75;
    private int SQUARE_WIDTH = 75;

    protected BoardLocation currentTempSource;

    BufferedImage[][] images = new BufferedImage[2][6];

    HashSet<BoardLocation> availableSources;
    // HashSet<BoardLocation> availableDestinations;

    private ChessModel model;

    BoardPanel(ChessModel m) {
        this.model = m;
        availableSources = new HashSet<BoardLocation>();
        // availableDestinations = new HashSet<BoardLocation>();

        loadImages();

    }

    //
    // public void setAvailableDestinations(HashSet<BoardLocation> dests) {
    // this.availableDestinations = dests;
    // }

    public void setAvailableSources(HashSet<BoardLocation> sources) {
        this.availableSources = sources;
    }

    @Override
    public void paint(Graphics g) {

        // check the current location
        BoardLocation tempSource = this.model.getCurrentModelState();

        if (tempSource != prevLoc) {
            prevLoc = tempSource;

            HashSet<BoardLocation> tempDests = this.model.getAvailableDestinationsFromLocation(tempSource);

            int darkBackground = 1;

            //super.paint(g);

            int numberOfRows = this.model.getBoardRowSize();
            int numberOfCols = this.model.getBoardRowSize();
            for (int rowIndex = numberOfRows - 1; rowIndex > -1; rowIndex--) {
                darkBackground++;
                for (int colIndex = 0; colIndex < numberOfCols; colIndex++) {
                    darkBackground++;
                    int currentX = colIndex * SQUARE_WIDTH;
                    int currentY = (numberOfRows - 1 - rowIndex) * SQUARE_HEIGHT;

                    int currentPieceLocation = colIndex * numberOfRows + rowIndex;
                    BoardLocation currentLocation = BoardLocation.values()[currentPieceLocation];
                    Piece temp = this.model.getPiece(currentLocation);


                    // paint the square first
                    if (darkBackground % 2 == 0) {
                        g.setColor(SQUARE_COLOR_DARK);
                    } else {
                        g.setColor(SQUARE_COLOR_LIGHT);
                    }

                    // color the source dests
                    if (this.availableSources.contains(currentLocation)) {
                        g.setColor(SQUARE_HAS_MOVE);
                    }

                    // color the current darker
                    if (tempSource == currentLocation) {
                        g.setColor(Color.ORANGE);
                    }

                    // colot the dests
                    if (tempDests.contains(currentLocation)) {
                        g.setColor(SQUARE_IS_DEST);
                    }

                    g.fillRect(currentX, currentY, SQUARE_WIDTH, SQUARE_HEIGHT);


                    if (temp != null) {
                        g.drawImage(getIconFromTypeAndColor(temp.getType(), temp.isWhite()), currentX, currentY, null);
                        // boardSquares[rowIndex][colIndex].setIcon(getIconFromTypeAndColor(temp.getType(), temp.isWhite()));
                    }
                }
            }
        }
    }

    //
    // public void setModel(ChessModel m) {
    // this.m = m;
    // }

    private BufferedImage getIconFromTypeAndColor(PieceType t, boolean c) {
        BufferedImage retVal = null;

        int colorInt = (c) ? 0 : 1;
        switch (t) {
            case k:
                retVal = images[colorInt][0];
                break;
            case q:
                retVal = images[colorInt][1];
                break;
            case r:
                retVal = images[colorInt][2];
                break;
            case b:
                retVal = images[colorInt][3];
                break;
            case n:
                retVal = images[colorInt][4];
                break;
            case p:
                retVal = images[colorInt][5];
                break;
        }


        return retVal;

    }

    private void loadImages() {
        try {
            BufferedImage bImage = ImageIO.read(new File("Chess_symbols2.PNG"));
            int colWidth = 63;
            int rowHeight = 72;
            int rowNum = 2;
            int colNum = 6;

            for (int i = 0; i < rowNum; i++) {
                // System.out.println("i: " + i);
                for (int j = 0; j < colNum; j++) {
                    // System.out.println("j: " + j);
                    int x = j * colWidth;
                    int y = i * rowHeight;
                    int w = colWidth;
                    int h = rowHeight;
                    // System.out.println("x: " + x + ",y: " + y + ",w: " + w + ",h: " + h);
                    images[i][j] = bImage.getSubimage(x, y, w, h);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
