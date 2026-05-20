import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;

public class BusPassForm extends JFrame implements ActionListener {

    JTextField nameField, fromField, toField, mobileField;

    JButton submitBtn;

    Font f = new Font("Arial", Font.BOLD, 16);

    BusPassForm() {

        setTitle("Bus Pass Registration System");

        getContentPane().setBackground(new Color(230, 240, 255));

        JLabel title = new JLabel("BUS PASS REGISTRATION");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(60, 10, 350, 40);

        JLabel name = new JLabel("Name");
        name.setFont(f);
        name.setBounds(40, 70, 100, 30);

        JLabel from = new JLabel("From");
        from.setFont(f);
        from.setBounds(40, 120, 100, 30);

        JLabel to = new JLabel("To");
        to.setFont(f);
        to.setBounds(40, 170, 100, 30);

        JLabel mobile = new JLabel("Mobile");
        mobile.setFont(f);
        mobile.setBounds(40, 220, 100, 30);

        nameField = new JTextField();
        nameField.setBounds(160, 70, 180, 30);

        fromField = new JTextField();
        fromField.setBounds(160, 120, 180, 30);

        toField = new JTextField();
        toField.setBounds(160, 170, 180, 30);

        mobileField = new JTextField();
        mobileField.setBounds(160, 220, 180, 30);

        submitBtn = new JButton("Generate Pass");
        submitBtn.setBounds(120, 290, 180, 40);

        submitBtn.setBackground(Color.BLUE);
        submitBtn.setForeground(Color.WHITE);

        submitBtn.addActionListener(this);

        add(title);

        add(name);
        add(from);
        add(to);
        add(mobile);

        add(nameField);
        add(fromField);
        add(toField);
        add(mobileField);

        add(submitBtn);

        setSize(450, 420);

        setLayout(null);

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {

        String name = nameField.getText();
        String from = fromField.getText();
        String to = toField.getText();
        String mobile = mobileField.getText();

        Random r = new Random();

        int distance = r.nextInt(40) + 5;

        int fare = distance * 3;

        // Token Generate
       String token = "BUS" + (100000 + r.nextInt(900000));

        try {

            Connection con = DBConnection.getConnection();

            String query =
"INSERT INTO passdetails(name, fromplace, toplace, distance, fare, mobile, token) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, name);
            pst.setString(2, from);
            pst.setString(3, to);
            pst.setInt(4, distance);
            pst.setInt(5, fare);
            pst.setString(6, mobile);
            pst.setString(7, token);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Bus Pass Generated Successfully");

            // Current Window Close
            dispose();

            // Open New Pass Window
            new PassPage(name, from, to,
                    distance, fare, mobile, token);

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new BusPassForm();
    }
}



// ================= PASS PAGE =================

class PassPage extends JFrame {

    JTextArea passArea;

    JLabel timerLabel;

    int timeLeft = 30; // 30 seconds validity

    Timer timer;

    PassPage(String name, String from, String to,
             int distance, int fare,
             String mobile, String token) {

        setTitle("Generated Bus Pass");

        getContentPane().setBackground(new Color(230, 240, 255));

        JLabel heading = new JLabel("BUS PASS");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBounds(140, 10, 200, 40);

        // Timer Label
        timerLabel = new JLabel("Pass Valid For : 30 sec");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        timerLabel.setForeground(Color.RED);
        timerLabel.setBounds(90, 50, 250, 30);

        passArea = new JTextArea();

        passArea.setText(
                "========= BUS PASS =========\n\n" +
                "Token No  : " + token + "\n\n" +
                "Passenger : " + name + "\n\n" +
                "From      : " + from + "\n" +
                "To        : " + to + "\n" +
                "Distance  : " + distance + " KM\n\n" +
                "Fare      : Rs. " + fare + "\n\n" +
                "Mobile    : " + mobile + "\n" +
                "Status    : APPROVED"
        );

        passArea.setFont(new Font("Monospaced", Font.BOLD, 15));

        passArea.setEditable(false);

        passArea.setBounds(40, 100, 340, 320);

        add(heading);
        add(timerLabel);
        add(passArea);

        setLayout(null);

        setSize(450, 500);

        setVisible(true);
  
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Countdown Timer
        timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                timeLeft--;

                timerLabel.setText(
                        "Pass Valid For : " + timeLeft + " sec");

                if (timeLeft <= 0) {

                    timer.stop();

                    timerLabel.setText("PASS EXPIRED");

                    passArea.append(
                            "\n\nEXPIRED : Please Generate New Pass");
                }
            }
        });

        timer.start();
    }
}