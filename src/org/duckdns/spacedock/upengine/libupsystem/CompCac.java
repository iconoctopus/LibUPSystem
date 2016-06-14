/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

/**
 * Compétence spécialisée dans l'attaque au corps à corps, qui comprend deux
 * sous compétences : pour attaque et parade (le combat à distance ne nécessite
 * pas de compétence spéciale) son indice dans le tableau de domaine correspond
 * à la catégorie d'arme
 *
 * @author iconoctopus
 */
class CompCac extends Competence
{

    /**
     * le rang de parade, celui d'attaque est dans la superclasse
     */
    private int m_rangParade;

    /**
     * constructeur, fournit un rang à la superclase (celui d'attaque)
     *
     * @param p_attaque
     * @param p_parade
     */
    CompCac(int p_attaque, int p_parade)
    {
	super(p_attaque);

	setParade(p_parade);

    }

    /**
     * renvoie le rang de parade, celui d'attaque vient de la superclasse
     *
     * @return
     */
    int getParade()
    {
	return m_rangParade;
    }

    /**
     * interroge la superclasse pour avoir le rang d'attaque (la superclasse
     * répond donc par défaut le rang d'attaque : c'est cohérent, on a souvent
     * l'attaque mais esquive en défense sauf pour les armes spéciales)
     *
     * @return
     */
    int getAttaque()
    {
	return getRang();
    }

    /**
     * met à jour le rang de parade celui d'attaque vient de la superclasse
     *
     * @return
     */
    void setParade(int p_rang)
    {
	if(p_rang >= 0)
	{
	    m_rangParade = p_rang;
	}
	else
	{
	    ErrorHandler.paramAberrant("rang:" + p_rang);
	}
    }

    /**
     * appelle la SuperClasse pour indiquer le rang d'attaque
     *
     * @return
     */
    void setAttaque(int p_rang)
    {
	setRang(p_rang);
    }

}
