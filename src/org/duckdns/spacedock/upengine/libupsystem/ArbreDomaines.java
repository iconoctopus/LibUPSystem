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

/**
 * Classe représentant l'ensemble des domaines et de leurs compétences (et les
 * contenant). Elle les construit pour un perso (les domaines et compétences ne
 * devraient jamais être manipulés en dehors de cette classe).
 *
 * @author ykonoclast
 */
public class ArbreDomaines
{

    /**
     * liste des domaines de l'arbre
     */
    private final ArrayList<Domaine> m_listDomaines = new ArrayList<>();

    /**
     * constructeur d'un arbre de domaines et compétences. Celui-ci est
     * initialement aux caracs minimales.
     */
    public ArbreDomaines()
    {
	int nbDomaines = UPReferenceSysteme.getInstance().getListDomaines().size();
	for (int i = 0; i < nbDomaines; ++i)
	{
	    m_listDomaines.add(new Domaine(i, 1));
	}
    }

    /**
     *
     * @param p_indDomaine
     * @param p_indComp
     * @return
     */
    public int getRangComp(int p_indDomaine, int p_indComp)
    {
	return m_listDomaines.get(p_indDomaine).getRangComp(p_indComp);
    }

    /**
     *
     * @param p_indDomaine
     * @return
     */
    public int getRangDomaine(int p_indDomaine)
    {
	return m_listDomaines.get(p_indDomaine).getRang();
    }

    /**
     *
     * @param p_indDomaine
     * @param p_indComp
     * @param p_rang
     */
    public void setRangComp(int p_indDomaine, int p_indComp, int p_rang)
    {
	m_listDomaines.get(p_indDomaine).setRangComp(p_indComp, p_rang);
    }

    /**
     *
     * @param p_indDomaine
     * @param p_rang
     */
    public void setRangDomaine(int p_indDomaine, int p_rang)
    {
	m_listDomaines.get(p_indDomaine).setRang(p_rang);
    }

    /**
     *
     * @param p_indDomaine
     * @param p_indComp
     * @param p_specialite
     */
    void addSpecialite(int p_indDomaine, int p_indComp, String p_specialite)
    {
	m_listDomaines.get(p_indDomaine).addSpecialite(p_indComp, p_specialite);
    }

    /**
     *
     * @param p_indDomaine l'INDEX de la comp
     * @param p_indComp l'INDEX de la comp
     * @param p_rangTrait le RANG du trait
     * @param p_nd
     * @param p_modifNbDesLances
     * @param p_modifNbDesGardes
     * @param p_modifScore
     * @param p_isSonne
     * @return
     */
    RollUtils.RollResult effectuerJetComp(int p_rangTrait, int p_indDomaine, int p_indComp, int p_nd, int p_modifNbDesLances, int p_modifNbDesGardes, int p_modifScore, boolean p_isSonne)
    {
	return m_listDomaines.get(p_indDomaine).effectuerJetComp(p_rangTrait, p_indComp, p_nd, p_modifNbDesLances, p_modifNbDesGardes, p_modifScore, p_isSonne);
    }

    /**
     *
     * @param p_indDomaine
     * @param p_indComp
     * @return
     */
    ArrayList<String> getSpecialites(int p_indDomaine, int p_indComp)
    {
	return m_listDomaines.get(p_indDomaine).getSpecialites(p_indComp);
    }

    /**
     *
     * @param p_indDomaine
     * @param p_indComp
     * @param p_indiceSpe
     */
    void removeSpecialite(int p_indDomaine, int p_indComp, int p_indiceSpe)
    {
	m_listDomaines.get(p_indDomaine).removeSpecialite(p_indComp, p_indiceSpe);
    }
}
