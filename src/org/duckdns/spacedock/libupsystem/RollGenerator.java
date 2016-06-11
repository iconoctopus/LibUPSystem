package org.duckdns.spacedock.libupsystem;

import java.io.IOException;
import java.util.Random;

final class RollGenerator
{

    static RollResult lancerCompetence(int domaine, Competence comp, int trait, boolean non_relance_dix, int ND)
    {
	return extraireIncrements(effectuerJetComp(domaine, comp.getRang(), trait, non_relance_dix, comp.isSpecialiste()), ND);
    }

    static RollResult extraireIncrements(int score, int ND)
    {
	RollResult result;
	if(score >= ND)
	{
	    double quotient = ((double) (score) - (double) (ND));
	    quotient = quotient / 5.0;
	    int increments = (int) quotient;
	    result = new RollResult(increments, true);
	}
	else
	{
	    result = new RollResult(0, false);
	}
	return result;

    }

    //todo passer objet domaine avec identificateur de comp plutôt
    private static int effectuerJetComp(int domaine, int comp, int trait, boolean estSonne, boolean specialite)
    {
	int result = 0;
	if(domaine > 0 && comp >= 0 && trait >= 0)
	{
	    int bonus = 0, lances = domaine + comp;
	    if(trait > 0)
	    {
		if(specialite)
		{
		    lances++;
		}
		if(comp >= 3)
		{
		    bonus = 5;
		}
		result = lancer(lances, trait, estSonne);
	    }

	    result = result + bonus;

	}
	else
	{
	    String message = "";

	    message = message.concat("Trait=" + trait);
	    message = message.concat(", Dom=" + domaine);
	    message = message.concat(", Comp=" + comp);
	    ErrorHandler.paramAberrant(message);

	}
	return (result);
    }

    static int lancer(int nbLances, int nbGardes, boolean non_relance_dix)
    {
	int i, total = 0;
	if(nbLances >= 0 && nbGardes >= 0)
	{
	    if(nbGardes > 0)
	    {
		Random rand = new Random();
		if(nbLances < nbGardes)
		{
		    int temp = nbLances;
		    nbLances = nbGardes;
		    nbGardes = temp;
		}
		int[] gardes = new int[nbGardes];
		for(i = 0; i < gardes.length; i++)
		{
		    gardes[i] = 0;
		}
		for(i = 0; i < (nbLances); i++)
		{
		    int alea = rand.nextInt(10) + 1;
		    while(!non_relance_dix && alea == 10)
		    {
			alea += rand.nextInt(10) + 1;
		    }
		    int j = 0;
		    while(j < gardes.length && alea > gardes[j])
		    {
			if((j - 1) >= 0)
			{
			    gardes[j - 1] = gardes[j];
			}
			gardes[j] = alea;
			j++;
		    }

		}
		for(i = 0; i < gardes.length; i++)
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

	    message = message.concat("Lancés=" + nbLances);
	    message = message.concat(", Gardés=" + nbGardes);
	    ErrorHandler.paramAberrant(message);
	}
	return total;
    }

    public static class RollResult
    {

	private final int nbIncrements;

	public int getNbIncrements()
	{
	    return nbIncrements;
	}

	public boolean isJetReussi()
	{
	    return jetReussi;
	}
	private boolean jetReussi;

	public RollResult(int increments, boolean reussite)
	{
	    super();
	    this.nbIncrements = increments;
	    this.jetReussi = reussite;
	}
    }
}
