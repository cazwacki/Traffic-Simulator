import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controller extends JPanel implements ChangeListener, ActionListener {
    
    int[] currentLights = new int[4];
    public static int[] ryg = new int[12];
    SpringLayout sl = new SpringLayout();
    JLabel redL = new JLabel("Red"), yellowL = new JLabel("Yellow"), greenL = new JLabel("Green");
    JSpinner red, green, yellow;
    SpinnerModel redM, greenM, yellowM;
    JLabel focus = new JLabel("FOCUS: UPPER");
    JButton save = new JButton("Save to Intersection"), start = new JButton("Start");
    JLabel flushed = new JLabel("Cars Flushed: ");
    boolean focusb;
    
    public Controller() {
        super();
        setLayout(sl);
        setPreferredSize(new Dimension(300, 700));
        initiateSpinners();
        addAndConstrainAll();
        lazy();
    }
    
    private void constrain(Component c, String relativeTo, int distance, Component relativeComp, String relativeFrom) {
        sl.putConstraint(relativeTo, c, distance, relativeFrom, relativeComp);
    }
    
    private void lazy() {
        for(int i = 0; i < ryg.length; i++) {
            ryg[i] = 5;
        }
    }
    
    private void addAndConstrainAll() {
        addAndConstrain();
        constrain(focus, "HorizontalCenter", -20, this, "HorizontalCenter");
        constrain(focus, "North", 30, this, "North");
        constrain(flushed, "HorizontalCenter", -20, this, "HorizontalCenter");
        constrain(flushed, "North", 275, this, "North");
        constrain(start, "HorizontalCenter", -20, this, "HorizontalCenter");
        constrain(start, "North", 150, this, "North");
        add(start);
        add(focus);
        add(flushed);        
    }
    
    private void initiateSpinners() {
        redM = new SpinnerNumberModel(1, 1, 120, 1);
        red = new JSpinner(redM);
        yellowM = new SpinnerNumberModel(1, 1, 10, 1);
        yellow = new JSpinner(yellowM);
        greenM = new SpinnerNumberModel(1, 1, 120, 1);
        green = new JSpinner(greenM);
    }
    
    private void addAndConstrain() {
        constrain(yellow, "North", 60, this, "North");
        constrain(red, "North", 60, this, "North");
        constrain(green, "North", 60, this, "North");
        constrain(red, "West", 5, redL, "East");
        constrain(redL, "North", 2, red, "North");
        constrain(yellowL, "West", 10, red, "East");
        constrain(yellow, "West", 5, yellowL, "East");
        constrain(yellowL, "North", 2, yellow, "North");
        constrain(greenL, "West", 10, yellow, "East");
        constrain(green, "West", 5, greenL, "East");
        constrain(greenL, "North", 2, green, "North");
        constrain(save, "HorizontalCenter", -20, this, "HorizontalCenter");
        constrain(save, "North", 100, this, "North");
        save.addActionListener(this);
        start.addActionListener(this);
        add(save);
        add(redL);
        add(yellowL);
        add(greenL);
        add(red);
        add(green);
        add(yellow);
    }
    
    protected void setFocus(String focus) {
        this.focus.setText("FOCUS: " + focus.toUpperCase());
    }
    
    public void setFocus(boolean focus) {
        focusb = focus;
        if(focusb) {
            redM.setValue(ryg[6]);
            yellowM.setValue(ryg[7]);
            greenM.setValue(ryg[8]);
        } else {
            redM.setValue(ryg[0]);
            yellowM.setValue(ryg[1]);
            greenM.setValue(ryg[2]);
        }
    }
    
    public void stateChanged(ChangeEvent arg0) {
        
    }
    
    public void setFlushed(int flushed) {
        this.flushed.setText("Flushed Cars: " + flushed);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if(arg0.getSource() == save) {
            if(!focusb) {
                ryg[0] = (Integer) redM.getValue();
                ryg[1] = (Integer) yellowM.getValue();
                ryg[2] = (Integer) greenM.getValue();
                ryg[3] = ryg[2];
                ryg[4] = ryg[1];
                ryg[5] = ryg[0];
                System.out.println("Saved to Upper!");
            } else {
                ryg[6] = (Integer) redM.getValue();
                ryg[7] = (Integer) yellowM.getValue();
                ryg[8] = (Integer) greenM.getValue();
                ryg[9] = ryg[8];
                ryg[10] = ryg[7];
                ryg[11] = ryg[6];
                System.out.println("Saved to Lower!");
            }
            for(int o : ryg) {
                System.out.println(o);
            }
        }
        if(arg0.getSource() == start) {
            boolean zeroExists = false;	
            for(int testfor0 : ryg) {
                if(testfor0 == 0) {
                    zeroExists = true;
                }
            }
            System.out.println(zeroExists);
            if(!zeroExists) {
                System.out.println("did i even get this far honestly");
                Runner.map.run();    
            }
        }
    }
    
}

