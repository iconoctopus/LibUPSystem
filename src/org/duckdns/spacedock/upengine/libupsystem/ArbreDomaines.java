/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;

/**
 *
 * @author iconoctopus
 */
public class ArbreDomaines
{

    private final ArrayList<Domaine> m_listDomaines = new ArrayList<>();

    /**
     * constructeur d'un arbre de domaines et compétences. Celui-ci est
     * initialement aux caracs minimales.
     */
    public ArbreDomaines()
    {
	int nbDomaines = UPReference.getInstance().getListDomaines().size();
	for (int i = 0; i < nbDomaines; ++i)
	{
	    m_listDomaines.add(new Domaine(i, 1));
	}
    }

    RollUtils.RollResult effectuerJetComp(int p_domaine, int p_comp, int p_trait, int p_nd, int p_modifNbDesLances, int p_modifNbDesGardes, int p_modifScore, boolean p_isSonne)
    {
	return m_listDomaines.get(p_domaine).effectuerJetComp(p_comp, p_trait, p_nd, p_modifNbDesLances, p_modifNbDesGardes, p_modifScore, p_isSonne);
    }

    public void setRangDomaine(int p_domaine, int p_rang)
    {
	m_listDomaines.get(p_domaine).setRang(p_rang);
    }

    public int getRangDomaine(int p_domaine)
    {
	return m_listDomaines.get(p_domaine).getRang();
    }

    public void setRangComp(int p_domaine, int p_comp, int p_rang)
    {
	m_listDomaines.get(p_domaine).setRangComp(p_comp, p_rang);
    }

    public int getRangComp(int p_domaine, int p_comp)
    {
	return m_listDomaines.get(p_domaine).getRangCompetence(p_comp);
    }

    ArrayList<String> getSpecialites(int p_domaine, int p_comp)
    {
	return m_listDomaines.get(p_domaine).getSpecialites(p_comp);
    }

    void addSpecialite(int p_domaine, int p_comp, String p_specialite)
    {
	m_listDomaines.get(p_domaine).addSpecialite(p_comp, p_specialite);
    }

    void removeSpecialite(int p_domaine, int p_comp, int p_indiceSpe)
    {
	m_listDomaines.get(p_domaine).removeSpecialite(p_comp, p_indiceSpe);
    }
}
