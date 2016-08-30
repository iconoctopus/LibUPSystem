/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import org.duckdns.spacedock.upengine.libupsystem.Armure.PieceArmure;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class ArmureTest
{

    static Armure.PieceArmure piece1;
    static Armure.PieceArmure piece2;
    static Armure.PieceArmure piece3;
    static Armure.PieceArmure piece4;
    static Armure.PieceArmure piece5;
    static Armure.PieceArmure bouclier1;
    static Armure.PieceArmure bouclier2;
    static ArrayList<PieceArmure> listPieces = new ArrayList<>();
    static Armure armure;

    @BeforeClass
    public static void setUpClass()
    {
	piece1 = new Armure.PieceArmure(0, 0, 0, false);
	piece2 = new Armure.PieceArmure(0, 1, 3, false);
	piece3 = new Armure.PieceArmure(7, 2, 0, false);
	piece4 = new Armure.PieceArmure(7, 3, 3, false);
	piece5 = new Armure.PieceArmure(4, 0, 1, false);
	bouclier1 = new Armure.PieceArmure(3, 3, 1, true);
	bouclier2 = new Armure.PieceArmure(0, 0, 0, true);

	listPieces.add(piece1);
	listPieces.add(piece3);

    }

    @Before
    public void setUp()
    {
	armure = new Armure(listPieces);
    }

    @After
    public void tearDown()
    {
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
	Assert.assertEquals(1, piece1.getnbMax());
	Assert.assertEquals("heaume complet en plates", piece1.toString());

	Assert.assertEquals(0, piece2.getIdPiece());
	Assert.assertEquals(0, piece2.getMalusEsquive());
	Assert.assertEquals(0, piece2.getMalusParade());
	Assert.assertEquals(3, piece2.getMateriau());
	Assert.assertEquals(1, piece2.getNbpoints());
	Assert.assertEquals(1, piece2.getType());
	Assert.assertEquals(1, piece2.getnbMax());
	Assert.assertEquals("heaume complet en cuir bouilli", piece2.toString());

	Assert.assertEquals(7, piece3.getIdPiece());
	Assert.assertEquals(10, piece3.getMalusEsquive());
	Assert.assertEquals(0, piece3.getMalusParade());
	Assert.assertEquals(0, piece3.getMateriau());
	Assert.assertEquals(6, piece3.getNbpoints());
	Assert.assertEquals(2, piece3.getType());
	Assert.assertEquals(1, piece3.getnbMax());
	Assert.assertEquals("cuirasse en plates", piece3.toString());

	Assert.assertEquals(7, piece4.getIdPiece());
	Assert.assertEquals(1, piece4.getMalusEsquive());
	Assert.assertEquals(0, piece4.getMalusParade());
	Assert.assertEquals(3, piece4.getMateriau());
	Assert.assertEquals(2, piece4.getNbpoints());
	Assert.assertEquals(3, piece4.getType());
	Assert.assertEquals(1, piece4.getnbMax());
	Assert.assertEquals("cuirasse en cuir bouilli", piece4.toString());

	Assert.assertEquals(4, piece5.getIdPiece());
	Assert.assertEquals(0, piece5.getMalusEsquive());
	Assert.assertEquals(2, piece5.getMalusParade());
	Assert.assertEquals(1, piece5.getMateriau());
	Assert.assertEquals(2, piece5.getNbpoints());
	Assert.assertEquals(0, piece5.getType());
	Assert.assertEquals(2, piece5.getnbMax());
	Assert.assertEquals("brassière en lamelles ou maille", piece5.toString());

	Assert.assertEquals(3, bouclier1.getIdPiece());
	Assert.assertEquals(0, bouclier1.getMalusEsquive());
	Assert.assertEquals(0, bouclier1.getMalusParade());
	Assert.assertEquals(1, bouclier1.getMateriau());
	Assert.assertEquals(4, bouclier1.getNbpoints());
	Assert.assertEquals(3, bouclier1.getType());
	Assert.assertEquals(1, bouclier1.getnbMax());
	Assert.assertEquals("pavois en bois", bouclier1.toString());

	Assert.assertEquals(0, bouclier2.getIdPiece());
	Assert.assertEquals(0, bouclier2.getMalusEsquive());
	Assert.assertEquals(0, bouclier2.getMalusParade());
	Assert.assertEquals(0, bouclier2.getMateriau());
	Assert.assertEquals(2, bouclier2.getNbpoints());
	Assert.assertEquals(0, bouclier2.getType());
	Assert.assertEquals(1, bouclier2.getnbMax());
	Assert.assertEquals("targe en métal", bouclier2.toString());
    }

    @Test
    public void testArmure()
    {
	Assert.assertEquals(10, armure.getMalusEsquive());
	Assert.assertEquals(0, armure.getMalusParade());
	Assert.assertEquals("heaume complet en plates", armure.getListPieces().get(0).toString());
	Assert.assertEquals("cuirasse en plates", armure.getListPieces().get(1).toString());
    }

    @Test
    public void testGetBonusND()
    {

	Assert.assertEquals(15, armure.getBonusND(0));

    }

    @Test
    public void testGetRedDegats()
    {
	Assert.assertEquals(0, armure.getRedDegats(4));
    }

    @Test
    public void testAddRemovePiece()
    {
	armure.addPiece(piece5);
	Assert.assertEquals(0, armure.getRedDegats(4));
	Assert.assertEquals(10, armure.getRedDegats(1));
	Assert.assertEquals(15, armure.getBonusND(0));
	Assert.assertEquals(10, armure.getMalusEsquive());
	Assert.assertEquals(2, armure.getMalusParade());
	armure.addPiece(piece5);
	Assert.assertEquals(4, armure.getMalusParade());

	try
	{
	    armure.addPiece(piece5);
	    fail();
	}
	catch (IllegalStateException e)
	{
	    Assert.assertEquals("emploi de la mauvaise méthode dans ce contexte:ajout de pièce d'armure:brassière en lamelles ou maille", e.getMessage());
	}

	armure.removePiece(3);
	armure.addPiece(piece5);
	armure.removePiece(3);

	armure.addPiece(bouclier1);
	Assert.assertEquals(5, armure.getRedDegats(3));
	Assert.assertEquals(15, armure.getRedDegats(2));
	Assert.assertEquals(15, armure.getBonusND(2));
	Assert.assertEquals(10, armure.getBonusND(3));
	Assert.assertEquals(10, armure.getMalusEsquive());
	Assert.assertEquals(2, armure.getMalusParade());

    }
}
