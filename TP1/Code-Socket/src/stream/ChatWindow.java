package stream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.BorderLayout;
import java.awt.Color;

public class ChatWindow extends JFrame implements ActionListener {
	
	int WIDTH = 700;
	int HEIGHT = 900;
	private JButton connexion;
	private JButton deconnexion;
	private JButton envoyer;
	private JTextField ip;
	private JTextField port;
	private JTextField response;
	private JTextArea grandeZone;
	Socket echoSocket = null;
	PrintStream socOut = null;
	BufferedReader socIn = null;

	public ChatWindow () {
		
        this.setTitle("Chat");          
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
		
        //panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(94, 141, 168));
        panelPrincipal.setBounds(0, 0, WIDTH, HEIGHT);
        panelPrincipal.setLayout(null);
        add(panelPrincipal);
        
        //zone de texte du chat
        grandeZone = new JTextArea();
        grandeZone.setBackground(new Color(237, 252, 237));
        grandeZone.setBounds(50, 100, WIDTH-100, HEIGHT-300);
        // grandeZone.setOpaque( false );
        // JLabel label = new JLabel( new ImageIcon("../../images/background2.jpg") );
        // label.setLayout( new BorderLayout() );
        // label.add( grandeZone );
        
        //texte a renvoyer chat
        response = new JTextField();
        response.setBackground(Color.white);
        response.setBounds(50, 700, WIDTH-100, 20);
        
        //bouton envoyer
        envoyer = new JButton ("send");
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
        connexion = new JButton ("enter");
        connexion.setBounds(400, 35, 100, 20);
        connexion.addActionListener(this);
        deconnexion = new JButton ("leave");
        deconnexion.setBounds(550, 35, 100, 20);
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
				System.out.println("Connexion IHM");
				String ip_nom = ip.getText();
				String port_nom = port.getText();
				try {
					echoSocket = new Socket(ip_nom, Integer.valueOf(port_nom).intValue());
					socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				    socOut = new PrintStream(echoSocket.getOutputStream());
				} catch (UnknownHostException e) {
				      System.err.println("Don't know about host:" + ip_nom);
				      System.exit(1);

			    } catch (IOException e) {
			      System.err.println("Couldn't get I/O for "
			          + "the connection to:" + ip_nom);
			      System.exit(1);
			    }

				ThreadEcritureIHM threadEcriture = new ThreadEcritureIHM(socIn, grandeZone);
			    threadEcriture.start();

			}
			if (o == deconnexion) {
				System.out.println("Deconnexion IHM");
				try {
					socOut.println("quit");
				    socOut.close();
				    socIn.close();
				    echoSocket.close();
				    System.exit(0);
			    } catch (IOException e) {
					System.err.println(e);
					System.exit(1);
			    }
			}
			if (o == envoyer) {
				System.out.println("Envoyer IHM");
				String text_to_send = response.getText();
				socOut.println(text_to_send);
				response.setText("");
			}
		};
}
