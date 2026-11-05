package com.github.ase.impl.match;

import com.github.ase.impl.Color;
import com.github.ase.impl.match.history.SelectTurnCommand;
import com.github.ase.impl.match.history.Turn;
import com.github.ase.impl.match.history.TurnHistoryManager;
import com.github.ase.impl.ruleset.Piece;
import com.github.ase.impl.ruleset.Ruleset;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TurnBased2PMatch<P extends Piece<P>> implements Match {
    private final Ruleset<P> ruleset;

    private final TurnHistoryManager<P> turnHistoryManager;

    private Color currentPlayer = Color.WHITE;
    private ArrayList<P> pieces;
    private Optional<P> selectedPiece = Optional.empty();

    public TurnBased2PMatch(Ruleset<P> ruleset, TurnHistoryManager<P> turnHistoryManager) {
        this.ruleset = ruleset;
        this.turnHistoryManager = turnHistoryManager;
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

        this.turnHistoryManager.addTurn(piece, move, List.copyOf(this.pieces));

        this.currentPlayer = this.currentPlayer.getOpponent();
    }

    @Override
    public List<String> moves() {
        return this.getTurnLabels().map(Map.Entry::getKey).toList();
    }

    @Override
    public void onSelectMove(String moveId) {
        this.getTurnLabels()
            .filter(turnEntry -> turnEntry.getKey().equals(moveId))
            .forEach(turnEntry -> {
                Turn<P> turn = turnEntry.getValue().selectTurn();
                this.currentPlayer = turn.getPiece().getColor().getOpponent();
                this.pieces = new ArrayList<>(turn.getPiecesSnapshot());
            });
    }

    private Stream<Map.Entry<String, SelectTurnCommand<P>>> getTurnLabels() {
        List<Turn<P>> turns = this.turnHistoryManager.getTurns();
        return IntStream.range(0, turns.size()).mapToObj(i -> Map.entry(
            "#%d %s".formatted(i + 1, this.ruleset.getTurnLabel(turns.get(i))),
            turns.get(i).selectCommand()
        ));
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
