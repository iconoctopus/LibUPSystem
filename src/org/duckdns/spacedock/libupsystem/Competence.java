package org.duckdns.spacedock.libupsystem;

public class Competence
{
	private int rang;
	public int getRang()
	{
		return rang;
	}

	public boolean isSpecialiste()
	{
		return specialiste;
	}

	private boolean specialiste;
	
	public Competence(int valRang, boolean isSpecialist)
	{
		rang=valRang;
		specialiste=isSpecialist;
	}
}