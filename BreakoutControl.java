import javax.swing.*;

public class BreakoutControl extends JFrame {

    BreakoutControl() {

        this.add(new BreakOut());
        this.setTitle("BreakOut");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);

    }

    public static void main(String[] args) {
        new BreakoutControl();
    }

}