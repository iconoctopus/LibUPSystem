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
	Domaine domaine1 = new Domaine(0, 0);
	Domaine domaine2 = new Domaine(8, 5);

	Assert.assertEquals(0, domaine1.getRang());
	Assert.assertEquals(3, domaine1.getCompetences().size());

	Assert.assertEquals(5, domaine2.getRang());
	Assert.assertEquals(4, domaine2.getCompetences().size());

	ArrayList<Competence> compSet1 = new ArrayList<>();
	ArrayList<Competence> compSet2 = new ArrayList<>();
	compSet2.add(new Competence(0));
	compSet2.add(new Competence(0));
	compSet2.add(new Competence(0));
	compSet2.add(new Competence(0));

	Domaine domaine3;

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("incohérence entre la référence et le paramétre sur le nombre de compétences du domaine:6");
	domaine3 = new Domaine(6, 0, compSet1);

	thrown.expect(IllegalArgumentException.class);
	thrown.expectMessage("incohérence entre la référence et le paramétre sur le nombre de compétences du domaine:6");
	domaine3 = new Domaine(6, 0, compSet2);

    }
}
