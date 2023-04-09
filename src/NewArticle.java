import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class NewArticle extends JFrame {

    private final JTextField titleField;
    JComboBox<String> categoryBox;
    private final JList<String> tagsList;
    private final JTextField priorityField;
    private final JTextField abstractField;
    private final JFileChooser thumbnailChooser;
    private final JFileChooser coverChooser;
    private JCheckBox enablePriority;
    private JCheckBox enableAbstract;
    private JCheckBox enableThumbnail;
    private JCheckBox enableCover;

    public NewArticle() {
        setTitle("Article Creator");
        setSize(450, 500);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Title:");
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(titleLabel, c);

        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(250, 20));
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(titleField, c);

        JLabel categoryLabel = new JLabel("Category:");
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(categoryLabel, c);

        String categoriesIn = PropertiesFile.loadFromSettingsFile("categories");
        String[] categoryArray = categoriesIn.split(",");
        categoryBox = new JComboBox<>(categoryArray);

        categoryBox.setPreferredSize(new Dimension(250, 20));
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(categoryBox, c);

        JLabel tagsLabel = new JLabel("Tags:");
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(tagsLabel, c);

        String tagsIn = PropertiesFile.loadFromSettingsFile("tags");
        String[] tagsArray = tagsIn.split(",");
        tagsList = new JList<>(tagsArray);

        tagsList.setPreferredSize(new Dimension(250, 150));
        tagsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane tagsScrollPane = new JScrollPane(tagsList);
        tagsScrollPane.setPreferredSize(new Dimension(250, 150));
        c.gridx = 1;
        c.gridy = 2;
        mainPanel.add(tagsScrollPane, c);

        JButton tagManager = new JButton("Categories & tags management");
        c.gridx = 1;
        c.gridy = 3;
        mainPanel.add(tagManager, c);

        JLabel priorityLabel = new JLabel("Priority:");
        c.gridx = 0;
        c.gridy = 4;
        mainPanel.add(priorityLabel, c);

        priorityField = new JTextField();
        priorityField.setPreferredSize(new Dimension(250, 20));
        c.gridx = 1;
        c.gridy = 4;
        mainPanel.add(priorityField, c);

        enablePriority = new JCheckBox("Enable", true);
        c.gridx = 2;
        c.gridy = 4;
        mainPanel.add(enablePriority, c);

        JLabel abstractLabel = new JLabel("Abstract:");
        c.gridx = 0;
        c.gridy = 5;
        mainPanel.add(abstractLabel, c);

        abstractField = new JTextField();
        abstractField.setPreferredSize(new Dimension(250, 20));
        c.gridx = 1;
        c.gridy = 5;
        mainPanel.add(abstractField, c);

        enableAbstract = new JCheckBox("Enable", true);
        c.gridx = 2;
        c.gridy = 5;
        mainPanel.add(enableAbstract, c);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");

        thumbnailChooser = new JFileChooser();
        thumbnailChooser.setDialogTitle("Choose Thumbnail Image");
        thumbnailChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        thumbnailChooser.setFileFilter(filter);

        coverChooser = new JFileChooser();
        coverChooser.setDialogTitle("Choose Cover Image");
        coverChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        coverChooser.setFileFilter(filter);

        JLabel thumbnailLabel = new JLabel("Thumbnail:");
        c.gridx = 0;
        c.gridy = 6;
        mainPanel.add(thumbnailLabel, c);

        JButton thumbnailButton = new JButton("Select Thumbnail");
        thumbnailButton.addActionListener(e -> {
            int result = thumbnailChooser.showOpenDialog(NewArticle.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = thumbnailChooser.getSelectedFile();
            }
        });
        c.gridx = 1;
        c.gridy = 6;
        mainPanel.add(thumbnailButton, c);

        enableThumbnail = new JCheckBox("Enable", true);
        c.gridx = 2;
        c.gridy = 6;
        mainPanel.add(enableThumbnail, c);

        JLabel coverLabel = new JLabel("Cover:");
        c.gridx = 0;
        c.gridy = 7;
        mainPanel.add(coverLabel, c);

        JButton coverButton = new JButton("Select Cover");
        coverButton.addActionListener(e -> {
            int result = coverChooser.showOpenDialog(NewArticle.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = coverChooser.getSelectedFile();
            }
        });
        c.gridx = 1;
        c.gridy = 7;
        mainPanel.add(coverButton, c);

        enableCover = new JCheckBox("Enable", true);
        c.gridx = 2;
        c.gridy = 7;
        mainPanel.add(enableCover, c);

        tagManager.addActionListener(e -> {
            Main.TGManager.setVisible(true);
        });

        enablePriority.addActionListener(e -> {
            priorityField.setEnabled(enablePriority.isSelected());
        });

        enableAbstract.addActionListener(e -> {
            abstractField.setEnabled(enableAbstract.isSelected());
        });

        enableThumbnail.addActionListener(e -> {
            thumbnailButton.setEnabled(enableThumbnail.isSelected());
        });

        enableCover.addActionListener(e -> {
            coverButton.setEnabled(enableCover.isSelected());
        });

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String title = titleField.getText();
            if (title.equals("")) {
                JOptionPane.showMessageDialog(null, "No input title.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (Files.exists(Paths.get(Main.Root, "source", "_posts", title + ".md"))) {
                JOptionPane.showMessageDialog(null, "The article has already exists.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String category = (String) categoryBox.getSelectedItem();
            List<String> selectedTags = tagsList.getSelectedValuesList();
            String[] tags = selectedTags.toArray(new String[0]);
            String priority = null;

            String abstractText = null;
            if (abstractField.isEnabled()) {
                abstractText = abstractField.getText();
            }
            if (priorityField.isEnabled()) {
                priority = priorityField.getText();
                if (!priority.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Invalid priority input.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            File thumbnailFile = null;
            File coverFile = null;

            if (thumbnailButton.isEnabled()) {
                thumbnailFile = thumbnailChooser.getSelectedFile();
                if (thumbnailFile == null) {
                    JOptionPane.showMessageDialog(NewArticle.this, "Please select thumbnail file.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            if (coverButton.isEnabled()) {
                coverFile = coverChooser.getSelectedFile();
                if (coverFile == null) {
                    JOptionPane.showMessageDialog(NewArticle.this, "Please select cover file.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }


            try {
                cmd NEW = new cmd(Main.Root, "hexo new " + title, true);
                NEW.execute();
                NEW.waitFor();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to create a new article.", "Hint", JOptionPane.INFORMATION_MESSAGE);
            }

            String pathPost = String.valueOf(Paths.get(Main.Root, "source", "_posts"));
            Path filePath = Paths.get(Main.Root, "source", "_posts", title + ".md");

            Path thumbnailTarget;
            if (thumbnailButton.isEnabled()) {
                thumbnailTarget = Paths.get(pathPost, title, thumbnailFile.getName());
                try {
                    Files.copy(thumbnailFile.toPath(), thumbnailTarget, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(NewArticle.this, "Failed to copy thumbnail image from:\n" + thumbnailFile.toPath() + "\nto\n" + thumbnailTarget, "Hint", JOptionPane.INFORMATION_MESSAGE);
                }
                img_process.InputImgPath = String.valueOf(thumbnailTarget);
                img_process.OutputImgPath = String.valueOf(Paths.get(pathPost, title, "thumbnail.webp"));
                img_process.webp(3);
                new File(thumbnailTarget.toUri()).delete();
            }


            Path coverTarget;
            if (coverButton.isEnabled()) {
                coverTarget = Paths.get(pathPost, title, coverFile.getName());
                try {
                    Files.copy(coverFile.toPath(), coverTarget, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(NewArticle.this, "Failed to copy cover image from:\n" + coverFile.toPath() + "\nto\n" + coverTarget, "Hint", JOptionPane.INFORMATION_MESSAGE);
                }
                img_process.InputImgPath = String.valueOf(coverTarget);
                img_process.OutputImgPath = String.valueOf(Paths.get(pathPost, title, "cover.webp"));
                img_process.webp(3);
                new File(coverTarget.toUri()).delete();
            }

            String year = null;
            String month = null;
            String day = null;

            try {
                String line;
                BufferedReader br = new BufferedReader(new FileReader(Paths.get(pathPost, title + ".md").toFile()));
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("date: ")) {
                        String[] parts = line.substring("date: ".length()).split("-");
                        year = parts[0];
                        month = parts[1];
                        day = parts[2].split(" ")[0];
                        break;
                    }
                }
                br.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }

            try {
                File file = new File(filePath.toUri());
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("tags:")) {
                        String tagsString = String.join(",", tags);
                        String metadata = "tags: [" + tagsString + "]\n" +
                                "categories: [" + category + "]\n";
                        if (abstractField.isEnabled()) {
                            metadata = metadata.concat("excerpt: " + abstractText + "\n");
                        }
                        if (priorityField.isEnabled()) {
                            metadata = metadata.concat("sticky: " + priority + "\n");
                        }
                        if (thumbnailButton.isEnabled()) {
                            metadata = metadata.concat("thumbnail: \"/" + year + "/" + month + "/" + day + "/" + title + "/thumbnail.webp\"\n");
                        }
                        if (coverButton.isEnabled()) {
                            metadata = metadata.concat("cover: \"/" + year + "/" + month + "/" + day + "/" + title + "/cover.webp\"\n");
                        }
                        stringBuilder.append(metadata);
                    } else {
                        stringBuilder.append(line).append("\n");
                    }
                }
                bufferedReader.close();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(stringBuilder.toString());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException ee) {
                System.out.println("Error: " + ee.getMessage());
            }
            Object[] options = {"Close", "View this article in a folder"};
            int selection = JOptionPane.showOptionDialog(null, "A new article was created.", "Hint", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (selection == 0) {
                Window window = JOptionPane.getRootFrame();
                window.dispose();
            } else {
                String openFolderPath = String.valueOf(Paths.get(Main.Root, "source", "_posts"));
                String selectFilePath = String.valueOf(Paths.get(Main.Root, "source", "_posts", title + ".md"));

                File folder = new File(openFolderPath);
                if (!folder.exists() || !folder.isDirectory()) {
                    JOptionPane.showMessageDialog(null, "The directory containing the post is missing.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                File file = new File(selectFilePath);
                if (!file.exists() || !file.isFile()) {
                    JOptionPane.showMessageDialog(null, "The article is missing.", "Hint", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String command = "explorer.exe /select,\"" + filePath + "\""; // Windows
                ProcessBuilder pb = new ProcessBuilder(command.split(" "));
                pb.directory(folder);
                try {
                    pb.start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            Main.ArticlePanel.loadArticles();
        });

        c.gridx = 1;
        c.gridy = 8;
        mainPanel.add(submitButton, c);
        add(mainPanel);

        setLocationRelativeTo(null);
    }

    public void loadTags() {
        String tagsIn = PropertiesFile.loadFromSettingsFile("tags");
        String[] tagsArray = tagsIn.split(",");
        tagsList.setListData(tagsArray);
    }
}
