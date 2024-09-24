package paquets;

import enumurations.Primitive;
import interfaces.IPaquet;

public class PaquetIndLiberation implements IPaquet
{
	private final int NUMERO_CONNEXION;
	private final int typeDePaquet;
	private int addresseSource;
	private int addresseDest;
	private int raison;
	private Primitive primitive;

	public PaquetIndLiberation(int nUMERO_CONNEXION, int addresseSource, int addresseDest, int raison)
	{
		NUMERO_CONNEXION = nUMERO_CONNEXION;
		this.typeDePaquet = 0b00010011;
		this.addresseSource = addresseSource;
		this.addresseDest = addresseDest;
		this.raison = raison;
		this.primitive = Primitive.N_DISCONNECT_ind;
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

	// Obtention de la chaine binaire de "raison"
	private String raisonBinaryDigits()
	{
		return String.format("%8s", Integer.toBinaryString(raison)).replace(' ', '0');
	}

	@Override
	public String toString()
	{
		return "Paquet d'indication de liberation :[NUMERO_CONNEXION=" + NUMERO_CONNEXION + ", typeDePaquet="
				+ typePaquetBinaryDigits() + ", addresseSource=" + addresseSource + ", addresseDest="
				+ addresseDest + ", Raison : " + raisonBinaryDigits() + "]";
	}

}
