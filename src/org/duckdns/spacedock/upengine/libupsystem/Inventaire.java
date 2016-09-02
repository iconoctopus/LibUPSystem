/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import org.duckdns.spacedock.commonutils.ErrorHandler;
import org.duckdns.spacedock.commonutils.PropertiesHandler;

/**
 * encapsule l'équipement d'un personnage, pour l'instant très simple mais
 * pourra s'étoffer à terme
 *
 * @author iconoctopus
 */
class Inventaire
{

    /**
     * liste des armes portées
     */
    private final ArrayList<Arme> m_listArmes;
    /**
     * armure portée
     */
    private Armure m_armure;
    /**
     * indice de l'arme courante dans la liste des armes, -1 par défaut
     * signifiant mains nues (rien)
     */
    private int m_armeCourante = -1;

    /**
     * constructeur sans éléments initiaux : on initialise quand même pour
     * assurer que l'ajout sera possible
     */
    Inventaire()
    {
	m_listArmes = new ArrayList<>();
    }

    /**
     * constructeur avec paramétres, ceux-ci sont ignorés si l'un est invalide
     *
     * @param p_armes
     * @param p_armure
     */
    Inventaire(ArrayList<Arme> p_armes, Armure p_armure)
    {
	if (p_armes != null)//si les paramétres sont incorects on les rejette mais l'inventaire est quand même créé pour ne pas perturber l'application, il sera toujours possible de le valuer plus tard
	{
	    m_listArmes = p_armes;
	}
	else
	{
	    m_listArmes = new ArrayList<>();
	}
	m_armure = p_armure;
    }

    /**
     * indiquer l'indice de l'arme a préparer comme arme actuelle
     *
     * @param p_indice
     */
    void setArmeCourante(int p_indice)
    {

	m_armeCourante = p_indice;

	if (p_indice >= 0 && p_indice < m_listArmes.size())
	{
	    m_armeCourante = p_indice;
	}
	else
	{
	    ErrorHandler.paramAberrant(PropertiesHandler.getInstance("libupsystem").getString("indice") + ":" + p_indice);
	}
    }

    /**
     *
     * @param p_arme
     */
    void addArme(Arme p_arme)
    {
	m_listArmes.add(p_arme);
    }

    /**
     *
     * @param p_indice
     */
    void removeArme(int p_indice)
    {
	m_listArmes.remove(p_indice);
    }

    /**
     * indiquer l'indice de l'arme a préparer comme arme actuelle
     *
     * @param p_indice
     */
    void rangerArmeCourante()
    {
	m_armeCourante = -1;
    }

    /**
     *
     * @return une copie de la liste des armes pour empêcher une modification
     * intempestive
     */
    ArrayList<Arme> getListArmes()
    {
	return new ArrayList<Arme>(m_listArmes);
    }

    /**
     *
     *
     * @param p_armure
     */
    void setArmure(Armure p_armure)
    {
	m_armure = p_armure;
    }

    /**
     *
     *
     * @returns m_armure
     */
    Armure getArmure()
    {
	return (m_armure);
    }

    /**
     *
     * @return l'arme couramment tenue
     */
    Arme getArmeCourante()
    {
	if (m_armeCourante >= 0)
	{
	    return m_listArmes.get(m_armeCourante);
	}
	else
	{
	    return null;
	}
    }

    public enum Lateralisation
    {
	GAUCHE, DROITE
    }
}
