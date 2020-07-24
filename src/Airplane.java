
public class Airplane extends FlyingObject implements Enemy {
    private int speed = 3;
    private int score;

    public Airplane() {
        super((int) (Math.random() * 400 - Main.airplane.getWidth()),-Main.airplane.getHeight(),Main.airplane,2);
        score = 5;
    }

    @Override
    public void move() {
        y += speed;

    }

    @Override
    public int getScore() {
        return score;
    }

}
