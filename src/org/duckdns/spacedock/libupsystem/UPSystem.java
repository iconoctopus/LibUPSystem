package org.duckdns.spacedock.libupsystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

final class UPSystem
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
	if(domaine > 0 && comp >= 0 && trait >= 0)
	{
	    int bonus = 0, lances = domaine + comp, result;
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
	    else
	    {
		result = 0;
	    }
	    result = result + bonus;
	    return (result);
	}
	else
	{
	    String message = "";
	    try
	    {
		message = message.concat(PropertiesHandler.getInstance().getErrorMessage("domaine_inf_0"));
		message = message.concat("Trait=" + trait);
		message = message.concat(", Dom=" + domaine);
		message = message.concat(", Comp=" + comp);
	    }
	    catch(FileNotFoundException e)
	    {
		message = message.concat("fichier de propriétés des textes d'exceptions introuvable");
	    }
	    catch(IOException e)
	    {
		message = message.concat("fichier de propriétés des textes d'exceptions trouvé mais impossible de le lire");
	    }
	    throw new IllegalArgumentException(message);
	}
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
	    return total;
	}
	else
	{
	    String message = "";
	    try
	    {
		message = message.concat(PropertiesHandler.getInstance().getErrorMessage("nbdes_inf_0"));
		message = message.concat("Lancés=" + nbLances);
		message = message.concat(", Gardés=" + nbGardes);
	    }
	    catch(FileNotFoundException e)
	    {
		message = message.concat("fichier de propriétés des textes d'exceptions introuvable");
	    }
	    catch(IOException e)
	    {
		message = message.concat("fichier de propriétés des textes d'exceptions trouvé mais impossible de le lire");
	    }
	    throw new IllegalArgumentException(message);
	}
    }
}
