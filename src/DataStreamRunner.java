import javax.swing.*;

public class DataStreamRunner
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new DataStreamGUI().setVisible(true));
    }
}
