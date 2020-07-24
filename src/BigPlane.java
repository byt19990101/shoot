

public class BigPlane extends FlyingObject implements Enemy,Award{
    private int speed = 2;
    private int score;
    private int awardType;


    public BigPlane() {
        super((int) (Math.random() * 400 - Main.bigplane.getWidth()),-Main.bigplane.getHeight(),Main.bigplane,5);
        score = 10;
        awardType =(int)(Math.random()*2);
    }

    @Override
    public void move() {
        y += speed;

    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getAwardType() {
        return awardType;
    }
}
