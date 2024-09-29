package couches;

import java.io.IOException;
import java.util.ArrayList;
import enumurations.*;
import paquets.*;
import interfaces.IPaquet;
import services.SauvegardeInfos;

public class ProcessusER // Couche Reseau
{
	private ArrayList<SauvegardeInfos> sauvegardeInfos;
	private static int count = 0;// g�n�res les num�ros de connexions
	private LiaisonDeDonnees liaisonDeDonnees;
	
	public ProcessusER() throws IOException
	{
		this.liaisonDeDonnees = new LiaisonDeDonnees();
		sauvegardeInfos = new ArrayList<SauvegardeInfos>();
	}

	// Demande de connexion
	public IPaquet DemandeDeConnexion(int numIdentifiant, int addrSource, int addrDestination) throws IOException
	{
		int numConnexion;

		if (estMultipleDe27(addrSource))
		{
			return new PaquetIndLiberation(-1, addrSource, addrDestination, 0b00000010);
		} else
		{
			numConnexion = count++;
			PaquetAppel paquetAppel = preparationPaquetAppel(addrSource, addrDestination, numConnexion);

			// sauvegarde info necessaire a cette connexion
			sauvegardeInfos.add(new SauvegardeInfos(numConnexion, addrSource, addrDestination,
					EtatDeConnexion.attenteDeConfirmation, numIdentifiant));

			return liaisonDeDonnees.appel(paquetAppel);// Transmission du paquet d'appel a la liaison
											// de donnees
		}
	}

	// Preparation de paquet d'appel
	private PaquetAppel preparationPaquetAppel(int addrSource, int addrDestination, int numeroConnexion)
	{
		return new PaquetAppel(numeroConnexion, addrSource, addrDestination);
	}

	// Preparation de paquets de donn�es
	public PaquetAcquittement preparationPaquetDeDonnees(String data, Primitive nDataReq, int numConnexion,
			int addrSource, int idApplication) throws IOException
	{
		PaquetAcquittement resultEnvois;
		PaquetDeDonnees paquetDonnees;

		if (data.length() > 128)
			return traitementGrosPaquet(numConnexion, data, addrSource, idApplication);
		else
		{
			String typePaquet = formatTypeDePaquet(0);
			paquetDonnees = new PaquetDeDonnees(numConnexion, typePaquet, data);

			resultEnvois = liaisonDeDonnees.envoisPaquetDeDonnees(paquetDonnees, addrSource, idApplication);
			fenetreAnticipation(resultEnvois, paquetDonnees, addrSource, idApplication);// pour gerer les acquittements

			return resultEnvois;
		}
	}

	// Traitement des grosses donnees(>128o)
	private PaquetAcquittement traitementGrosPaquet(int numConnexion, String data, int addrSource, int idApplication) throws IOException
	{
		byte[] dataBytes = data.getBytes();
		int nbrPaquet = dataBytes.length % 128 == 0 ? dataBytes.length / 128 : dataBytes.length / 128 + 1;
		int index = 0;
		while (nbrPaquet > 1)
		{
			String typePaquet = formatTypeDePaquet(1);
			String dataPartiel = data.substring(index, index + 127);
			liaisonDeDonnees.envoisPaquetDeDonnees(
					new PaquetDeDonnees(numConnexion, typePaquet, dataPartiel), addrSource, idApplication);
			index += 128;
			nbrPaquet--;
		}

		String typePaquet = formatTypeDePaquet(0);
		String dataPartiel = data.substring(index, dataBytes.length);
		liaisonDeDonnees
				.envoisPaquetDeDonnees(new PaquetDeDonnees(numConnexion, typePaquet, dataPartiel), addrSource, idApplication);

		return null;
	}

	// Demande de Liberation de connexion
	public void liberation(int idConnexion, Primitive nDisconnectReq) throws IOException
	{
		int addrSource = getAddrSource(idConnexion);
		int addrDest = getAdrrDest(idConnexion);
		int numConnexion = getNumConnexion(idConnexion);
		PaquetDemandeLib paquetLib = new PaquetDemandeLib(numConnexion, addrSource, addrDest);
		liaisonDeDonnees.envoisPaquetLiberation(paquetLib);
	}

	// Fenetre d'anticipation pour la gestion des acquittements
	private void fenetreAnticipation(PaquetAcquittement resultEnvois, PaquetDeDonnees paquetDonnees, int addrSource, int idApplication)
			throws IOException
	{
		if (resultEnvois == null || resultEnvois.getTypeDePaquet().substring(3) == "01001")
			liaisonDeDonnees.retransmissionDonnees(paquetDonnees, addrSource, idApplication);
	}

	// Formatage du champ type de paquet
	private String formatTypeDePaquet(int bitM)
	{
		String ps = ProcessusET.getPs();
		String pr = ProcessusET.getPr();
		return pr + bitM + ps + 0;
	}

	private boolean estMultipleDe27(int numeroDemande)
	{
		if (numeroDemande != 0)
			return numeroDemande % 15 == 0;
		else
			return false;
	}

	// Accesseur de num�ro d'indentifiant d'extr�mit� de connexion
	private int getNumConnexion(int idConnexion)
	{
		for (SauvegardeInfos save : sauvegardeInfos)
			if (save.getNumeroDemande() == idConnexion)
				return save.getNumeroConnexion();
		return -1;
	}

	// Accesseur d'addresse source
	private int getAddrSource(int idConnexion)
	{
		for (SauvegardeInfos save : sauvegardeInfos)
			if (save.getNumeroDemande() == idConnexion)
				return save.getAddrSource();
		return -1;
	}

	// Accesseur d'addresse destination
	private int getAdrrDest(int idConnexion)
	{
		for (SauvegardeInfos save : sauvegardeInfos)
			if (save.getNumeroDemande() == idConnexion)
				return save.getAddrDestination();
		return -1;
	}
}