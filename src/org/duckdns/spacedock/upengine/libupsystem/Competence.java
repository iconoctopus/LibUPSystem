package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;

/**
 * classe représentant une compétence, elle porte un numéro d'identifiant qui la
 * rattache à un domaine.
 *
 * @author iconoctopus
 */
class Competence
{//TODO : gérer les spécialités via un tableau les contenant

    /**
     * le rang de la compétence, déclenche des jets spéciaux à 3 et 5
     */
    private final int m_rang;

    /**
     * libellé de la compétence
     */
    private final String m_nom;

    int getRang()
    {
	return m_rang;
    }

    @Override
    public String toString()
    {
	return m_nom;
    }

    public Competence(int p_rang, String p_nom)
    {
	m_rang = p_rang;
	m_nom = p_nom;
    }
}
