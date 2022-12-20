import java.awt.*;

public class BreakOutBall extends Rectangle{

    BreakOutBall(int topLeftx, int topLefty, int width, int height){
        super(topLeftx, topLefty, width, height);
    }

    public boolean overlaps (BreakOutRect r) {
        //System.out.println(this.x + " " + this.y + " " + this.width + " " + this.height);
        //System.out.println(r.x + " " + r.y + " " + r.width + " " + r.height);
        return (this.x <= r.x + r.width) && (this.x+ this.width >= r.x) && (this.y <= r.y + r.height) && (this.y + this.height >= r.y);
    }

    public void moveL(){
        x = x - 5;
        y = y - 5;
    }

    public void moveT(){
        x = x + 5 ;
        y = y - 5 ;
    }

    public void moveB(){
        x = x - 5 ;
        y = y + 5 ;
    }

    public void moveR(){
        x = x + 5 ;
        y = y + 5 ;
    }

    public void draw(Graphics g) {
        g.setColor(Color.PINK);
        g.drawOval(x , y , width , height);
    }

}
