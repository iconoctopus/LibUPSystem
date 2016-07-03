/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import org.duckdns.spacedock.upengine.libupsystem.RollUtils.RollResult;

/**
 *
 * @author iconoctopus
 */
public class Domaine
{

    private int m_rang;

    private ArrayList<Competence> m_competences = new ArrayList<>();

    /**
     * pour l'instant le domaine ignore son nom car il est prévu de le faire
     * exposer par les classes supérieures notamment le Perso.
     *
     * Dans ce constructeur par défaut on initialise les comps à 0
     *
     *
     * @param p_rang
     * @param p_competences
     */
    Domaine(int p_indice, int p_rang)
    {
	if (p_indice >= 0)//on n'a rien à faire là si le domaine ne vaut pas 1
	{
	    setRang(p_rang);
	    int nbComps = 0;

	    if (p_indice != 3)//cas général
	    {
		nbComps = UPReference.getInstance().getListComp(p_indice).size();
		for (int i = 0; i < nbComps; ++i)
		{
		    m_competences.add(new Competence(0));
		}
	    }
	    else//traitement particulier du corps à corps
	    {//TODO ATTENTION BROKEN : les catégories ont été mélées, virer ce code et le reporter dans getListComp avec différenciation entre CaC et CaD
		nbComps = UPReference.getInstance().getListCatArme().size();//TODO volontairement pété pour forcer à réparer ça avant de committer l'erreur
		for (int i = 0; i < nbComps; ++i)
		{
		    m_competences.add(new CompCac(0, 0));
		}
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}

    }

    /**
     * constructeur acceptant une liste de comps qu'il confronte à la référence
     * (sauf pour les domaines free form)
     *
     * @param p_indice
     * @param p_rang
     * @param p_competences
     */
    Domaine(int p_indice, int p_rang, ArrayList<Competence> p_competences)
    {
	setRang(p_rang);
	int nbComps = UPReference.getInstance().getListComp(p_indice).size();

	if (p_indice != 1 && p_indice != 3 && p_indice != 4 && nbComps == p_competences.size())//si la référence indique bien ce nombre de compétences, sauf pour les domaines free-form
	{
	    m_competences = p_competences;
	}
	else
	{
	    ErrorHandler.paramAberrant("incohérence entre la référence et le paramétre sur le nombre de compétences du domaine:" + p_indice);
	}
    }

    /**
     * @return the m_rang
     */
    int getRang()
    {
	return m_rang;
    }

    void setRang(int p_rang)
    {
	if (p_rang >= 0)
	{
	    m_rang = p_rang;
	}
	else
	{
	    ErrorHandler.paramAberrant("rang:" + p_rang);
	}
    }

    /**
     * @return the m_competences
     */
    ArrayList<Competence> getCompetences()
    {
	return m_competences;
    }

    /**
     * met à jour le rang d'une compétence, surcharger pour mettre à jour la
     * liste de spécialités quand ce sera géré
     *
     * @param p_indice
     * @param p_rang
     */
    void setComp(int p_indice, int p_rang)
    {
	getCompetences().set(p_indice, new Competence(p_rang));
    }

    /**
     * effectue le jet de l'une des compétences du domaine
     *
     * @param p_comp
     * @param p_trait
     * @param p_nd
     * @param p_modifNbDes un modificateur au nombre de dés lancés
     * @param p_modifScore le modificateur à appliquer au résultat final
     * @param p_isSonne
     * @return un RollResult encapsulant la réussite ou non du jet et les
     * incréments obtenus
     */
    RollResult effectuerJetComp(int p_comp, int p_trait, int p_nd, int p_modifNbDes, int p_modifScore, boolean p_isSonne)
    {//TODO : cette fonction pour les jets d'attaques grâce à getRang() qui abrite la valeur d'attaque, il faudrait un traitement particulier pour les parades actives
	int result = 0;
	int comp = getCompetences().get(p_comp).getRang();
	if (getRang() > 0 && comp >= 0 && p_trait >= 0)
	{
	    int bonus = p_modifScore;
	    int lances = getRang() + comp + p_modifNbDes;
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
		    result = RollUtils.lancer(lances, p_trait, p_isSonne);
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

	    message = message.concat("Trait=" + p_trait);
	    message = message.concat(", Dom=" + getRang());
	    message = message.concat(", Comp=" + comp);
	    ErrorHandler.paramAberrant(message);
	}
	return RollUtils.extraireIncrements(result, p_nd);
    }

}
