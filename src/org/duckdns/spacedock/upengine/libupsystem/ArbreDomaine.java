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
public class ArbreDomaine
{
//TODO ajouter interface pour les spécialités des compétences

    private final ArrayList<Domaine> m_listDomaines = new ArrayList<>();

    /**
     * constructeur d'un arbre de domaines et compétences. Celui-ci est
     * initialement aux caracs minimales.
     */
    public ArbreDomaine()
    {
	for (int i = 0; i < 9; ++i)
	{
//TODO : mettre en référence le nombre des domaines et le récupérer ici plutot que d'utiliser la valeur hardcodée ci-dessous
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
}
