package Phase3.AP.entities.mob.enemy;

import Phase3.AP.Board;
import Phase3.AP.Game;
import Phase3.AP.entities.Entity;
import Phase3.AP.entities.mob.Player;
import Phase3.AP.entities.tile.GrassTile;
import Phase3.AP.entities.tile.WallTile;
import Phase3.AP.entities.tile.destroyable.BrickTile;


public class Minvo_3 extends Enemy {
    private boolean onRandomMovement;
    private int randomMovementCount;
    private boolean[] exp = {false,false,false,false};
    public Minvo_3(int x, int y, Board board, Game game) {
        super(x, y, board, game, game.getPlayerspeedConstant(), 3 , "src/images/Minvo.jpg");
        onRandomMovement = false;
        randomMovementCount = 0;
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Player) {
            ((Player) e).kill(null);
            return true;
        }
        return !(e instanceof BrickTile) &&
                !(e instanceof WallTile) &&
                !((e instanceof GrassTile) && ((GrassTile)e).HasBomb());
    }

    @Override
    public void calculateMove() {
        findNearestPlayer();
        if (dx != 0 && canMove(dx / Math.abs(dx), 0) && dy != 0 && canMove(0, dy / Math.abs(dy)) && !onRandomMovement) {
            if (random.nextInt(2) == 0) {
                vx = dx / Math.abs(dx);
                vy = 0;
            } else {
                vx = 0;
                vy = dy / Math.abs(dy);
            }
        } else if (dx != 0 && canMove(dx / Math.abs(dx), 0) && !onRandomMovement) {
            vx = dx / Math.abs(dx);
            vy = 0;
        } else if (dy != 0 && canMove(0, dy / Math.abs(dy)) && !onRandomMovement) {
            vx = 0;
            vy = dy / Math.abs(dy);
        } else {
            onRandomMovement = true;
        }
        if (randomMovementCount < 11) {
            if (onRandomMovement) {
                while (vx == vy || (!canMove(vx, vy)
                        && (!exp[0] || !exp[1] || !exp[2] || !exp[3]))) //Check this while
                    calc();
                randomMovementCount++;
            }
        } else
            onRandomMovement = false;
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
