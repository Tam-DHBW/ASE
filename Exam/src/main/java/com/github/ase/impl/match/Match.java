package com.github.ase.impl.match;

import java.util.List;

public interface Match {
    String pieceAt(int row, int col);
    boolean isCellHighlighted(int row, int col);
    void onCellClick(int row, int col);
    List<String> moves() ;
    void onSelectMove(String moveId) ;
    String currentPlayer();
    String getStatus();
}
