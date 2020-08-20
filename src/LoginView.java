import javax.swing.*;

public class LoginView extends JPanel {

    private final Client _model;

    private final JTextField username;
    private final JButton  login;

    public LoginView(Client model) {

        this._model = model;

        setLayout(null);

        JLabel info = new JLabel("<html>Username must be a max of 12 characters and may not contain a ','</html>");
        info.setSize(290,30);
        info.setLocation(5, 100);
        add(info);

        JLabel usernameLbl = new JLabel("Username: ");
        usernameLbl.setSize(100, 30);
        usernameLbl.setLocation(50, 150);
        usernameLbl.setBorder(BorderFactory.createEmptyBorder());
        usernameLbl.setOpaque(false);
        add(usernameLbl);

        username = new JTextField();
        username.setSize(100,30);
        username.setLocation(120,150);
        add(username);

        login = new JButton("Login");
        login.setSize(100,30);
        login.setLocation(100,200);
        login.setName("LOGIN");
        add(login);


        setSize(300,300);
    }

    // Getters and Setters
    public JButton getLogin() {return this.login;}
    public JTextField getUsername() {return this.username;}
}
