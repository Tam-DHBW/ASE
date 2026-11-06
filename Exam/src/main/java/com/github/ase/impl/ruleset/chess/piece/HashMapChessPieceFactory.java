package com.github.ase.impl.ruleset.chess.piece;

import com.github.ase.impl.Color;

import java.util.HashMap;
import java.util.Objects;

public class HashMapChessPieceFactory implements ChessPieceFactory {
    private final HashMap<Integer, ChessPieceType> cache = new HashMap<>();

    @Override
    public ChessPiece createPawn(Color color, int row, int col) {
        return new ChessPiece(
            cache.computeIfAbsent(
                Objects.hash(PawnPieceType.class, color),
                _ -> new PawnPieceType(color, this)
            ),
            row,
            col
        );
    }

    @Override
    public ChessPiece createKing(Color color, int row, int col) {
        return new ChessPiece(
            cache.computeIfAbsent(
                Objects.hash(KingPieceType.class, color),
                _ -> new KingPieceType(color, this)
            ),
            row,
            col
        );
    }

    @Override
    public ChessPiece createQueen(Color color, int row, int col) {
        return new ChessPiece(
            cache.computeIfAbsent(
                Objects.hash(QueenPieceType.class, color),
                _ -> new QueenPieceType(color, this)
            ),
            row,
            col
        );
    }

    @Override
    public ChessPiece createKnight(Color color, int row, int col) {
        return new ChessPiece(
            cache.computeIfAbsent(
                Objects.hash(KnightPieceType.class, color),
                _ -> new KnightPieceType(color, this)
            ),
            row,
            col
        );
    }

    @Override
    public ChessPiece createBishop(Color color, int row, int col) {
        return new ChessPiece(
            cache.computeIfAbsent(
                Objects.hash(BishopPieceType.class, color),
                _ -> new BishopPieceType(color, this)
            ),
            row,
            col
        );
    }

    @Override
    public ChessPiece createRook(Color color, int row, int col) {
        return new ChessPiece(
            cache.computeIfAbsent(
                Objects.hash(RookPieceType.class, color),
                _ -> new RookPieceType(color, this)
            ),
            row,
            col
        );
    }
}
