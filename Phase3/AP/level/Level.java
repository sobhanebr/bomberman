package Phase3.AP.level;

import Phase3.AP.Board;
import Phase3.AP.entities.mob.Player;

import java.io.Serializable;

public class Level implements Serializable {
    private static final long serialVersionUID = 6529685011267107690L;
    private int _level;
    private Board _board;

    public Level(Board _board) {
        this._board = _board;
        _level = 1;
    }


    public int getLevel() {
        return _level;
    }

    public void nextLevel() {
        _level++;
        _board.initialize();
        for (Player p : _board.getPlayers()) {
            p.reveal();
            p.getGameClient().getGameFrame().getInfoPanel().gotToNextLevel();
        }

        if (_level > 4)
            _board.setEnemyNumbers((int) (1.05 * _board.getEnemyNumbers()));
        _board.makeEnemies();
        _board.makePowerups();
        _board.fixPlayersPlace();

    }


}
