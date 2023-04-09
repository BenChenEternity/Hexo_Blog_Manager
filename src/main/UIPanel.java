import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.*;

public class UIPanel extends JFrame {

    public UIPanel() {
        super("Hexo blog manager ver1.0");
        setSize(600, 400);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                for (Process PRO : Main.childProcess) {
                    try {
                        PRO.getInputStream().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        PRO.getOutputStream().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    PRO.destroy();
                }
                LocalServer.KillProcessOccupyingPort();
                System.exit(0);
            }
        });

        for (int i = 1; i <= 6; i++) {
            JButton button = new JButton();
            try {
                InputStream ImageInput;

                Image I;
                URL U = Main.class.getResource("/resource/" + i + ".png");
                assert U != null;
                ImageInput = U.openStream();
                I = ImageIO.read(ImageInput);
                button.setIcon(new ImageIcon(I));
            } catch (IOException e) {
                e.printStackTrace();
            }
            button.setPreferredSize(new Dimension(200, 200));
            int finalI = i;
            button.addActionListener(e -> {
                switch (finalI) {
                    case 1: {
                        Main.ArticlePanel.setVisible(true);
                        Main.ArticlePanel.loadArticles();
                        break;
                    }
                    case 2: {
                        Main.Deployer.frame.setVisible(true);
                        break;
                    }
                    case 3: {
                        Main.Options.setVisible(true);
                        break;
                    }
                    case 4: {
                        if (Main.Root != null) {
                            File folder = new File(Paths.get(Main.Root,"source","_posts").toUri());

                            try {
                                if (folder.exists()) {
                                    Desktop.getDesktop().open(folder);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Folder does not exist.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "An error occurred:\n " + ex.getMessage(), "Hint", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Folder does not exist.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    }
                    case 5: {
                        Main.Info.setVisible(true);
                        break;
                    }
                    case 6: {
                        String url = "https://ajie.wiki/2023/04/09/TheHexoBlogManagerDOCS-en-US/";
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            });
            panel.add(button);
        }
        add(panel);
        setVisible(true);
    }

    public static void setIconForWindows() {
        try {
            Image I;
            URL U = Main.class.getResource("/resource/Icon.png");
            InputStream ImageInput;
            assert U != null;
            ImageInput = U.openStream();
            I = ImageIO.read(ImageInput);
            Main.GUI.setIconImage(I);
            Main.Deployer.frame.setIconImage(I);
            Main.newer.setIconImage(I);
            Main.Options.setIconImage(I);
            Main.TGManager.setIconImage(I);
            Main.ArticlePanel.setIconImage(I);
            Main.Info.setIconImage(I);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
