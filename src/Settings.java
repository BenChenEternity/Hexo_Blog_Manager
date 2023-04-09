import javax.swing.*;
import java.awt.*;

public class Settings extends JFrame {
    JTextField rootDirTextField;
    JTextField webpDirTextField;
    JButton submitButton;
    JButton loadButton;


    public Settings() {
        setTitle("Settings");
        rootDirTextField = new JTextField(20);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String rootDirectory = rootDirTextField.getText();
            Main.Root = rootDirectory;
            PropertiesFile.saveToSettingsFile("RootDirectory", rootDirectory);

            String webpDirectory = webpDirTextField.getText();
            Main.WebpPath = webpDirectory;
            PropertiesFile.saveToSettingsFile("webpDirectory", webpDirectory);
        });

        loadButton = new JButton("Load");
        loadButton.addActionListener(e -> {
            String rootDirectory = PropertiesFile.loadFromSettingsFile("RootDirectory");
            String webpDirectory = PropertiesFile.loadFromSettingsFile("webpDirectory");
            rootDirTextField.setText(rootDirectory);
            webpDirTextField.setText(webpDirectory);
            Main.Root = rootDirectory;
            Main.WebpPath = webpDirectory;
        });

        webpDirTextField = new JTextField(20);
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Root path of hexo blog: "));
        inputPanel.add(rootDirTextField);
        inputPanel.add(new JLabel("Path of webp processor: "));
        inputPanel.add(webpDirTextField);
        container.add(inputPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(loadButton);
        container.add(buttonPanel, BorderLayout.SOUTH);

        String rootDirectory = PropertiesFile.loadFromSettingsFile("RootDirectory");
        String webpDirectory = PropertiesFile.loadFromSettingsFile("webpDirectory");
        rootDirTextField.setText(rootDirectory);
        webpDirTextField.setText(webpDirectory);
        Main.Root = rootDirectory;
        Main.WebpPath = webpDirectory;

        setLocationRelativeTo(null);
        pack();
    }
}