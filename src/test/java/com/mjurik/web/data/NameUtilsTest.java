package com.mjurik.web.data;

import com.mjurik.web.crawler.db.entity.NominalValue;
import com.mjurik.web.crawler.db.entity.Variant;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Marian Jurik on 20.11.2015.
 */
public class NameUtilsTest {

    @Test
    public void nominal1() {
        NominalValue nom = NameUtils.parseNominal("10 Euro/2011 - Ján Cikker – 100. výročie narodenia");
        Assert.assertEquals(NominalValue.E_10, nom);
    }

    @Test
    public void nominal2() {
        NominalValue nom = NameUtils.parseNominal("5000 Sk/2006 - Mojmír II. - 1100. výročie úmrtia veľkomoravského panovníka");
        Assert.assertEquals(NominalValue.SK_5000, nom);
    }

    @Test
    public void nominal3() {
        NominalValue nom = NameUtils.parseNominal("Sada mincí Slovenskej republiky 2014 - XXII. ZOH Soči 2014 Proof Like");
        Assert.assertEquals(NominalValue.COLLECTION, nom);
    }

    @Test
    public void nominal4() {
        NominalValue nom = NameUtils.parseNominal("Sada Slovenskej republiky XY");
        Assert.assertEquals(null, nom);
    }

    @Test
    public void variant1() {
        Variant variant = NameUtils.parseVariant("Sada mincí Slovenskej republiky 2014 - XXII. ZOH Soči 2014 Proof Like");
        Assert.assertEquals(Variant.PROOF, variant);
    }

    @Test
    public void variant2() {
        Variant variant = NameUtils.parseVariant("Sada mincí Slovenskej republiky 2014 - XXII. ZOH Soči 2014 bk");
        Assert.assertEquals(Variant.BK, variant);
    }

    @Test
    public void variant3() {
        Variant variant = NameUtils.parseVariant("Sada mincí Slovenskej republiky 2014 - XXII. ZOH Soči 2014 bk razba");
        Assert.assertEquals(Variant.BK, variant);
    }

    @Test
    public void variant4() {
        Variant variant = NameUtils.parseVariant("Sada mincí Slovenskej republiky 2014 - XXII. ZOH Soči 2014 bk-razba");
        Assert.assertEquals(Variant.BK, variant);
    }

    @Test
    public void variant5() {
        Variant variant = NameUtils.parseVariant("Sada mincí Slovenskej republiky 2014 - XXII. ZOH Soči 2014 bkrazba");
        Assert.assertEquals(Variant.UNKNOWN, variant);
    }

}