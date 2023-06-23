import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ImageGUI gui = new ImageGUI();
            gui.setVisible(true);
            gui.setExtendedState(JFrame.MAXIMIZED_BOTH);
            gui.loadImage();
        });
    }
}
