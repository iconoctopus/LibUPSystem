/*
 * Copyright (C) 2017 ykonoclast
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.Random;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 * classe de méthodes statiques techniques effectuant la réalité des jets
 * nécessaires, seul endroit où les dés sont effectivement manipulés et les
 * incréments effectivement calculés
 *
 * @author ykonoclast
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
	    result = new RollResult(increments, true, p_score);
	}
	else
	{
	    result = new RollResult(0, false, p_score);
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
    public static int lancer(int p_nbLances, int p_nbGardes, boolean p_non_relance_dix)
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
	    message = message.concat(PropertiesHandler.getInstance("libupsystem").getString("lances") + ":" + p_nbLances);
	    message = message.concat(" " + PropertiesHandler.getInstance("libupsystem").getString("gardes") + ":" + p_nbGardes);
	    ErrorHandler.paramAberrant(message);
	}
	return total;
    }

    /**
     * classe interne encapsulant un résultat de jet : sa réussite et le nombre
     * d'incréments obtenus le cas échéant.
     */
    public static final class RollResult
    {

	private final int m_nbIncrements;
	private final boolean m_jetReussi;
	private final int m_scoreBrut;

	public int getNbIncrements()
	{
	    return m_nbIncrements;
	}

	public boolean isJetReussi()
	{
	    return m_jetReussi;
	}

	public int getScoreBrut()
	{
	    return m_scoreBrut;
	}

	public RollResult(int p_increments, boolean p_reussite, int p_scoreBrut)
	{
	    this.m_nbIncrements = p_increments;
	    this.m_jetReussi = p_reussite;
	    this.m_scoreBrut = p_scoreBrut;
	}
    }
}
