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
class Domaine
{

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
	    int nbComps = 0;
	    nbComps = UPReference.getInstance().getListComp(p_indice).size();
	    for (int i = 0; i < nbComps; ++i)
	    {
		m_competences.add(new Competence(0, null));
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance().getString("indice") + ":" + p_indice);
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
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance().getString("rang") + ":" + p_rang);
	}
    }

    /**
     * @param p_comp l'indice de la compétence visée
     * @return le rang de la competence en question
     */
    int getRangCompetence(int p_comp)
    {
	return m_competences.get(p_comp).getRang();
    }

    /**
     *
     * @param p_indice
     * @param p_rang
     */
    void setRangComp(int p_indice, int p_rang)
    {
	if (p_rang <= m_rang)
	{
	    m_competences.get(p_indice).setRang(p_rang);
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance().getString("rang") + " " + PropertiesHandler.getInstance().getString("comp") + " > " + PropertiesHandler.getInstance().getString("rang") + " " + PropertiesHandler.getInstance().getString("dom"));
	}
    }

    /**
     *
     * @param p_comp
     * @returns la liste des spécialités de la comp passée en paramétre
     */
    ArrayList<String> getSpecialites(int p_comp)
    {
	return m_competences.get(p_comp).getSpecialites();
    }

    /**
     *
     * @param p_comp
     * @param p_specialite
     */
    void addSpecialite(int p_comp, String p_specialite)
    {
	m_competences.get(p_comp).addSpecialite(p_specialite);
    }

    /**
     *
     * @param p_comp
     * @param p_indiceSpe
     */
    void removeSpecialite(int p_comp, int p_indiceSpe)
    {
	m_competences.get(p_comp).removeSpecialite(p_indiceSpe);
    }

    /**
     * effectue le jet de l'une des compétences du domaine
     *
     * @param p_comp
     * @param p_trait
     * @param p_nd
     * @param p_modifNbDesLances
     * @param p_modifNbDesGardes
     * @param p_modifScore
     * @param p_isSonne
     * @return
     */
    RollUtils.RollResult effectuerJetComp(int p_comp, int p_trait, int p_nd, int p_modifNbDesLances, int p_modifNbDesGardes, int p_modifScore, boolean p_isSonne)
    {
	int result = 0;

	if (getRang() > 0 && p_comp >= 0 && p_trait >= 0)
	{
	    int comp = m_competences.get(p_comp).getRang();

	    int bonus = p_modifScore;
	    int lances = getRang() + comp + p_modifNbDesLances;
	    int gardes = p_trait + p_modifNbDesGardes;
	    if (lances > 0)
	    {
		if (p_trait > 0)
		{
		    /*if(specialite)
		{
		//TODO gérer spécialité ici plus tard (et pas dans le getRang de la comp pour ne pas passer le rang 3 ou 5 par erreur)

		    lances++;
		}*/
		    if (comp >= 3)
		    {
			bonus += 5;
		    }
		    result = RollUtils.lancer(lances, gardes, p_isSonne);
		}
		result = result + bonus;
		if (result < 0)
		{
		    result = 0;
		}
	    }
	}
	else
	{
	    String message = "";

	    message = message.concat(PropertiesHandler.getInstance().getString("trait") + ":" + p_trait);
	    message = message.concat(" " + PropertiesHandler.getInstance().getString("dom") + ":" + getRang());
	    message = message.concat(" " + PropertiesHandler.getInstance().getString("indice") + " " + PropertiesHandler.getInstance().getString("comp") + ":" + p_comp);
	    ErrorHandler.paramAberrant(message);
	}
	return RollUtils.extraireIncrements(result, p_nd);
    }
}
