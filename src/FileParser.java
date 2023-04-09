import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class FileParser {
    static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skip = true;
            while ((line = br.readLine()) != null) {
                if (skip && line.equals("---")) {
                    skip = false;
                    continue;
                }
                if (!skip && line.equals("---")) {
                    break;
                }
                if (!skip) {
                    contentBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    static Map<String, String> parseContent(String content) {
        Map<String, String> keyValueMap = new HashMap<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            String[] keyValue = line.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                keyValueMap.put(key, value);
            }
        }
        return keyValueMap;
    }


    static void displayKeyValueMap(Map<String, String> keyValueMap,Path filePath) {
        List<String> keyOrder = Arrays.asList(
                "title", "date", "updated", "categories", "tags", "sticky", "keywords",
                "description", "thumbnail", "cover", "top", "comments", "excerpt", "toc", "mathjax");

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> keyList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(keyList);


        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 5));
        for (String key : keyOrder) {
            String value = keyValueMap.get(key);
            if (value != null) {
                JTextField textField = new JTextField(value);
                panel.add(new JTextField(key));
                panel.add(new JLabel(":"));
                panel.add(textField);
                listModel.addElement(key);
            }
        }

        JButton addButton = new JButton("Add new");
        addButton.addActionListener(e -> {
            String newKey = "key";
            JTextField textField = new JTextField("");
            panel.add(new JTextField(newKey));
            panel.add(new JLabel(":"));
            panel.add(textField);
            listModel.addElement(newKey);
            panel.revalidate();
            panel.repaint();
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            int selectedIndex = keyList.getSelectedIndex();
            if (selectedIndex != -1) {
                String keyToRemove = listModel.get(selectedIndex);
                Component[] components = panel.getComponents();
                for (int i = 0; i < components.length - 2; i += 3) {
                    if (components[i] instanceof JTextField && ((JTextField) components[i]).getText().equals(keyToRemove)) {
                        panel.remove(components[i]);
                        panel.remove(components[i + 1]);
                        panel.remove(components[i + 2]);
                        break;
                    }
                }
                listModel.remove(selectedIndex);
                panel.revalidate();
                panel.repaint();
                keyValueMap.remove(keyToRemove);
            }
        });



        JButton submitButton = new JButton("Submit Changes");
        submitButton.addActionListener(e -> {
            Component[] components = panel.getComponents();
            for (int i = 0; i < components.length; i += 3) {
                if (components[i] instanceof JTextField) {
                    String oldKey = listModel.get(i / 3);
                    String newKey = ((JTextField) components[i]).getText();
                    String value = ((JTextField) components[i + 2]).getText();
                    if (!newKey.equals(oldKey)) {
                        keyValueMap.remove(oldKey);
                        keyValueMap.put(newKey, value);
                        listModel.set(i / 3, newKey);
                    } else {
                        keyValueMap.put(newKey, value);
                    }
                }
            }

            Map<String, String> newKeyValueMap = new LinkedHashMap<>();
            for (String key : keyOrder) {
                if (newKeyValueMap.containsKey(key)) {
                    continue;
                }
                if (keyValueMap.containsKey(key)) {
                    newKeyValueMap.put(key, keyValueMap.get(key));
                }
            }

            String markdownContent = null;
            try {
                markdownContent = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "File not found", "Hint", JOptionPane.INFORMATION_MESSAGE);
            }
            assert markdownContent != null;
            String[] markdownParts = markdownContent.split("---");
            if (markdownParts.length < 3) {
                JOptionPane.showMessageDialog(null, "Invalid Markdown file format: no YAML front matter found", "Hint", JOptionPane.INFORMATION_MESSAGE);
            }
            String markdownBody = String.join("---", Arrays.copyOfRange(markdownParts, 2, markdownParts.length));

            StringBuilder yamlBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : newKeyValueMap.entrySet()) {
                yamlBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
            }
            String newMarkdownContent = "---" + System.lineSeparator() +
                    yamlBuilder.toString().trim() +
                    System.lineSeparator() +
                    "---" + System.lineSeparator() +
                    markdownBody.trim();
            try {
                Files.write(filePath, newMarkdownContent.getBytes(StandardCharsets.UTF_8));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to write markdown file.", "Hint", JOptionPane.INFORMATION_MESSAGE);
            }

            SwingUtilities.getWindowAncestor((Component)e.getSource()).dispose();

            String content = FileParser.readFile(String.valueOf(filePath));
            Map<String, String> updatedkeyValueMap = FileParser.parseContent(content);
            FileParser.displayKeyValueMap(updatedkeyValueMap, Path.of(filePath.toUri()));

            JOptionPane.showMessageDialog(null, "Applied new changes", "Hint", JOptionPane.INFORMATION_MESSAGE);
        });


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(submitButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(listScrollPane, BorderLayout.WEST);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JFrame frame = new JFrame("Details");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }




}
