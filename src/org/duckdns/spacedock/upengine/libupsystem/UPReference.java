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
//TODO : remplacer les for classiqus avec i++ qui itèrent sur les tableaux JSON par des itérateurs

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
    final JsonArray m_arbreDomaines;
    /**
     * tableau contenant toutes les armes existant en jeu
     */
    final JsonArray m_tableArmes;

    private final JsonArray m_tableAjustementArmure;

    private final JsonArray m_listLblTypArm;

    private final JsonArray m_listLblCatArm;

    private final JsonArray m_listCatArmCaC;

    private final JsonArray m_listCatArmDist;

    private final JsonArray m_tabPiecesArmures;

    private final JsonArray m_listLblMatArmures;

    private final JsonArray m_listLblTypArmures;

    private final JsonArray m_tabBoucliers;

    private final JsonArray m_listLblMatBoucliers;

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
	m_tableAjustementArmure = object.getJsonArray("ajustements");
	m_tabPiecesArmures = object.getJsonArray("pieces");
	m_listLblMatArmures = object.getJsonArray("materiaux_armures");
	m_listLblTypArmures = object.getJsonArray("types_armures");
	m_tabBoucliers = object.getJsonArray("boucliers");
	m_listLblMatBoucliers = object.getJsonArray("materiaux_boucliers");

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
	m_arbreDomaines = object.getJsonArray("arbre_domaines");

	//chargement des libellés divers
	object = loadJsonFile("JSON/tables_systeme/tab_libelles.json");
	libelles = new CollectionLibelles(object);

	//chargement ded règles des armes
	object = loadJsonFile("JSON/equipement/caracs_armes.json");
	m_tableArmes = object.getJsonArray("armes");
	m_listLblTypArm = object.getJsonArray("types_armes");
	m_listLblCatArm = object.getJsonArray("cat_armes");
	m_listCatArmCaC = object.getJsonArray("cat_armes_cac");
	m_listCatArmDist = object.getJsonArray("cat_armes_dist");
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
	    JsonArray tabPourType = m_tableAjustementArmure.getJsonArray(p_typeArmure);
	    resultat = tabPourType.getJsonNumber(p_typeArme).doubleValue();
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

    /*














    DEBUT DES AJOUTS












     */
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
	    default: ErrorHandler.paramAberrant("indice :" + p_identifiant);
	}

	return res;
    }

    String getLblArme(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getNbLancesArme(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getNbGardesArme(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getNBonusInitArme(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getMalusAttaqueArme(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getPhysMinArme(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getCategorieArme(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getTypeArme(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    String getLblTypeArme(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    String getLblCatArme(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    ArrayList<Integer> getCatArmCac()
    {
	ArrayList<Integer> res = new ArrayList<>();

	return res;
    }

    ArrayList<Integer> getCatArmDist()
    {
	ArrayList<Integer> res = new ArrayList<>();

	return res;
    }

    int getNbPointsBouclier(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    String getLblMatBouclier(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    ArrayList<Competence> getListComp(int p_indice)
    {
	ArrayList<Competence> res = new ArrayList<>();
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    String getLblDomaine(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    String getLblComp(int p_indiceDomaine, int p_indiceComp)
    {
	String res = "";
	if(p_indiceDomaine >= 0 && p_indiceComp >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice1:" + p_indiceDomaine + " indice2:" + p_indiceComp);
	}
	return res;
    }

    int getPtsArmure(int p_idPiece, int p_type, int p_materiau)
    {
	int res = 0;
	if(p_idPiece >= 0 && p_materiau >= 0 && p_type >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice1:" + p_idPiece + " indice2:" + p_type + " indice3:" + p_materiau);
	}
	return res;
    }

    String getLblMateriauArmure(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    String getLblTypeArmure(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    String getLblPiece(int p_indice)
    {
	String res = "";
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getMalusEsquive(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getMalusParade(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    int getNbMaxPieces(int p_indice)
    {
	int res = 0;
	if(p_indice >= 0)
	{

	}
	else
	{
	    ErrorHandler.paramAberrant("indice:" + p_indice);
	}
	return res;
    }

    /*











    FIN DES AJOUTS













     */
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

	CollectionLibelles(JsonObject p_libelles)
	{
	    if(p_libelles != null)
	    {
		typarm = p_libelles.getString("typarm");
		typarmure = p_libelles.getString("typarmure");
		ptsarmure = p_libelles.getString("ptsarmure");
		trait = p_libelles.getString("trait");
		interArme = p_libelles.getString("interArme");
	    }
	    else
	    {
		typarm = "typarm";
		typarmure = "typarmure";
		ptsarmure = "ptsarmure";
		trait = "trait";
		interArme = "interArme";
	    }
	}
    }

    public enum IdCompsFreeFrom
    {
	attaque, parade, metier
    };
}
