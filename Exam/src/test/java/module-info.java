module com.github.ase.test {
    requires com.github.ase;
    requires org.junit.jupiter.api;
    requires org.mockito;
    exports com.github.ase.test;
    exports com.github.ase.test.match;
    exports com.github.ase.test.ruleset.checkers;
    exports com.github.ase.test.ruleset.checkers.piece;
}