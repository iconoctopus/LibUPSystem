package org.duckdns.spacedock.libupsystem;

public class Armure
{//TODO : reecriture : l'armure sera composée de pièces avec des points et d'éventuels malus; tester pour vérifier que les maxima de chaque pièce sont respectés

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

    public Armure(int nbPoints, int typeArmure)
    {
	points = nbPoints;

	augND = UPReference.getInstance().getArmureBonusND(nbPoints);
	redDegats = UPReference.getInstance().getArmureRedDegats(nbPoints);
	type = typeArmure;
    }
}
