package com.github.ase.impl.match.history;

import com.github.ase.impl.ruleset.Piece;

public interface SelectTurnCommand<P extends Piece<P>> {
    Turn<P> selectTurn();
}
