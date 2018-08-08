package com.jfinalshop;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.wtshop.model.Activity;
import org.junit.After;
import org.junit.BeforeClass;

public class JFinalModelCase {
    protected static DruidPlugin dp;
    protected static ActiveRecordPlugin activeRecord;

    /**
     * 数据连接地址
     */
    private static final String  URL = "jdbc:mysql://59.110.18.65:3306/rxm_shop_db?useUnicode=true&characterEncoding=UTF-8&useSSL=false";

    /**
     * 数据库账号
     */
    private static final String  USERNAME = "rxm_db";

    /**
     * 数据库密码
     */
    private static final String  PASSWORD = "2x1CpGT2C5URxfSE";

    /**
     * 数据库驱动
     */
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    /**
     * 数据库类型（如mysql，oracle）
     */
    private static final String DATABASE_TYPE = "mysql";

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dp=new DruidPlugin(URL, USERNAME,PASSWORD,DRIVER);

        dp.addFilter(new StatFilter());

        dp.setInitialSize(3);
        dp.setMinIdle(2);
        dp.setMaxActive(5);
        dp.setMaxWait(60000);
        dp.setTimeBetweenEvictionRunsMillis(120000);
        dp.setMinEvictableIdleTimeMillis(120000);

        WallFilter wall = new WallFilter();
        wall.setDbType(DATABASE_TYPE);
        dp.addFilter(wall);

        dp.getDataSource();
        dp.start();

        activeRecord = new ActiveRecordPlugin(dp);
        activeRecord.setDialect(new MysqlDialect())
        //.setDevMode(true)
        //.setShowSql(true)  //是否打印sql语句
        ;

        //映射数据库的表和继承与model的实体
        //只有做完该映射后，才能进行junit测试
        //只有做完该映射后，才能进行junit测试
        activeRecord.addMapping("activity", Activity.class);
        //activeRecord.
        activeRecord.start();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        activeRecord.stop();
        dp.stop();
    }
}
