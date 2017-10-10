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
import org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.COORDINATION;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.MENTAL;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PHYSIQUE;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.PRESENCE;
import static org.duckdns.spacedock.upengine.libupsystem.GroupeTraits.Trait.VOLONTE;

/**
 * Classe permetant l'accès aux éléments de référence du UP!System
 *
 * @author ykonoclast
 */
public final class UPReferenceSysteme
{

    /**
     * table des libellés divers
     */
    private final CollectionLibelles m_collectionLibelles;
    /**
     * tableau contenant tous les domaines et leurs compétences non free form,
     */
    private final JsonArray m_arbreDomaines;
    /**
     * la liste des valeurs de ND
     */
    public final EnumMap<ND, Integer> m_listValeurND;
    /**
     * la liste des libellés des ND
     */
    public final EnumMap<ND, String> m_listLblND;
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
     * liste des libellés de traits
     */
    public final EnumMap<Trait, String> m_listLblTrait;
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
	object = loadJsonFile("libupsystem", "tables_systeme/tab_sys.json");
	m_tableInitCoord = object.getJsonObject("init").getJsonArray("coordination");
	m_tableInitMental = object.getJsonObject("init").getJsonArray("mental");
	JsonArray nDarray = object.getJsonArray("ND");
	JsonArray lblNDarray = object.getJsonArray("lbl_ND");
	m_listLblND = new EnumMap<>(ND.class);
	m_listValeurND = new EnumMap<>(ND.class);
	int i = 0;
	for (ND n : ND.values())
	{
	    m_listValeurND.put(n, nDarray.getInt(i));
	    m_listLblND.put(n, lblNDarray.getString(i));
	    ++i;
	}

	//chargement des libellés des caractéristiques particulières
	object = loadJsonFile("libupsystem", "tables_systeme/tab_caracs.json");
	m_lblCompAttaque = object.getString("lbl_attaque");
	m_lblCompParade = object.getString("lbl_parade");
	m_lblCompMetier = object.getString("lbl_metier");

	//chargement des libellés de traits
	JsonArray tabTraits = object.getJsonArray("traits");
	m_listLblTrait = new EnumMap<>(Trait.class);
	m_listLblTrait.put(PHYSIQUE, tabTraits.getString(0));
	m_listLblTrait.put(COORDINATION, tabTraits.getString(1));
	m_listLblTrait.put(MENTAL, tabTraits.getString(2));
	m_listLblTrait.put(VOLONTE, tabTraits.getString(3));
	m_listLblTrait.put(PRESENCE, tabTraits.getString(4));

	//chargement de l'arbre des domaines et competences
	m_arbreDomaines = object.getJsonArray("arbre_domaines");

	//chargement des libellés divers
	object = loadJsonFile("libupsystem", "tables_systeme/tab_libelles.json");
	m_collectionLibelles = new CollectionLibelles(object);
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
     * @return la collection des libellés
     */
    public CollectionLibelles getCollectionLibelles()
    {
	return m_collectionLibelles;
    }

    /**
     *
     * @return la valeur d'un ND
     */
    public int getValeurND(ND p_nD)
    {
	return m_listValeurND.get(p_nD);
    }

    /**
     *
     * @return le libellé d'un ND
     */
    public String getLblND(ND p_nD)
    {
	return m_listLblND.get(p_nD);
    }

    /**
     *
     * @param p_coordination la valeur de coordination
     * @return le modificateur de coordination à l'initiative
     */
    public int getInitModCoord(int p_coordination)
    {
	return m_tableInitCoord.getInt(p_coordination);
    }

    /**
     *
     * @param p_mental la valeur de mental
     * @return le modificateur à l'initiative issu du mental
     */
    public int getInitModMental(int p_mental)
    {
	return m_tableInitMental.getInt(p_mental);
    }

    /**
     *
     * @param p_idTrait l'identifiant du trait
     * @return le libelle du trait indicé
     */
    public String getLibelleTrait(Trait p_idTrait)
    {
	return m_listLblTrait.get(p_idTrait);
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
	    {//on préfixe les compétences d'attaque et de parade puis on les suffixe de la catégorie
		res.add(m_collectionLibelles.attaque + " " + listCaC.get(i));
		res.add(m_collectionLibelles.parade + " " + listCaC.get(i));
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
     * caractéristiques et l'équipement (surtout utilisé pour l'entrée sortie
     * des caracs de système)
     */
    public final class CollectionLibelles
    {

	public final String typarm;
	public final String typarmure;
	public final String ptsarmure;
	public final String trait;
	public final String interArmure;
	public final String liaison;
	public final String addition;
	public final String qualite;
	public final String equilibrage;
	public final String attaque;
	public final String parade;
	public final String facture;
	public final String mainsnues;

	CollectionLibelles(JsonObject p_libelles)
	{
	    if (p_libelles != null)
	    {
		typarm = p_libelles.getString("typarm");
		typarmure = p_libelles.getString("typarmure");
		ptsarmure = p_libelles.getString("ptsarmure");
		trait = p_libelles.getString("trait");
		interArmure = p_libelles.getString("interArmure");
		liaison = p_libelles.getString("liaison_standard");
		addition = p_libelles.getString("liaison_addition");
		qualite = p_libelles.getString("qualite");
		equilibrage = p_libelles.getString("equilibrage");
		attaque = p_libelles.getString("attaque");
		parade = p_libelles.getString("parade");
		facture = p_libelles.getString("facture");
		mainsnues = p_libelles.getString("mains_nues");
	    }
	    else
	    {
		typarm = "typarm";
		typarmure = "typarmure";
		ptsarmure = "ptsarmure";
		trait = "trait";
		interArmure = "interArmure";
		liaison = "de";
		addition = "et";
		qualite = "qualite";
		equilibrage = "equilibrage";
		attaque = "attaque";
		parade = "parade";
		facture = "facture";
		mainsnues = "mains nues";
	    }
	}
    }

    /**
     * Enum contenant les niveaux de difficulté standards
     */
    public enum ND
    {
	facile, moyen, difficile, tres_difficile
    };
}
