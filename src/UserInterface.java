import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class UserInterface extends JFrame {

    private JTextArea textArea = new JTextArea("Enter text",30,50);
    private JPanel buttonPanel = new JPanel();
    private JButton italicsButton = new JButton("I");
    private JButton boldButton = new JButton("B");
    private JButton plainButton = new JButton("P");
    private JComboBox fontsBox = new JComboBox();
    private JComboBox sizeBox = new JComboBox();
    private int style, size;
    private JMenuBar menuBar = new JMenuBar();

    public UserInterface() {
        super("Text Redactor");
        setIconImage(new ImageIcon("resourses/textIcon.png").getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600,600);
        setResizable(true);
        setLayout(new BorderLayout());
        italicsButton.setFont(new Font("Times new Roman",Font.ITALIC,20));
        boldButton.setFont(new Font("Times new Roman",Font.BOLD,20));
        plainButton.setFont(new Font("Times new Roman",Font.PLAIN,20));
        //create ComboBox model for fonts syle
        String[] items = {"Times New Roman", "Verdana", "Helvetica", "Courier", "Arial"};
        DefaultComboBoxModel fontsModel = new DefaultComboBoxModel();
        for (int i = 0; i < items.length; i++) {
            fontsModel.addElement(items[i]);
        }
        fontsBox.setModel(fontsModel);
        //create ComboBox model for fonts size
        int[] sizeItems = {10,12,14,16,18,20,24,36};
        DefaultComboBoxModel sizeModel = new DefaultComboBoxModel();
        for (int i = 0; i < sizeItems.length; i++) {
            sizeModel.addElement(sizeItems[i]);
        }
        sizeBox.setModel(sizeModel);
        size = (int)sizeBox.getSelectedItem();
        //create menu
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu aboutMenu = new JMenu("About");
        //create menu items
        JMenuItem newMenuItem = new JMenuItem("New",new ImageIcon("resourses/textIcon.png"));
        JMenuItem openMenuItem = new JMenuItem("Open",new ImageIcon("resourses/openIcon.png"));
        JMenuItem saveMenuItem = new JMenuItem("Save As", new ImageIcon("resourses/saveIcon.png"));
        JMenuItem quitMenuItem = new JMenuItem("Quit",new ImageIcon("resourses/quitIcon.png"));

        JMenuItem copyMenuItem = new JMenuItem("Copy",new ImageIcon("resourses/copyIcon.png"));
        JMenuItem cutMenuItem = new JMenuItem("Cut",new ImageIcon("resourses/cutIcon.png"));
        JMenuItem pasteMenuItem = new JMenuItem("Paste",new ImageIcon("resourses/pasteIcon.png"));

        JMenuItem aboutMenuItem = new JMenuItem("About",new ImageIcon("resourses/aboutIcon.png"));

        Clipboard clipboard = getToolkit().getSystemClipboard();

        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new FileReader(file));
                        for (;;){
                            String str = in.readLine();
                            if (str == null) break;
                            textArea.append(str + "\n");
                        }
                    } catch (IOException ex){
                        ex.printStackTrace();
                    } finally {
                        if (in != null){
                            try {
                                in.close();
                            } catch (IOException ex){}
                        }
                    }
                }
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        if (!file.exists())
                            file.createNewFile();
                        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
                        try{
                            out.print(textArea.getText());
                        }finally {
                            out.close();
                        }
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        quitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        copyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = textArea.getSelectedText();
                if (selection == null) return;
                StringSelection clipString = new StringSelection(selection);
                clipboard.setContents(clipString,clipString);
            }
        });

        cutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selection = textArea.getSelectedText();
                if (selection == null) return;
                StringSelection clipString = new StringSelection(selection);
                clipboard.setContents(clipString,clipString);
                textArea.replaceRange("",textArea.getSelectionStart(),textArea.getSelectionEnd());
            }
        });

        pasteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Transferable clipData = clipboard.getContents(copyMenuItem);
                try{
                    String clipString = (String)clipData.getTransferData(DataFlavor.stringFlavor);
                    textArea.replaceRange(clipString,textArea.getSelectionStart(),textArea.getSelectionEnd());
                } catch (Exception ex){
                    System.err.println("No string");
                }
            }
        });

        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InformDialog();
            }
        });

        italicsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(new Font("Times new Roman",Font.ITALIC,size));
                style = Font.ITALIC;
            }
        });

        boldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(new Font("Times new Roman",Font.BOLD,size));
                style = Font.BOLD;
            }
        });

        plainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(new Font("Times new Roman",Font.PLAIN,size));
                style = Font.PLAIN;
            }
        });

        fontsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(new Font((String) fontsBox.getSelectedItem(),style,size));
            }
        });

        sizeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(new Font((String) fontsBox.getSelectedItem(),style,(int)sizeBox.getSelectedItem()));
                size = (int)sizeBox.getSelectedItem();
            }
        });

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(quitMenuItem);

        editMenu.add(copyMenuItem);
        editMenu.add(cutMenuItem);
        editMenu.add(pasteMenuItem);

        aboutMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(aboutMenu);

        textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        buttonPanel.add(italicsButton);
        buttonPanel.add(boldButton);
        buttonPanel.add(plainButton);
        buttonPanel.add(fontsBox);
        buttonPanel.add(sizeBox);

        getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
        getContentPane().add(buttonPanel,BorderLayout.SOUTH);

        setJMenuBar(menuBar);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
