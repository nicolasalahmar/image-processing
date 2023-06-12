import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.util.Date;

public class DatePickerPanel extends JPanel {
    private JDateChooser dateChooser;

    public DatePickerPanel() {
        dateChooser = new JDateChooser();
        add(dateChooser);
    }

    public Date getSelectedDate() {
        return dateChooser.getDate();
    }
}
