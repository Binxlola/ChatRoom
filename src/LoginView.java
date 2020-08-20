import javax.swing.*;

public class LoginView extends JPanel {

    private final Client _model;

    private final JTextField username;
    private final JButton profileImg, login;

    public LoginView(Client model) {

        this._model = model;

        setLayout(null);

        profileImg = new JButton(new ImageIcon("src\\profile.png"));
        profileImg.setSize(64,64);
        profileImg.setLocation(111, 50);
        profileImg.setName("PROFILE_IMAGE");
        add(profileImg);

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
        add(login);


        setSize(300,300);
    }

    // Getters and Setters
    public JButton getProfileImg() {return this.profileImg;}
    public JButton getLogin() {return this.login;}
    public JTextField getUsername() {return this.username;}
}
