package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import java.util.EnumMap;
import javax.json.JsonArray;
import javax.json.JsonObject;
import static org.duckdns.spacedock.commonutils.JSONHandler.loadJsonFile;

/**
 * Classe permetant l'accès aux éléments de référence du UP!System
 *
 * @author iconoctopus
 */
public final class UPReference
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
     * tableau contenant toutes les armes existant en jeu
     */
    private final JsonArray m_tabArmes;
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
     * liste des libellés des catégories d'armes de corps à corps
     */
    private final JsonArray m_listLblCatArmCaC;
    /**
     * liste des libellés des catégories d'armes à distance
     */
    private final JsonArray m_listLblCatArmDist;
    /**
     * liste des libellés des localisations
     */
    private final JsonArray m_listLblLoca;
    /**
     * list des libellés des matériaux d'armures
     */
    private final JsonArray m_listLblMatArmures;
    /**
     * liste des libellés des matériaux des boucliers
     */
    private final JsonArray m_listLblMatBoucliers;
    /**
     * liste des libellés des modes d'attaque des armes
     */
    private final JsonArray m_listLblModArm;
    /**
     * liste des libellés des types d'armes
     */
    private final JsonArray m_listLblTypArm;
    /**
     * liste de libellés des types d'armures
     */
    private final JsonArray m_listLblTypArmures;
    /**
     * table de tous les boucliers
     */
    private final JsonArray m_tabBoucliers;
    /**
     * tableau des pièces d'armure
     */
    private final JsonArray m_tabPiecesArmures;
    /**
     * table des ajustemnts à effectuer sur les points d'armure pour adapter un
     * type d'armure à un type d'arme. C'est un tableau de tableaux (un pour
     * chaque type d'armure), chaque tableau interne est indicé par les types
     * d'armes
     */
    private final JsonArray m_tableAjustementArmure;

    /**
     * table des bonus au ND, indexée par le rang d'armure (issu du tableau des
     * rangs)
     */
    private final JsonArray m_tableArmureBonusND;
    /**
     * table des rangs d'armure, à chaque rang (indice) correspond un seuil de
     * points d'armure à atteindre
     */
    private final JsonArray m_tableArmureRangs;
    /**
     * table des réductions de dégâts, indexée par le rang d'armure (issu du
     * tableau des rangs)
     */
    private final JsonArray m_tableArmureRedDegats;
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
    private static UPReference m_instance;

    /**
     * véritable constructeur privé effectuant tous les accès fichiers à
     * l'instanciation afin de limiter les temps de latence à une grosse fois
     */
    private UPReference()
    {
	JsonObject object;

	//chargement des règles d'armure
	object = loadJsonFile("libupsystem", "equipement/caracs_armures.json");
	m_tableArmureBonusND = object.getJsonArray("bonusND");
	m_tableArmureRedDegats = object.getJsonArray("red_degats");
	m_tableArmureRangs = object.getJsonArray("rangs");
	m_tableAjustementArmure = object.getJsonArray("ajustements");
	m_tabPiecesArmures = object.getJsonArray("pieces");
	m_listLblMatArmures = object.getJsonArray("materiaux_armures");
	m_listLblTypArmures = object.getJsonArray("types_armures");
	m_tabBoucliers = object.getJsonArray("boucliers");
	m_listLblMatBoucliers = object.getJsonArray("materiaux_boucliers");
	m_listLblLoca = object.getJsonArray("localisations");

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

	//chargement ded règles des armes
	object = loadJsonFile("libupsystem", "equipement/caracs_armes.json");
	m_tabArmes = object.getJsonArray("armes");
	m_listLblTypArm = object.getJsonArray("types_armes");
	m_listLblCatArmCaC = object.getJsonArray("cat_armes_cac");
	m_listLblCatArmDist = object.getJsonArray("cat_armes_dist");
	m_listLblModArm = object.getJsonArray("mod_armes");
    }

    /**
     *
     * @return l'instance unique de la référence, la construit si elle n'existe
     * pas
     */
    public static UPReference getInstance()
    {
	if (m_instance == null)
	{
	    m_instance = new UPReference();
	}
	return (m_instance);
    }

    /**
     *
     * @param p_points les points d'armure
     * @param p_typeArme
     * @param p_typeArmure
     * @return le bonus au ND offert par l'armure sur ce type d'arme
     */
    public int getArmureBonusND(int p_points, int p_typeArme, int p_typeArmure)
    {
	int pointsEffectifs = getPtsArmureEffectifs(p_points, p_typeArme, p_typeArmure);

	int resultat = 0;

	int rang = getRang(pointsEffectifs);
	if (rang >= 0)//impossible si l'on est arrivé jusque là mais on ne sait jamais
	{
	    resultat = m_tableArmureBonusND.getInt(rang);
	}
	return resultat;
    }

    /**
     *
     * @param p_points
     * @param p_typeArme
     * @param p_typeArmure
     * @return la réduction aux dégâts de l'armure par rapport au type d'arme
     */
    public int getArmureRedDegats(int p_points, int p_typeArme, int p_typeArmure)
    {
	int pointsEffectifs = getPtsArmureEffectifs(p_points, p_typeArme, p_typeArmure);

	int resultat = 0;

	int rang = getRang(pointsEffectifs);
	if (rang >= 0)//impossible si l'on est arrivé jusque là mais on ne sait jamais
	{
	    resultat = m_tableArmureRedDegats.getInt(rang);
	}
	return resultat;
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
     * @param p_indice
     * @return le libellé d'une arme
     */
    public String getLblArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getString("nom");
    }

    /**
     * renvoie le libellé d'une catégorie d'arme de corps à corps
     *
     * @param p_indice
     * @return
     */
    public String getLblCatArmeCaC(int p_indice)
    {
	return m_listLblCatArmCaC.getString(p_indice);
    }

    /**
     * renvoie le libellé d'une catégorie d'arme à distance
     *
     * @param p_indice
     * @return
     */
    public String getLblCatArmeDist(int p_indice)
    {
	return m_listLblCatArmDist.getString(p_indice);
    }

    /**
     *
     * @param p_indice
     * @return le libllé d'une localisation
     */
    public String getLblLoca(int p_indice)
    {
	return m_listLblLoca.getString(p_indice);
    }

    /**
     *
     * @param p_indice
     * @param p_isBouclier
     * @return le libellé d'un matériau d'armure
     */
    public String getLblMateriauArmure(int p_indice, boolean p_isBouclier)
    {
	String res;
	if (p_isBouclier)
	{
	    res = m_listLblMatBoucliers.getString(p_indice);
	}
	else
	{
	    res = m_listLblMatArmures.getString(p_indice);
	}
	return res;
    }

    /**
     *
     * @param p_indice
     * @return le libellé du mode d'attaque représenté par son index
     */
    public String getLblModArme(int p_indice)
    {
	return m_listLblModArm.getString(p_indice);
    }

    /**
     *
     * @param p_indice
     * @param p_isBouclier
     * @return le libellé d'une pièce d'armure
     */
    public String getLblPiece(int p_indice, boolean p_isBouclier)
    {
	String res;
	if (p_isBouclier)
	{
	    JsonObject objetIntermediaire = m_tabBoucliers.getJsonObject(p_indice);
	    res = objetIntermediaire.getString("lbl");
	}
	else
	{
	    JsonObject piece = m_tabPiecesArmures.getJsonObject(p_indice);
	    res = piece.getString("lbl");
	}
	return res;
    }

    /**
     *
     * @param p_indice
     * @return le libellé d'un type d'arme représenté par son index
     */
    public String getLblTypeArme(int p_indice)
    {
	return m_listLblTypArm.getString(p_indice);
    }

    /**
     *
     * @param p_indice
     * @return le libllé d'un type d'armure
     */
    public String getLblTypeArmure(int p_indice)
    {
	return m_listLblTypArmures.getString(p_indice);
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

	if (p_indice == 3)//domaine corps à corps : on renvoie la liste des catégories d'armes de corps à corps dédoublée en parade et attaque
	{
	    for (int i = 0; i < m_listLblCatArmCaC.size(); ++i)
	    {
		res.add(libelles.attaque + " " + m_listLblCatArmCaC.getString(i));
		res.add(libelles.parade + " " + m_listLblCatArmCaC.getString(i));
	    }
	}
	else
	{
	    if (p_indice == 4)//domaine combat à distance : on renvoie la liste des catégories d'armes à distance
	    {
		for (int i = 0; i < m_listLblCatArmDist.size(); ++i)
		{
		    res.add(m_listLblCatArmDist.getString(i));
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
     * la liste des noms de toutes les armes
     *
     * @return
     */
    public ArrayList<String> getListLblArme()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_tabArmes.size(); ++i)
	{
	    res.add(m_tabArmes.getJsonObject(i).getString("lbl"));
	}
	return res;
    }

    /**
     * la liste des noms de tous les boucliers
     *
     * @return
     */
    public ArrayList<String> getListLblBouclier()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_tabBoucliers.size(); ++i)
	{
	    res.add(m_tabBoucliers.getJsonObject(i).getString("lbl"));
	}
	return res;
    }

    /**
     * la liste des noms de tous les matériaux d'armure
     *
     * @return
     */
    public ArrayList<String> getListLblMateriauArmure()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_listLblMatArmures.size(); ++i)
	{
	    res.add(m_listLblMatArmures.getString(i));
	}
	return res;
    }

    /**
     * la liste des noms de tous les matériaux de boucliers
     *
     * @return
     */
    public ArrayList<String> getListLblMateriauBouclier()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_listLblMatBoucliers.size(); ++i)
	{
	    res.add(m_listLblMatBoucliers.getString(i));
	}
	return res;
    }

    /**
     * la liste des noms de toutes les pieces d'armures
     *
     * @return
     */
    public ArrayList<String> getListLblPieceArmure()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_tabPiecesArmures.size(); ++i)
	{
	    res.add(m_tabPiecesArmures.getJsonObject(i).getString("lbl"));
	}
	return res;
    }

    /**
     * la liste des noms de tous les types d'arme
     *
     * @return
     */
    public ArrayList<String> getListLblTypeArme()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_listLblTypArm.size(); ++i)
	{
	    res.add(m_listLblTypArm.getString(i));
	}
	return res;
    }

    /**
     * la liste des noms de tous les types d'armure
     *
     * @return
     */
    public ArrayList<String> getListLblTypeArmure()
    {
	ArrayList<String> res = new ArrayList<>();
	for (int i = 0; i < m_listLblTypArmures.size(); ++i)
	{
	    res.add(m_listLblTypArmures.getString(i));
	}
	return res;
    }

    /**
     *
     * @param p_indice
     * @param p_isBouclier
     * @return la localisation d'une pièce
     */
    public int getLocalisation(int p_indice, boolean p_isBouclier)
    {
	JsonArray tab;

	if (!p_isBouclier)
	{
	    tab = m_tabPiecesArmures;
	}
	else
	{
	    tab = m_tabBoucliers;
	}
	JsonObject piece = tab.getJsonObject(p_indice);
	return piece.getInt("loca");
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
     * renvoie le malus à l'attaque à courte portée de l'arme
     *
     * @param p_indice
     * @return
     */
    public int getMalusCourtArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("malus_court");
    }

    /**
     *
     * @param p_indicePiece
     * @param p_materiau
     * @param p_isBouclier
     * @return le malus d'esquive d'une pièce d'un matériau donné
     */
    public int getMalusEsquive(int p_indicePiece, int p_materiau, boolean p_isBouclier)
    {
	int res = 0;
	if (!p_isBouclier)
	{
	    JsonObject piece = m_tabPiecesArmures.getJsonObject(p_indicePiece);
	    JsonArray tabMalus = piece.getJsonArray("malus_esquive");
	    res = tabMalus.getInt(p_materiau);
	}
	return res;
    }

    /**
     * renvoie le malus à l'attaque à longue portée de l'arme
     *
     * @param p_indice
     * @return
     */
    public int getMalusLongArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("malus_long");
    }

    /**
     *
     * @param p_indicePiece
     * @param p_materiau
     * @param p_isBouclier
     * @return le malus de parade d'une pièce d'un matériau donné
     */
    public int getMalusParade(int p_indicePiece, int p_materiau, boolean p_isBouclier)
    {
	int res = 0;
	if (!p_isBouclier)
	{
	    JsonObject piece = m_tabPiecesArmures.getJsonObject(p_indicePiece);
	    JsonArray tabMalus = piece.getJsonArray("malus_parade");
	    res = tabMalus.getInt(p_materiau);
	}
	return res;
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
     * @return le nombre de dés gardés d'une arme
     */
    public int getNbGardesArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("garde");

    }

    /**
     *
     * @param p_indice
     * @return le nb de dés lancés par une arme
     */
    public int getNbLancesArme(int p_indice)
    {
	JsonObject arme = m_tabArmes.getJsonObject(p_indice);
	return arme.getInt("lance");
    }

    /**
     *
     * @param p_indice
     * @return le nombre de mains utilisées par une arme
     */
    public int getNbMainsArme(int p_indice)
    {
	return m_tabArmes.getJsonObject(p_indice).getInt("mains");
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
     * @param p_idPiece
     * @param p_materiau
     * @param p_isBouclier
     * @return le nombre de points d'armure d'une pièce d'un matériau spécifiés
     * par leurs indices
     */
    public int getPtsArmure(int p_idPiece, int p_materiau, boolean p_isBouclier)
    {
	int res;
	if (p_isBouclier)
	{
	    JsonObject objetIntermediaire = m_tabBoucliers.getJsonObject(p_idPiece);
	    JsonArray tabIntermediaire = objetIntermediaire.getJsonArray("points");
	    res = tabIntermediaire.getInt(p_materiau);
	}
	else
	{
	    JsonObject piece = m_tabPiecesArmures.getJsonObject(p_idPiece);
	    JsonArray tabPoints = piece.getJsonArray("points");
	    res = tabPoints.getInt(p_materiau);
	}
	return res;
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

    /**
     * @param p_nbPts les points d'armure à modifier
     * @param p_typeArme type d'arme considéré
     * @param p_typeArmure type d'armure considéré
     * @return les points d'armure à effectivement utiliser face à un type
     * d'arme donné en fonction du type d'armure
     */
    private int getPtsArmureEffectifs(int p_nbPts, int p_typeArme, int p_typeArmure)
    {
	JsonArray tabPourType = m_tableAjustementArmure.getJsonArray(p_typeArmure);
	double coeff = tabPourType.getJsonNumber(p_typeArme).doubleValue();

	double preResult = (((double) p_nbPts) * coeff);
	long IntResult = Math.round(preResult);
	return (int) IntResult;
    }

    /**
     *
     * @param p_points les points d'armure
     * @return le rang d'armure
     */
    private int getRang(int p_points)
    {
	int i = -1;//on commence avec i en dehors du tableau (0 points d'armure, pas de bonus) et l'on teste si on peut l'augmenter, quand on ne peut plus l'augmenter on le renvoie.
	while (i <= 4 && p_points >= m_tableArmureRangs.getInt(i + 1))
	{
	    ++i;
	}
	return (i);
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

	public final EnumMap libQualite;
	public final EnumMap libEquilibrage;

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

		JsonArray tabQualite = p_libelles.getJsonArray("lib_qualite");
		libQualite = new EnumMap(Arme.QualiteArme.class);
		libQualite.put(Arme.QualiteArme.inferieure, tabQualite.getString(0));
		libQualite.put(Arme.QualiteArme.moyenne, tabQualite.getString(1));
		libQualite.put(Arme.QualiteArme.superieure, tabQualite.getString(2));
		libQualite.put(Arme.QualiteArme.maitre, tabQualite.getString(3));

		JsonArray tabEquilibrage = p_libelles.getJsonArray("lib_equilibrage");
		libEquilibrage = new EnumMap(Arme.EquilibrageArme.class);
		libEquilibrage.put(Arme.EquilibrageArme.mauvais, tabEquilibrage.getString(0));
		libEquilibrage.put(Arme.EquilibrageArme.normal, tabEquilibrage.getString(1));
		libEquilibrage.put(Arme.EquilibrageArme.bon, tabEquilibrage.getString(2));
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
		libQualite = new EnumMap(Arme.QualiteArme.class);
		libQualite.put(Arme.QualiteArme.inferieure, "inf");
		libQualite.put(Arme.QualiteArme.moyenne, "moy");
		libQualite.put(Arme.QualiteArme.superieure, "sup");
		libQualite.put(Arme.QualiteArme.maitre, "maitre");
		libEquilibrage = new EnumMap(Arme.EquilibrageArme.class);
		libEquilibrage.put(Arme.EquilibrageArme.mauvais, "mauvais");
		libEquilibrage.put(Arme.EquilibrageArme.normal, "normal");
		libEquilibrage.put(Arme.EquilibrageArme.bon, "bon");
	    }
	}
    }
}
