package com.github.ase.impl;

import com.github.ase.BoardModel;
import com.github.ase.impl.match.Match;
import com.github.ase.impl.match.MatchFactory;

import java.util.List;

public class MatchBoardModel implements BoardModel {
    private final MatchFactory matchFactory;

    private Match currentMatch = null;

    public MatchBoardModel(MatchFactory matchFactory) {
        this.matchFactory = matchFactory;
    }

    @Override
    public void setup(Game game) {
        this.currentMatch = matchFactory.createMatch(game);
    }

    @Override
    public String pieceAt(int row, int col) {
        return this.currentMatch.pieceAt(row, col);
    }

    @Override
    public boolean isCellHighlighted(int row, int col) {
        return this.currentMatch.isCellHighlighted(row, col);
    }

    @Override
    public void onCellClick(int row, int col) {
        this.currentMatch.onCellClick(row, col);
    }

    @Override
    public List<String> moves() {
        return this.currentMatch.moves();
    }

    @Override
    public void onSelectMove(String moveId) {
        this.currentMatch.onSelectMove(moveId);
    }

    @Override
    public String currentPlayer() {
        return this.currentMatch.currentPlayer();
    }

    @Override
    public String getStatus() {
        return this.currentMatch.getStatus();
    }
}
