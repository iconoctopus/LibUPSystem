package org.duckdns.spacedock.libupsystem;

import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
//TODO blinder dans toutes les méthodes de toutes les classes.....

/**
 * Classe permetant l'accès aux éléments de référence du UP!System
 *
 * @author iconoctopus
 */
public final class UPReference
{

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
	object = loadJsonFile("JSON/equipement/armures/caracs_armures.json");
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

	//chargement des libellés des caractéristiques
	object = loadJsonFile("JSON/tables_systeme/tab_caracs.json");
	m_tableTraits = object.getJsonArray("traits");

	//chargement des autres libellés
	object = loadJsonFile("JSON/tables_systeme/tab_libelles.json");
	libelles = new CollectionLibelles(object);
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
	if(p_coordination > 0)
	{
	    //le tableau est indexé à partir de 0, pas la coordination
	    return m_tableInitCoord.getInt(p_coordination - 1);
	}
	else
	{
	    if(p_coordination < 0)
	    {

		ErrorHandler.paramAberrant(libelles.trait + p_coordination);

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

	if(p_mental > 0)
	{
	    //le tableau est indexé à partir de 0, pas le mental
	    return m_tableInitMental.getInt(p_mental - 1);
	}
	else
	{
	    if(p_mental < 0)
	    {

		ErrorHandler.paramAberrant(libelles.trait + p_mental);

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
	    int i = -1;//on commence avec i en dehors du tableau (0 points d'armure, pas de bonus) et l'on teste si on peut l'augmenter, quand on ne peut plus l'augmenter on le renvoie. A noter que cela blinde du même coup la fonction contre les chiffres négatifs sans erreur (ce qui est préférable ici)
	    while(p_points > m_tableArmureRangs.getInt(i + 1))
	    {
		++i;
	    }
	    res = i;
	}
	else
	{
	    ErrorHandler.paramAberrant(libelles.ptsarmure + p_points);
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
		ErrorHandler.paramAberrant(libelles.ptsarmure + p_points);
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
		ErrorHandler.paramAberrant(libelles.ptsarmure + p_points);
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
    double getPointsArmureEffectifs(int p_typeArme, int p_typeArmure)
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
	    ErrorHandler.paramAberrant(libelles.typarm + p_typeArme + libelles.typarmure + p_typeArmure);
	}

	return resultat;
    }

    /**
     *
     * @param p_indice l'indice du trait tel que défini dan le fichier JSON
     * @return le libelle du trait indicé tel
     */
    public String getLibelleTrait(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{
	    res = m_tableTraits.getString(p_indice);
	}
	else
	{
	    ErrorHandler.paramAberrant("indice=" + p_indice);
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
}
