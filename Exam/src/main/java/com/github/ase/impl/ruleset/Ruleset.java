package com.github.ase.impl.ruleset;

import com.github.ase.impl.match.history.Turn;

import java.util.Collection;

public interface Ruleset<P extends Piece<P>> {
    Collection<P> getInitialBoard();
    String getTurnLabel(Turn<P> turn);
}
