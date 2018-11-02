package com.android.lazyloading.recyclerview;

import com.android.lazyloading.recyclerview.models.Proficiency;
import com.android.lazyloading.recyclerview.models.Row;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

/**
 * Proficiency local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ProficiencyUnitTest {


    @Before
    public void setUp() {
        System.out.println("Set up called");
    }

    /*
     * Tests the data in the getters/setters of title of response datya for various scenarios (pass and fail test cases)
     */

    @Test
    public void testTitle() {
        Proficiency test = Mockito.mock(Proficiency.class);
        when(test.getTitle()).thenReturn("About Canada");
        test.getTitle();
        test.setTitle("Hello World");
        test.setTitle("called at least once");
        test.setTitle("called at least twice");
        test.setTitle("called five times");
        test.setTitle("called at most 3 times");
        // now check if method testing was called with the parameter 12
        verify(test).setTitle(Matchers.eq("called at most 3 times"));
        // was the method called twice?
        verify(test, times(1)).getTitle();
        // other alternatives for verifiying the number of method calls for a
        verify(test, never()).setRows(new ArrayList<Row>());
        verify(test, atLeastOnce()).setTitle("called at least once");
    }

    /*
     * Tests the data in the getters/setters of each row/post for various scenarios (pass and fail test cases)
     */
    @Test
    public void testRows() {
        Proficiency test = Mockito.mock(Proficiency.class);
        test.getRows();
        Row row = new Row();
        row.setDescription("well known polar bear in canada");
        row.setImageHref("http://img.jpg");
        row.setTitle("transportation");
        ArrayList rows = new ArrayList();
        rows.add(row);
        test.setRows(rows);
        // now check if method testing was called with the parameter 12
        verify(test).setRows(Matchers.eq(rows));
        // was the method called once?
        verify(test, times(1)).getRows();
        // will pass
        // was setRows() called with "rows" object?
        verify(test, atLeast(1)).setRows(rows);

    }

    @After
    public void tearDown() {
        System.out.println("tear down called");
    }


}