package com.example.carparkdemo;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void Lat_isCorrect() {

        LatLng x = new LatLng(-37.722573, 145.045072);
        MapsActivity mapsActivity = new MapsActivity();

        assertEquals(-37.722573, mapsActivity.getLat(x), 5);
    }


    @Test
    public void currPos_isCorrect() {


        MapsActivity mapsActivity = new MapsActivity();

        assertNull(mapsActivity.getPos());
    }
}