/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;

/**
 * encapsule l'équipement d'un personnage, pour l'instant très simple mais
 * pourra s'étoffer à terme
 *
 * @author iconoctopus
 */
class Inventaire
{

    /**
     * liste des armes portées, visible par le package pour être facilement
     * manipulable pour l'instant car de toute façon sera transmis sous forme de
     * liste quand interrogé par le contrôleur
     */
    final ArrayList<Arme> listArmes;
    /**
     * m_armure portée
     *
     */
    Armure m_armure;
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
	listArmes = new ArrayList<>();
    }

    /**
     * constructeur avec paramétres, ceux-ci sont ignorés si l'un est invalide
     *
     * @param p_armes
     * @param p_armures
     */
    Inventaire(ArrayList<Arme> p_armes, Armure p_armure)
    {
	if (p_armes != null)//si les paramétres sont incorects on les rejette mais l'inventaire est quand même créé pour ne pas perturber l'application, il sera toujours possible de le valuer plus tard
	{
	    listArmes = p_armes;
	}
	else
	{
	    listArmes = new ArrayList<>();
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
	    return listArmes.get(m_armeCourante);
	}
	else
	{
	    return null;
	}
    }
}
