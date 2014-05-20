//package chess_rollins_blake.view;
//
//import java.awt.Dimension;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import chess_rollins_blake.lib.BoardLocation;
//
//public class GUIView extends ChessView {
//
//    JPanel[][] cBoard;
//    
//    public GUIView() {
//        JFrame frame = new JFrame();
//        JPanel outerPanel = new JPanel();
//        
//        
//        cBoard = new JPanel[8][8];
//        for (int i = 0; i < cBoard.length; i++) {
//            cBoard[i] = new JPanel[8];
//            for (int j = 0; j < cBoard[0].length; j++) {
//                cBoard[i][j] = new JPanel();
//                cBoard[i][j].setMinimumSize(new Dimension(50, 50));
//                outerPanel.add(cBoard[i][j]);
//            }
//        }
//        
//        
//        
//        
//        frame.add(outerPanel);
//        frame.pack();
//        frame.setVisible(true);
//        
//    }
//    @Override
//    public void requestInput() {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public BoardLocation requestSourcePiece() {
//        return null;
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public BoardLocation requestDestinationPiece(BoardLocation srcLoc) {
//        return null;
//        // TODO Auto-generated method stub
//        
//    }
//
//}
