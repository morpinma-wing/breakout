import java.awt.*;

public class BreakOutRect extends Rectangle {

    BreakOutRect(int topLeftx, int topLefty, int width, int height){
        super(topLeftx, topLefty, width, height);
    }

    public void move(Point point){
        x = point.x - BreakoutConstants.CANVAS_WIDTH/2;
    }

    public void draw(Graphics g, Color color) {
        //g.fillRect(x, y, width, height);
        g.setColor(color);
        g.drawRect(x , y , width , height);
    }

}
