package org.duckdns.spacedock.libupsystem;

import java.util.Random;

public final class UPSystem
{	
	public static RollResult lancerCompetence(int domaine, Competence comp, int trait, boolean non_relance_dix, int ND) throws UPCompParameterFormatException, UPRollParameterFormatException
	{
		return extraireIncrements(effectuerJetComp(domaine, comp.getRang(), trait, non_relance_dix, comp.isSpecialiste()),ND);
	}
	
	public static RollResult extraireIncrements(int score, int ND)
	{
		RollResult result;
		if (score>=ND)
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
	
	private static int effectuerJetComp(int domaine, int comp, int trait, boolean non_relance_dix, boolean specialite) throws UPCompParameterFormatException, UPRollParameterFormatException
	{
		if (domaine > 0 && comp >= 0 && trait >= 0)
		{
			int bonus = 0, lances = domaine + comp, result;
			if (trait > 0)
			{
				if (specialite)
				{
					lances++;
				}
				if (comp >= 3)
				{
					bonus = 5;
				}
				result = lancer(lances, trait, non_relance_dix);
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
			throw new UPCompParameterFormatException(domaine, comp, trait);
		}
	}

	public static int lancer(int nbLances, int nbGardes, boolean non_relance_dix) throws UPRollParameterFormatException
	{
		int i, total = 0;
		if (nbLances >= 0 && nbGardes >= 0)
		{
			if (nbGardes > 0)
			{
				Random rand = new Random();
				if (nbLances < nbGardes)
				{
					int temp = nbLances;
					nbLances = nbGardes;
					nbGardes = temp;
				}
				int[] gardes = new int[nbGardes];
				for (i = 0; i < gardes.length; i++)
				{
					gardes[i] = 0;
				}
				for (i = 0; i < (nbLances); i++)
				{
					int alea = rand.nextInt(10) + 1;
					while (!non_relance_dix && alea == 10)
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
			return total;
		}
		else
		{
			throw new UPRollParameterFormatException(nbLances, nbGardes);
		}
	}
}