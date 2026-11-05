package com.github.ase.impl.match.history;

import com.github.ase.impl.ruleset.Piece;

public class ArrayListTurnHistoryManagerFactory<P extends Piece<P>> implements TurnHistoryManagerFactory<P> {
    @Override
    public TurnHistoryManager<P> createTurnHistoryManager() {
        return new ArrayListTurnHistoryManager<>();
    }
}
