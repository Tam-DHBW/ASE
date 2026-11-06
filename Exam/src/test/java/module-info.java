module com.github.ase.test {
    requires com.github.ase;
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    exports com.github.ase.test;
    exports com.github.ase.test.match;
    exports com.github.ase.test.match.history;
    exports com.github.ase.test.ruleset.checkers;
    exports com.github.ase.test.ruleset.checkers.piece;
    exports com.github.ase.test.ruleset.chess;
    exports com.github.ase.test.ruleset.chess.piece;
}