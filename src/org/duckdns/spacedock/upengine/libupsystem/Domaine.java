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

import java.util.ArrayList;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 *
 * @author ykonoclast
 */
class Domaine
{

    /**
     * le rang du domaine en question
     */
    private int m_rang;

    /**
     * la liste des compétences du domaine. Pour le corps à corps les comps
     * d'attaque ont l'indice catégorie * 2 et les comps de parade l'indice
     * catégorie * 2 +1
     */
    private final ArrayList<Competence> m_competences = new ArrayList<>();

    /**
     * Dans ce constructeur par défaut on initialise les comps à 0
     *
     * @param p_indice
     * @param p_rang
     */
    Domaine(int p_indice, int p_rang)
    {
	if (p_indice >= 0)
	{
	    setRang(p_rang);
	    int nbComps = UPReferenceSysteme.getInstance().getListComp(p_indice).size();
	    for (int i = 0; i < nbComps; ++i)
	    {
		m_competences.add(new Competence(0, new ArrayList<>()));
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("commonutils").getString("indice") + ":" + p_indice);
	}
    }

    /**
     * @return the m_rang
     */
    int getRang()
    {
	return m_rang;
    }

    /**
     *
     * @param p_rang
     */
    final void setRang(int p_rang)
    {
	if (p_rang > 0)//les domaines possédés sont toujours supérieurs à 0 car sinon on ne pourrait pas faire de jet
	{
	    m_rang = p_rang;
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("rang") + ":" + p_rang);
	}
    }

    /**
     * @param p_indComp l'indice de la compétence visée dans le tableau interne
     * du domaine
     * @return le rang de la competence en question
     */
    int getRangComp(int p_indComp)
    {
	return m_competences.get(p_indComp).getRang();
    }

    /**
     *
     * @param p_indComp
     * @param p_rang
     */
    void setRangComp(int p_indComp, int p_rang)
    {
	if (p_rang <= m_rang)
	{
	    m_competences.get(p_indComp).setRang(p_rang);
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("rang") + " " + PropertiesHandler.getInstance("libupsystem").getString("comp") + " > " + PropertiesHandler.getInstance("libupsystem").getString("rang") + " " + PropertiesHandler.getInstance("libupsystem").getString("dom"));
	}
    }

    /**
     *
     * @param p_indComp l'indice de la comp dans le tableau interne du domaine
     * @returns la liste des spécialités de la comp passée en paramétre
     */
    ArrayList<String> getSpecialites(int p_indComp)
    {
	return m_competences.get(p_indComp).getSpecialites();
    }

    /**
     *
     * @param p_indComp
     * @param p_specialite
     */
    void addSpecialite(int p_indComp, String p_specialite)
    {
	m_competences.get(p_indComp).addSpecialite(p_specialite);
    }

    /**
     *
     * @param p_indComp
     * @param p_indiceSpe
     */
    void removeSpecialite(int p_indComp, int p_indiceSpe)
    {
	m_competences.get(p_indComp).removeSpecialite(p_indiceSpe);
    }

    /**
     * effectue le jet de l'une des compétences du domaine
     *
     * @param p_indComp
     * @param p_rangTrait
     * @param p_nd
     * @param p_modifNbDesLances
     * @param p_modifNbDesGardes
     * @param p_modifScore
     * @param p_isSonne
     * @return
     */
    RollGenerator.RollResult effectuerJetComp(int p_rangTrait, int p_indComp, int p_nd, int p_modifNbDesLances, int p_modifNbDesGardes, int p_modifScore, boolean p_isSonne)
    {
	RollGenerator.RollResult result = new RollGenerator.RollResult(0, false, 0);
	RollGenerator generator = RollGenerator.getInstance();

	if (getRang() > 0 && p_indComp >= 0 && p_rangTrait >= 0)
	{
	    int comp = m_competences.get(p_indComp).getRang();
	    int modif = p_modifScore + ((comp >= 3) ? 5 : 0);
	    int lances = getRang() + comp + p_modifNbDesLances;
	    int gardes = p_rangTrait + p_modifNbDesGardes;
	    if (lances > 0)
	    {
		if (p_rangTrait > 0)
		{
		    /*if(specialite)
		{
		//TODO gérer spécialité ici plus tard (et pas dans le getRang de la comp pour ne pas passer le rang 3 ou 5 par erreur)

		    lances++;
		}*/
		    result = generator.effectuerJet(p_nd, lances, gardes, p_isSonne, modif);
		}
	    }
	}
	else
	{
	    String message = "";
	    message = message.concat(PropertiesHandler.getInstance("libupsystem").getString("trait") + ":" + p_rangTrait);
	    message = message.concat(" " + PropertiesHandler.getInstance("libupsystem").getString("dom") + ":" + getRang());
	    message = message.concat(" " + PropertiesHandler.getInstance("commonutils").getString("indice") + " " + PropertiesHandler.getInstance("libupsystem").getString("comp") + ":" + p_indComp);
	    ErrorHandler.paramAberrant(message);
	}
	return result;
    }
}
