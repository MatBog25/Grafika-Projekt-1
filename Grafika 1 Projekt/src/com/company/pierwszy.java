package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.Scanner;

import static java.lang.Math.*;


class Gra extends JPanel{
    //Gra

    int crx,cry;
    int car_x,car_y;
    int speedX,speedY;
    int nOpponent;
    String[] imageLoc;
    int[] lx,ly;
    int score;
    int highScore;
    int[] speedOpponent;
    boolean isFinished;
    boolean isUp, isDown, isRight, isLeft;

    public Gra(){
        crx = cry = -999;

        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyReleased(KeyEvent e) {
                stopCar(e);
            }
            public void keyPressed(KeyEvent e) {
                moveCar(e);
            }
        });
        setFocusable(true);
        car_x = car_y = 300;
        isUp = isDown = isLeft = isRight = false;
        speedX = speedY = 0;
        nOpponent = 0;
        lx = new int[20];
        ly = new int[20];
        imageLoc = new String[20];
        speedOpponent = new int[20];
        isFinished = false;
        score = highScore = 0;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D obj = (Graphics2D) g;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            obj.drawImage(getToolkit().getImage("images/st_road.png"), 0, 0, this);
            if (cry >= -499 && crx >= -499)
                obj.drawImage(getToolkit().getImage("images/cross_road.png"), crx, cry, this);

            obj.drawImage(getToolkit().getImage("images/car_self.png"), car_x, car_y, this);

            if (isFinished) {
                obj.drawImage(getToolkit().getImage("images/boom.png"), car_x - 30, car_y - 30, this);
            }

            if (this.nOpponent > 0) {
                for (int i = 0; i < this.nOpponent; i++) {
                    obj.drawImage(getToolkit().getImage(this.imageLoc[i]), this.lx[i], this.ly[i], this);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void moveRoad(int count){
        if(crx == -999 && cry == -999){
            if(count%10 == 0){
                crx = 499;
                cry = 0;
            }
        }
        else{
            crx--;
        }
        if(crx == -499 && cry == 0){
            crx = cry = -999;
        }
        car_x += speedX;
        car_y += speedY;

        if(car_x < 0)
            car_x = 0;

        if(car_x+93 >= 500)
            car_x = 500-93;

        if(car_y <= 124)
            car_y = 124;

        if(car_y >= 364-50)
            car_y = 364-50;

        for(int i=0;i<this.nOpponent;i++){
            this.lx[i] -= speedOpponent[i];
        }

        int index[] = new int[nOpponent];
        for(int i=0;i<nOpponent;i++){
            if(lx[i] >= -127){
                index[i] = 1;
            }
        }
        int c = 0;
        for(int i=0;i<nOpponent;i++){
            if(index[i] == 1){
                imageLoc[c] = imageLoc[i];
                lx[c] = lx[i];
                ly[c] = ly[i];
                speedOpponent[c] = speedOpponent[i];
                c++;
            }
        }

        score += nOpponent - c;

        if(score > highScore)
            highScore = score;

        nOpponent = c;

        int diff = 0;
        for(int i=0;i<nOpponent;i++){
            diff = car_y - ly[i];
            if((ly[i] >= car_y && ly[i] <= car_y+46) || (ly[i]+46 >= car_y && ly[i]+46 <= car_y+46)){
                if(car_x+87 >= lx[i] && !(car_x >= lx[i]+87)){
                    System.out.println("My car : "+car_x+", "+car_y);
                    System.out.println("Colliding car : "+lx[i]+", "+ly[i]);
                    this.finish();
                }
            }
        }
    }

    void finish(){
        String str = "";
        isFinished = true;
        this.repaint();
        if(score == highScore && score != 0)
            str = "\nGratulacje! To najlepszy wynik";
        JOptionPane.showMessageDialog(this,"Przegrales\nTwoj wynik: "+score+"\nNajlepszy wynik: "+highScore+str,     "Game Over", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }

    public void moveCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            isUp = true;
            speedX = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            isDown = true;
            speedX = -2;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            isRight = true;
            speedY = 1;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            isLeft = true;
            speedY = -1;
        }
    }


    public void stopCar(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            isUp = false;
            speedX = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            isDown = false;
            speedX = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            isLeft = false;
            speedY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            isRight = false;
            speedY = 0;
        }
    }
}

class Okno extends JFrame {
    public int width=800;
    public int height=600;
    Okno(){
        setVisible(true);
        setSize(width, height);
        setTitle("Obrazek");
        setLocation(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

class Krzywe extends JPanel {
    double t;
    double[] x = new double[3];
    double[] y = new double[3];

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g5 = (Graphics2D) g;
        x = new double[]{59,55 , 48, 59};
        y = new double[]{377, 395, 143,143 };
        bezzie(g5, x, y);
        x = new double[]{59,73 , 119, 129};
        y = new double[]{143, 143, 308, 307};
        bezzie(g5, x, y);
        x = new double[]{129,140 ,168 ,186};
        y = new double[]{307,306 , 143, 142};
        bezzie(g5, x, y);
        x = new double[]{186,205 ,217 ,209};
        y = new double[]{142, 141, 393, 373};
        bezzie(g5, x, y);

        x = new double[]{267,267 ,260 ,263};
        y = new double[]{375,390 ,155 ,140 };
        bezzie(g5, x, y);
        x = new double[]{263,266 ,312 ,324};
        y = new double[]{140,125 , 131,132 };
        bezzie(g5, x, y);
        x = new double[]{324,339 , 359,368};
        y = new double[]{132,133 ,151 ,163 };
        bezzie(g5, x, y);
        x = new double[]{368,376 , 378,378};
        y = new double[]{163, 174, 185, 201};
        bezzie(g5, x, y);
        x = new double[]{378,378 , 373,364};
        y = new double[]{201,219 ,230 ,239 };
        bezzie(g5, x, y);
        x = new double[]{364,353 ,352 ,337};
        y = new double[]{239,250 , 251,250 };
        bezzie(g5, x, y);
        x = new double[]{337,322 ,279 ,264};
        y = new double[]{250,249 , 249, 248};
        bezzie(g5, x, y);
        x = new double[]{264,249 , 349,335};
        y = new double[]{248, 247,254 ,249 };
        bezzie(g5, x, y);
        x = new double[]{335,321 ,349 ,361};
        y = new double[]{249,244 ,250 ,255 };
        bezzie(g5, x, y);
        x = new double[]{361,371 ,384 ,389};
        y = new double[]{255,259 ,268 , 279};
        bezzie(g5, x, y);
        x = new double[]{389,397 ,397 ,399};
        y = new double[]{279, 298,300 ,313 };
        bezzie(g5, x, y);
        x = new double[]{399,402 ,401 ,397};
        y = new double[]{313,334 , 333, 344};
        bezzie(g5, x, y);
        x = new double[]{397,392 ,390 ,356};
        y = new double[]{344, 357, 361, 374};
        bezzie(g5, x, y);
        x = new double[]{356,342 ,341 ,329};
        y = new double[]{374, 379, 378, 383};
        bezzie(g5, x, y);
        x = new double[]{329,315 ,288 ,267};
        y = new double[]{383,389 , 384, 375};
        bezzie(g5, x, y);

    }
    public void bezzie(Graphics2D g, double[] x, double[] y){
        for (t = 0; t < 1; t += 0.00005) {
            double xt = pow(1 - t, 3) * x[0] + 3 * t * pow(1 - t, 2) * x[1] + 3 * pow(t, 2) * (1 - t) * x[2] + pow(t, 3) * x[3];
            double yt = pow(1 - t, 3) * y[0] + 3 * t * pow(1 - t, 2) * y[1] + 3 * pow(t, 2) * (1 - t) * y[2] + pow(t, 3) * y[3];
            g.draw(new Line2D.Double(xt, yt, xt, yt));
        }
    }

}

class rysunek extends JPanel {
    public void paint(Graphics g) {


        //Draw a beautiful sky
        setBackground(Color.cyan);

        //Grass
        g.setColor(Color.green);
        g.fillRect(0, 570, 1500, 3000);

        //Draw a round face
        g.setColor(new Color(217, 206, 215));
        g.fillArc(555, 300, 140, 150, 0, 360);

        //Eyes
        g.setColor(Color.blue);
        g.fillArc(600, 360, 15, 20, 0, 360);
        g.fillArc(640, 360, 15, 20, 0, 360);

        //Smile
        g.setColor(Color.red);
        g.drawArc(615, 420, 20, 8, 190, 200);




        //Body
        g.setColor(new Color(217, 206, 215));
        g.fillRect(543, 470, 25, 70);
        g.fillRect(683, 470, 25, 70);
        g.fillArc(543, 535, 20, 25, 0, 360);
        g.fillArc(685, 535, 20, 25, 0, 360);

        //T-shirt
        g.setColor(Color.red);
        g.fillRect(570, 450, 110, 200);
        g.fillRect(540, 450, 30,30);
        g.fillRect(680, 450, 30,30);

        //Pants
        g.setColor(Color.black);
        g.fillRect(570, 580, 110, 150);
        g.setColor(Color.green);
        int[] x3 = {625, 598, 638};
        int[] y3 = {600, 730, 730};
        g.fillPolygon(x3, y3, 3);

        //Shoes
        g.setColor(Color.darkGray);
        g.fillArc(570, 728, 35, 30, 0, 360);
        g.fillArc(642, 728, 35, 30, 0, 360);

        //Clouds
        g.setColor(Color.white);
        g.fillArc(100, 100, 60, 60, 0, 360);
        g.fillArc(120, 120, 70, 70, 0, 360);
        g.fillArc(120, 80, 70, 70, 0, 360);
        g.fillArc(160, 70, 80, 80, 0, 360);
        g.fillArc(190, 75, 90, 90, 0, 360);
        g.fillArc(240, 85, 60, 60, 0, 360);
        g.fillArc(220, 110, 70, 70, 0, 360);
        g.fillArc(170, 120, 70, 70, 0, 360);

        g.fillArc(550, 120, 60, 60, 0, 360);
        g.fillArc(570, 140, 70, 70, 0, 360);
        g.fillArc(570, 100, 70, 70, 0, 360);
        g.fillArc(610, 90, 80, 80, 0, 360);
        g.fillArc(640, 95, 90, 90, 0, 360);
        g.fillArc(690, 105, 60, 60, 0, 360);
        g.fillArc(670, 130, 70, 70, 0, 360);
        g.fillArc(620, 140, 70, 70, 0, 360);


        g.setColor(Color.yellow);
        int x[] = {900, 1080, 1180, 980, 1080, 1180, 800, 900, 1000};
        int y[] = {100, 80, 150, 80, 100, 80, 110, 100, 60};
        // g.fillPolygon(x,y,9);
         g.setColor(Color.yellow);
         g.fillArc(1250,50,120,120,0,360);

        g.setColor(Color.red);
        g.fillArc(80, 500, 20, 20, 0, 360);
        g.fillArc(95, 485, 20, 20, 0, 360);
        g.fillArc(110, 500, 20, 20, 0, 360);
        g.fillArc(86, 515, 20, 20, 0, 360);
        g.fillArc(103, 515, 20, 20, 0, 360);
        g.setColor(Color.yellow);
        g.fillArc(95, 503, 20, 20, 0, 360);
        g.setColor(Color.black);
        g.fillRect(103, 525, 5, 70);

        g.setColor(Color.white);
        g.fillArc(300, 500, 20, 20, 0, 360);
        g.fillArc(315, 485, 20, 20, 0, 360);
        g.fillArc(330, 500, 20, 20, 0, 360);
        g.fillArc(306, 515, 20, 20, 0, 360);
        g.fillArc(323, 515, 20, 20, 0, 360);
        g.setColor(Color.yellow);
        g.fillArc(315, 503, 20, 20, 0, 360);
        g.setColor(Color.black);
        g.fillRect(323, 525, 5, 70);

        g.setColor(Color.pink);
        g.fillArc(190, 500, 20, 20, 0, 360);
        g.fillArc(205, 485, 20, 20, 0, 360);
        g.fillArc(220, 500, 20, 20, 0, 360);
        g.fillArc(196, 515, 20, 20, 0, 360);
        g.fillArc(217, 515, 20, 20, 0, 360);
        g.setColor(Color.yellow);
        g.fillArc(205, 503, 20, 20, 0, 360);
        g.setColor(Color.black);
        g.fillRect(215, 525, 5, 70);


        g.setColor(Color.gray);
        int[] x4 = {850, 1050, 1250};
        int[] y4 = {300, 200, 300};
        g.fillPolygon(x4, y4, 3);
        g.setColor(Color.lightGray);
        g.fillRect(850, 300, 400, 300);



        g.setColor(new Color(218, 238, 240));
        g.fillRect(1100, 350,100,100);
        g.setColor(new Color(170,70,0));
        g.drawRect(1100, 350, 100, 100);
        g.drawLine(1100, 400, 1200, 400);
        g.drawLine(1150, 350, 1150, 450);








        g.setColor(new Color(122, 104, 113));
        g.fillRect(900,400,130,200);
        g.setColor(new Color(79, 69, 77));
        g.drawRect(900,400,130,200);
        g.setColor(Color.yellow);
        g.fillArc(930, 480, 10, 10, 0, 360);




    }
}


public class pierwszy extends JPanel {
    public static void main(String[] args) {



        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj numer odpowiadajacy danej opcji");
        System.out.println("1.Rysunek \n2.Inicjaly\n3.Gra\n4.Rysunki3D");
        int x = scan.nextInt();
        switch(x){
            case 1:
                Okno fr = new Okno();
                rysunek rys = new rysunek();
                fr.getContentPane().add(rys);
                break;
            case 2:
                Okno fr2 = new Okno();
                Krzywe rys2 = new Krzywe();
                fr2.getContentPane().add(rys2);
                break;
            case 3:
                JFrame frame = new JFrame("Gra");
                Gra game = new Gra();
                frame.add(game);
                frame.setSize(500,500);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                int count = 1, c = 1;
                while(true){
                    game.moveRoad(count);
                    while(c <= 1){
                        game.repaint();
                        try{
                            Thread.sleep(5);
                        }
                        catch(Exception e){
                            System.out.println(e);
                        }
                        c++;
                    }
                    c = 1;
                    count++;
                    if(game.nOpponent < 4 && count % 200 == 0){
                        game.imageLoc[game.nOpponent] = "images/car_left_"+((int)((Math.random()*100)%3)+1)+".png";
                        game.lx[game.nOpponent] = 499;
                        int p = (int)(Math.random()*100)%4;
                        if(p == 0){
                            p = 250;
                        }
                        else if(p == 1){
                            p = 300;
                        }
                        else if(p == 2){
                            p = 185;
                        }
                        else{
                            p = 130;
                        }
                        game.ly[game.nOpponent] = p;
                        game.speedOpponent[game.nOpponent] = (int)(Math.random()*100)%2 + 2;
                        game.nOpponent++;
                        }
                    }
            case 4:
                System.out.println("Niestety nie ma, nie potrafilem");
            default:
                break;

        }
    }
}
