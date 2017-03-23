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
import javax.json.JsonArray;
import javax.json.JsonObject;
import static org.duckdns.spacedock.commonutils.JSONHandler.loadJsonFile;

/**
 * Classe permetant l'accès aux éléments de référence du UP!System
 *
 * @author ykonoclast
 */
public final class UPReferenceSysteme
{

    /**
     * table des autres libellés
     */
    public final CollectionLibelles libelles;
    /**
     * tableau contenant tous les domaines et leurs compétences non free form,
     */
    private final JsonArray m_arbreDomaines;

    /**
     * préfixe des libellés des compétences d'attaque
     */
    private final String m_lblCompAttaque;
    /**
     * préfixe des libellés des compétences d'arts et métiers
     */
    private final String m_lblCompMetier;
    /**
     * préfixe des libellés des compétences de parade
     */
    private final String m_lblCompParade;

    /**
     * table des modificateurs à l'init issus de la coordination
     */
    private final JsonArray m_tableInitCoord;
    /**
     * table des modificateurs à l'init issus du mental
     */
    private final JsonArray m_tableInitMental;
    /**
     * table des libellés des traits
     */
    private final JsonArray m_tableTraits;
    /**
     * instance unique de cet objet
     */
    private static UPReferenceSysteme m_instance;

    /**
     * véritable constructeur privé effectuant tous les accès fichiers à
     * l'instanciation afin de limiter les temps de latence à une grosse fois
     */
    private UPReferenceSysteme()
    {
	JsonObject object;

	//chargement des règles de calcul de l'initiative
	object = loadJsonFile("libupsystem", "tables_systeme/tab_init.json");
	m_tableInitCoord = object.getJsonArray("coordination");
	m_tableInitMental = object.getJsonArray("mental");

	//chargement des libellés des caractéristiques particulières
	object = loadJsonFile("libupsystem", "tables_systeme/tab_caracs.json");
	m_tableTraits = object.getJsonArray("traits");
	m_lblCompAttaque = object.getString("lbl_attaque");
	m_lblCompParade = object.getString("lbl_parade");
	m_lblCompMetier = object.getString("lbl_metier");

	//chargement de l'arbre des domaines et competences
	m_arbreDomaines = object.getJsonArray("arbre_domaines");

	//chargement des libellés divers
	object = loadJsonFile("libupsystem", "tables_systeme/tab_libelles.json");
	libelles = new CollectionLibelles(object);

    }

    /**
     *
     * @return l'instance unique de la référence, la construit si elle n'existe
     * pas
     */
    public static UPReferenceSysteme getInstance()
    {
	if (m_instance == null)
	{
	    m_instance = new UPReferenceSysteme();
	}
	return (m_instance);
    }

    /**
     *
     * @param p_coordination la valeur de coordination
     * @return le modificateur de coordination à l'initiative
     */
    public int getInitModCoord(int p_coordination)
    {
	//le tableau est indexé à partir de 0, pas la coordination
	return m_tableInitCoord.getInt(p_coordination);
    }

    /**
     *
     * @param p_mental la valeur de mental
     * @return le modificateur à l'initiative issu du mental
     */
    public int getInitModMental(int p_mental)
    {
	//le tableau est indexé à partir de 0, pas le mental
	return m_tableInitMental.getInt(p_mental);
    }

    /**
     *
     * @param p_indice l'indice du trait tel que défini dan le fichier JSON
     * @return le libelle du trait indicé
     */
    public String getLibelleTrait(int p_indice)
    {
	return m_tableTraits.getString(p_indice);

    }

    /**
     * Renvoie la liste des libellés de compétences d'un domaine, peut-être plus
     * prosaïquement utilisé pour obtenir le nombre de compétence d'un domaine
     * avec size()
     *
     * @param p_indice
     * @return la liste des libellés des compétences d'un domaine donné par son
     * indice
     */
    public ArrayList<String> getListComp(int p_indice)
    {
	ArrayList<String> res = new ArrayList<>();
	UPReferenceArmes reference = UPReferenceArmes.getInstance();

	if (p_indice == 3)//domaine corps à corps : on renvoie la liste des catégories d'armes de corps à corps dédoublée en parade et attaque
	{
	    ArrayList<String> listCaC = reference.getListCatArmeCaC();
	    for (int i = 0; i < listCaC.size(); ++i)
	    {
		res.add(libelles.attaque + " " + listCaC.get(i));
		res.add(libelles.parade + " " + listCaC.get(i));
	    }
	}
	else
	{
	    if (p_indice == 4)//domaine combat à distance : on renvoie la liste des catégories d'armes à distance
	    {
		ArrayList<String> listDist = reference.getListCatArmeDist();
		for (int i = 0; i < reference.getListCatArmeDist().size(); ++i)
		{
		    res.add(listDist.get(i));
		}
	    }
	    else
	    {
		JsonObject domaine = m_arbreDomaines.getJsonObject(p_indice);
		JsonArray tabComp = domaine.getJsonArray("comps");

		for (int i = 0; i < tabComp.size(); ++i)
		{
		    res.add(tabComp.getString(i));
		}
	    }
	}
	return res;
    }

    /**
     *
     * @return la liste complète des domaines, peut être plus prosaïquement
     * utilisé pour connaître leur nombre total
     */
    public ArrayList<String> getListDomaines()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_arbreDomaines.size(); ++i)
	{
	    res.add(m_arbreDomaines.getJsonObject(i).getString("lbl"));
	}
	return res;
    }

    /**
     * Classe encapsulant les libellés autres que ceux utilisés dans les
     * caractéristiques et l'équipement (surtout utilisé pour l'entrée sortie)
     */
    public final class CollectionLibelles
    {

	public final String typarm;
	public final String typarmure;
	public final String ptsarmure;
	public final String trait;
	public final String interArme;
	public final String liaison;
	public final String addition;
	public final String qualite;
	public final String equilibrage;
	public final String attaque;
	public final String parade;

	CollectionLibelles(JsonObject p_libelles)
	{
	    if (p_libelles != null)
	    {
		typarm = p_libelles.getString("typarm");
		typarmure = p_libelles.getString("typarmure");
		ptsarmure = p_libelles.getString("ptsarmure");
		trait = p_libelles.getString("trait");
		interArme = p_libelles.getString("interArme");
		liaison = p_libelles.getString("liaison_standard");
		addition = p_libelles.getString("liaison_addition");
		qualite = p_libelles.getString("qualite");
		equilibrage = p_libelles.getString("equilibrage");
		attaque = p_libelles.getString("attaque");
		parade = p_libelles.getString("parade");

	    }
	    else
	    {
		typarm = "typarm";
		typarmure = "typarmure";
		ptsarmure = "ptsarmure";
		trait = "trait";
		interArme = "interArme";
		liaison = "de";
		addition = "et";
		qualite = "qualite";
		equilibrage = "equilibrage";
		attaque = "attaque";
		parade = "parade";
	    }
	}
    }
}
