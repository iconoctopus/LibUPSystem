/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author iconoctopus
 */
public class DomaineTest
{

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDomaine()
    {
	Domaine domaine1 = new Domaine(0, 1);
	Domaine domaine2 = new Domaine(8, 5);

	Assert.assertEquals(1, domaine1.getRang());
	Assert.assertEquals(3, domaine1.getCompetences().size());

	Assert.assertEquals(5, domaine2.getRang());
	Assert.assertEquals(4, domaine2.getCompetences().size());

	Domaine domaine3;

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("indice:-1");
	domaine3 = new Domaine(-1, 1);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("rang:-11");
	domaine1.setRang(-11);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("rang:-11");
	new Competence(-11);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("rang:-11");
	new CompCac(1, -11);

    }
}
