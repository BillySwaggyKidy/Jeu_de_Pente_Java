import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.io.IOException;


public class Juge// cette classe analyse le jeu du plateau
{
	private Pions [][] tableau_pions_analyse; // correspond à l'ensemble des cases pouvant ccontenir le nombre de cases de pions
	private Vector<Pions> liste_pion_capture_a_enlever = new Vector<Pions>(); // correspond au pion à enlever lorsque on les a capturaient
	private int compteur_capture_équipe_blanc = 0; // compte le nombre de capture de paire faite par les blancs
	private int compteur_capture_équipe_noir = 0;  // compte le nombre de capture de paire faite par les noirs
	private String vainqueur = ""; // correspond à qu'elle couleur est le vainqueur





	public Juge(Pions [] [] tableau_pion_para)
	{
		tableau_pions_analyse = new Pions [19] [19]; // on crée le tableau d'analyse de dimensions 19 X 19

		// on transfére toute les cases du tableau de pions en parametre aux cases du tableau d'analyse
		for (int i = 0 ; i < tableau_pions_analyse[0].length ; i++) // pour chaque colonne
		{
			for (int j = 0 ; j < tableau_pions_analyse.length ; j++) // pour chaque lignes
			{
				tableau_pions_analyse[j][i] = tableau_pion_para[j][i]; // telle case du tableau d'analyse va prendre le même objet
				// que la valeur aux coordonnées du tableau en parametre
			}
		}
	}


	public void mise_a_jour(int x_tab, int y_tab, Pions pion_maj)
	{
		tableau_pions_analyse[x_tab][y_tab].changer_caracteristique_pion(pion_maj.get_couleur_pion(), pion_maj.get_statut_pion());
		// on change les caractéristiques du pion aux coordonnées par la couleur et le statut du pion en parametre
	}


	public void analyse_plateau_pion(int x_param, int y_param, String couleur, int nombre_pions_aligne, int nombre_pion_a_capture)
	{
		boolean gagne = false;

		gagne = detection_alignement_pions(x_param,y_param, couleur, nombre_pions_aligne);

		detection_capture_pions(x_param,y_param,couleur,nombre_pion_a_capture);

		if (gagne == true || compteur_capture_équipe_blanc == 5 || compteur_capture_équipe_noir == 5)
		{
			vainqueur = couleur; // la couleur du vainqueur correspond à la couleur avec quoi on a commencé la fonction
		}
	}


	public int get_compteur_capture_équipe_blanc()
	{
		return compteur_capture_équipe_blanc;
	}

	public int get_compteur_capture_équipe_noir()
	{
		return compteur_capture_équipe_noir;
	}

	public String get_vainqueur()
	{
		return vainqueur;
	}

	public Vector<Pions> get_liste_pion_capture_a_enlever()
	{
		return liste_pion_capture_a_enlever;
	}

	public void clear_liste_pion_capture_a_enlever()
	{
		liste_pion_capture_a_enlever.clear();
	}





	//===============================================================================================================================






	public boolean detection_pion_precis_autour(int x_param, int y_param, int x_a_detect, int y_a_detect, String couleur)
	{
		boolean trouve = false; // determine si la fonction a trouve le pion ciblé
		for (int i = -1; i < 2 ; i++)
		{
			for (int j = -1 ; j < 2 ; j++)
			{
				int x_autour = (x_param + i);
				int y_autour = (y_param + j);

				if ( (x_autour >= 0 && x_autour < 19) && (y_autour >= 0 && y_autour < 19) && (x_autour != x_param || y_autour != y_param))
				{
					if (tableau_pions_analyse[x_autour][y_autour].get_statut_pion() == "PLACE" 
					&& tableau_pions_analyse[x_autour][y_autour].get_couleur_pion() == couleur 
					&& (x_autour == x_a_detect && y_autour == y_a_detect))
					{
						trouve = true;
					}
				}
			}
		}

		return trouve;
	}



	public boolean detection_alignement_pions(int x_param, int y_param, String couleur ,int nombre_pions_aligne)
	{
		for (int i = -1; i < 2 ; i++)
		{
			for (int j = -1 ; j < 2 ; j++)
			{
				int x_autour = (x_param + i); // on calcul la nouvelle coordonne en X autour du pion cible
				int y_autour = (y_param + j);
				//System.out.println("pion autour: " + x_autour + " " + y_autour);

				if ( (x_autour >= 0 && x_autour < 19) 
				&& (y_autour >= 0 && y_autour < 19) 
				&& (x_autour != x_param || y_autour != y_param))
				{
					//System.out.println("premiere condition passe");
					if (tableau_pions_analyse[x_autour][y_autour].get_statut_pion() == "PLACE" 
						&& tableau_pions_analyse[x_autour][y_autour].get_couleur_pion() == couleur)
					{
						boolean pion_trouve = false; // demande à être confirmer si les pions sont tous alignées
						int compteur_pion_aligne = 1;
						for (int b = 0 ; b < nombre_pions_aligne-1 ; b++)
						{
							pion_trouve = detection_pion_precis_autour((x_param + (b*i)), (y_param + (b*j)), (x_param + ((b+1)*i)), (y_param + ((b+1)*j)), couleur);
							if (pion_trouve == true)
							{
								compteur_pion_aligne++;
							}
							else if (pion_trouve == false)
							{
								break;
							}
						}
						for (int n = 0 ; n < nombre_pions_aligne-1 ; n++)
						{
							if (compteur_pion_aligne < nombre_pions_aligne)
							{
								pion_trouve = detection_pion_precis_autour((x_param + (n*(-i))), (y_param + (n*(-j))), (x_param + ((n+1)*(-i))), (y_param + ((n+1)*(-j))), couleur);
								if (pion_trouve == true)
								{
									compteur_pion_aligne++;
								}
							}
							else
							{
								 break;
							}
						}
						if (compteur_pion_aligne == nombre_pions_aligne)
						{
							//System.out.println("FELICITATION, TU a aligné 5 pions");
							return true;
						}
					}
				}
			}
		}
		return false;
	}



	public void detection_capture_pions(int x_param, int y_param, String couleur ,int nombre_pions_capture) // detecte une capture de paire de pions adverse.
	{
		String couleur_adverse = "";
		if (couleur == "NOIR")
		{
			couleur_adverse = "BLANC";
		}
		else if (couleur == "BLANC")
		{
			couleur_adverse = "NOIR";
		}


		for (int i = -1; i < 2 ; i++)
		{
			for (int j = -1 ; j < 2 ; j++)
			{
				int x_autour = (x_param + i); // on calcul la nouvelle coordonne en X autour du pion cible
				int y_autour = (y_param + j);
				//System.out.println(x_param + " " + y_param);
				//System.out.println("pion autour: " + x_autour + " " + y_autour);

				if ( (x_autour >= 0 && x_autour < 19) && (y_autour >= 0 && y_autour < 19) && (x_autour != x_param || y_autour != y_param))
				{
					if (tableau_pions_analyse[x_autour][y_autour].get_statut_pion() == "PLACE" && tableau_pions_analyse[x_autour][y_autour].get_couleur_pion() == couleur_adverse)
					{
						boolean pion_capture = true;
						for (int b = 0 ; b < nombre_pions_capture+1 ; b++)
						{
							if (b < nombre_pions_capture)
							{
								//System.out.println("couleur_adverse = " + couleur_adverse);
								pion_capture = detection_pion_precis_autour((x_param + (b*i)), (y_param + (b*j)), (x_param + ((b+1)*i)), (y_param + ((b+1)*j)), couleur_adverse);
								liste_pion_capture_a_enlever.addElement(new Pions(tableau_pions_analyse[x_param + ((b+1)*i)] [y_param + ((b+1)*j)].get_couleur_pion(),
									tableau_pions_analyse[x_param + ((b+1)*i)] [y_param + ((b+1)*j)].get_statut_pion(),x_param + ((b+1)*i),y_param + ((b+1)*j) ) );
								//System.out.println("pion_capture = " + pion_capture);
							}
							else
							{
								//System.out.println("couleur = " + couleur);
								pion_capture = detection_pion_precis_autour((x_param + (b*i)), (y_param + (b*j)), (x_param + ((b+1)*i)), (y_param + ((b+1)*j)), couleur);
								//System.out.println("pion_capture = " + pion_capture);
							}

							if (pion_capture == false)
							{
								break;
							}

						}

						if (pion_capture == true)
						{

							//System.out.println("FELICITATION, Tu a capturé une paire de pion");
							if (couleur == "BLANC")
							{
								compteur_capture_équipe_blanc++;
								//System.out.println("nombre de capture de pair blanc = " + compteur_capture_équipe_blanc);
							}
							else if (couleur == "NOIR")
							{
								compteur_capture_équipe_noir++;
								//System.out.println("nombre de capture de paire noir = " + compteur_capture_équipe_noir);
							}
						}
						else
						{
							clear_liste_pion_capture_a_enlever();
						}
					}
				}
			}
		}

	}


	/*public void detection_pion_autour(int x_param,int y_param) // detecte un pion autour du pion ciblé
	{

		//System.out.println("pion posé à: " + x_param + " " + y_param);
		for (int i = -1; i < 2 ; i++)
		{
			for (int j = -1 ; j < 2 ; j++)
			{
				int x_autour = (x_param + i);
				int y_autour = (y_param + j);
				//System.out.println("pion autour: " + x_autour + " " + y_autour);

				if ( (x_autour >= 0 && x_autour < 19) && (y_autour >= 0 && y_autour < 19) && (x_autour != x_param || y_autour != y_param))
				{
					//System.out.println("premiere condition passe");
					if (tableau_pions_analyse[x_autour][y_autour].get_statut_pion() == "PLACE")
					{
						System.out.println("##Pion trouvé à : " + x_autour + " " + y_autour);
					}
				}
			}
		}
	}*/

	/*public void detection_pion_meme_couleur_autour(int x_param,int y_param, String couleur) // detecte un pion autour du pion ciblé
	{

		//System.out.println("pion posé à: " + x_param + " " + y_param);
		for (int i = -1; i < 2 ; i++)
		{
			for (int j = -1 ; j < 2 ; j++)
			{
				int x_autour = (x_param + i);
				int y_autour = (y_param + j);
				//System.out.println("pion autour: " + x_autour + " " + y_autour);

				if ( (x_autour >= 0 && x_autour < 19) && (y_autour >= 0 && y_autour < 19) && (x_autour != x_param || y_autour != y_param))
				{
					//System.out.println("premiere condition passe");
					if (tableau_pions_analyse[x_autour][y_autour].get_statut_pion() == "PLACE" && tableau_pions_analyse[x_autour][y_autour].get_couleur_pion() == couleur)
					{
						System.out.println("##Pion trouvé à : " + x_autour + " " + y_autour);
					}
				}
			}
		}
	}*/

}	



	