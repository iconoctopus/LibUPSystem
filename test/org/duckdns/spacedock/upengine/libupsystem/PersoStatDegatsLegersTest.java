/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author iconoctopus
 */
public class PersoStatDegatsLegersTest
{

    @Test
    public void testStatDegatsLegers()
    {
	int nbGraves = StatUtils.nbBlessuresGravesStatistique(30, 1);
	Assert.assertTrue(nbGraves == 1);
    }
}
