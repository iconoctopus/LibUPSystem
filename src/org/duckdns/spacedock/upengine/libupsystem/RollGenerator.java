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
public final class RollGenerator
{

    /**
     * Random seedé une fois à la première instanciation
     */
    private final Random m_random;
    /**
     * instance unique de cet objet
     */
    private static volatile RollGenerator m_instance;

    /**
     * véritable constructeur privé, permet de garantir le seedage unique du
     * random
     */
    private RollGenerator()
    {
	m_random = new Random();
    }

    /**
     *
     * @return l'instance unique du générateur, la construit si elle n'existe
     * pas
     */
    public static RollGenerator getInstance()
    {
	if (m_instance == null)
	{
	    synchronized (RollGenerator.class)
	    {
		if (m_instance == null)
		{
		    m_instance = new RollGenerator();
		}
	    }
	}
	return (m_instance);
    }

    /**
     *
     * @param p_ND
     * @param p_nbLances
     * @param p_nbGardes
     * @param p_non_relance_dix
     * @param p_modif le modificateur au score final
     * @return le résultat d'un jet donné : sa réussite et les incréments
     * obtenus le cas échéant. Seul endroit où les incréments sont calculés.
     */
    public RollResult effectuerJet(int p_ND, int p_nbLances, int p_nbGardes, boolean p_non_relance_dix, int p_modif)
    {//on ne vérifie pas le ND, il pourrait être négatif suite à des bonus divers
	RollResult result = null;
	int score = lancerGarder(p_nbLances, p_nbGardes, p_non_relance_dix) + p_modif;
	if (score >= p_ND)
	{//réussite
	    int increments = (score - p_ND) / 5;//division entre entiers donc troncature, nickel car les incréments sont par tranches entières
	    result = new RollResult(increments, true, score);
	}
	else
	{//échec
	    result = new RollResult(0, false, score);
	}

	return result;
    }

    /**
     * méthode centrale du système de jeu : fait manipuler les dés et obtient
     * les résultats chiffrés
     *
     * @param p_nbLances
     * @param p_nbGardes
     * @param p_non_relance_dix
     * @return le score obtenu
     */
    public int lancerGarder(int p_nbLances, int p_nbGardes, boolean p_non_relance_dix)
    {
	int total = 0;
	if (p_nbLances >= 0 && p_nbGardes >= 0)
	{
	    if (p_nbLances < p_nbGardes)
	    {//inversion des dés lancés et gardés si nécessaire
		total = wnosort(p_nbGardes, p_nbLances, p_non_relance_dix);
	    }
	    else
	    {//jet normal
		total = wnosort(p_nbLances, p_nbGardes, p_non_relance_dix);
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
     * lance un dé, éventuellement explosif
     *
     * @param p_non_relance_dix
     * @return
     */
    public int lancerDe(boolean p_non_relance_dix)
    {
	int result = m_random.nextInt(10) + 1;
	while (!p_non_relance_dix && result == 10)
	{
	    result += m_random.nextInt(10) + 1;
	}
	return result;
    }

    /**
     * méthode qui traite effectivement les groupements de dés, les laissant et
     * décidant lesquels garder
     *
     * @param p_nbLances
     * @param p_nbGardes
     * @param p_non_relance_dix
     * @return
     */
    private int wnosort(int p_nbLances, int p_nbGardes, boolean p_non_relance_dix)
    {
	int total = 0;
	int[] gardes = new int[p_nbGardes];//automatiquement initialisé à zéro en java
	//lancement et traitement de chaque dé
	for (int i = 0; i < (p_nbLances); i++)
	{
	    int deLance = lancerDe(p_non_relance_dix);
	    //vérification de si le dé doit être gardé

	    for (int j = 0; (j < p_nbGardes && deLance > gardes[j]); ++j)
	    {
		if ((j - 1) >= 0)//on n'est pas au début du tableau des dés gardés et on peut donc inverser le dé actuellement considéré avec son prédecesseur
		{
		    gardes[j - 1] = gardes[j];
		}
		gardes[j] = deLance;//on tente d'insérer le nouveau dé à cette place avant un nouveau tour
	    }
	}
	for (int de : gardes)//somme des dés gardés
	{
	    total += de;
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
