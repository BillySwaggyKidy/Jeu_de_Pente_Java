import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class Fenetre extends JFrame // JFrame correspond à l'application, la fenetre
{
	private Plateau plateau;


	public Fenetre(String s) throws IOException
	{
		super(s);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // quand on clique sur le bouton rouge, ça quitte l'application
		this.setSize(850, 520); // définit la taille

		this.setLocationRelativeTo(null);
		this.setResizable(false); // on ne peut pas la redimenssionner la fenetre
		
		Color newColor = new Color (179, 137, 96); // on crée une couleur marron
		this.setBackground(newColor); //on place la couleur en arriére plan


		plateau = new Plateau(); // on crée un objet plateau de type JPanel

		this.setContentPane(plateau);
		this.getContentPane().setLayout(null);
		pack();
		this.setVisible(true);


	}
}