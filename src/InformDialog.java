import javax.swing.*;
import java.awt.*;

public class InformDialog extends JDialog {

    private JPanel panel = new JPanel();

    public InformDialog() {
        setTitle("Information");
        setIconImage(new ImageIcon("resourses/textIcon.png").getImage());
        setSize(300,150);
        setResizable(false);

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JLabel label1 = new JLabel();
        label1.setIcon(new ImageIcon("resourses/aboutIcon.png"));
        panel.add(label1);
        createLabel("Simple Text Redactor",panel);
        createLabel("Autor: Lupandina Maria",panel);
        createLabel("created: 2018",panel);

        getContentPane().add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * add label to panel procedure
     * @param text
     * @param panel
     */
    private static void createLabel(String text,JPanel panel){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Times new Roman",Font.BOLD,16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
    }
}
