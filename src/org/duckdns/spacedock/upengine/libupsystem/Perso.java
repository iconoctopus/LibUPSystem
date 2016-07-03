package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import java.util.Collections;
import org.duckdns.spacedock.upengine.libupsystem.Arme.Degats;
import org.duckdns.spacedock.upengine.libupsystem.RollUtils.RollResult;

//TODO ajouter la possibiité de faire un jet général : de compétence ou de trait et réorienter les jets déjà effectués vers ces nouvelles méthodes
public abstract class Perso
{

    /**
     * les actions du personnage dans ce tour sous la forme de la phase dans
     * laquelle l'action se déroule dans l'ordre des phases (donc tableau de la
     * taille de l'init) ,celles consommées sont fixées à 11
     */
    private ArrayList<Integer> m_actions;
    /**
     * l'indice de l'action courantes dans le tableau des actions
     */
    private int m_actionCourante;
    /**
     * le nom du personage
     */
    private String m_libellePerso;
    /**
     * non finale pour augment à l'xp et définition hors constructeur
     */
    private CoupleJauge m_jaugeSanteInit;
    /**
     * non finale pour augment à l'xp et définition hors constructeur
     */
    private CoupleJauge m_jaugeFatigueForceDAme;
    /**
     * rassemble armes et armures du personnage ainsi que quelques
     * fonctionalités utiles notamment les armes et armures courantes
     */
    private final Inventaire m_inventaire;
    /**
     * dans l'ordre : 0:Physique ; 1:Coordination ; 2:Mental ; 3:Volonté ;
     * 4:Sociabilité
     */
    private final int[] m_traits;
    /**
     * arbre des domaines/compétences du personnage
     */
    private final ArrayList<Domaine> m_listDomaines = new ArrayList<>();//TODO : remplacer cela par un objet spécialisé genre "ArbreDomaine" et lui faire embarquer la méthode initDomaines et d'autres utilitaires, notamment d'initialisation

    /**
     * constructeur produisant des PNJ générés par rang de menace (RM)
     *
     * @param p_RM
     */
    public Perso(int p_RM)//TODO créer un autre constructeur prenant en paramétre des caracs
    {
	if (p_RM < 1)
	{
	    ErrorHandler.paramAberrant("rang:" + p_RM);
	}
	//configuration des traits
	m_traits = new int[5];
	m_traits[0] = p_RM;//physique
	m_traits[1] = p_RM;//coordination
	m_traits[2] = p_RM - 1;//mental
	m_traits[3] = p_RM - 1;//volonté
	m_traits[4] = p_RM - 1;//sociabilité

	//configuration automatique des autres caractéristiques maintenant possible car les traits sont générés
	initPerso();

	//configuration des caractéristiques de combat (corps à corps uniquement pour l'instant) une fois que l'arbre des domaines est généré
	m_listDomaines.get(3).setRang(p_RM);//on initialise le domaine corps à corps avec le RM

	//on parcourt tout le domaine CàC et on met toutes les attaques et parades au RM
	for (Competence i : m_listDomaines.get(3).getCompetences())
	{
	    ((CompCac) i).setAttaque(p_RM);
	    ((CompCac) i).setParade(p_RM);
	}

	//on ajoute des rangs en esquive
	m_listDomaines.get(2).getCompetences().get(0).setRang(p_RM);

	//configuration d'un inventaire vide
	m_inventaire = new Inventaire();

	m_libellePerso = "PersoRM" + p_RM;
    }

    /**
     * initialise les caractéristiques hors traits du personnage
     */
    private void initPerso()
    {
	initDomaines();
	initJauges();
	genInit();
    }

    /**
     * initialise la liste des domaines
     */
    private void initDomaines()//TODO déplacer ce code dans une classe ArbreDomaine qui l'utiliserait dans son constructeur
    {//TODO : mettre en référence le nombre des domaines et le récupérer ici plutot que d'utiliser la valeur hardcodée ci-dessous

	for (int i = 0; i < 9; ++i)
	{

	    m_listDomaines.add(new Domaine(i, 1));

	}
    }

    /**
     * initialise les jauges du personnage avec le tableau de ses traits, doit
     * donc être appelé par le onstructeur après cette initialisation
     */
    private void initJauges()
    {
	int traitMin = m_traits[0];
	for (int i = 1; i < m_traits.length; ++i)
	{
	    if (m_traits[i] < traitMin)
	    {
		traitMin = m_traits[i];
	    }
	}
	m_jaugeFatigueForceDAme = new CoupleJauge(m_traits[0], m_traits[3], traitMin);
	m_jaugeSanteInit = new CoupleJauge(m_traits[0], m_traits[3], m_traits[2], m_traits[1]);
    }

    /**
     * génère l'initiative du personnage, devrait être appelée dans le
     * constructeur mais par la suite contrôlée de l'extérieur
     */
    public final void genInit()
    {//TODO mieux trier le tableau, couteux en l'état

	int initiative = m_jaugeSanteInit.getRemplissage_externe();
	m_actionCourante = 0;
	ArrayList<Integer> tabResult = new ArrayList<>();
	if (initiative > 0)
	{

	    for (int i = 0; i < initiative; i++)
	    {
		tabResult.add(RollUtils.lancer(1, 1, true));
	    }

	}
	Collections.sort(tabResult);
	m_actions = tabResult;
    }

    /**
     * Fait effectuer une attaque au personnage, la génération des dégâts est
     * séparée et déclenchée depuis l'extérieur afin que le contrôleur puisse
     * choisir d'utiliser les incréments pour autre chose que des dégâs. Me
     * personnage attaquera systématiquement avec l'arme courante, il faut donc
     * la configurer avant. Cette méthode vérifie que l'action est possible dans
     * la phase courante en fonction de l'init du perso, elle est donc conçue
     * pour le combat uniquement
     *
     * @param p_phaseActuelle
     * @param p_ND
     * @param p_distance ignoré si arme de corps à corps équipée ou attaque à
     * mains nues
     * @param p_mainsNues booléen pour forcer l'attaque sans arme
     * @return
     */
    public RollUtils.RollResult attaquerCac(int p_phaseActuelle, int p_ND, boolean p_mainsNues)
    {
	Arme arme = m_inventaire.getArmeCourante();
	int catArm = 0;
	if (arme != null && !p_mainsNues)//une arme est équipée et l'on veut s'en servir
	{

	    if (arme.getMode() == 0)
	    {
		catArm = arme.getCategorie();
	    }
	    else
	    {
		ErrorHandler.mauvaiseMethode("mauvais mode d'attaque");
	    }

	    return effectuerAttaque(p_phaseActuelle, p_ND, catArm, 3, 0);
	}









    public RollResult attaquerDist(int p_phaseActuelle, int p_ND, int p_distance)
    {
	Arme arme = m_inventaire.getArmeCourante();
	if ()
	{
	    if (p_distance >= 0)
	    {
		if (p_distance <= ((ArmeDist) arme).getPortee())//TODO mieux arrondir et tester cas pile la moitié
		{//portée courte
		    modDist = ((ArmeDist) arme).getMalusCourt();
		}
		else
		{//portée longue
		    modDist = ((ArmeDist) arme).getMalusLong();
		}
	    }
	    else
	    {
		ErrorHandler.paramAberrant("distance:" + p_distance);
	    }
	}

    }

    private RollResult effectuerAttaque(int p_phaseActuelle, int p_ND, int p_catArm, int p_domaine, int p_modifScore)
    {
	RollResult result = null;
	if (agirEnCombat(p_phaseActuelle))
	{
	    int MalusDes = 0;
	    int ecartPhyMin = 0;
	    int modDist = 0;
	    int catArm = 0; //mains nues par défaut
	    int domaine = 3; //CaC par défaut
	    Arme arme = m_inventaire.getArmeCourante();
	    if (arme != null)//une arme est équipée et on veut l'utiliser
	    {
		catArm = arme.getCategorie();
		if (arme.getphysMin() > m_traits[0])
		{
		    ecartPhyMin += m_traits[0] - arme.getphysMin();
		}
		MalusDes += arme.getMalusAttaque();
		if (arme.getMode() == 1)//arme à distance
		{
		    domaine = 4;

		}
	    }
	    int modFinal = (ecartPhyMin * 10) - modDist;
	    result = m_listDomaines.get(domaine).effectuerJetComp(catArm, m_traits[1], p_ND, -MalusDes, modFinal, isSonne());
	}
	return result;
    }

    /**
     * fait dépenser une action au personnage dans la phase en cours si c'est
     * possible
     *
     * @param p_phaseActuelle
     * @return un booléen indiquant si il est possible d'agir dans la phase en
     * cours
     */
    public boolean agirEnCombat(int p_phaseActuelle)
    {//TODO : gérer ici les interruptions
	boolean result = false;
	if (p_phaseActuelle > 0 && p_phaseActuelle < 11)
	{

	    if ((m_actions.size() - m_actionCourante) > 0 && p_phaseActuelle == m_actions.get(m_actionCourante))
	    {
		m_actions.set(m_actionCourante, 11);
		m_actionCourante++;
		result = true;
	    }
	}
	else
	{
	    ErrorHandler.paramAberrant("phase:" + p_phaseActuelle);
	}
	return result;
    }

    /**
     * génère des dégâts avec l'arme courante en corps à corps, séparée de
     * l'attaque pour que le contrôleur puisse utiliser les incréments pour
     * autre chose (comme cibler)
     *
     * @param p_mainsNues indique si les dégâts doivent ignorer l'arme portée si
     * elle existe
     * @param p_increments
     * @return
     */
    public Degats genererDegatsCaC(int p_increments, boolean p_mainsNues)
    {
	Degats result;
	Arme arme = m_inventaire.getArmeCourante();

	if (p_mainsNues || arme == null)//mains nues
	{
	    result = new Degats(RollUtils.lancer(m_traits[0] + p_increments, 1, isSonne()), 0);
	}
	else
	{
	    if (arme.getMode() == 0)//arme de corps à corps employée
	    {
		result = ((ArmeCaC) arme).genererDegats(p_increments, m_traits[0], isSonne());
	    }
	    else//arme à distance employée
	    {
		result = ((ArmeDist) arme).genererDegats(p_increments, isSonne());
	    }
	}

	return result;
    }

    /**
     * inflige des dégâts, via la jauge de Santé après avoir appliqué les effets
     * d'armure
     *
     * @param degats
     */
    public void etreBlesse(Degats degats)
    {
	int redDegats = 0;
	Armure armure = m_inventaire.getArmureCourante();
	if (armure != null)
	{
	    redDegats = armure.getRedDegats(degats.getTypeArme());
	}
	int degatsEffectifs = degats.getQuantite() - redDegats;

	if (degatsEffectifs > 0)
	{
	    int resultTest = RollUtils.lancer(m_traits[0], m_traits[0], false);
	    m_jaugeSanteInit.recevoirDegats(degatsEffectifs, resultTest, m_traits[3]);
	}
    }

    /**
     *
     * @param p_typeArme
     * @param p_parade catégorie d'arme à employer en parade, ignoré si esquive
     * @param p_esquive : si l'squive doit être employée, sinon c'est une parade
     * qui est effectuée
     * @return le ND passif calculé à partir des comps et de l'armure
     */
    public int getNDPassif(int p_typeArme, int p_parade, boolean p_esquive)
    {//TODO : gérer quand on n'a pas de compétence
	int ND = 5;

	int rang = 0;
	int effetArmure = 0;
	Armure armure = m_inventaire.getArmureCourante();

	if (!p_esquive)
	{
	    //calcul de la valeur issue de la compétence parade
	    rang = m_listDomaines.get(3).getCompetences().get(p_parade).getRang();

	    //ajout des bonus  et malus d'armure
	    if (armure != null)
	    {
		effetArmure += armure.getBonusND(p_typeArme);
		effetArmure -= armure.getMalusParade();
	    }
	}
	else
	{
	    //calcul de la valeur issue de la compétence esquive
	    rang = m_listDomaines.get(2).getCompetences().get(0).getRang();

	    //ajout des bonus  et malus d'armure
	    if (armure != null)
	    {
		effetArmure += armure.getBonusND(p_typeArme);
		effetArmure -= armure.getMalusEsquive();
	    }
	}

	ND = rang * 5 + 5 + effetArmure;
	if (rang >= 3)
	{
	    ND += 5;
	}

	return ND;
    }

    /**
     * renvoie vrai si le personnage a une action dans la phase active
     * comportement indéfini si demande pour pĥase ultérieure ou antérieure
     *
     * @param phaseActuelle
     * @return
     */
    public boolean isActif(int phaseActuelle)
    {
	return ((m_actions.size() - m_actionCourante) > 0 && phaseActuelle == m_actions.get(m_actionCourante));
    }

    /**
     *
     * @return l'initiative totale du personnage en comptant le bonus de l'arme
     */
    public int getInitTotale()
    {
	int result = 0;

	//traitement de la partie dûe aux dés d'action
	for (int i = 0; i < m_actions.size(); ++i)
	{
	    if (m_actions.get(i) < 11)//si l'action considérée est toujours disponible
	    {
		result += m_actions.get(i);
	    }
	}

	//traitement du bonus dû à l'arme
	Arme arme = m_inventaire.getArmeCourante();
	if (arme != null)
	{
	    result += arme.getBonusInit();
	}
	return result;
    }

    @Override
    public String toString()
    {
	return m_libellePerso;
    }

    public void setLibellePerso(String libellePerso)
    {
	this.m_libellePerso = libellePerso;
    }

    /**
     * renvoie une copie de la liste des domaines pour ne pas risquer une
     * édition malencontreuse
     *
     * @return
     */
    public ArrayList<Domaine> GetDomaines()
    {
	return new ArrayList<Domaine>(m_listDomaines);
    }

    /**
     *
     * @return cette liste est directement éditable, il vaudrait mieux la
     * confiner au CharacterAssembly
     */
    public ArrayList<Arme> getListArmes()
    {
	return m_inventaire.listArmes;
    }

    public Arme getArmeCourante()
    {
	return m_inventaire.getArmeCourante();
    }

    public void setArmeCourante(int p_indice)
    {
	m_inventaire.setArmeCourante(p_indice);
    }

    /**
     *
     * @return cette liste est directement éditable, il vaudrait mieux la
     * confiner au CharacterAssembly
     */
    public ArrayList<Armure> getListArmures()
    {
	return m_inventaire.listArmures;
    }

    public Armure getArmureCourante()
    {
	return m_inventaire.getArmureCourante();
    }

    public void setArmureCourante(int p_armure)
    {
	m_inventaire.setArmureCourante(p_armure);
    }

    /**
     * les 2 jauges sont prises en compte
     *
     * @return
     */
    public boolean isInconscient()
    {
	return m_jaugeSanteInit.isInconscient() || m_jaugeFatigueForceDAme.isInconscient();
    }

    /**
     * les 2 jauges sont prises en compte
     *
     * @return
     */
    public boolean isSonne()
    {
	return (m_jaugeSanteInit.isSonne() || m_jaugeFatigueForceDAme.isSonne());
    }

    /**
     * les 2 jauges sont prises en compte
     *
     * @return
     */
    public boolean isElimine()
    {
	return (m_jaugeFatigueForceDAme.isElimine() || m_jaugeSanteInit.isElimine());
    }

    public int getBlessuresGraves()
    {
	return m_jaugeSanteInit.getRemplissage_interne();
    }

    public int getBlessuresLegeres()
    {
	return m_jaugeSanteInit.getBlessuresLegeres();
    }

    public int getPointsDeFatigue()
    {
	return m_jaugeFatigueForceDAme.getRemplissage_interne();
    }

    public int getBlessuresLegeresMentales()
    {
	return m_jaugeFatigueForceDAme.getBlessuresLegeres();
    }

    /**
     *
     * @return une copie : la liste n'est pas modifiable de l'extérieur
     */
    public ArrayList<Integer> getActions()
    {
	return new ArrayList<>(m_actions);
    }
}
