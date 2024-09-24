package services;
import enumurations.EtatDeConnexion;

public class EntreeDeTable // Représentation des entrées de la table utilisée par la couche transport ET
{
	private final int identifiantExtremite;
	private EtatDeConnexion etatDeConnexion;
	private final int numApplication;

	public EntreeDeTable(int numeroDeConnexion, EtatDeConnexion etatDeConnexion, int numApplication)
	{
		this.identifiantExtremite = numeroDeConnexion;
		this.etatDeConnexion = etatDeConnexion;
		this.numApplication = numApplication;

	}

	public int getIdConnexion()
	{
		return identifiantExtremite;
	}

	public int getNumApplication()
	{
		return numApplication;
	}

	public EtatDeConnexion getEtatDeConnexion()
	{
		return etatDeConnexion;
	}

	public void setEtatDeConnexion(EtatDeConnexion etatDeConnexion)
	{
		this.etatDeConnexion = etatDeConnexion;
	}

}
