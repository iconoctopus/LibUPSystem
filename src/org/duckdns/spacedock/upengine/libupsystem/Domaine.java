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
public class Domaine
{//TODO : déplacer la logique des jets ici

    private final String m_nom;

    private final int m_rang;

    private ArrayList<Competence> m_competences = new ArrayList<>();

    Domaine(String p_nom, int p_rang, ArrayList<Competence> p_competences)
    {
	m_nom = p_nom;
	m_competences = p_competences;
	m_rang = p_rang;
    }

    @Override
    public String toString()
    {
	return m_nom;
    }
}
