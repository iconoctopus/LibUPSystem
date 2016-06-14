package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
//TODO : comment gérer les comps de combat : ajouter une classe dérivée de celle-ci avec deux rangs : un pour l'attaque et la défense (getRang de super renvoie l'attaque), l'indice de la comp étant la catégorie d'arme(les armes de corps à corps et à distance sont séparées : elles n'ont pas le même domaine) (les ajouter dans le JSON) ; 0 devant être corps à corps. C'est la classe appelante qui choisit la comp à utiliser (ataque ou défense : passer un paramétre dans getNDPassif pour ça)) : plu de désignation par défaut dans la classe perso, vérifier au mieux que c'est l'arme équipée qui est utilisée.

//TODO ajouter la possibilité de faire évoluer ces compétences par l'XP
/**
 * classe représentant une compétence
 *
 * @author iconoctopus
 */
class Competence
{//TODO : gérer les spécialités via un tableau les contenant

    /**
     * le rang de la compétence, déclenche des jets spéciaux à 3 et 5
     */
    private int m_rang;

    int getRang()
    {
	return m_rang;
    }

    /**
     * pour l'instant la compétence ignore son nom car il est fourni par les
     * classes plus élevées en fonction de son indice. Pour l'instant cette
     * classe est inutile : elle n'encapsule qu'un entier, mais à terme elle
     * pourra contenir un tableau de spécalités
     *
     *
     * @param p_rang
     */
    Competence(int p_rang)
    {
	m_rang = p_rang;
    }
}
