module com.github.ase {
    requires javafx.controls;
    requires transitive javafx.graphics;
    exports com.github.ase;
    exports com.github.ase.ui.boards;
    exports com.github.ase.ui.widgets;
    exports com.github.ase.impl;
    exports com.github.ase.impl.match;
    exports com.github.ase.impl.ruleset;
    exports com.github.ase.impl.ruleset.chess;
}
