package couches;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;

import interfaces.IPaquet;
import paquets.*;

public class LiaisonDeDonnees // Couche liaison de donn�es
{
	private static boolean grosPaquet;
	private static String grosData = "";
	private Path l_lec;
	private Path l_ecr;
	private Path s_ecr;

	// R�ception des paquets d'appel. La liason de donn�es �crira dans L_ecr
	public LiaisonDeDonnees() throws IOException{
		/* Creation des fichiers */
		
		l_lec = Paths.get("fichiers/L_lec.txt");
		l_ecr = Paths.get("fichiers/L_ecr.txt");
		s_ecr = Paths.get("fichiers/S_ecr.txt");
		StandardOpenOption mode2 = StandardOpenOption.CREATE_NEW;
		ArrayList<Path> paths = new ArrayList<>();
		
		paths.add(l_ecr);
		paths.add(l_lec);
		paths.add(s_ecr);

		BufferedWriter buf;
		for(Path p : paths){
			
			try {
				buf = Files.newBufferedWriter(p, mode2);
			} catch (FileAlreadyExistsException e) {
				Files.deleteIfExists(p);
				buf = Files.newBufferedWriter(p, mode2);
			}
			buf.close();
		}

	}
	public IPaquet appel(PaquetAppel paqueAppel) throws IOException
	{
		String ecriture = paqueAppel.toString();
		ecrireDansFichiers(l_ecr, ecriture);

		return reponseDemandeDeConnexion(paqueAppel.getAddresseSource(), paqueAppel);
	}

	// Reception des demande de connexion
	private static IPaquet reponseDemandeDeConnexion(int nbr, PaquetAppel paqueAppel)
	{
		int numConexion = paqueAppel.getNumConnexion();
		int addrSource = paqueAppel.getAddresseSource();
		int addrDest = paqueAppel.getAddresseDest();

		if (estMultipleDe19(nbr))
		{
			return null;
		}
		if (estMultipleDe13(nbr))
		{
			return new PaquetIndLiberation(numConexion, addrSource, addrDest, 0b00000001);
		}

		return new PaquetComEtablie(numConexion, addrSource, addrDest);
	}

	// R�ception des paquets de donn�es
	public PaquetAcquittement envoisPaquetDeDonnees(PaquetDeDonnees paquetDeDonnees, int addrSource, int idApplication)
			throws IOException
	{
		// Ecriture dans L_ecr
		ecrireDansFichiers(l_ecr, paquetDeDonnees.getDonnees(), idApplication);
		
		// Acquittement
		char bitM = paquetDeDonnees.getTypeDePaquet().charAt(3);

		if (bitM == '0' && !grosPaquet)
			return acquittement(paquetDeDonnees, addrSource);

		else if (bitM == '1' && !grosPaquet)
		{
			grosPaquet = true;
			grosData = new String(paquetDeDonnees.getDonnees());
		}

		else if (bitM == '1' && grosPaquet)
			grosData.concat(paquetDeDonnees.getDonnees());

		else if (bitM == '0' && grosPaquet)
		{
			grosPaquet = false;
			grosData.concat(paquetDeDonnees.getDonnees());
			paquetDeDonnees.setData(grosData);

			return acquittement(paquetDeDonnees, addrSource);
		}
		return null;
	}

	// Acquittement des paquets de donnees
	private PaquetAcquittement acquittement(PaquetDeDonnees paquetDeDonnees, int addrSource) throws IOException
	{
		if (estMultipleDe15(addrSource))
			return null;

		String typeAcquittement;

		if (addrSource == new Random().nextInt(7))
			typeAcquittement = ProcessusET.getPr() + "01001";

		else
			typeAcquittement = ProcessusET.getPr() + "00001";
		PaquetAcquittement pAcquittement = new PaquetAcquittement(paquetDeDonnees.getNumeroDeConnexion(),
				typeAcquittement);
		ecrireDansFichiers(l_lec, pAcquittement.toString());
		return pAcquittement;

	}

	// reception de donn�es retransmis(apr�s acquittement n�gatif)
	public PaquetAcquittement retransmissionDonnees(PaquetDeDonnees paquetDeDonnees, int addrSource, int idApplication)	throws IOException
	{
		return envoisPaquetDeDonnees(paquetDeDonnees, addrSource, idApplication);
	}

	// R�ception de demandes de lib�ration
	public void envoisPaquetLiberation(IPaquet paquetLib) throws IOException
	{
		ecrireDansFichiers(l_ecr, paquetLib.toString());
	}

	// Ecriture dans le fichier L_ecr
	private void ecrireDansFichiers(Path path, String ecriture, int idApplication) throws IOException
	{
		ecrire(path, ecriture, idApplication);
	}

	private void ecrireDansFichiers(Path path, String ecriture) throws IOException
	{
		ecrire(path, ecriture, -1);
	}

	private void ecrire(Path path, String ecriture, int idApplication) throws IOException{
		StandardOpenOption standardOpenOption = StandardOpenOption.APPEND;
		BufferedWriter buf = Files.newBufferedWriter(path, standardOpenOption);

		if (idApplication < 0 ) buf.write(ecriture);
		else buf.write("000" + idApplication + "0=>" + ecriture );

		buf.newLine();
		buf.close();
	}

	private static boolean estMultipleDe13(int nbr)
	{
		if (nbr != 0)
			return nbr % 13 == 0;
		else
			return false;
	}

	private static boolean estMultipleDe15(int nbr)
	{
		if (nbr != 0)
			return nbr % 15 == 0;
		else
			return false;
	}

	private static boolean estMultipleDe19(int nbr)
	{
		if (nbr != 0)
			return nbr % 19 == 0;
		else
			return false;
	}
}
