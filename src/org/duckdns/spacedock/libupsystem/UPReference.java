package org.duckdns.spacedock.libupsystem;

import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

final class UPReference
{

    private static UPReference m_instance;

    //TODO réorganiser la classe et mieux expliquer le fonctionnement des indices par des commentaires en regarde de chaque table
    //TODO blinder tous les paramétres
    //TODO blinder dans toutes les méthodes de toutes les classes.....
    private final JsonArray m_tableArmureRedDegats;
    private final JsonArray m_tableArmureBonusND;
    private final JsonArray m_tableArmureRangs;

    private final JsonArray m_tableArmureTyp0;
    private final JsonArray m_tableArmureTyp1;
    private final JsonArray m_tableArmureTyp2;
    private final JsonArray m_tableArmureTyp3;

    private final JsonArray m_tableInitCoord;
    private final JsonArray m_tableInitMental;

    private final JsonArray m_tableTraits;

    static UPReference getInstance()
    {
	if(m_instance == null)
	{
	    m_instance = new UPReference();
	}
	return (m_instance);
    }

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

    }

    private JsonObject loadJsonFile(String p_filePath)
    {
	InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(p_filePath);
	JsonReader reader = Json.createReader(in);
	return (reader.readObject());
    }

    int getInitModCoord(int p_coordination)
    {
	//le tableau est indexé à partir de 0, pas la coordination
	return m_tableInitCoord.getInt(p_coordination - 1);
    }

    int getInitModMental(int p_mental)
    {
	//le tableau est indexé à partir de 0, pas la coordination
	return m_tableInitMental.getInt(p_mental - 1);
    }

    private int getRang(int p_points)
    {
	int i = -1;//on commence avec i en dehors du tableau (0 points d'armure, pas de bonus) et l'on teste si on peut l'augmenter, quand on ne peut plus l'augmenter on le renvoie. A noter que cela blinde du même coup la fonction contre les chiffres négatifs sans erreur (ce qui est préférable ici)
	while(p_points > m_tableArmureRangs.getInt(i + 1))
	{
	    ++i;
	}
	return (i);
    }

    String getLibelleTrait(int p_indice)
    {
	return m_tableTraits.getString(p_indice);
    }

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
	return resultat;

    }

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
	return resultat;

    }

    double getPointsArmureEffectifs(int p_typeArme, int p_typeArmure)
    {
	double resultat;
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

	return resultat;
    }

}
