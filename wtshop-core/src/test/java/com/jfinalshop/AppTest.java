package com.jfinalshop;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.wtshop.model._MappingKit;
import com.wtshop.service.ActivityService;
import com.wtshop.service.ProductService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }
private ActivityService activityService = Enhancer.enhance(ActivityService.class);

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }



    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        long dd=0l;
        // dd = activityService.count();
        System.out.println(dd);
        assertTrue( true );
    }
}
