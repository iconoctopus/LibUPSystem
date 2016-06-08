package org.duckdns.spacedock.libupsystem;

public class RollResult
{
	private int nbIncrements;
	public int getNbIncrements()
	{
		return nbIncrements;
	}

	public boolean isJetReussi()
	{
		return jetReussi;
	}

	private boolean jetReussi;
	
	public RollResult(int increments, boolean reussite)
	{
		this.nbIncrements=increments;
		this.jetReussi=reussite;
	}
	
	
}