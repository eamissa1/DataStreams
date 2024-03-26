import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.util.Arrays;

public class DataStreamGUI extends JFrame {
    private JTextArea originalTextArea = new JTextArea();
    private JTextArea filteredTextArea = new JTextArea();
    private JTextField searchField = new JTextField();
    private JButton loadButton = new JButton("Load File");
    private JButton searchButton = new JButton("Search");
    private JButton quitButton = new JButton("Quit");
    private JFileChooser fileChooser = new JFileChooser();

    public DataStreamGUI()
    {
        setTitle("Data Streams Lab");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents()
    {
        JPanel controlPanel = new JPanel();
        controlPanel.add(loadButton);

        searchField.setPreferredSize(new Dimension(200, 30)); // Set preferred size of the search field
        controlPanel.add(searchField);

        controlPanel.add(searchButton);
        controlPanel.add(quitButton);

        originalTextArea.setEditable(false);
        originalTextArea.setLineWrap(true);
        originalTextArea.setWrapStyleWord(true);

        filteredTextArea.setEditable(false);
        filteredTextArea.setLineWrap(true);
        filteredTextArea.setWrapStyleWord(true);

        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, originalScrollPane, filteredScrollPane);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        searchButton.setEnabled(false);
    }

    private void loadFile()
    {
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            try (Stream<String> lines = Files.lines(fileChooser.getSelectedFile().toPath()))
            {
                originalTextArea.setText("");
                lines.forEach(line -> originalTextArea.append(line + "\n"));
                searchButton.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty())
        {
            try (Stream<String> lines = Files.lines(fileChooser.getSelectedFile().toPath()))
            {
                filteredTextArea.setText("");
                String sentenceBoundary = "\\.";
                lines.map(line -> line.split(sentenceBoundary))
                        .flatMap(Arrays::stream)
                        .filter(sentence -> sentence.contains(searchText))
                        .forEach(sentence -> filteredTextArea.append(sentence.trim() + ".\n"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error processing file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
