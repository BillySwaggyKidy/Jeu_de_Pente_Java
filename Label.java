import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class Label
{
	private int coordonne_x; // coordonnée de l'image du pion
	private int coordonne_y;
	private JLabel label;
	private boolean en_gras;
	private int r;
	private int g;
	private int b;


	Label(int x, int y, String texte, boolean gras, int _r, int _g, int _b)
	{
		coordonne_x = x;
		coordonne_y = y;
		en_gras = gras;

		r = _r;
		g = _g;
		b = _b;


		label = new JLabel(texte);
		label.setForeground(new Color(r,g,b));
		if (gras == true)
		{
			Font font = new Font("Arial",Font.BOLD,12);
			label.setFont(font);
		}
		label.setBounds(coordonne_x,coordonne_y,200,200);
	}


	public void modifier_label(String texte, boolean gras)
	{
		label.setText(texte);
		label.setForeground(new Color(r,g,b));
		if (gras == true)
		{
			Font font = new Font("Arial",Font.BOLD,12);
			label.setFont(font);
		}
		label.setBounds(coordonne_x,coordonne_y,200,200);

	}

	public JLabel get_label()
	{
		return label;
	}
}
