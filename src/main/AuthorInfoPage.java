import java.awt.*;
import java.net.URI;
import javax.swing.*;
import javax.swing.border.*;

public class AuthorInfoPage extends JFrame {
    public AuthorInfoPage() {
        setSize(400, 300);
        setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        JLabel versionLabel = new JLabel("Hexo blog manager version: 1.0", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(versionLabel);
        JLabel thanksLabel = new JLabel("Thank you for using Hexo blog manager!", SwingConstants.CENTER);
        thanksLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(thanksLabel);
        JLabel emailLabel = new JLabel("Author's email: eternitybenthegreat@gmail.com", SwingConstants.CENTER);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(emailLabel);
        JLabel githubLabel = new JLabel("Author's GitHub homepage:", SwingConstants.CENTER);
        githubLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(githubLabel);
        JButton githubButton = new JButton("Visit");
        githubButton.setFont(new Font("Arial", Font.PLAIN, 14));
        githubButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        githubButton.addActionListener(e -> {
            String url = "https://github.com/BenChenEternity";
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        infoPanel.add(githubButton);
        add(infoPanel, BorderLayout.CENTER);
        setTitle("Author Information Page");
        setLocationRelativeTo(null);
    }
}
