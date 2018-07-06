package com.wtshop.util;

/**
 * Created by Administrator on 2017/5/31.
 */


import org.nlpcn.commons.lang.util.StringUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private RedisUtil() {
    }

    ;

    protected static final  ThreadLocal<Jedis> threadLocalJedis = new ThreadLocal<Jedis>();

    //Redis服务器IP
    private static String ADDR_ARRAY = ReadProper.getResourceValue("redis.url");

    //Redis的端口号
    private static int PORT = Integer.parseInt(ReadProper.getResourceValue("redis.port"));

    //访问密码
    private static String AUTH = ReadProper.getResourceValue("redis.password");

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 2000;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 500;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 1000 * 5;

    //超时时间
    private static int TIMEOUT = 1000 * 5;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * redis过期时间,以秒为单位
     */
    public final static int EXRP_HOUR = 60 * 60;          //一小时
    public final static int EXRP_DAY = 60 * 60 * 24;        //一天
    public final static int EXRP_MONTH = 60 * 60 * 24 * 30;   //一个月

    /**
     * 初始化Redis连接池,注意一定要在使用前初始化一次,一般在项目启动时初始化就行了
     */
    public static void initialPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            config.setTestOnCreate(true);
            config.setTestWhileIdle(true);
            config.setTestOnReturn(true);
            config.setNumTestsPerEvictionRun(-1);
            jedisPool = new JedisPool(config, ADDR_ARRAY, PORT, TIMEOUT, AUTH);
            threadLocalJedis.set(getJedis());
        } catch (Exception e) {
            e.printStackTrace();
//            try{
//                //如果第一个IP异常，则访问第二个IP
//                JedisPoolConfig config = new JedisPoolConfig();
//                config.setMaxTotal(MAX_ACTIVE);
//                config.setMaxIdle(MAX_IDLE);
//                config.setMaxWaitMillis(MAX_WAIT);
//                config.setTestOnBorrow(TEST_ON_BORROW);
//                jedisPool = new JedisPool(config, ADDR_ARRAY.split(",")[1], PORT, TIMEOUT,AUTH);
//            }catch(Exception e2){
//                e.printStackTrace();
//            }
        }
    }



    public static void close(Jedis jedis) {
        if (threadLocalJedis.get() == null && jedis != null)
            jedis.close();
    }






    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (jedisPool == null) {
            initialPool();
        }
    }


    /**
     * 获取Jedis实例,一定先初始化
     *
     * @return Jedis
     */
    public static Jedis getJedis() {
        boolean success = false;
        Jedis jedis = null;
//        if (jedisPool == null) {
//            poolInit();
//        }
        int i=0;
        while (!success) {
            i++;
            try {
                if (jedisPool != null) {
                    jedis=threadLocalJedis.get();
                    if (jedis==null){
                        jedis = jedisPool.getResource();
                    }else {
                        //System.out.println(Thread.currentThread().getName()+":第"+i+"次获取成功#@利用了本地缓存redis");
                        return jedis;
                    }

                }
            } catch (Exception e) {

               // System.out.println(Thread.currentThread().getName()+":第"+i+"次获取失败!!!");
                success = false;
                e.printStackTrace();
            }
            if (jedis!=null){
                success=true;
            }


        }
        if (threadLocalJedis.get()==null){threadLocalJedis.set(jedis);}
        //System.out.println(Thread.currentThread().getName()+":第"+i+"次获取成功@");

        return jedis;
    }




    /**
     * 设置 String
     *
     * @param key
     * @param value
     */
    public static void setString(String key, String value) {
        Jedis jd = null;
        try {

            value = StringUtil.isBlank(value) ? "" : value;
            jd = getJedis();
            jd.set(key, value);
        } catch (Exception e) {
            threadLocalJedis.set(null);
        } finally {
            if (jd != null) {
                close(jd);
            }

        }
    }

    /**
     * 设置 过期时间
     *
     * @param key
     * @param seconds 以秒为单位
     * @param value
     */
    public static void setString(String key, int seconds, String value) {
        Jedis jd = null;
        try {
            value = StringUtil.isBlank(value) ? "" : value;
            jd = getJedis();
            jd.setex(key, seconds, value);
        } catch (Exception e) {
            threadLocalJedis.set(null);
            e.printStackTrace();
        } finally {
            if (jd != null) {
                close(jd);

            }
        }


    }

    /**
     * 获取String值
     *
     * @param key
     * @return value
     */
    public static String getString(String key) {
        Jedis jd = null;

        try {
            jd = getJedis();

            if (jd == null || !jd.exists(key)) {
                return null;
            }
            return jd.get(key);
        } catch (Exception e) {
            threadLocalJedis.set(null);
            e.printStackTrace();
        } finally {
            if (jd != null) {
                close(jd);
            }
        }

        return null;
    }

    public static long incrBy(String key, long integer) {
        Jedis jd = null;
        try {
            jd = getJedis();

            return jd.incrBy(key, integer);
        } catch (Exception e) {
            threadLocalJedis.set(null);
            e.printStackTrace();
        } finally {
            if (jd != null) {
                close(jd);
            }
        }
        return -9999;
    }

    public static long decrBy(String key, long integer) {
        Jedis jd = null;
        try {
            jd = getJedis();
            return jd.decrBy(key, integer);
        } catch (Exception e) {
            threadLocalJedis.set(null);
            e.printStackTrace();
        } finally {
            if (jd != null) {
                close(jd);
            }
        }
        return -9999;
    }




}