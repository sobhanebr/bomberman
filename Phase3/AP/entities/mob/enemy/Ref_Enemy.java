package Phase3.AP.entities.mob.enemy;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.Entity;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.entities.tile.GrassTile;
import Phase3.AP.entities.tile.WallTile;
import Phase3.AP.entities.tile.destroyable.BrickTile;

public class Ref_Enemy extends Enemy {

    private boolean[] exp = {false, false, false, false};

    Ref_Enemy(int x, int y, Board board, Game game) {
        super(x, y, board, game, game.getPlayerspeedConstant(), 2, "src/images/player.png");
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Player) {
            ((Player) e).kill(null);
            return true;
        }
        return !(e instanceof BrickTile) &&
                !(e instanceof WallTile) &&
                !((e instanceof GrassTile) && ((GrassTile) e).HasBomb());
    }

    @Override
    public void calculateMove() {
        while (vx == vy || (!canMove(vx, vy)
                && (!exp[0] || !exp[1] || !exp[2] || !exp[3])))
            calc();
        if (!(exp[0] && exp[1] && exp[2] && exp[3]))
            move();
        else
            vx = vy = 0;
        exp = new boolean[]{false, false, false, false};
    }

    private void calc() {
        _direction = random.nextInt(4);
        switch (_direction) {
            case 0:
                exp[0] = true;
                vx = 1;
                vy = 0;
                break;
            case 1:
                exp[1] = true;
                vx = 0;
                vy = -1;
                break;
            case 2:
                exp[2] = true;
                vx = -1;
                vy = 0;
                break;
            default:
                exp[3] = true;
                vx = 0;
                vy = 1;
                break;
        }
    }
}
