package chess_rollins_blake.lib;

public abstract class Piece {

    protected PieceColor color;
    protected PieceStatus status;
    protected PieceType type;
    protected boolean canCollideOnMove;
    //private BoardLocation loc;
    
    public Piece(PieceColor color/*, PieceStatus status/*, PieceType type, BoardLocation startLocation*/) {
        this.color = color;
        this.status = PieceStatus.ALIVE;
        this.canCollideOnMove = false;
//        this.type = type;
        //this.loc = startLocation;
    }
    
    public PieceColor getColor() {
        return this.color;
    }
    
    public PieceStatus getStatus() {
        return this.status;
    }
    
    public PieceType getType() {
        return this.type;
    }
//    
//    public BoardLocation getLocation() {
//        return this.loc;
//    }
    
    public void setStatus(PieceStatus status) {
        this.status = status;
    }
    
    public String toString() {
        return this.type.name();
    }
    
    public boolean canCollide() {
        return this.canCollideOnMove;
    }
    
    public abstract boolean isValidMovement(BoardLocation src, BoardLocation dest);
    
}
