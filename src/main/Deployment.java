import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Deployment {
    JFrame frame;
    private JPanel panel;
    JButton btnGenerateAndOpen;
    private JLabel ServerStatus;
    public Deployment() {
        frame = new JFrame("Hexo Blog Manager");
        frame.setSize(350, 300);
        Main.localhost = null;
        Main.childProcess = new ArrayList<>();
        Main.TGManager = new TagCategoryManager();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        Dimension buttonSize = new Dimension(230, 50);

        JButton btnClear = new JButton("Clear");
        btnClear.setPreferredSize(buttonSize);
        btnClear.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        btnGenerateAndOpen = new JButton("Generate and open on localhost");
        btnGenerateAndOpen.setPreferredSize(buttonSize);
        btnGenerateAndOpen.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JButton btnGenerateAndDeploy = new JButton("Generate and deploy");
        btnGenerateAndDeploy.setPreferredSize(buttonSize);
        btnGenerateAndDeploy.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(btnClear);
        panel.add(Box.createVerticalStrut(30));

        ServerStatus = new JLabel();
        ServerStatus.setForeground(Color.RED);
        ServerStatus.setText("Local server not started");
        ServerStatus.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(btnGenerateAndOpen);
        panel.add(Box.createVerticalStrut(10));
        panel.add(ServerStatus);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JButton access = new JButton("Access in a web page");
        JButton shut = new JButton("Shut down the server");

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(access);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(shut);
        buttonPanel.add(Box.createHorizontalGlue());

        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnGenerateAndDeploy);
        panel.add(Box.createVerticalStrut(10));
        panel.add(Box.createVerticalGlue());

        btnClear.setMaximumSize(buttonSize);
        btnGenerateAndOpen.setMaximumSize(buttonSize);
        btnGenerateAndDeploy.setMaximumSize(buttonSize);

        frame.add(panel);
        frame.setLocationRelativeTo(null);

        btnClear.addActionListener(e -> {
            cmd CLEAN = new cmd(Main.Root, "hexo clean", true);
            CLEAN.execute();
            try {
                CLEAN.waitFor();
                JOptionPane.showMessageDialog(null, "Cleared", "Hint", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to clean.", "Hint", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnGenerateAndOpen.addActionListener(e -> {
            if (Main.serverOn) {
                try {
                    Main.localhost.getInputStream().close();
                    Main.localhost.getOutputStream().close();
                    Main.localhost.destroy();
                    btnGenerateAndOpen.setText("Generate and open on localhost");
                    Main.serverOn = false;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                cmd GENERATE = new cmd(Main.Root, "hexo g", true);
                GENERATE.execute();
                try {
                    GENERATE.waitFor();
                    new LocalServer().start();
                    btnGenerateAndOpen.setEnabled(false);
                    ServerStatus.setForeground(new Color(0, 128, 0));
                    ServerStatus.setText("Local server open on http://localhost:4000/");
                    ServerStatus.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to generate.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        access.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("http://localhost:4000/"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        shut.addActionListener(e -> {
            LocalServer.KillProcessOccupyingPort();
            ServerStatus.setForeground(Color.RED);
            ServerStatus.setText("Local server not started");
            ServerStatus.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            btnGenerateAndOpen.setEnabled(true);
        });

        btnGenerateAndDeploy.addActionListener(e -> {
            try {
                cmd GENERATE = new cmd(Main.Root, "hexo g", true);
                GENERATE.execute();
                GENERATE.waitFor();
                new cmd(Main.Root, "hexo d", true).execute();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to generate.", "Hint", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        String formattedDateTime = now.format(formatter);

        File dir2 = new File(Paths.get(Main.Root, "source", "backup").toUri());
        dir2.mkdir();

        File source = new File(Paths.get(Main.Root, "source", "_posts").toUri());
        File target = new File(Paths.get(Main.Root, "source", "backup", "backup[" + formattedDateTime + "]").toUri());

        try {
            FileCopy.copyDirectory(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class LocalServer extends Thread {
    public void run() {
        try {
            KillProcessOccupyingPort();
            cmd Server = new cmd(Main.Root, "hexo s", true);
            Server.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to start server", "Hint", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void KillProcessOccupyingPort() {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netstat -aon");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(":4000")) {
                    String[] parts = line.trim().split("\\s+");
                    String pid = parts[parts.length - 1];
                    ProcessBuilder taskkillBuilder = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /PID " + pid);
                    taskkillBuilder.start();
                    System.out.println("Killed process with PID " + pid);
                }
            }
            reader.close();
            inputStream.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class FileCopy {
    public static void copyDirectory(File source, File target) throws IOException {
        if (!source.isDirectory()) {
            return;
        }

        if (!target.exists()) {
            target.mkdir();
        }

        String[] files = source.list();
        for (String file : files) {
            File sourceFile = new File(source, file);
            File targetFile = new File(target, file);
            if (sourceFile.isDirectory()) {
                copyDirectory(sourceFile, targetFile);
            } else {
                copyFile(sourceFile, targetFile);
            }
        }
    }

    public static void copyFile(File source, File target) throws IOException {
        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(target);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }
}


