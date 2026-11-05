package com.github.ase.impl.match.history;

import com.github.ase.impl.Color;
import com.github.ase.impl.ruleset.Piece;

import java.util.*;

class ArrayListTurnHistoryManager<P extends Piece<P>> implements TurnHistoryManager<P> {
    private ArrayList<Memento> turns = new ArrayList<>();
    private Optional<List<Memento>> selectedTurns = Optional.empty();

    @Override
    public void addTurn(Piece<P> piece, Piece.Move<P> move, Collection<P> piecesSnapshot) {
        this.selectedTurns.ifPresent(selected -> {
            this.turns = new ArrayList<>(selected);
            this.selectedTurns = Optional.empty();
        });

        Memento memento = new Memento(piece, move, piecesSnapshot);
        this.turns.add(memento);
    }

    @Override
    public List<Turn<P>> getTurns() {
        return this.turns.stream().map(turn ->
            // Java does not like the implicit upcast??
            (Turn<P>) new SelectableTurn(
                turn,
                this.turns.subList(0, this.turns.indexOf(turn) + 1)
            )
        ).toList();
    }

    private class Memento {
        private final Piece<P> piece;
        private final Piece.Move<P> move;
        private final Collection<P> piecesSnapshot;

        Memento(Piece<P> piece, Piece.Move<P> move, Collection<P> piecesSnapshot) {
            this.piece = piece;
            this.move = move;
            this.piecesSnapshot = piecesSnapshot;
        }
    }

    private class SelectableTurn implements Turn<P>, SelectTurnCommand<P> {
        private final Memento turn;
        private final List<Memento> selection;

        SelectableTurn(Memento turn, List<Memento> selection) {
            this.turn = turn;
            this.selection = selection;
        }

        @Override
        public Piece<P> getPiece() {
            return this.turn.piece;
        }

        @Override
        public Piece.Move<P> getMove() {
            return this.turn.move;
        }

        @Override
        public Collection<P> getPiecesSnapshot() {
            return this.turn.piecesSnapshot;
        }

        @Override
        public SelectTurnCommand<P> selectCommand() {
            return this;
        }

        @Override
        public Turn<P> selectTurn() {
            ArrayListTurnHistoryManager.this.selectedTurns = Optional.of(this.selection);
            return this;
        }
    }
}
