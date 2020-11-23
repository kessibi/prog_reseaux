package stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.*;
import java.awt.Color;

public class ChatWindow extends JFrame implements ActionListener {
	
	int WIDTH = 600;
	int HEIGHT = 800;
	private JButton connexion;
	private JButton deconnexion;
	private JButton envoyer;
	private JTextField ip;
	private JTextField port;
	EchoClient client;

	public ChatWindow () {
		
        this.setTitle("Chat");          
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
		
        //panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(Color.gray);
        panelPrincipal.setBounds(0, 0, WIDTH, HEIGHT);
        panelPrincipal.setLayout(null);
        add(panelPrincipal);
        
        //zone de texte du chat
        JTextArea grandeZone = new JTextArea();
        grandeZone.setBackground(Color.white);
        grandeZone.setBounds(50, 100, WIDTH-100, HEIGHT-300);
        
        //texte a renvoer chat
        JTextField response = new JTextField();
        response.setBackground(Color.white);
        response.setBounds(50, 700, WIDTH-100, 20);
        
        //bouton envoyer
        envoyer = new JButton ("Envoyer");
        envoyer.setBounds(400, 750, 100, 20);
        envoyer.addActionListener(this);
        
        //infos serveur
        ip = new JTextField();
        JLabel ip_name = new JLabel("ip");
        port = new JTextField(5);
        JLabel port_name = new JLabel("port");
        ip.setBounds(100, 20, 100, 20);
        ip_name.setBounds(50, 20, 100, 20);
        port.setBounds(100, 50, 100, 20);
        port_name.setBounds(50, 50, 100, 20);
        
        //bouton connexion 
        connexion = new JButton ("Se connecter");
        connexion.setBounds(300, 35, 100, 20);
        connexion.addActionListener(this);
        deconnexion = new JButton ("Se deconnecter");
        deconnexion.setBounds(450, 35, 100, 20);
        deconnexion.addActionListener(this);
        
        
        panelPrincipal.add(ip);
        panelPrincipal.add(ip_name);
        panelPrincipal.add(port);
        panelPrincipal.add(port_name);
        panelPrincipal.add(connexion);
        panelPrincipal.add(deconnexion);
        panelPrincipal.add(grandeZone);
        panelPrincipal.add(response);
        panelPrincipal.add(envoyer);
        panelPrincipal.updateUI();
        
	}
	
	public void actionPerformed(ActionEvent evt) {
			Object o = evt.getSource();
			if (o == connexion) {
				System.out.println("Connexion");
				client = new EchoClient (ip.getText(), port.getText());
			}
			if (o == deconnexion) {
				System.out.println("Deconnexion");
			}
			if (o == envoyer) {
				System.out.println("Envoyer");
			}
		};
}
