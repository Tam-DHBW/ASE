package com.github.ase;

import com.github.ase.impl.MatchBoardModel;
import com.github.ase.impl.match.TurnBased2PMatch;
import com.github.ase.impl.match.history.ArrayListTurnHistoryManagerFactory;
import com.github.ase.impl.ruleset.checkers.CheckersRuleset;
import com.github.ase.impl.ruleset.checkers.piece.CheckersPiece;
import com.github.ase.impl.ruleset.checkers.piece.StaticCheckersPieceFactory;
import com.github.ase.impl.ruleset.chess.ChessRuleset;
import com.github.ase.impl.ruleset.chess.piece.ChessPiece;
import com.github.ase.impl.ruleset.chess.piece.HashMapChessPieceFactory;
import com.github.ase.ui.widgets.MainMenuView;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // STUDENTS: edit ONLY the line below.
        // Create an instance of YOUR BoardModel implementation here, e.g.:
        //     BoardModel model = new MyBoardModel();
        // Until you do, the app compiles but fails as soon as you start a game.
        BoardModel model = new MatchBoardModel(game -> switch (game) {
            case CHECKERS -> new TurnBased2PMatch<>(
                new CheckersRuleset(StaticCheckersPieceFactory.getInstance()),
                new ArrayListTurnHistoryManagerFactory<CheckersPiece>().createTurnHistoryManager()
            );
            case CHESS -> new TurnBased2PMatch<>(
                new ChessRuleset(new HashMapChessPieceFactory()),
                new ArrayListTurnHistoryManagerFactory<ChessPiece>().createTurnHistoryManager()
            );
        }); // TODO: replace null with your BoardModel implementation

        MainMenuView mainMenu = new MainMenuView(model);
         mainMenu.show(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
