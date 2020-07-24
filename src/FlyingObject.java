
import java.awt.image.BufferedImage;

public abstract class FlyingObject {
    protected int x;
    protected int y;
    protected BufferedImage img;
    protected int width;
    protected int height;
    protected int life;

    public FlyingObject(int x, int y, BufferedImage img, int life) {
        this.x = x;
        this.y = y;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.img = img;
        this.life = life;
    }

    //判断飞行物被子弹撞击的方法
    public boolean shootByBullet(Bullet b){
        int bx = b.getX();
        int by = b.getY();
        if (bx >= x && bx <= x + this.getWidth() && by >= y && by <= y + this.getHeight()){
            return true;
        }else{
            return false;
        }

    }

    public void minusLife(){
        this.life --;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public abstract void move();

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
