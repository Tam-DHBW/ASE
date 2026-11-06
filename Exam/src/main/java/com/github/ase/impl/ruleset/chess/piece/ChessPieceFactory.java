package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

public interface ChessPieceFactory {
    ChessPiece createPawn(Color color, int row, int col);
    ChessPiece createKing(Color color, int row, int col);
    ChessPiece createQueen(Color color, int row, int col);
    ChessPiece createKnight(Color color, int row, int col);
    ChessPiece createBishop(Color color, int row, int col);
    ChessPiece createRook(Color color, int row, int col);
}
