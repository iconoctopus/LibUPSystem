package libupsystem;

public class Arme//TODO ajouter malus attaque
{
	private int desLances;
	public int getDesLances()
	{
		return desLances;
	}

	public int getDesGardes()
	{
		return desGardes;
	}

	public int getBonusInit()
	{
		return bonusInit;
	}

	public int getTypeArme()
	{
		return typeArme;
	}

	private int desGardes;
	private int bonusInit;
	private int typeArme; //simple, perce-amure, penetrante, perce-blindage ou energetique, respectivement de 0 à 4
	
	public Arme(int lance, int garde, int initBonus, int type)
	{
		desLances=lance;
		desGardes = garde;
		bonusInit = initBonus;
		typeArme = type;
	}
	
}