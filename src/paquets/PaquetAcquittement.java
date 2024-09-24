package paquets;

public class PaquetAcquittement
{
	private int numeroDeConnexion;
	private String typeDePaquet;

	public PaquetAcquittement(int numeroDeConnexion, String typeDePaquet)
	{
		this.numeroDeConnexion = numeroDeConnexion;
		this.typeDePaquet = typeDePaquet;
	}

	public int getNumeroDeConnexion()
	{
		return numeroDeConnexion;
	}

	public String getTypeDePaquet()
	{
		return typeDePaquet;
	}

	@Override
	public String toString()
	{
		String typeAcquitement = typeDePaquet.substring(3).equals("00001") ? "Acquitement Positif"
				: "Acquitement Négatif";
		return typeAcquitement + " :[numeroDeConnexion=" + numeroDeConnexion + ", typeDePaquet=" + typeDePaquet
				+ "]";
	}

}
