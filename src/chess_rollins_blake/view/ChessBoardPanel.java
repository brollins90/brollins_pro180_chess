package chess_rollins_blake.view;

import javax.swing.JPanel;


public class ChessBoardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    JPanel[] boardLocations;
    
    public ChessBoardPanel() {
        boardLocations = new JPanel[64];
        
    }

}
