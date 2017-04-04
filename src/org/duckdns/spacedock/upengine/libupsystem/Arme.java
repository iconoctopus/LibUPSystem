/*
 * Copyright (C) 2017 ykonoclast
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.duckdns.spacedock.upengine.libupsystem;

/**
 * Classe représentant une arme et permettant de générer des dégâts avec
 * celle-ci.Elle est abstraite car l'on ne doit pouvoir instancier que ses
 * dérivées qui sont porteuses du code signifiant pour le CaC et le CaD
 *
 * @author ykonoclast
 */
public abstract class Arme
{

    /**
     * le nombre de mains nécessaire au maniement de l'arme
     */
    private final int m_NbMainsArme;
    /**
     * le bonus apporté à l'initiative totale
     */
    private int m_bonusInit;
    /**
     * la catégorie d'arme (permet de définir la compétence à utiliser)
     */
    private final int m_categorie;
    /**
     * la VD de l'arme
     */
    private int m_vd;
    /**
     * le malus donné par l'arme à l'attaque
     */
    private final int m_malusAttaque;
    /**
     * le mode d'attaque de l'arme
     */
    private final int m_mode;
    /**
     * le nom de l'arme
     */
    private final String m_nom;
    /**
     * le physique minimal pour manier l'arme.
     */
    private final int m_physMin;
    /**
     * le type de l'arme : simple, perce-amure, pénétrante, perce-blindage ou
     * energétique, respectivement de 0 à 4
     */
    private final int m_typeArme;

    /**
     * constructeur d'arme de corps à corps à parti de la référence UP!
     *
     * @param p_indice
     * @param p_qualite la qualite de l'arme
     * @param p_equilibrage l'equilibrage de l'arme, ignoré si l'arme est de
     * maître
     */
    public Arme(int p_indice, QualiteArme p_qualite, EquilibrageArme p_equilibrage)
    {
	UPReferenceArmes referenceArm = UPReferenceArmes.getInstance();

	String nom = referenceArm.getLblArme(p_indice);

	UPReferenceSysteme referenceSys = UPReferenceSysteme.getInstance();
	nom = nom.concat(" ");
	//récupération des éléments liés à la qualité et l'équilibrage de l'arme
	if (p_qualite == QualiteArme.maitre)//traitement spécial des armes de maître
	{
	    nom = nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.maitre));
	}
	else
	{
	    nom = nom.concat(referenceSys.getCollectionLibelles().liaison);
	    nom = nom.concat(" ");
	    nom = nom.concat(referenceSys.getCollectionLibelles().qualite);
	    nom = nom.concat(" ");

	    switch (p_qualite)
	    {
		case inferieure:
		    nom = nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.inferieure));
		    break;
		case moyenne:
		    nom = nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.moyenne));
		    break;
		case superieure:
		    nom = nom.concat((String) referenceArm.getListQualiteArme().get(QualiteArme.superieure));
		    break;
	    }

	    nom = nom.concat(" ");
	    nom = nom.concat(referenceSys.getCollectionLibelles().addition);
	    nom = nom.concat(" ");
	    nom = nom.concat(referenceSys.getCollectionLibelles().equilibrage);
	    nom = nom.concat(" ");

	    switch (p_equilibrage)
	    {
		case mauvais:
		    nom = nom.concat((String) referenceArm.getListEquilibrage().get(EquilibrageArme.mauvais));
		    break;
		case normal:
		    nom = nom.concat((String) referenceArm.getListEquilibrage().get(EquilibrageArme.normal));
		    break;
		case bon:
		    nom = nom.concat((String) referenceArm.getListEquilibrage().get(EquilibrageArme.bon));
		    break;
	    }
	}

	//récupération et construction des caractéristiques de l'arme
	m_vd = referenceArm.getVDArme(p_indice);
	m_bonusInit = referenceArm.getBonusInitArme(p_indice);
	m_typeArme = referenceArm.getTypeArme(p_indice);
	m_malusAttaque = referenceArm.getMalusAttaqueArme(p_indice);
	m_physMin = referenceArm.getPhysMinArme(p_indice);
	m_nom = nom;
	m_categorie = referenceArm.getCategorieArme(p_indice);
	m_NbMainsArme = referenceArm.getNbMainsArme(p_indice);
	m_mode = referenceArm.getModArme(p_indice);
    }

    public int getBonusInit()
    {
	return m_bonusInit;
    }

    public int getCategorie()
    {
	return m_categorie;
    }

    public int getVD()
    {
	return m_vd;
    }

    public int getMalusAttaque()
    {
	return m_malusAttaque;
    }

    public int getMode()
    {
	return m_mode;
    }

    public int getNbMainsArme()
    {
	return m_NbMainsArme;
    }

    public int getTypeArme()
    {
	return m_typeArme;
    }

    public int getphysMin()
    {
	return m_physMin;
    }

    @Override
    public String toString()
    {
	return m_nom;
    }

    /**
     * Enum contenant les niveaux de qualite des armes
     */
    public enum QualiteArme
    {
	inferieure, moyenne, superieure, maitre
    };

    /**
     * Enum contenant les niveaux d'équilibrage
     */
    public enum EquilibrageArme
    {
	mauvais, normal, bon
    };
}
