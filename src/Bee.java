

public class Bee extends FlyingObject implements Award {
    private int x_speed = 2;
    private int y_speed = 2;
    private int awardType;


    public Bee() {
        super((int) (Math.random() * 400 - Main.bee.getWidth()),-Main.bee.getHeight(),Main.bee,1);
        awardType = (int) (Math.random() * 2);
    }

    @Override
    public void move() {
        y += y_speed;
        x += x_speed;
        if (x >= 400 - Main.bee.getWidth()) {
            x_speed = -2;
        } else if (x <= 0) {
            x_speed = 2;
        }

    }

    @Override
    public int getAwardType() {
        return awardType;
    }
}
