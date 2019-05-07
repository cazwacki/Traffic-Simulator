import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

//DECIDED SCALE: 3X

public class Runner {
    
    static Container frame = new Container();
    static Controller control = new Controller();
    static Map map = new Map();
    static JPanel container = new JPanel();
    public static boolean isRunning;
    
    private void run(String[] args) {
        SpringLayout sl = new SpringLayout();
        container.setLayout(sl);
        container.add(map);
        sl.putConstraint(SpringLayout.EAST, control, 1000, SpringLayout.EAST, frame);
        container.add(control);
        frame.setContentPane(container);
        frame.setVisible(true); 
    }
    
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Runner code = new Runner();
        code.run(args);
    }
    
    public static void toggleFocus() {
        if(map.focusOnLower) {
            control.setFocus("Lower");
            control.setFocus(true);
        } else {
            control.setFocus("Upper");
            control.setFocus(false);
        }
    }
    
}

