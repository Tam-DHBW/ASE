package com.github.ase.impl.match.history;

import com.github.ase.impl.ruleset.Piece;

public interface TurnHistoryManagerFactory<P extends Piece<P>> {
    TurnHistoryManager<P> createTurnHistoryManager();
}
