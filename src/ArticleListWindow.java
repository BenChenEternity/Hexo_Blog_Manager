import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class ArticleListWindow extends JFrame implements ActionListener {

    private final JList<String> articleList;
    private final DefaultListModel<String> listModel;
    private final JButton newButton;
    private final JButton deleteButton;
    private final JButton refreshButton;
    private final JButton detailsButton;

    public ArticleListWindow() {
        super("Article Manager");

        listModel = new DefaultListModel<>();
        articleList = new JList<>(listModel);

        newButton = new JButton("New Article");
        newButton.addActionListener(this);
        deleteButton = new JButton("Delete Article");
        deleteButton.addActionListener(this);
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);
        detailsButton = new JButton("Details");
        detailsButton.addActionListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(articleList), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(detailsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadArticles();

        setSize(450, 300);
        setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newButton) {
            Main.newer.setVisible(true);
        } else if (e.getSource() == deleteButton) {
            try {
                String path = String.valueOf(Paths.get(Main.Root, "source", "_posts"));
                String filename = articleList.getSelectedValue().replaceAll("\\s* \\(.*?\\)$", "");
                UIManager.put("OptionPane.yesButtonText", "Yes");
                UIManager.put("OptionPane.noButtonText", "No");
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure to delete the file?\n" + path + "\\" + filename + ".md\n" + path + "\\" + filename, "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        Files.delete(Path.of(path + "\\" + filename + ".md"));
                        Files.walk(Path.of(path + "\\" + filename))
                                .sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                        Main.ArticlePanel.loadArticles();
                        JOptionPane.showMessageDialog(null, "The selected files have been successfully deleted:\n" + path + "\\" + filename + ".md\n" + path + "\\" + filename, "Hint", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Failed to deleted files", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "No files are found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == refreshButton) {
            Main.ArticlePanel.loadArticles();
        } else if (e.getSource() == detailsButton) {
            String filename;
            try {
                filename = articleList.getSelectedValue().replaceAll("\\s* \\(.*?\\)$", "");
            } catch (NullPointerException ex) {
                return;
            }
            String path = String.valueOf(Paths.get(Main.Root, "source", "_posts"));
            String filePath = path + "\\" + filename + ".md";


            String content = FileParser.readFile(filePath);
            Map<String, String> keyValueMap = FileParser.parseContent(content);
            FileParser.displayKeyValueMap(keyValueMap, Path.of(filePath));

        }
    }

    void loadArticles() {
        listModel.clear();
        if (Main.Root == null) {
            return;
        }
        try {
            File folder = new File(Paths.get(Main.Root, "source", "_posts").toUri());
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".md")) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line = br.readLine();
                        if (line.equals("---")) {
                            line = br.readLine();
                            String value = null;
                            while (line != null && !line.equals("---")) {
                                if (line.startsWith("sticky: ")) {
                                    value = line.substring(8);
                                    break;
                                }
                                if (line.contains("top:") && (line.contains("true"))) {
                                    value = String.valueOf(2147483647);
                                    break;
                                }
                                line = br.readLine();
                            }
                            br.close();
                            String name = file.getName().substring(0, file.getName().length() - 3);
                            if (value != null) {
                                if (!value.equals(String.valueOf(2147483647))) {
                                    listModel.addElement(name + " (" + value + ")");
                                } else {
                                    listModel.addElement(name + " (top)");
                                }

                            } else {
                                listModel.addElement(name);
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "No markdown files found at:\n" + Paths.get(Main.Root, "source", "_posts"), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        sortArticles();
    }

    private void sortArticles() {
        List<String> articlesWithNumbers = new ArrayList<>();
        List<String> articlesWithoutNumbers = new ArrayList<>();
        Enumeration<String> enumeration = listModel.elements();
        while (enumeration.hasMoreElements()) {
            String article = enumeration.nextElement();
            if (article.matches(".*\\(\\d+\\).*")) {
                articlesWithNumbers.add(article);
            } else if (article.endsWith(" (top)")) {
                articlesWithNumbers.add(0, article);
            } else {
                articlesWithoutNumbers.add(article);
            }
        }
        articlesWithNumbers.sort((a, b) -> {
            String aVal = a.substring(a.indexOf("(") + 1, a.indexOf(")"));
            String bVal = b.substring(b.indexOf("(") + 1, b.indexOf(")"));
            if (a.endsWith(" (top)")) {
                aVal = String.valueOf(Integer.MAX_VALUE);
            }
            if (b.endsWith(" (top)")) {
                bVal = String.valueOf(Integer.MAX_VALUE);
            }
            return Integer.compare(Integer.parseInt(bVal), Integer.parseInt(aVal));
        });
        articlesWithoutNumbers.sort(String::compareTo);
        listModel.clear();
        for (String article : articlesWithNumbers) {
            listModel.addElement(article);
        }
        for (String article : articlesWithoutNumbers) {
            listModel.addElement(article);
        }
    }


}
