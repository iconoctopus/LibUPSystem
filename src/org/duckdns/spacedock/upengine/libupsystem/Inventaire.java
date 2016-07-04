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
     * liste des armures portées, visible par le package pour être facilement
     * manipulable pour l'instant car de toute façon sera transmis sous forme de
     * liste quand interrogé par le contrôleur
     */
    final ArrayList<Armure> listArmures;
    /**
     * indice de l'arme courante dans la liste des armes, -1 par défaut
     * signifiant mains nues (rien)
     */
    private int m_armeCourante = -1;
    /**
     * indice de l'armure courante dans la liste des armes, -1 par défaut
     * signifiant aucune
     */
    private int m_armureCourante = -1;

    /**
     * constructeur sans éléments initiaux : on initialise quand même pour
     * assurer que l'ajou sera possible
     */
    Inventaire()
    {
	listArmes = new ArrayList<>();
	listArmures = new ArrayList<>();
    }

    /**
     * constructeur avec paramétres, ceux-ci sont ignorés si l'un est invalide
     *
     * @param p_armes
     * @param p_armures
     */
    Inventaire(ArrayList<Arme> p_armes, ArrayList<Armure> p_armures)
    {
	if (p_armes != null)//si les paramétres sont incorects on les rejette mais l'inventaire est quand même créé pour ne pas perturber l'application, il sera toujours possible de le valuer plus tard
	{
	    listArmes = p_armes;
	}
	else
	{
	    listArmes = new ArrayList<>();
	}
	if (p_armures != null)
	{
	    listArmures = p_armures;
	}
	else
	{
	    listArmures = new ArrayList<>();
	}
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
     * indiquer l'indice de l'armure à enfiler
     *
     * @param p_indice
     */
    void setArmureCourante(int p_indice)
    {
	m_armureCourante = p_indice;
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

    /**
     *
     * @return l'armure actuellement portée
     */
    Armure getArmureCourante()
    {
	if (m_armureCourante >= 0)
	{
	    return listArmures.get(m_armureCourante);
	}
	else
	{
	    return null;
	}
    }
}
