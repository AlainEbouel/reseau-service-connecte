package couches;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import enumurations.*;
import interfaces.IPaquet;
import services.EntreeDeTable;

public class ET_Thread extends Thread // Threadind des op�rations de la couche transport ET
{
	private ProcessusER ER;
	private String data;
	private File S_ecr;
	private int idConnexion;
	private int idApplication;

	public ET_Thread(ProcessusER ER, String data, int idConnexion, int idApplication)
	{
		this.ER = ER;
		this.data = data;
		this.idConnexion = idConnexion;
		this.idApplication = idApplication;
		S_ecr = new File("fichiers/S_ecr.txt");
	}

	@Override
	public void run()
	{
		IPaquet reponse = null;
		int addrSource = generateAddress(-1);
		int addrDestination = generateAddress(addrSource);
		EtatDeConnexion etatConnexion = ProcessusET.getEntreeDeTable(idConnexion).getEtatDeConnexion();

		// Si la connexion n'est pas encore etablie, tentative d'etablissement de
		// connnexion
		if (etatConnexion == EtatDeConnexion.attenteDeConfirmation)
		{
			try
			{
				// Demande de connexion
				reponse = ER.DemandeDeConnexion(idConnexion, addrSource, addrDestination);

				// ecriture dans S_ecr du resultat de la demande de connexion
				if (reponse != null)
					ecrireDansS_ecr(reponse.toString());

			} catch (IOException e1)
			{
				e1.printStackTrace();
			}

			// En cas de refus de connexion
			if (reponse != null && reponse.getPrimitive() == Primitive.N_DISCONNECT_ind)
				liberationDesRessources(idConnexion);

			// En cas de connexion acceptee
			else if (reponse != null && reponse.getPrimitive() == Primitive.N_CONNECT_resp)
			{
				ProcessusET.getEntreeDeTable(idConnexion).setEtatDeConnexion(EtatDeConnexion.connexionEtablie);

				try
				{
					ER.preparationPaquetDeDonnees(data, Primitive.N_DATA_req, idConnexion,
							addrSource, idApplication);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		// Si la connexion est etablie on transfert directement des donnees
		else
		{
			try
			{
				ER.preparationPaquetDeDonnees(data, Primitive.N_DATA_req, idConnexion, addrSource, idApplication);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// Liberation des ressouces
	private void liberationDesRessources(int idConnexion)
	{
		for (EntreeDeTable entree : ProcessusET.getTable())
		{
			if (entree.getIdConnexion() == idConnexion)
			{
				ProcessusET.getTable().remove(entree);
				data = null;
				break;
			}
		}
	}

	// Generation aleatoire d'addresse
	private int generateAddress( int addrSource)
	{
		Random rand = new Random();
		int addr = rand.nextInt(254);

		if( addrSource == -1 || addrSource != addr) return addr;

		else{
			while (addrSource == addr ){
				addr = rand.nextInt(254);
			}
			return addr;
		}
	}

	// ecriture dans S_ecr
	private void ecrireDansS_ecr(String reponse) throws IOException
	{
		FileOutputStream file = null;
		file = new FileOutputStream(S_ecr, true);
		file.write(reponse.concat("\n").getBytes());
		file.close();
	}

}
