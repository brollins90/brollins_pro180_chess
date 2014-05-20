package chess_rollins_blake.lib;

public enum BoardLocation {
//    7, 15, 23, 31, 39, 47, 55, 63,
//    6, 14, 22, 30, 38, 46, 54, 62,
//    5, 13, 21, 29, 37, 45, 53, 61,
//    4, 12, 20, 28, 36, 44, 52, 60,
//    3, 11, 19, 27, 35, 43, 51, 59,
//    2, 10, 18, 26, 34, 42, 50, 58,
//    1,  9, 17, 25, 33, 41, 49, 57,
//    0,  8, 16, 24, 32, 40, 48, 56

    a1, a2, a3, a4, a5, a6, a7, a8,
    b1, b2, b3, b4, b5, b6, b7, b8,
    c1, c2, c3, c4, c5, c6, c7, c8,
    d1, d2, d3, d4, d5, d6, d7, d8,
    e1, e2, e3, e4, e5, e6, e7, e8,
    f1, f2, f3, f4, f5, f6, f7, f8,
    g1, g2, g3, g4, g5, g6, g7, g8,
    h1, h2, h3, h4, h5, h6, h7, h8,
    none;
    
    public int getColumn() {
        return this.ordinal() / 8;
    }
    
    public int getRow() {
        return this.ordinal() % 8;
    }
}
