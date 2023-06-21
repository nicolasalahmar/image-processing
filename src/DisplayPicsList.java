import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class DisplayPicsList extends javax.swing.JFrame {
    private javax.swing.JLabel jLabel_Image;

    static ArrayList<File> images_files;

    /**
     * Creates new form Images_From_Folder_Navigation
     */
    public DisplayPicsList() {
        initComponents();
        // display first image
        showImage(pos);
    }

    // the index of the images
    int pos = 0;

    // get images list
    public static void setImages(ArrayList<File> files) {
        images_files = files;
    }

    public static ArrayList<File> getImages() {
        return images_files;
    }

    // display the image by index
    public void showImage(int index) {
        ArrayList<File> imagesList = getImages();

        File file = imagesList.get(index);
        Image image;
        try {
            image = ImageIO.read(file);
            Image scaled_image = image.getScaledInstance(jLabel_Image.getWidth(), jLabel_Image.getHeight(), Image.SCALE_SMOOTH);
            jLabel_Image.setIcon(new ImageIcon(scaled_image));
            jLabel_Image.setHorizontalAlignment(JLabel.CENTER);
            jLabel_Image.setVerticalAlignment(JLabel.CENTER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void initComponents() {

        jLabel_Image = new javax.swing.JLabel();
        JButton jButton1 = new JButton();
        JButton jButton_Next = new JButton();
        JButton jButton_Previous = new JButton();
        JButton jButton_Last = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("First");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton_Next.setText("Next");
        jButton_Next.addActionListener(evt -> jButton_NextActionPerformed());

        jButton_Previous.setText("Previous");
        jButton_Previous.addActionListener(this::jButton_PreviousActionPerformed);

        jButton_Last.setText("Last");
        jButton_Last.addActionListener(this::jButton_LastActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel_Image, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(200, 200, 200)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(jButton_Next, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jButton_Previous)
                                .addGap(49, 49, 49)
                                .addComponent(jButton_Last, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton_Next, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                        .addComponent(jButton_Previous, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                        .addComponent(jButton_Last, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>

    // The First , Next , Previous ,Last Navigation Buttons

    // First
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        pos = 0;
        showImage(pos);

    }

    // Next
    private void jButton_NextActionPerformed() {
        pos = pos + 1;
        if (pos >= getImages().size()) {
            pos = getImages().size() - 1;
        }
        showImage(pos);
    }

    // Previous
    private void jButton_PreviousActionPerformed(java.awt.event.ActionEvent evt) {
        pos = pos - 1;
        if (pos < 0) {
            pos = 0;
        }
        showImage(pos);
    }

    // Last
    private void jButton_LastActionPerformed(java.awt.event.ActionEvent evt) {
        pos = getImages().size() - 1;
        showImage(pos);
    }


    public static void display() {
if(!images_files.isEmpty()){

    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
             UnsupportedLookAndFeelException ex) {

        java.util.logging.Logger.getLogger(DisplayPicsList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(() -> new DisplayPicsList().setVisible(true));
}  else{System.out.println("No Results Found !!");}
}



}
