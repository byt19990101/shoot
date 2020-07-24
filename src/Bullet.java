

public class Bullet extends FlyingObject {
    private int speed = 3;

    public Bullet(int x, int y) {
        super(x,y,Main.bullet,1);
    }

    @Override
    public void move() {
        y -= speed;
    }
}
