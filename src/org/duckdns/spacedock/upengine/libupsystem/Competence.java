package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;

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
     * compte comme rang d'attaque dans les CompCac
     *
     * @param p_rang
     */
    void setRang(int p_rang)
    {
	m_rang = p_rang;
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
