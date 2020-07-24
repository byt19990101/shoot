
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Main {
    public static BufferedImage hero0;
    public static BufferedImage hero1;
    public static BufferedImage bee;
    public static BufferedImage bullet;
    public static BufferedImage airplane;
    public static BufferedImage bigplane;
    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage pause;
    public static BufferedImage gameover;
    private static final int MAIN_WIDTH = 400;
    private static final int MAIN_HEIGHT = 650;


    static {
        //读取图片
        try {
            hero0 = ImageIO.read(Main.class.getResourceAsStream("pic/hero0.png"));
            hero1 = ImageIO.read(Main.class.getResourceAsStream("pic/hero1.png"));
            bee = ImageIO.read(Main.class.getResourceAsStream("pic/bee.png"));
            bullet = ImageIO.read(Main.class.getResourceAsStream("pic/bullet.png"));
            airplane = ImageIO.read(Main.class.getResourceAsStream("pic/airplane.png"));
            bigplane = ImageIO.read(Main.class.getResourceAsStream("pic/bigplane.png"));
            background = ImageIO.read(Main.class.getResourceAsStream("pic/background.png"));
            start = ImageIO.read(Main.class.getResourceAsStream("pic/start.png"));
            pause = ImageIO.read(Main.class.getResourceAsStream("pic/pause.png"));
            gameover = ImageIO.read(Main.class.getResourceAsStream("pic/gameover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("飞机大战");
        JFrame window = new JFrame("飞机大战");
        window.setSize(MAIN_WIDTH, MAIN_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        MyPanel p = new MyPanel();
        window.add(p);
        window.setVisible(true);
        window.setAlwaysOnTop(true);

        //调用游戏开始的方法
        p.action();


    }


    static class MyPanel extends JPanel {
        //定义所有的飞行物对象 - 包括小蜜蜂\敌机\大敌机
        private ArrayList<FlyingObject> flyings;
        //定义一个存放所有子弹的列表对象
        private ArrayList<FlyingObject> bullets;
        //固定的四种游戏状态
        public final int START = 0;
        public final int RUNNING = 1;
        public final int PAUSE = 2;
        public final int GAME_OVER = 3;
        //定义当前的游戏状态
        private int state = START;

        Hero hero = new Hero();


        MyPanel() {
            flyings = new ArrayList<>();
            bullets = new ArrayList<>();
        }


        public void action() {
            java.util.Timer timer = new java.util.Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    //游戏状态的判断
                    if (state == RUNNING) {
                        if (hero.getLife() <= 0) {
                            state = GAME_OVER;
                        }
                        //1、生成新的飞行物
                        creatFlyingObject();
                        //2、飞行物移动
                        flyingObjectStep();
                        //3、判断飞行物越界
                        outOfBounds();
                        hero.move();
                        //4、发射子弹
                        shootAction();
                        //6、判断子弹和飞行物相撞
                        bangAction();
                        //7、判断飞行物与英雄机相撞
                        shootByAction();
                    }

                    repaint();
                }
            }, 100, 10);

            MouseAdapter adapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //判断游戏状态，然后改变游戏状态
                    if (state == START) {
                        state = RUNNING;
                    } else if (state == GAME_OVER) {
                        removeAllFlyings();
                        hero.setX((400 - hero.getHeight()) / 2);
                        hero.setY(400);
                        hero.setLife(hero.getMaxlife());
                        hero.setDoubleFire(0);
                        state = START;
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (state == PAUSE) {
                        state = RUNNING;
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (state == RUNNING) {
                        state = PAUSE;
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    //先判断游戏状态
                    if (state == RUNNING) {
                        //根据鼠标的坐标改变英雄机的坐标
                        int mouse_x = e.getX();
                        int mouse_y = e.getY();
                        hero.setX(mouse_x - hero.width / 2);
                        hero.setY(mouse_y - hero.height / 2);
                    }
                    repaint();
                }
            };
            this.addMouseListener(adapter);
            this.addMouseMotionListener(adapter);
        }

        private void removeAllFlyings() {
            for (int i = 0; i < bullets.size(); i++) {
                bullets.remove(i);
                i--;
            }
            for (int j = 0; j < flyings.size(); j++) {
                flyings.remove(j);
                j--;
            }
        }

        private void shootByAction() {
            for (int i = 0; i < flyings.size(); i++) {
                FlyingObject fly = flyings.get(i);
                hero.shootByFlying(fly);
                if (hero.shootByFlying((fly))) {
                    hero.minusLife();
                    flyings.remove(i);
                    i--;
                }
            }
        }

        //判断子弹与飞行物碰撞
        private void bangAction() {
            //先循环子弹
            for (int i = 0; i < bullets.size(); i++) {
                for (int j = 0; j < flyings.size(); j++) {
                    FlyingObject b = bullets.get(i);
                    FlyingObject fly = flyings.get(j);
                    if (fly.shootByBullet((Bullet) b)) {
                        fly.minusLife();
                        if (fly.getLife() == 0) {
                            if (fly instanceof Enemy) {
                                Enemy enemy = (Enemy) fly;
                                hero.addScore(enemy.getScore());
                            }
                            if (fly instanceof Award) {
                                Award award = (Award) fly;
                                if (award.getAwardType() == Award.DOUBLE_FIRE) {
                                    hero.addDoubleFire();
                                } else if (award.getAwardType() == Award.ADD_LIFE) {
                                    hero.addLife(2);
                                }
                            }
                            //奖励 -> 火力加成，生命值加成
                            flyings.remove(j);
                        }
                        bullets.remove(i);
                        i--;
                        break;

                    }

                }

            }
        }

        private int bullesIndex0 = 0;

        private void shootAction() {
            //创建子弹对象
            bullesIndex0++;
            if (bullesIndex0 % 15 == 0) {
                //int ran = (int) (Math.random() * 20);
                Bullet[] bs = hero.shoot();
                if (hero.getDoubleFire() == 0) {
                    for (int i = 0; i < bs.length; i++) {
                        bullets.add(bs[0]);
                    }
                } else {
                    for (int i = 0; i < bs.length; i++) {
                        bullets.add(bs[0]);
                        bullets.add(bs[1]);
                    }
                }


            }

            //子弹运动
            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).move();
            }
        }

        //判断飞行物越界
        private void outOfBounds() {
            //飞行物越界
            for (int i = 0; i < flyings.size(); i++) {
                FlyingObject fly = flyings.get(i);
                if (fly.getY() >= MAIN_HEIGHT) {
                    flyings.remove(i);
                    i--;
                }
            }
            //子弹越界
            for (int i = 0; i < bullets.size(); i++) {
                Bullet bullet = (Bullet) bullets.get(i);
                if (bullet.getY() < 0) {
                    bullets.remove(i);
                    i--;
                }
            }
        }

        //飞行物移动
        private void flyingObjectStep() {
            for (int i = 0; i < flyings.size(); i++) {
                FlyingObject fly = flyings.get(i);
                fly.move();
            }
        }

        //生成新的飞行物
        private int flyingIndex = 0; //控制频率

        private void creatFlyingObject() {
            flyingIndex++;
            if (flyingIndex % 20 == 0) {
                int ran = (int) (Math.random() * 20);
                FlyingObject fly;
                if (ran == 0) {
                    fly = new Bee();
                } else if (ran == 5 || ran == 15) {
                    fly = new BigPlane();
                } else {
                    fly = new Airplane();
                }
                flyings.add(fly);
            }
        }

        //画出所有对象
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            //以下是绘画的自定义增强
            //画背景图片
            paintBackground(g);
            //画一个英雄机
            paintFlyingObject(g, hero);
            //画所有的小蜜蜂、敌机、大敌机（飞行物）
            paintFlyingObjects(g, flyings);
            //画所有的子弹
            paintFlyingObjects(g, bullets);
            //画分数和生命值
            paintScore(g);
            //画状态
            if (state == START) {
                paintStart(g);
            } else if (state == PAUSE) {
                paintPause(g);
            } else if (state == GAME_OVER) {
                paintGameOver(g);
            }


        }

        private void paintScore(Graphics g) {
            g.drawString("分数" + hero.getScore(), 5, 20);
            g.drawString("生命值" + hero.getLife(), 5, 30);
        }

        //画出单个飞行物
        private void paintFlyingObject(Graphics g, FlyingObject f) {
            g.drawImage(f.getImg(), f.getX(), f.getY(), this);
        }

        //画出多个飞行物
        private void paintFlyingObjects(Graphics g, ArrayList<FlyingObject> flyings) {
            for (int i = 0; i < flyings.size(); i++) {
                FlyingObject fly = flyings.get(i);
                g.drawImage(fly.getImg(), fly.getX(), fly.getY(), this);
            }
        }

        //绘制背景图片
        private void paintBackground(Graphics g) {
            BufferedImage img = null;
            img = background;
            g.drawImage(img, 0, 0, this);
        }

        //绘制开始标题
        private void paintStart(Graphics g) {
            BufferedImage img = null;
            img = start;
            g.drawImage(img, 0, 0, this);
        }

        private void paintPause(Graphics g) {
            BufferedImage img = null;
            img = pause;
            g.drawImage(img, 0, 0, this);
        }

        private void paintGameOver(Graphics g) {
            BufferedImage img = null;
            img = gameover;
            g.drawImage(img, 0, 0, this);
        }
    }
}
