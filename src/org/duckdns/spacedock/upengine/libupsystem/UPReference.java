package org.duckdns.spacedock.upengine.libupsystem;

import java.io.InputStream;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Classe permetant l'accès aux éléments de référence du UP!System Pour des
 * raisons de simplicité il a été décidé d'utiliser plusieurs tableaux plutôt
 * que des tableaux de tableaux (notamment pour les armures) il pourrait être
 * remédié à cela
 *
 * @author iconoctopus
 */
public final class UPReference
{
    //TODO : tableau de tableaux en JSON pour les armures plutôt que le système simpliste actuel utilisant plusieurs variables (une par ligne) faire ça avant de s'occuper d'implémenter la classe armure
    //TODO : voir si des libellés pourraient être intégré dans des tableaux sous la forme d'objets, Les armes fonctionnent déjà sur ce modèle contrairement aux listes séparées libellés/effets actuelles des autres éléments (on pourrait notamment fusionner les tables "domaine" et "comps_domaine" ensemble puis avec les libellés des comps
    //TODO : ajouter la gastion des libellés pour l'équipement (rien pour les armures actuellement, seuls les libellés des armes sont intégrés côté armement)

    /**
     * instance unique de cet pbjet
     */
    private static UPReference m_instance;

    /**
     * table des réductions de dégâts, indexée par le rang d'armure (issu du
     * tableau des rangs)
     */
    private final JsonArray m_tableArmureRedDegats;
    /**
     * table des bonus au ND, indexée par le rang d'armure (issu du tableau des
     * rangs)
     */
    private final JsonArray m_tableArmureBonusND;
    /**
     * table des rangs d'armure, à chaque rang (indice) correspond un nombre de
     * points d'armure à atteindre
     */
    private final JsonArray m_tableArmureRangs;

    /**
     * table des effets des armes par type (indice) pour les armures de type 0
     * (anciennes)
     */
    private final JsonArray m_tableArmureTyp0;
    /**
     * table des effets des armes par type (indice) pour les armures de type 1
     * (modernes)
     */
    private final JsonArray m_tableArmureTyp1;
    /**
     * table des effets des armes par type (indice) pour les armures de type 2
     * (blindage)
     */
    private final JsonArray m_tableArmureTyp2;
    /**
     * table des effets des armes par type (indice) pour les armures de type 3
     * (énergétiques)
     */
    private final JsonArray m_tableArmureTyp3;

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
     * table des autres libellés
     */
    public final CollectionLibelles libelles;
    /**
     * préfixe des libellés des compétences d'attaque
     */
    private final String m_lblCompAttaque;
    /**
     * préfixe des libellés des compétences de parade
     */
    private final String m_lblCompParade;
    /**
     * préfixe des libellés des compétences d'arts et métiers
     */
    private final String m_lblCompMetier;
    /**
     * tableau contenant tous les domaines et leurs compétences non free form,
     */
    final ArrayList<Domaine> m_arbreDomaines;
    /**
     * tableau contenant toutes les armes existant en jeu, accessible uniquement
     * en lecture
     */
    final ArrayList<Arme> m_tableArmes;

    /**
     * pseudo constructeur statique renvoyant l'instance unique et la
     * construisant si absente
     *
     * @return
     */
    static UPReference getInstance()
    {
	if(m_instance == null)
	{
	    m_instance = new UPReference();
	}
	return (m_instance);
    }

    /**
     * véritable constructeur privé effectuant tous les accès fichiers à
     * l'instanciation afin de limiter les temps de latence à une grosse fois
     */
    private UPReference()
    {
	JsonObject object;

	//chargement des règles d'armure
	object = loadJsonFile("JSON/equipement/caracs_armures.json");
	m_tableArmureBonusND = object.getJsonArray("bonusND");
	m_tableArmureRedDegats = object.getJsonArray("red_degats");
	m_tableArmureRangs = object.getJsonArray("rangs");
	m_tableArmureTyp0 = object.getJsonArray("ajustement0");
	m_tableArmureTyp1 = object.getJsonArray("ajustement1");
	m_tableArmureTyp2 = object.getJsonArray("ajustement2");
	m_tableArmureTyp3 = object.getJsonArray("ajustement3");

	//chargement des règles de calcul de l'initiative
	object = loadJsonFile("JSON/tables_systeme/tab_init.json");
	m_tableInitCoord = object.getJsonArray("coordination");
	m_tableInitMental = object.getJsonArray("mental");

	//chargement des libellés des caractéristiques particulières
	object = loadJsonFile("JSON/tables_systeme/tab_caracs.json");
	m_tableTraits = object.getJsonArray("traits");
	m_lblCompAttaque = object.getString("lbl_attaque");
	m_lblCompParade = object.getString("lbl_parade");
	m_lblCompMetier = object.getString("lbl_metier");

	//chargement de l'arbre des domaines et competences
	JsonArray arbreBrut = object.getJsonArray("arbre_domaines");

	m_arbreDomaines = new ArrayList<>();

	for(int i = 0; i < arbreBrut.size(); ++i)
	{
	    JsonObject row = arbreBrut.getJsonObject(i);
	    String nomDomaineCourant = row.getString("lbl");
	    JsonArray competencesBrutes = row.getJsonArray("comps");
	    ArrayList<Competence> newCompetences = new ArrayList<>();

	    for(int j = 0; j < competencesBrutes.size(); ++j)
	    {
		String nomCompCourante = competencesBrutes.getString(j);
		Competence addedComp = new Competence(0, nomCompCourante);
		newCompetences.add(addedComp);
	    }

	    Domaine newDomaine = new Domaine(nomDomaineCourant, 0, newCompetences);

	    m_arbreDomaines.add(newDomaine);
	}

	//chargement des autres libellés
	object = loadJsonFile("JSON/tables_systeme/tab_libelles.json");
	libelles = new CollectionLibelles(object);

	//chargement de la liste des armes
	object = loadJsonFile("JSON/equipement/caracs_armes.json");
	JsonArray armesBrutes = object.getJsonArray("liste_armes");
	m_tableArmes = new ArrayList<Arme>();
	for(int i = 0; i < armesBrutes.size(); i++)
	{
	    JsonObject armeCourante = armesBrutes.getJsonObject(i);
	    int lanceCourant = armeCourante.getInt("lance");
	    int gardeCourant = armeCourante.getInt("garde");
	    String nomCourant = armeCourante.getString("nom");
	    int bonusInitCourant = armeCourante.getInt("bonus_init");
	    int malusAttaqueCourant = armeCourante.getInt("malus_attaque");
	    int phyMinCourant = armeCourante.getInt("physique_minimal");
	    int categorieCourante = armeCourante.getInt("categorie");
	    int typeCourant = armeCourante.getInt("type");

	    Arme newArme = new Arme(lanceCourant, gardeCourant, bonusInitCourant, malusAttaqueCourant, phyMinCourant, categorieCourante, typeCourant, nomCourant);
	    m_tableArmes.add(newArme);
	}
    }

    /**
     *
     * @param p_filePath chemin de package vers le fichier JSON
     * @return un objet JSON lu dans le fichier
     */
    private JsonObject loadJsonFile(String p_filePath)
    {
	InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(p_filePath);//on utilise le classloader du thread et pas de la classe pour plus de sûreté
	JsonReader reader = Json.createReader(in);
	return (reader.readObject());
    }

    /**
     *
     * @param p_coordination la valeur de coordination
     * @return le modificateur de coordination à l'initiative
     */
    int getInitModCoord(int p_coordination)
    {
	int res = 0;
	if(p_coordination > 0 && p_coordination < 8)
	{
	    //le tableau est indexé à partir de 0, pas la coordination
	    return m_tableInitCoord.getInt(p_coordination - 1);
	}
	else
	{
	    if(p_coordination < 0)
	    {
		ErrorHandler.paramAberrant(libelles.trait + ":" + p_coordination);
	    }
	}
	return res;
    }

    /**
     *
     * @param p_mental la valeur de mental
     * @return le modificateur à l'initiative issu du mental
     */
    int getInitModMental(int p_mental)
    {
	int res = 0;

	if(p_mental > 0 && p_mental < 8)
	{
	    //le tableau est indexé à partir de 0, pas le mental
	    return m_tableInitMental.getInt(p_mental - 1);
	}
	else
	{
	    if(p_mental < 0)
	    {
		ErrorHandler.paramAberrant(libelles.trait + ":" + p_mental);
	    }
	}
	return res;
    }

    /**
     *
     * @param p_points les points d'armure
     * @return le rang d'armure
     */
    private int getRang(int p_points)
    {
	int res = 0;
	if(p_points >= 0)
	{
	    int i = -1;//on commence avec i en dehors du tableau (0 points d'armure, pas de bonus) et l'on teste si on peut l'augmenter, quand on ne peut plus l'augmenter on le renvoie.
	    while(i <= 4 && p_points >= m_tableArmureRangs.getInt(i + 1))
	    {
		++i;
	    }
	    res = i;
	}
	else
	{
	    ErrorHandler.paramAberrant(libelles.ptsarmure + ":" + p_points);
	}
	return (res);
    }

    /**
     *
     * @param p_points les points d'armure
     * @return la réduction de dégâts offerte par l'armure
     */
    int getArmureRedDegats(int p_points)
    {
	int resultat = 0;
	if(p_points > 0)
	{
	    int rang = getRang(p_points);
	    if(rang >= 0)//impossible si l'on est arrivé jusque là mais on ne sait jamais
	    {
		resultat = m_tableArmureRedDegats.getInt(rang);
	    }
	}
	else
	{
	    if(p_points < 0)
	    {
		ErrorHandler.paramAberrant(libelles.ptsarmure + ":" + p_points);
	    }
	}
	return resultat;
    }

    /**
     *
     * @param p_points les points d'armure
     * @return le bonus au ND offert par l'armure
     */
    int getArmureBonusND(int p_points)
    {
	int resultat = 0;
	if(p_points > 0)
	{
	    int rang = getRang(p_points);
	    if(rang >= 0)//impossible si l'on est arrivé jusque là mais on ne sait jamais
	    {
		resultat = m_tableArmureBonusND.getInt(rang);
	    }
	}
	else
	{
	    if(p_points < 0)
	    {
		ErrorHandler.paramAberrant(libelles.ptsarmure + ":" + p_points);
	    }
	}
	return resultat;
    }

    /**
     *
     * @param p_typeArme type d'arme considéré
     * @param p_typeArmure type d'armure considéré
     * @return les points d'armure à effectivement utiliser face à un type
     * d'arme donné en fonction du type d'armure
     */
    double getCoeffArmeArmure(int p_typeArme, int p_typeArmure)
    {
	double resultat = 0;
	if(p_typeArme >= 0 && p_typeArmure >= 0)
	{
	    switch(p_typeArmure)
	    {
		case 1: resultat = m_tableArmureTyp1.getJsonNumber(p_typeArme).doubleValue();
		    break;
		case 2: resultat = m_tableArmureTyp2.getJsonNumber(p_typeArme).doubleValue();
		    break;
		case 3: resultat = m_tableArmureTyp3.getJsonNumber(p_typeArme).doubleValue();
		    break;
		default: resultat = m_tableArmureTyp0.getJsonNumber(p_typeArme).doubleValue();
		    break;//par défaut on se place dans les armures anciennes
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant(libelles.typarm + ":" + p_typeArme + " " + libelles.typarmure + ":" + p_typeArmure);
	}
	return resultat;
    }

    /**
     *
     * @param p_indice l'indice du trait tel que défini dan le fichier JSON
     * @return le libelle du trait indicé
     */
    public String getLibelleTrait(int p_indice)
    {
	String res = "";
	if(p_indice >= 0 && p_indice <= 4)
	{
	    res = m_tableTraits.getString(p_indice);
	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    /**
     *
     * @param p_indice l'indice de la comp tel que définie dans le fichier JSON
     * @return le libelle de la comp identifiée
     */
    public String getLibelleCompFreeFrom(IdCompsFreeFrom p_identifiant)
    {
	String res = "";

	switch(p_identifiant)
	{
	    case attaque: res = m_lblCompAttaque;
		break;
	    case metier: res = m_lblCompMetier;
		break;
	    case parade: res = m_lblCompParade;
		break;
	    default: ErrorHandler.paramAberrant("indice:" + p_identifiant);
	}

	return res;
    }

    /**
     * Classe encapsulant les libellés autres que ceux utilisés dans les
     * caractéristiques
     */
    public final class CollectionLibelles
    {

	public final String typarm;
	public final String typarmure;
	public final String ptsarmure;
	public final String trait;

	CollectionLibelles(JsonObject p_libelles)
	{
	    if(p_libelles != null)
	    {
		typarm = p_libelles.getString("typarm");
		typarmure = p_libelles.getString("typarmure");
		ptsarmure = p_libelles.getString("ptsarmure");
		trait = p_libelles.getString("trait");
	    }
	    else
	    {
		typarm = "typarm";
		typarmure = "typarmure";
		ptsarmure = "ptsarmure";
		trait = "trait";
	    }
	}
    }

    public enum IdCompsFreeFrom
    {
	attaque, parade, metier
    };
}
