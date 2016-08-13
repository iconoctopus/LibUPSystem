package org.duckdns.spacedock.upengine.libupsystem;

import java.util.Random;

/**
 * classe de méthodes statiques techniques effectuant la réalité des jets
 * nécessaires, seul endroit où les dés sont effectivement manipulés et les
 * incréments effectivement calculés
 *
 * @author iconoctopus
 */
public final class RollUtils
{

    /**
     *
     * @param p_score
     * @param p_ND
     * @return le résultat d'un jet donné : sa réussite et les incréments
     * obtenus le cas échéant. Seul endroit où les incréments sont calculés.
     */
    public static RollResult extraireIncrements(int p_score, int p_ND)
    {//on ne vérifie pas le ND, il pourrait être négatif suite à des bonus divers
	RollResult result;
	if (p_score >= p_ND)
	{
	    double quotient = ((double) (p_score) - (double) (p_ND));
	    quotient = quotient / 5.0;
	    int increments = (int) quotient;//on caste simplement pour tronquer car les incréments sont par tranches entières
	    result = new RollResult(increments, true);
	}
	else
	{
	    result = new RollResult(0, false);
	}
	return result;
    }

    /**
     * méthode centrale du système de jeu : manipule les dés et obtient les
     * résultats chiffrés
     *
     * @param p_nbLances
     * @param p_nbGardes
     * @param p_non_relance_dix
     * @return le score obtenu
     */
    public static int lancer(int p_nbLances, int p_nbGardes, boolean p_non_relance_dix)//todo tester les cas d'erreur sur cette méthode
    {
	int i, total = 0;
	if (p_nbLances >= 0 && p_nbGardes >= 0)
	{
	    if (p_nbGardes > 0)
	    {
		Random rand = new Random();
		if (p_nbLances < p_nbGardes)
		{
		    int temp = p_nbLances;
		    p_nbLances = p_nbGardes;
		    p_nbGardes = temp;
		}
		int[] gardes = new int[p_nbGardes];
		for (i = 0; i < gardes.length; i++)
		{
		    gardes[i] = 0;
		}
		for (i = 0; i < (p_nbLances); i++)
		{
		    int alea = rand.nextInt(10) + 1;
		    while (!p_non_relance_dix && alea == 10)
		    {
			alea += rand.nextInt(10) + 1;
		    }
		    int j = 0;
		    while (j < gardes.length && alea > gardes[j])
		    {
			if ((j - 1) >= 0)
			{
			    gardes[j - 1] = gardes[j];
			}
			gardes[j] = alea;
			j++;
		    }
		}
		for (i = 0; i < gardes.length; i++)
		{
		    total += gardes[i];
		}
	    }
	    else
	    {
		total = 0;
	    }

	}
	else
	{
	    String message = "";
	    message = message.concat(PropertiesHandler.getInstance().getString("lances") + ":" + p_nbLances);
	    message = message.concat(" " + PropertiesHandler.getInstance().getString("gardes") + ":" + p_nbGardes);
	    ErrorHandler.paramAberrant(message);
	}
	return total;
    }

    /**
     * classe interne encapsulant un résultat de jet : sa réussite et le nombre
     * d'incréments obtenus le cas échéant. On n'utilise pas de collections
     * clé/valeur comme une EnumMap car l'on veut juste un accès simple à des
     * champs définis : inutile de dégrader les performances avec toute la
     * mécanique des collections.
     */
    public static class RollResult
    {

	private final int m_nbIncrements;
	private final boolean m_jetReussi;

	public int getNbIncrements()
	{
	    return m_nbIncrements;
	}

	public boolean isJetReussi()
	{
	    return m_jetReussi;
	}

	public RollResult(int p_increments, boolean p_reussite)
	{
	    this.m_nbIncrements = p_increments;
	    this.m_jetReussi = p_reussite;
	}
    }
}
