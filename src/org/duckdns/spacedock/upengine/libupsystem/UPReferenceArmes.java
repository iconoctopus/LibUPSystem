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

import java.util.ArrayList;
import java.util.EnumMap;
import javax.json.JsonArray;
import javax.json.JsonObject;
import static org.duckdns.spacedock.commonutils.JSONHandler.loadJsonFile;

/**
 * classe permettant l'accès aux éléments de référence concernant les armes
 *
 * @author ykonoclast
 */
public class UPReferenceArmes
{

    /**
     * tableau contenant toutes les armes existant en jeu
     */
    private final JsonArray m_tabArmes;
    /**
     * liste des libellés des catégories d'armes de corps à corps
     */
    private final JsonArray m_listCatArmCaC;
    /**
     * liste des libellés des catégories d'armes à distance
     */
    private final JsonArray m_listCatArmDist;

    /**
     * liste des libellés des modes d'attaque des armes
     */
    private final JsonArray m_listLblModArm;
    /**
     * liste des libellés des types d'armes
     */
    private final JsonArray m_listLblTypArm;
    /**
     * liste des libellés de qualité d'arme
     */
    public final EnumMap<Arme.QualiteArme, String> m_listLblQualite;
    /**
     * liste des libellés d'équilibrage d'arme
     */
    public final EnumMap<Arme.EquilibrageArme, String> m_listLblEquilibrage;
    /**
     * instance unique de cet objet
     */
    private static UPReferenceArmes m_instance;

    /**
     * véritable constructeur privé effectuant tous les accès fichiers à
     * l'instanciation afin de limiter les temps de latence à une grosse fois
     */
    private UPReferenceArmes()
    {
	JsonObject object;

	object = loadJsonFile("libupsystem", "equipement/caracs_armes.json");
	m_tabArmes = object.getJsonArray("armes");
	m_listLblTypArm = object.getJsonArray("types_armes");
	m_listCatArmCaC = object.getJsonArray("cat_armes_cac");
	m_listCatArmDist = object.getJsonArray("cat_armes_dist");
	m_listLblModArm = object.getJsonArray("mod_armes");

	JsonArray tabQualite = object.getJsonArray("qualite_armes");
	m_listLblQualite = new EnumMap<>(Arme.QualiteArme.class);
	m_listLblQualite.put(Arme.QualiteArme.inferieure, tabQualite.getString(0));
	m_listLblQualite.put(Arme.QualiteArme.moyenne, tabQualite.getString(1));
	m_listLblQualite.put(Arme.QualiteArme.superieure, tabQualite.getString(2));
	m_listLblQualite.put(Arme.QualiteArme.maitre, tabQualite.getString(3));

	JsonArray tabEquilibrage = object.getJsonArray("equilibrage_armes");
	m_listLblEquilibrage = new EnumMap<>(Arme.EquilibrageArme.class);
	m_listLblEquilibrage.put(Arme.EquilibrageArme.mauvais, tabEquilibrage.getString(0));
	m_listLblEquilibrage.put(Arme.EquilibrageArme.normal, tabEquilibrage.getString(1));
	m_listLblEquilibrage.put(Arme.EquilibrageArme.bon, tabEquilibrage.getString(2));
    }

    /**
     *
     * @return l'instance unique de la référence, la construit si elle n'existe
     * pas
     */
    public static UPReferenceArmes getInstance()
    {
	if (m_instance == null)
	{
	    m_instance = new UPReferenceArmes();
	}
	return (m_instance);
    }

    /**
     *
     * @param p_indice
     * @return le bonus d'init de l'arme
     */
    public int getBonusInitArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("bonus_init");
    }

    /**
     *
     * @param p_indice
     * @return l'indice de catégorie de l'arme
     */
    public int getCategorieArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("categorie");
    }

    /**
     *
     * @param p_indice
     * @return le libellé d'une arme
     */
    public String getLblArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getString("nom");
    }

    /**
     *
     * @return la liste des équilibrage d'armes
     */
    public EnumMap getListEquilibrage()
    {
	return m_listLblEquilibrage;
    }

    /**
     *
     * @return la liste des qualités d'armes
     */
    public EnumMap getListQualiteArme()
    {
	return m_listLblQualite;
    }

    /**
     *
     * @param p_index
     * @return le libellé d'un mode d'attaque
     */
    public String getLblModeArme(int p_index)
    {
	return m_listLblModArm.getString(p_index);
    }

    /**
     * renvoie la liste des catégories d'armes de corps à corps
     *
     * @return
     */
    public ArrayList<String> getListCatArmeCaC()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_listCatArmCaC.size(); ++i)
	{
	    res.add(m_listCatArmCaC.getString(i));
	}
	return res;
    }

    /**
     * renvoie la liste des catégories d'armes à distance
     *
     * @return
     */
    public ArrayList<String> getListCatArmeDist()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_listCatArmDist.size(); ++i)
	{
	    res.add(m_listCatArmDist.getString(i));
	}
	return res;
    }

    /**
     * la liste des noms de toutes les armes
     *
     * @return
     */
    public ArrayList<String> getListArmes()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_tabArmes.size(); ++i)
	{
	    res.add(m_tabArmes.getJsonObject(i).getString("nom"));
	}
	return res;
    }

    /**
     * la liste des noms de tous les types d'arme
     *
     * @return
     */
    public ArrayList<String> getListTypesArmes()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_listLblTypArm.size(); ++i)
	{
	    res.add(m_listLblTypArm.getString(i));
	}
	return res;
    }

    /**
     * renvoie le max de munitions au sein de l'arme
     *
     * @param p_indice
     * @return
     */
    public int getMagasinArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("magasin");
    }

    /**
     *
     * @param p_indice
     * @return le malus aux jets d'attaque de l'arme
     */
    public int getMalusAttaqueArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("malus_attaque");
    }

    /**
     *
     * @param p_indice
     * @return le mode d'attaque d'une arme représentée par son index
     */
    public int getModArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("mode");
    }

    /**
     * renvoie le nombre d'actions nécessaire au rechargement de l'arme
     *
     * @param p_indice
     * @return
     */
    public int getNbActionsRechargeArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("actions_recharge");
    }

    /**
     *
     * @param p_indice
     * @return la valeur de dégâts d'une arme
     */
    public int getVDArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("VD");
    }

    /**
     *
     * @param p_indice
     * @return si l'arme doit s'employer à deux mains
     */
    public boolean isArme2Mains(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getBoolean("2mains");
    }

    /**
     *
     * @param p_indice
     * @return le physique minimal de l'arme
     */
    public int getPhysMinArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("physique_minimal");
    }

    /**
     * renvoie la portée de l'arme
     *
     * @param p_indice
     * @return
     */
    public int getPorteeArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("portee");
    }

    /**
     *
     * @param p_indice
     * @return l'indice du type de l'arme
     */
    public int getTypeArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("type");
    }
}
