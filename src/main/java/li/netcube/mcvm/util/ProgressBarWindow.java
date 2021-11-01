package li.netcube.mcvm.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressBarWindow extends JPanel {

    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JFrame frame;

    private String statusText;
    private int progressValue;

    public ProgressBarWindow() {
        super(new BorderLayout());
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize( new Dimension (512, 32));

        statusLabel = new JLabel();
        statusLabel.setText("Please Wait...");
        statusLabel.setPreferredSize( new Dimension (512, 32));

        //JPanel panel = new JPanel();
        add(progressBar,BorderLayout.PAGE_START);
        add(statusLabel,BorderLayout.PAGE_END);

        //add(panel, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Minecraft Virtual Machines Redistributable Installer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void setProgress(int value) {
        progressValue = value;
        ((ProgressBarWindow)frame.getContentPane()).progressBar.setValue(progressValue);
        ((ProgressBarWindow)frame.getContentPane()).progressBar.repaint();
    }

    public void setStatus(String value) {
        statusText = value;
        ((ProgressBarWindow)frame.getContentPane()).statusLabel.setText(statusText);
    }

    public void closeWindow() {
        frame.dispose();
    }
}
