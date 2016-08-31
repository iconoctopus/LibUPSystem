package org.duckdns.spacedock.upengine.libupsystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe chargeant les fichiers de propriétés (notamment strings) et rendant
 * accessibles les informations de configuration au reste du package
 *
 * @author iconoctopus
 */
final class PropertiesHandler
{

    /**
     * message d'erreur par défaut
     */
    private final static String m_propertiesErrorMessage = "erreur d'accès à un fichier de propriétés";

    /**
     * si les propriétés des exceptions ont pu être récupérées
     */
    private boolean m_exceptionsRecovered;

    /**
     * si les propriétés des chaînes ont pu être récupérées
     */
    private boolean m_stringsRecovered;

    /**
     * instance statique unique
     */
    private static PropertiesHandler m_instance;

    /**
     * Textes des exceptions
     */
    private Properties m_exceptionsProperties;

    /**
     * Textes des chaînes générales (pas d'erreurs)
     */
    private Properties m_stringsProperties;

    /**
     * pseudo-constructeur statique permettant d'accéder à l'instance (la crée
     * si elle n'existe pas)
     *
     * @return l'instance unique
     */
    static PropertiesHandler getInstance()
    {
	if (m_instance == null)
	{
	    m_instance = new PropertiesHandler();
	}

	return (m_instance);
    }

    /**
     * véritable contructeur, appelé par getInstance() si l'instance n'existe
     * pas. Construit les objets properties à partir des fichiers idoines afin
     * qu'il ne soit pas nécessaire de le faire de façon synchrone durant le
     * reste de l'exécution
     *
     */
    private PropertiesHandler()
    {
	try
	{
	    m_exceptionsProperties = readProperties("strings/exceptions.properties");
	    m_exceptionsRecovered = true;
	}
	catch (IOException e)
	{
	    m_exceptionsRecovered = false;
	}

	try
	{
	    m_stringsProperties = readProperties("strings/generalstrings.properties");
	    m_stringsRecovered = true;
	}
	catch (IOException e)
	{
	    m_stringsRecovered = false;
	}
    }

    private Properties readProperties(String p_path) throws IOException
    {
	//InputStream in = PropertiesHandler.class.getClassLoader().getResourceAsStream("strings/exceptions.properties");
	/*on utilise le classloader pour récupérer le fichier de propriétés ailleurs que dans le même package : il utilise le classpath.
	 *On utilise le classloader du thread afin d'être davantage sur qu'il explorera tout le classpath, contrairement au classloader
	 *de la classe (utilisé dans le bout de code commenté ci-dessus). J'ignore si cela marche bien avec les threads android.
	 */
	InputStream in;
	in = Thread.currentThread().getContextClassLoader().getResourceAsStream(p_path);

	//on construit l'objet propriété demandé avant de le renvoyer
	Properties result = new Properties();
	result.load(in);
	in.close();

	return result;
    }

    /**
     *
     * @param p_property
     * @return le message d'erreur idoine
     */
    String getErrorMessage(String p_property)
    {
	String result;
	if (m_exceptionsRecovered)
	{
	    result = m_exceptionsProperties.getProperty(p_property);
	}
	else
	{
	    result = m_propertiesErrorMessage;
	}
	return result;
    }

    /**
     *
     * @param p_property
     * @return la chaîne d'usage général (pas de traitement d'erreurs ici)
     * indiquée en paramétre
     */
    String getString(String p_property)
    {
	String result;
	if (m_stringsRecovered)
	{
	    result = m_stringsProperties.getProperty(p_property);
	}
	else
	{
	    result = m_propertiesErrorMessage;
	}
	return result;
    }
}
