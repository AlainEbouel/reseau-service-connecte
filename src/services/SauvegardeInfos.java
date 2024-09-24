package services;
import enumurations.EtatDeConnexion;

public class SauvegardeInfos // Sauvergarde des infos de connexion
{
	private int numeroDemande, addrSource, addrDestination, numeroConnexion;
	private EtatDeConnexion etatConnexion;

	public SauvegardeInfos(int numeroConnexion, int addrSource, int addrDestination, EtatDeConnexion etatConnexion,
			int numeroDemande)
	{
		this.numeroDemande = numeroDemande;
		this.addrSource = addrSource;
		this.addrDestination = addrDestination;
		this.numeroConnexion = numeroConnexion;
		this.etatConnexion = etatConnexion;
	}

	public EtatDeConnexion getEtatConnexion()
	{
		return etatConnexion;
	}

	public void setEtatConnexion(EtatDeConnexion etatConnexion)
	{
		this.etatConnexion = etatConnexion;
	}

	public int getNumeroDemande()
	{
		return numeroDemande;
	}

	public int getAddrSource()
	{
		return addrSource;
	}

	public int getAddrDestination()
	{
		return addrDestination;
	}

	public int getNumeroConnexion()
	{
		return numeroConnexion;
	}

}
