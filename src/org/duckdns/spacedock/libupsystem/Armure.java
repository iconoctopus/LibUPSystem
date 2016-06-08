package org.duckdns.spacedock.libupsystem;

public class Armure
{
	private int redDegats;
	public int getRedDegats()
	{
		return redDegats;
	}

	public int getAugND()
	{
		return augND;
	}

	public int getType()
	{
		return type;
	}

	public int getPoints()
	{
		return points;
	}

	private int augND;
	private int type;//ancienne, moderne, blindage, energetique, respectivement de 0 à 3
	private int points; //nombre de points d'armure
	
	public Armure(int nbPoints,int typeArmure)
	{
		points=nbPoints;
		BiValue effets = ReferenceTableGroup.getEffetsArmure(points);
		augND = ((int) effets.getX());
		redDegats = ((int) effets.getY());
		type=typeArmure;
	}
}