package com.github.ase.impl.match;

import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece;
import com.github.ase.impl.ruleset.Ruleset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TurnBased2PMatch<P extends Piece<P>> implements Match {
    private final Ruleset<P> ruleset;
    private final ArrayList<P> pieces;

    private Color currentPlayer = Color.WHITE;
    private Optional<P> selectedPiece = Optional.empty();

    public TurnBased2PMatch(Ruleset<P> ruleset) {
        this.ruleset = ruleset;
        this.pieces = new ArrayList<>(ruleset.getInitialBoard());
    }

    @Override
    public String pieceAt(int row, int col) {
        return this.pieces.stream()
            .filter(piece ->
                piece.getRow() == row && piece.getCol() == col
            )
            .findFirst()
            .map(Piece::getCode)
            .orElse(null);
    }

    @Override
    public boolean isCellHighlighted(int row, int col) {
        return this.selectedPiece.map(piece ->
            piece
                .getValidMoves(this.pieces)
                .stream()
                .anyMatch(move -> move.replacement().getRow() == row && move.replacement().getCol() == col)
        ).orElse(false);
    }

    @Override
    public void onCellClick(int row, int col) {
        Optional<P> clickedPiece = this.pieces.stream()
            .filter(piece -> piece.getRow() == row && piece.getCol() == col)
            .findFirst();

        if (this.selectedPiece.isEmpty()) {
            if (clickedPiece.isEmpty() || clickedPiece.get().getColor() == currentPlayer) {
                this.selectedPiece = clickedPiece;
            }
        }
        else {
            P selectedPiece = this.selectedPiece.get();
            selectedPiece.getValidMoves(this.pieces).stream()
                .filter(validMove -> validMove.replacement().getRow() == row && validMove.replacement().getCol() == col)
                .findFirst()
                .ifPresent(move -> this.performMove(selectedPiece, move));

            this.selectedPiece = Optional.empty();
        }
    }

    private void performMove(P piece, Piece.Move<P> move) {
        this.pieces.remove(piece);
        this.pieces.add(move.replacement());
        move.takes().ifPresent(this.pieces::remove);

        this.currentPlayer = switch (this.currentPlayer) {
            case BLACK -> Color.WHITE;
            case WHITE -> Color.BLACK;
        };
    }

    @Override
    public List<String> moves() {
        // TODO also test
        return List.of();
    }

    @Override
    public void onSelectMove(String moveId) {
        // TODO also test
    }

    @Override
    public String currentPlayer() {
        return this.currentPlayer.getNameTitleCase();
    }

    @Override
    public String getStatus() {
        // TODO also test
        return "";
    }
}
