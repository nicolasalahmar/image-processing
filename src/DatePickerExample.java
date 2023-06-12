import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class DatePickerExample extends JFrame {
    private JPanel panel;
    private JDateChooser dateChooser;
    private JButton retrieveButton;

    public DatePickerExample() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);

        dateChooser = new JDateChooser();
        panel.add(dateChooser);

        retrieveButton = new JButton("Retrieve Date");
        retrieveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date selectedDate = dateChooser.getDate();
                System.out.println("Selected date: " + selectedDate);
            }
        });
        panel.add(retrieveButton);
    }

    public static void main(String[] args) {
        DatePickerExample example = new DatePickerExample();
        example.setVisible(true);
    }
}
