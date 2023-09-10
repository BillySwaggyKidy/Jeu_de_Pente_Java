import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class Plateau extends JPanel implements MouseListener // correspond à l'ensemble de composant que de la fenetre
{
	private Color fond = new Color (179, 137, 96);
	private Label label_capture_equipe_blanc;
	private Label label_capture_equipe_noir;
	private Label label_tour_equipe;
	private Juge analyse_plateau;
	private Image plateau_jeu_pente; // image du plateau du jeu de pente
	private Pions [][] tableau_pions_plateau; // correspond à l'ensemble des cases pouvant ccontenir le nombre de cases de pions
	private final int x_plateau = 185;
	private final int y_plateau = 473;
	private Pions sac_blanc; // pion blanc à interagir pour jouer
	private Pions sac_noir; // pion noir à interagir pour jouer
	private String mouse_tient_couleur = "AUCUN"; // determine si la souris tient un pion
	private boolean blanc_joue_en_premier = true; // permet de dire qui va jouer en premier dans la partie


	public Plateau() throws IOException
	{
		setLayout(null); // on met le layout du JPanel à null

		try
		{
			plateau_jeu_pente = ImageIO.read(new File("image/plateau/plateau_jeu_de_pente_agrandi.jpg"));
			setPreferredSize(new Dimension(850, 520)); // on met en place la dimension de la fenetre
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		sac_blanc = new Pions("BLANC", "ATTENTE", 20, 200); // on crée un pion de statut PLACE et de couleur blanche
		sac_noir = new Pions("NOIR", "ATTENTE", 720, 200); // on crée un pion de statut PLACE et de couleur noir

		tableau_pions_plateau = new Pions [19] [19];

		add(sac_blanc.get_label()); // on fait afficher le sac blanc
		sac_blanc.get_label().addMouseListener(this); // le label du sac blanc réagit aux évènements
		add(sac_noir.get_label()); // on fait afficher le sac noir
		sac_noir.get_label().addMouseListener(this); // le label du sac noir réagit aux évènements

		preparer_tableaux_point(tableau_pions_plateau); // on crée et place les pions vide du plateau

		analyse_plateau = new Juge(tableau_pions_plateau); // on prépare la partie analyse envoyant le tableau des pions

		label_capture_equipe_blanc = new Label(20,250,"Paires capturées: " + analyse_plateau.get_compteur_capture_équipe_blanc(),false,255,255,255);
		label_capture_equipe_noir = new Label(720,250,"Paires capturées: " + analyse_plateau.get_compteur_capture_équipe_noir(),false,0,0,0);	
		label_tour_equipe = new Label(350,-85,"Au tour de l'equipe: " + donner_equipe_couleur(blanc_joue_en_premier),true,255,0,0);

		add(label_capture_equipe_blanc.get_label());
		add(label_capture_equipe_noir.get_label());
		add(label_tour_equipe.get_label());

		// on fait afficher une boîte de dialogue avant le lancement de la fenêtre
		JOptionPane.showConfirmDialog(null, "Pour placer un pion, cliquez sur les sacs de pions puis cliquez sur une intersection \nLes Blancs commencent en premier." , "Comment jouer", JOptionPane.DEFAULT_OPTION);
	}	


	public String donner_equipe_couleur(boolean blanc_joue)
	{
		if (blanc_joue == true)
		{
			return "BLANC";
		}
		else
		{
			return "NOIR";
		}
	}

	public void preparer_tableaux_point(Pions [] [] plateau_pions) // on fait placer dans tous les intersections du plateau des pions vides, sans couleur
	{
		//System.out.println(plateau_pions[0].length);
		//System.out.println(plateau_pions.length);
		for (int i = 0 ; i < plateau_pions[0].length ; i++) // pour chaque colonne
		{
			for (int j = 0 ; j < plateau_pions.length ; j++) // pour chaque lignes
			{
				tableau_pions_plateau[j][i] = new Pions("PAS_COUELUR","VIDE",((int)(x_plateau + 25.2*j)) , ((int)(y_plateau - 25.2*i)));
				// on crée un pion avec les donnees et les coordonnees de x et y qui varie en fonction des intersections
				add(tableau_pions_plateau[j][i].get_label()); // on fait afficher son label et son image
				tableau_pions_plateau[j][i].get_label().addMouseListener(this); // on lui rajoute son propre mouse listener
			}
		}
	}

	public void placer_un_pion_dans_le_plateau(Pions [] [] plateau_pions) // permet de faire placer un pion d'une couleur dans le plateau
	{
		for (int i = 0 ; i < plateau_pions[0].length ; i++) // pour chaque colonne
		{
			for (int j = 0 ; j < plateau_pions.length ; j++) // pour chaque lignes
			{
				if (tableau_pions_plateau[j][i].get_clique_par_souris() == true && tableau_pions_plateau[j][i].get_statut_pion() == "VIDE")
				{
					//System.out.println(mouse_tient_couleur);
					if (tableau_pions_plateau[j][i].get_clique_par_souris() == true && mouse_tient_couleur == "BLANC")
					{
						//System.out.println("on a sélectionner le sac blanc");
						tableau_pions_plateau[j][i].changer_caracteristique_pion("BLANC", "PLACE"); // on fait placer dans cette intersection un pion blanc
						tableau_pions_plateau[j][i].get_label().repaint(); // on rafraichit le JPanel
						mouse_tient_couleur = "AUCUN"; // on remet la sélection du pion d'une couleur au départ
						tableau_pions_plateau[j][i].set_false_clique_par_souris(); // on a plus clique sur cette élèment maintenant

						analyse_plateau.mise_a_jour(j, i, tableau_pions_plateau[j][i]); // on met a jour le plateau d'analyse de la classe juge
						analyse_plateau.analyse_plateau_pion(j,i,"BLANC", 5, 2); // on cherche a trouver des figures de pions à partir du pion posé

						label_capture_equipe_blanc.modifier_label("Paires capturées: " + analyse_plateau.get_compteur_capture_équipe_blanc(),false);
						label_capture_equipe_blanc.get_label().repaint();

						blanc_joue_en_premier = false;
						label_tour_equipe.modifier_label("Au tour de l'equipe: " + donner_equipe_couleur(blanc_joue_en_premier),false);
						label_tour_equipe.get_label().repaint();

						if (analyse_plateau.get_liste_pion_capture_a_enlever().isEmpty() == false)
						{
							//System.out.println("suppresion des pions capturées");
							for (int c = 0; c < analyse_plateau.get_liste_pion_capture_a_enlever().size(); c++)
							{
								int x_tab = analyse_plateau.get_liste_pion_capture_a_enlever().elementAt(c).get_coordonne_x();
								int y_tab = analyse_plateau.get_liste_pion_capture_a_enlever().elementAt(c).get_coordonne_y();

								tableau_pions_plateau[x_tab][y_tab].changer_caracteristique_pion("PAS_COULEUR", "VIDE"); // on fait placer dans cette intersection un pion blanc
								tableau_pions_plateau[x_tab][y_tab].get_label().repaint(); // on rafraichit le JPanel
							}
							analyse_plateau.clear_liste_pion_capture_a_enlever();
						}

						qui_a_gagne(analyse_plateau.get_vainqueur());

						this.setBackground(fond);


					}
					else if (tableau_pions_plateau[j][i].get_clique_par_souris() == true && mouse_tient_couleur == "NOIR")
					{
						tableau_pions_plateau[j][i].changer_caracteristique_pion("NOIR", "PLACE"); // on fait placer dans cette intersection un pion noir
						tableau_pions_plateau[j][i].get_label().repaint(); // on rafraichit le JPanel
						mouse_tient_couleur = "AUCUN"; // on remet la sélection du pion d'une couleur au départ
						tableau_pions_plateau[j][i].set_false_clique_par_souris(); // on a plus clique sur cette élèment maintenant

						analyse_plateau.mise_a_jour(j, i, tableau_pions_plateau[j][i]);
						analyse_plateau.analyse_plateau_pion(j,i,"NOIR", 5, 2);

						label_capture_equipe_noir.modifier_label("Paires capturées: " + analyse_plateau.get_compteur_capture_équipe_noir(),false);
						label_capture_equipe_noir.get_label().repaint();

						blanc_joue_en_premier = true;
						label_tour_equipe.modifier_label("Au tour de l'equipe: " + donner_equipe_couleur(blanc_joue_en_premier),false);
						label_tour_equipe.get_label().repaint();

						if (analyse_plateau.get_liste_pion_capture_a_enlever().isEmpty() == false)
						{
							//System.out.println("suppresion des pions capturées");
							for (int c = 0; c < analyse_plateau.get_liste_pion_capture_a_enlever().size(); c++)
							{
								int x_tab = analyse_plateau.get_liste_pion_capture_a_enlever().elementAt(c).get_coordonne_x();
								int y_tab = analyse_plateau.get_liste_pion_capture_a_enlever().elementAt(c).get_coordonne_y();

								tableau_pions_plateau[x_tab][y_tab].changer_caracteristique_pion("PAS_COULEUR", "VIDE"); // on fait placer dans cette intersection un pion blanc
								tableau_pions_plateau[x_tab][y_tab].get_label().repaint(); // on rafraichit le JPanel
							}
							analyse_plateau.clear_liste_pion_capture_a_enlever();
						}

						qui_a_gagne(analyse_plateau.get_vainqueur());

						this.setBackground(fond);
					}

					else if (mouse_tient_couleur == "AUCUN")
					{
						//System.out.println("sinon");
						tableau_pions_plateau[j][i].set_false_clique_par_souris(); // si le joueur n'a pas pris un pion d'un des sacs, alors on ne fera pas placer de pions
						//System.out.println("ANNULATION DU CLIC");// la sélection du pion vide est annulé.
					}
				}
			}
		}
	}

	public void qui_a_gagne(String gagnant)
	{
		if (gagnant == "NOIR")
		{
			JOptionPane.showConfirmDialog(null, "L'équipe " + gagnant + " a gagné la partie", "Fin de la partie.", JOptionPane.DEFAULT_OPTION);
			System.exit(0);
		}
		else if (gagnant == "BLANC")
		{
			JOptionPane.showConfirmDialog(null, "L'équipe " + gagnant + " a gagne la partie", "Fin de la partie.", JOptionPane.DEFAULT_OPTION);
			System.exit(0);
		}
	}


	public void paintComponent(Graphics g) // methode paintComponent est propre à JPanel et à Swing
	{
		super.paintComponents(g);
		g.drawImage(plateau_jeu_pente,165,0,null);

	}

	//=========================================================================================================================================


	@Override
	public void mouseClicked(MouseEvent e)
	{
		placer_un_pion_dans_le_plateau(tableau_pions_plateau); // gére le clic et les modifications du coté des pions vide du plateau

		if ((sac_blanc.get_statut_pion() == "ATTENTE" && sac_blanc.get_clique_par_souris() == true) || (sac_noir.get_statut_pion() == "ATTENTE" && sac_noir.get_clique_par_souris() == true)) // si la pion a son statut attente
		{
			//System.out.println("on a clique dans le sac blanc: " + sac_blanc.get_clique_par_souris());
			//System.out.println("on a clique dans le sac noir: " + sac_noir.get_clique_par_souris());

			if (sac_blanc.get_couleur_pion() == "BLANC" && sac_blanc.get_clique_par_souris() == true && blanc_joue_en_premier == true) // si sa couleur est blanc
			{
				//System.out.println("clic sac blanc");
				mouse_tient_couleur = "BLANC"; // on dit que dans le plateau, le joueur a sélectionner pris un pion blanc pour le placerblanc_joue_en_premier = true;
				sac_blanc.set_false_clique_par_souris(); // on a plus clique sur cette élèment maintenant
			}
			else if (sac_noir.get_couleur_pion() == "NOIR" && sac_noir.get_clique_par_souris() == true && blanc_joue_en_premier == false) // si sa couleur est noir
			{
			 	//System.out.println("clic sac noir");
			 	mouse_tient_couleur = "NOIR"; //on dit que dans le plateau, le joueur a sélectionner pris un pion noir pour le placer
				sac_noir.set_false_clique_par_souris();  // on a plus clique sur cette élèment maintenant
			}
			else
			{
				//System.out.println("annulation des clics sur les sacs");

				if (sac_blanc.get_clique_par_souris() == true)
				{
					sac_blanc.set_false_clique_par_souris();
				}
				else if (sac_noir.get_clique_par_souris() == true)
				{
					sac_noir.set_false_clique_par_souris();
				}
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