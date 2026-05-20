

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {

    JLabel title, userLabel, passLabel;

    JTextField userField;

    JPasswordField passField;

    JButton loginBtn, registerBtn, verifybtn;

    Font f = new Font("Arial", Font.BOLD, 16);

    LoginPage() {

        setTitle("Bus Pass Login System");

        getContentPane().setBackground(new Color(230, 240, 255));

        title = new JLabel("LOGIN SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(110, 30, 250, 40);

        userLabel = new JLabel("Username");
        userLabel.setFont(f);
        userLabel.setBounds(40, 110, 120, 30);

        passLabel = new JLabel("Password");
        passLabel.setFont(f);
        passLabel.setBounds(40, 180, 120, 30);

        userField = new JTextField();
        userField.setBounds(160, 110, 180, 30);

        passField = new JPasswordField();
        passField.setBounds(160, 180, 180, 30);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(70, 270, 120, 40);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(210, 270, 120, 40);
        verifybtn = new JButton("Verify Token");
        verifybtn.setBounds(120, 330, 150, 40);

        loginBtn.addActionListener(this);
        registerBtn.addActionListener(this);
        verifybtn.addActionListener(this);

        add(title);

        add(userLabel);
        add(passLabel);

        add(userField);
        add(passField);

        add(loginBtn);
        add(registerBtn);
        add(verifybtn);

        setLayout(null);

        setSize(420, 420);

        setVisible(true);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {

        String username = userField.getText();

        String password = passField.getText();

        // REGISTER BUTTON
        if (e.getSource() == registerBtn) {

            try {

                Connection con = DBConnection.getConnection();

                String query =
                "INSERT INTO users(username,password) VALUES(?,?)";

                PreparedStatement pst =
                        con.prepareStatement(query);

                pst.setString(1, username);

                pst.setString(2, password);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Registration Successful");

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

        // LOGIN BUTTON
        if (e.getSource() == loginBtn) {

            try {

                Connection con = DBConnection.getConnection();

                String query =
                "SELECT * FROM users WHERE username=? AND password=?";

                PreparedStatement pst =
                        con.prepareStatement(query);

                pst.setString(1, username);

                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    JOptionPane.showMessageDialog(this,
                            "Login Successful");

                    dispose();

                    new BusPassForm();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Invalid Username or Password");
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
        if (e.getSource() == verifybtn) {

          new SearchPass();
        }
    }

    public static void main(String[] args) {

        new LoginPage();
    }
}