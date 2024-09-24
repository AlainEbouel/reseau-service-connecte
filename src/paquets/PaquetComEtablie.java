package paquets;
import enumurations.Primitive;
import interfaces.IPaquet;

public class PaquetComEtablie implements IPaquet
{
	private final int NUMERO_CONNEXION;
	private final int typeDePaquet;
	private int addresseSource;
	private int addresseDest;
	private Primitive primitive;

	public PaquetComEtablie(int numeroDeConnexion, int addresseSource, int addresseDest)
	{
		this.NUMERO_CONNEXION = numeroDeConnexion;
		this.typeDePaquet = 0b00001111;
		this.addresseSource = addresseSource;
		this.addresseDest = addresseDest;
		this.primitive = Primitive.N_CONNECT_resp;
	}

	@Override
	public Primitive getPrimitive()
	{
		return primitive;
	}

	// Obtention de la chaine binaire du type de paquet
	private String typePaquetBinaryDigits()
	{
		return String.format("%8s", Integer.toBinaryString(typeDePaquet)).replace(' ', '0');
	}

	@Override
	public String toString()
	{
		return "Paquet de Communication etablie [NUMERO_CONNEXION=" + NUMERO_CONNEXION + ", typeDePaquet="
				+ typePaquetBinaryDigits() + ", addresseSource=" + addresseSource + ", addresseDest="
				+ addresseDest + ", primitive=" + primitive + "]";
	}

}
