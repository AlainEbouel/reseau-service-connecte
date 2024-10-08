package paquets;
import enumurations.Primitive;
import interfaces.IPaquet;

public class PaquetAppel implements IPaquet
{
	private final int NUMERO_CONNEXION;
	private final int typeDePaquet;
	private int addresseSource;
	private int addresseDest;
	private Primitive primitive;

	public PaquetAppel(int numeroDeConnexion, int addresseSource, int addresseDest)
	{
		this.NUMERO_CONNEXION = numeroDeConnexion;
		this.typeDePaquet = 0b0001011;
		this.addresseSource = addresseSource;
		this.addresseDest = addresseDest;
		this.primitive = Primitive.N_CONNECT_req;
	}

	public int getNumConnexion()
	{
		return NUMERO_CONNEXION;
	}

	public int getTypeDePaquet()
	{
		return typeDePaquet;
	}

	public int getAddresseSource()
	{
		return addresseSource;
	}

	public int getAddresseDest()
	{
		return addresseDest;
	}

	// Obtention de la chaine binaire du type de paquet
	private String typePaquetBinaryDigits()
	{
		return String.format("%8s", Integer.toBinaryString(typeDePaquet)).replace(' ', '0');
	}

	@Override
	public Primitive getPrimitive()
	{
		return primitive;
	}

	@Override
	public String toString()
	{
		return "******** PaquetAppel ******** [NUMERO_CONNEXION=" + NUMERO_CONNEXION + ", typeDePaquet=" + typePaquetBinaryDigits()
				+ ", addresseSource=" + addresseSource + ", addresseDest=" + addresseDest + ", primitive="
				+ primitive + "]\n";
	}

}
