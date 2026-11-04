package com.github.ase.impl.ruleset;

import com.github.ase.impl.Color;

import java.util.Collection;
import java.util.Optional;

public interface Piece<P extends Piece<P>> {
    String getCode();
    Color getColor();
    int getRow();
    int getCol();
    Collection<Move<P>> getValidMoves(Collection<P> pieces);

    record Move<P>(P replacement, Optional<P> takes) {}
}
