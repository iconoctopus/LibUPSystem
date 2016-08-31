/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duckdns.spacedock.upengine.libupsystem;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iconoctopus
 */
public class PersoStatDegatsLourdsTest
{

    @Test
    public void testStatDegatsLegers()
    {

	int nbGraves = StatUtils.nbBlessuresGravesStatistique(30, 5);
	Assert.assertTrue(nbGraves == 0);

    }
}
