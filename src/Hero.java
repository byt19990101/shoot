
import java.awt.image.BufferedImage;

public class Hero extends FlyingObject {
    private int score;
    private static int maxlife = 5;

    public int getLife() {
        return life;
    }

    public static int getMaxlife() {
        return maxlife;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public Hero() {
        super((400 - Main.hero0.getHeight()) / 2, 400, Main.hero0, maxlife);

    }

    private int count = 0;
    private BufferedImage[] heroImg = {Main.hero0, Main.hero1};

    @Override
    public void move() {
        count++;
        setImg(heroImg[count % 2]);

    }

    //新增变量，标记双倍火力剩余次数
    private int doubleFire = 0;

    public int getDoubleFire() {
        return doubleFire;
    }

    public void addDoubleFire() {
        doubleFire = 0;
        doubleFire += 20;
        System.out.println("双倍火力");
    }

    //发射子弹，生成新的子弹，并返回
    public Bullet[] shoot() {
        Bullet[] bullets;
        if (doubleFire == 0) {
            bullets = new Bullet[1];
            bullets[0] = new Bullet(x + 45, y - 10);
        } else {
            bullets = new Bullet[2];
            bullets[0] = new Bullet(x + 20, y - 10);
            bullets[1] = new Bullet(x + 70, y - 10);
            doubleFire--;
        }
        return bullets;
    }

    public void addScore(int score) {
        this.setScore(this.getScore() + score);
    }

    public void addLife(int life) {
        this.setLife(this.getLife() + life);
    }

    public void setDoubleFire(int doubleFire) {
        this.doubleFire = doubleFire;
    }

    //判断英雄机和敌机有没有相撞
    public boolean shootByFlying(FlyingObject fly) {
        //this.x  this.y  fly.getX()  fly.getY()
        int flyx = fly.getX();
        int flyy = fly.getY();
        if (flyx + fly.getWidth() > x && flyx < x + this.getWidth() && flyy + fly.getHeight() > y) {
            return true;
        } else {
            return false;
        }

    }

}
