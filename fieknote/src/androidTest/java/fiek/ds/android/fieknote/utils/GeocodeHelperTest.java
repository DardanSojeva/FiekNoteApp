

package fiek.ds.android.fieknote.utils;

import android.test.InstrumentationTestCase;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import fiek.ds.android.fieknote.FiekNote;


@Ignore
public class GeocodeHelperTest extends InstrumentationTestCase {

    @Test
    public void testGetAddressFromCoordinates() throws IOException {
        if (ConnectionManager.internetAvailable(FiekNote.getAppContext())) {
            Double LAT = 43.799328;
            Double LON = 11.171552;
            String address = GeocodeHelper.getAddressFromCoordinates(FiekNote.getAppContext(), LAT, LON);
            Assert.assertTrue(address.length() > 0);
        }
    }
}
