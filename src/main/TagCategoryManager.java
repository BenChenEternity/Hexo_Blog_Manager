import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class TagCategoryManager extends JFrame {
    private JTextField categoriesTextField, tagsTextField;
    private JList<String> categoriesList, tagsList;
    private JButton categoriesAddButton, tagsAddButton;
    private JButton categoriesRemoveButton, tagsRemoveButton;
    private Properties settings;
    DefaultComboBoxModel<String> model;

    public TagCategoryManager() {
        super("Tags & Categories Manager");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        model = (DefaultComboBoxModel<String>) Main.newer.categoryBox.getModel();

        settings = new Properties();
        try {
            FileInputStream in = new FileInputStream("settings.properties");
            settings.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        categoriesTextField = new JTextField();

        categoriesList = new JList<>(settings.getProperty("categories", "").split(","));
        JScrollPane categoriesScrollPane = new JScrollPane(categoriesList);

        categoriesAddButton = new JButton("+");
        categoriesAddButton.addActionListener(e -> {
            String newCategory = categoriesTextField.getText();
            if (!newCategory.contains(",")) {
                if (!newCategory.isEmpty()) {
                    String currentCategories = settings.getProperty("categories", "");
                    String[] CurrCategories = currentCategories.split(",");
                    for (String S : CurrCategories) {
                        if (S.equals(newCategory)) {
                            JOptionPane.showMessageDialog(null, "Category already exists.","Hint", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    settings.setProperty("categories", currentCategories + "," + newCategory);
                    saveSettings();
                    categoriesList.setListData(settings.getProperty("categories", "").split(","));
                    model.addElement(newCategory);
                }
                categoriesTextField.setText(null);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input.","Hint", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        categoriesRemoveButton = new JButton("-");
        categoriesRemoveButton.addActionListener(e -> {
            String selectedCategory = categoriesList.getSelectedValue();
            if (selectedCategory != null) {
                String currentCategories = settings.getProperty("categories", "");
                String newCategories = Arrays.stream(currentCategories.split(","))
                        .filter(c -> !c.equals(selectedCategory))
                        .reduce((c1, c2) -> c1 + "," + c2)
                        .orElse("");
                settings.setProperty("categories", newCategories);
                saveSettings();
                categoriesList.setListData(settings.getProperty("categories", "").split(","));
                model.removeElement(selectedCategory);
            }
        });

        tagsTextField = new JTextField();

        tagsList = new JList<>(settings.getProperty("tags", "").split(","));
        JScrollPane tagsScrollPane = new JScrollPane(tagsList);

        tagsAddButton = new JButton("+");
        tagsAddButton.addActionListener(e -> {
            String newTag = tagsTextField.getText();
            if (!newTag.contains(",")) {
                if (!newTag.isEmpty()) {
                    String currentTags = settings.getProperty("tags", "");
                    String[] CurrTags = currentTags.split(",");
                    for (String S : CurrTags) {
                        if (S.equals(newTag)) {
                            JOptionPane.showMessageDialog(null, "Tag already exists.","Hint", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    settings.setProperty("tags", currentTags + "," + newTag);
                    saveSettings();
                    tagsList.setListData(settings.getProperty("tags", "").split(","));
                }
                tagsTextField.setText(null);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input.","Hint", JOptionPane.INFORMATION_MESSAGE);
            }
            Main.newer.loadTags();
        });

        tagsRemoveButton = new JButton("-");
        tagsRemoveButton.addActionListener(e -> {
            String selectedTag = tagsList.getSelectedValue();
            if (selectedTag != null) {
                String currentTags = settings.getProperty("tags", "");
                String newTags = Arrays.stream(currentTags.split(","))
                        .filter(c -> !c.equals(selectedTag))
                        .reduce((c1, c2) -> c1 + "," + c2)
                        .orElse("");
                settings.setProperty("tags", newTags);
                saveSettings();
                tagsList.setListData(settings.getProperty("tags", "").split(","));
            }
            Main.newer.loadTags();
        });

        Container contentPane = getContentPane();
        GridBagLayout layout = new GridBagLayout();
        contentPane.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        contentPane.add(new JLabel("Categories"), c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(categoriesTextField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        contentPane.add(categoriesScrollPane, c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        contentPane.add(categoriesAddButton, c);

        c.gridx = 2;
        c.gridy = 2;
        c.anchor = GridBagConstraints.NORTH;
        contentPane.add(categoriesRemoveButton, c);

        c.gridx = 3;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        contentPane.add(new JLabel("Tags"), c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(tagsTextField, c);

        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 3;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        contentPane.add(tagsScrollPane, c);

        c.gridx = 5;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        contentPane.add(tagsAddButton, c);

        c.gridx = 5;
        c.gridy = 2;
        c.anchor = GridBagConstraints.NORTH;
        contentPane.add(tagsRemoveButton, c);

        contentPane.setPreferredSize(new Dimension(500, 500));
    }

    private void saveSettings() {
        try {
            FileOutputStream out = new FileOutputStream("settings.properties");
            settings.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
