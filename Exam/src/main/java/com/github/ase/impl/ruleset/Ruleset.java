package com.github.ase.impl.ruleset;

import java.util.Collection;

public interface Ruleset<P extends Piece<P>> {
    Collection<P> getInitialBoard();
}
