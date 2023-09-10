import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.io.IOException;


public class Pions implements MouseListener// la classe Pion est un JLabel
{

	private int coordonne_x; // coordonnée de l'image du pion
	private int coordonne_y;
	private ImageIcon img_icon; // correspond à l'image du pions
	private JLabel label;

	private String couleur_pion; // soit elle est noir ou blanche
	private String statut_pion; // Soit Attente, Place ou Vide

	private boolean clique_par_souris; // determine si le pion a été clique par une souris



	public Pions(String _couleur_pion , String _statut_pion, int x, int y)
	{
		set_statut_couleur(_couleur_pion, _statut_pion);

		set_coordonne(x , y);
		
		if (statut_pion == "PLACE")// si la pion a son statut place
		{
			if (couleur_pion == "BLANC") // si sa couleur est blanc
			{				
				affiche_pion("image/plateau/pion_blanc_24X24.png", x, y, 24,24);
			}
			else if (couleur_pion == "NOIR") // si sa couleur est noir
			{
			 	affiche_pion("image/plateau/pion_noir_24X24.png", x, y, 24,24);
			}
		}
		else if (statut_pion == "VIDE") // si la pion a son statut vide
		{
			affiche_pion("image/plateau/pion_vide.png", x, y, 24, 24);
		}
		else if (statut_pion == "ATTENTE") // si la pion a son statut attente
		{
			if (couleur_pion == "BLANC") // si sa couleur est blanc
			{
				affiche_pion("image/plateau/sac_pion_blanc.png", x, y, 100, 100);
			}
			else if (couleur_pion == "NOIR") // si sa couleur est noir
			{
			 	affiche_pion("image/plateau/sac_pion_noir.png", x, y, 100, 100);
			}
		}


		label.addMouseListener(this); // on ajoute la possibilité de recevoir les évènements de la souris
	}

	public void changer_caracteristique_pion(String _couleur_pion , String _statut_pion) // permet de changer les infos sur le pion ainsi que son affichage.
	{
		set_statut_couleur(_couleur_pion, _statut_pion); // on change la couleur et le statut du pion

		if (statut_pion == "PLACE")// si la pion a son statut place
		{
			if (couleur_pion == "BLANC") // si sa couleur est blanc
			{				
				change_label("image/plateau/pion_blanc_24X24.png");
			}
			else if (couleur_pion == "NOIR") // si sa couleur est noir
			{
			 	change_label("image/plateau/pion_noir_24X24.png");
			}
		}
		else if (statut_pion == "VIDE") // si la pion a son statut vide
		{
			change_label("image/plateau/pion_vide.png");
		}
		else if (statut_pion == "ATTENTE") // si la pion a son statut attente
		{
			if (couleur_pion == "BLANC") // si sa couleur est blanc
			{
				change_label("image/plateau/sac_pion_blanc.png");
			}
			else if (couleur_pion == "NOIR") // si sa couleur est noir
			{
			 	change_label("image/plateau/sac_pion_noir.png");
			}
		}
	}

	//---------------------------------------------------------------------------------------------------------

	public void affiche_pion(String nom_img, int x, int y, int largeur, int hauteur) // permet de faire afficher le label contenant l'image avec ses coordonnes et ses dimensions (longueur et largeur)
	{
		img_icon = new ImageIcon(nom_img); // on prépare une image icone
		label = new JLabel(img_icon); // on crée le label qui afficheras cette icône
		label.setBounds(x,y,largeur,hauteur); // on positionne le label
	}

	public void change_label(String nom_img)
	{
		img_icon = new ImageIcon(nom_img); // on prépare une image icone
		label.setIcon(img_icon);
	}

	public ImageIcon get_img_icon() // permet d'avoir l'image du pion
	{
		return img_icon;
	}


	public JLabel get_label() // permet d'avoir le label du pion
	{
		return label;
	}



	public int get_coordonne_x() // permet de voir les coordonnées de l'axe X du pion
	{
		return coordonne_x;
	}

	public int get_coordonne_y() // // permet de voir les coordonnées de l'axe Y du pion
	{
		return coordonne_y;
	}

	public void set_coordonne(int _x, int _y) // permet de modifier les coordonnées X et Y du pion
	{
		coordonne_x = _x;
		coordonne_y = _y;
	}



	public String get_couleur_pion() // permet d'avoir l'info à propos de la couleur du pion
	{
		return couleur_pion;
	}

	public void set_couleur_pion(String couleur) // permet de modifier la couleur du pion
	{
		couleur_pion = couleur;
	}

	public String get_statut_pion() // permet d'avoir le statut du pion
	{
		return statut_pion;
	}

	public void set_statut_pion(String statut) // permet de modifier le statut du pion
	{
		statut_pion = statut;
	}

	public void set_statut_couleur(String couleur, String statut) // permet de modifier la couleur et le statut du pion
	{
		set_couleur_pion(couleur);
		set_statut_pion(statut);
	}

	public boolean get_clique_par_souris() // permet de savoir si l'élèment pion a été cliqué par la souris
	{
		return clique_par_souris;
	}

	public void set_false_clique_par_souris() // permet de réinitialiser le fait que la souris a cliqué sur le pion donc que la souris l'a juste sélectionne temporairement
	{
		clique_par_souris = (!clique_par_souris);
	}


	//=========================================================================================================================================

	@Override
	public void mouseClicked(MouseEvent e)
	{

		if (statut_pion == "ATTENTE" || statut_pion == "VIDE")
		{
			clique_par_souris = true;
			//System.out.println(get_couleur_pion() + " " + get_statut_pion() + " " + get_coordonne_x() + " " + get_coordonne_y());
			if (statut_pion == "VIDE")
			{
				//System.out.println("ATTENTION, il faut que vous sélectionner un pion d'une couleur d'un des sacs de pion pour placer un pion dans le plateau");
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
				
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
			
		
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}
}	