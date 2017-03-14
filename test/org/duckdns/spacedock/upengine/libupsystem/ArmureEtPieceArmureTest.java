/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class ArmureEtPieceArmureTest
{

    static PieceArmure piece1;
    static PieceArmure piece2;
    static PieceArmure piece3;
    static PieceArmure piece4;
    static PieceArmure piece5;
    static PieceArmure bouclier1;
    static PieceArmure bouclier2;
    static Armure armure;

    @BeforeClass
    public static void setUpClass()
    {
	piece1 = new PieceArmure(0, 0, 0, false);
	piece2 = new PieceArmure(0, 1, 3, false);
	piece3 = new PieceArmure(7, 2, 0, false);
	piece4 = new PieceArmure(7, 3, 3, false);
	piece5 = new PieceArmure(4, 0, 1, false);
	bouclier1 = new PieceArmure(3, 3, 1, true);
	bouclier2 = new PieceArmure(0, 0, 0, true);
    }

    @Test
    public void testPieceArmure()
    {
	Assert.assertEquals(0, piece1.getIdPiece());
	Assert.assertEquals(0, piece1.getMalusEsquive());
	Assert.assertEquals(0, piece1.getMalusParade());
	Assert.assertEquals(0, piece1.getMateriau());
	Assert.assertEquals(3, piece1.getNbpoints());
	Assert.assertEquals(0, piece1.getType());
	Assert.assertEquals(0, piece1.getLocalisation());
	Assert.assertEquals("heaume complet en plates", piece1.toString());

	Assert.assertEquals(0, piece2.getIdPiece());
	Assert.assertEquals(0, piece2.getMalusEsquive());
	Assert.assertEquals(0, piece2.getMalusParade());
	Assert.assertEquals(3, piece2.getMateriau());
	Assert.assertEquals(1, piece2.getNbpoints());
	Assert.assertEquals(1, piece2.getType());
	Assert.assertEquals(0, piece2.getLocalisation());
	Assert.assertEquals("heaume complet moderne", piece2.toString());

	Assert.assertEquals(7, piece3.getIdPiece());
	Assert.assertEquals(10, piece3.getMalusEsquive());
	Assert.assertEquals(0, piece3.getMalusParade());
	Assert.assertEquals(0, piece3.getMateriau());
	Assert.assertEquals(6, piece3.getNbpoints());
	Assert.assertEquals(2, piece3.getType());
	Assert.assertEquals(1, piece3.getLocalisation());
	Assert.assertEquals("cuirasse avec blindage", piece3.toString());

	Assert.assertEquals(7, piece4.getIdPiece());
	Assert.assertEquals(1, piece4.getMalusEsquive());
	Assert.assertEquals(0, piece4.getMalusParade());
	Assert.assertEquals(3, piece4.getMateriau());
	Assert.assertEquals(2, piece4.getNbpoints());
	Assert.assertEquals(3, piece4.getType());
	Assert.assertEquals(1, piece4.getLocalisation());
	Assert.assertEquals("cuirasse énergétique", piece4.toString());

	Assert.assertEquals(4, piece5.getIdPiece());
	Assert.assertEquals(0, piece5.getMalusEsquive());
	Assert.assertEquals(0, piece5.getMalusParade());
	Assert.assertEquals(1, piece5.getMateriau());
	Assert.assertEquals(2, piece5.getNbpoints());
	Assert.assertEquals(0, piece5.getType());
	Assert.assertEquals(2, piece5.getLocalisation());
	Assert.assertEquals("brassière en lamelles ou maille", piece5.toString());

	Assert.assertEquals(3, bouclier1.getIdPiece());
	Assert.assertEquals(0, bouclier1.getMalusEsquive());
	Assert.assertEquals(0, bouclier1.getMalusParade());
	Assert.assertEquals(1, bouclier1.getMateriau());
	Assert.assertEquals(4, bouclier1.getNbpoints());
	Assert.assertEquals(3, bouclier1.getType());
	Assert.assertEquals(3, bouclier1.getLocalisation());
	Assert.assertEquals("pavois énergétique", bouclier1.toString());

	Assert.assertEquals(0, bouclier2.getIdPiece());
	Assert.assertEquals(0, bouclier2.getMalusEsquive());
	Assert.assertEquals(0, bouclier2.getMalusParade());
	Assert.assertEquals(0, bouclier2.getMateriau());
	Assert.assertEquals(2, bouclier2.getNbpoints());
	Assert.assertEquals(0, bouclier2.getType());
	Assert.assertEquals(3, bouclier2.getLocalisation());
	Assert.assertEquals("targe en métal", bouclier2.toString());
    }

    @Test
    public void testArmure()
    {
	Assert.assertEquals(10, armure.getMalusEsquive());
	Assert.assertEquals(0, armure.getMalusParade());
    }

    @Test
    public void testGetBonusND()
    {
	Assert.assertEquals(15, armure.getBonusND(0));
    }

    @Test
    public void testGetRedDegats()
    {
	Assert.assertEquals(5, armure.getRedDegats(4));
    }
}
