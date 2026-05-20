import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SearchPass extends JFrame implements ActionListener {

    JLabel title, tokenLabel;

    JTextField tokenField;

    JButton searchBtn;

    JTextArea resultArea;

    Font f = new Font("Arial", Font.BOLD, 16);

    SearchPass() {

        setTitle("Search Bus Pass");

        getContentPane().setBackground(new Color(230, 240, 255));

        title = new JLabel("TOKEN VERIFICATION");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(70, 20, 320, 40);

        tokenLabel = new JLabel("Enter Token");
        tokenLabel.setFont(f);
        tokenLabel.setBounds(40, 100, 150, 30);

        tokenField = new JTextField();
        tokenField.setBounds(180, 100, 160, 30);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(120, 170, 150, 40);

        resultArea = new JTextArea();
        resultArea.setBounds(40, 250, 340, 250);

        resultArea.setEditable(false);

        resultArea.setFont(
                new Font("Monospaced", Font.BOLD, 14));

        searchBtn.addActionListener(this);

        add(title);

        add(tokenLabel);

        add(tokenField);

        add(searchBtn);

        add(resultArea);

        setLayout(null);

        setSize(450, 580);

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {

        String token = tokenField.getText();

        try {

            Connection con = DBConnection.getConnection();

            String query =
            "SELECT * FROM passdetails WHERE token=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, token);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                resultArea.setText(

                        "======= BUS PASS DETAILS =======\n\n" +

                        "Token No  : " +
                        rs.getString("token") + "\n" +

                        "Passenger : " +
                        rs.getString("name") + "\n" +

                        "From      : " +
                        rs.getString("fromplace") + "\n" +

                        "To        : " +
                        rs.getString("toplace") + "\n" +

                        "Distance  : " +
                        rs.getInt("distance") + " K\n" +

                        "Fare      : Rs. " +
                        rs.getInt("fare") + "\n" +

                        "Mobile    : " +
                        rs.getString("mobile")

                );

            } else {

                JOptionPane.showMessageDialog(this,
                        "Invalid Token");
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new SearchPass();
    }
}