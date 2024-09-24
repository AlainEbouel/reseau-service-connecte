package couches;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import enumurations.*;
import services.EntreeDeTable;

public class ProcessusET // Couche Transport
{
	private static ArrayList<EntreeDeTable> table = new ArrayList<>();
	private static int ps = -1, pr = -1;
	private static int countIndentifiant = 0; // Compteur utilis� pour g�n�rer des Identifiants d'extr�mit� de
								// connexion

	// Traitement de chaque transaction(Chaque ligne dans le fichier S_lec)
	public void traitement(ProcessusER ER, String data, BufferedReader l_lec) throws IOException
	{
		EntreeDeTable entree;
		int idApplication;// D�finie dans le fichier S_lec pour chaque application
		int idConnexion;// Identifiant d'extr�mit� de connexion

		// Si l'id de l'application correspond � une entr�e dans la table
		if ((entree = getEntreeDeTable(data.substring(0, 4))) != null)
		{
			idApplication = entree.getNumApplication();
			idConnexion = entree.getIdConnexion();

		}

		// Si l'id de l'application ne correspond � aucune entr�e dans la table
		else
		{
			idApplication = Integer.parseInt(data.substring(0, 4));
			idConnexion = countIndentifiant++;

			table.add(entree = new EntreeDeTable(idConnexion, EtatDeConnexion.attenteDeConfirmation,
					idApplication));
		}

		ET_Thread Et_thread; // Thread responsable de la suite du processus de communication

		// Test si l'application tente de communiquer ou de mettre fin � la
		// communication
		if (data.charAt(4) == '0')
		{
			Et_thread = new ET_Thread(ER, data.substring(5), idConnexion);
			Et_thread.run();
		} else
			ER.liberation(idConnexion, Primitive.N_DISCONNECT_req);
	}

	public static String getPs()
	{
		return String.format("%3s", Integer.toBinaryString(ps)).replace(' ', '0');
	}

	public static String getPr()
	{
		incrementePr();
		return String.format("%3s", Integer.toBinaryString(pr)).replace(' ', '0');
	}

	private static void incrementePr()
	{
		if (pr == 7)
			pr = 0;
		else
			ProcessusET.pr += 1;
	}

	public static ArrayList<EntreeDeTable> getTable()
	{
		return table;
	}

	// Retrouver une entr�e dans la table gr�ce au num�ro d'identifiant d'extr�mit�
	// de connexion
	public static EntreeDeTable getEntreeDeTable(int numConnexion)
	{
		for (EntreeDeTable entree : ProcessusET.getTable())
			if (entree.getIdConnexion() == numConnexion)
				return entree;
		return null;
	}

	// Retrouver une entr�e dans la table gr�ce au num�ro d'identifiant d'une
	// application
	public static EntreeDeTable getEntreeDeTable(String idApplication)
	{
		for (EntreeDeTable entree : ProcessusET.getTable())
			if (entree.getNumApplication() == Integer.parseInt(idApplication))
				return entree;
		return null;
	}

}
