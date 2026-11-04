package com.github.ase.impl.match;

import com.github.ase.BoardModel;

public interface MatchFactory {
    Match createMatch(BoardModel.Game game);
}
