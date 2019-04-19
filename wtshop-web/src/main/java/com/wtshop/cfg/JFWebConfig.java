package com.wtshop.cfg;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.FakeStaticHandler;
import com.jfinal.ext.plugin.monogodb.MongodbPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.tx.TxByActionKeyRegex;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.wtshop.FreeMarkerExceptionHandler;
import com.wtshop.activiti.ActivitiPlugin;
import com.wtshop.constants.Code;
import com.wtshop.controller.admin.DoTimeController;
import com.wtshop.model._MappingKit;
import com.wtshop.security.MyJdbcAuthzService;
import com.wtshop.shiro.core.ShiroInterceptor;
import com.wtshop.shiro.core.ShiroPlugin;
import com.wtshop.shiro.core.SubjectKit;
import com.wtshop.shiro.freemarker.ShiroTags;
import com.wtshop.template.directive.*;
import com.wtshop.template.method.AbbreviateMethod;
import com.wtshop.template.method.CurrencyMethod;
import com.wtshop.template.method.MessageMethod;
import com.wtshop.util.ReadProper;
import com.wtshop.util.RedisUtil;
import com.wtshop.util.SystemUtils;
import com.wtshop.validator.ValidationPlugin;
import com.wtshop.validator.ValidatorInterceptor;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.StandardCompress;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JFWebConfig extends JFinalConfig {

    final static Logger _logger = Logger.getLogger(JFWebConfig.class);

    /**
     * 供Shiro插件使用。
     */
    Routes routes;

    @Override
    public void configConstant(Constants constants) {
        loadPropertyFile("wtshop.properties");

        Code.isDevMode = getPropertyToBoolean("devMode", false);
        constants.setDevMode(getPropertyToBoolean("devMode", false));
        //constants.setEncoding("UTF-8");
        constants.setI18nDefaultBaseName("i18n");
        constants.setViewType(ViewType.FREE_MARKER);
        constants.setViewExtension(".ftl");
        constants.setI18nDefaultLocale(getProperty("locale"));
        constants.setError401View("/admin/common/error.ftl");
        constants.setError403View("/admin/common/unauthorized.ftl");
        constants.setError404View("/admin/common/error.ftl");
        constants.setError500View("/admin/common/error.ftl");

    }

    @Override
    public void configRoute(Routes routes) {
        this.routes = routes;

        AutoBindRoutes abr = new AutoBindRoutes();
        // api 模块
        abr.addJars("wtshop-api-1.0-SNAPSHOT.jar");
        // 忽略不自动扫描的Controller
        List<Class<? extends Controller>> clazzes = new ArrayList<Class<? extends Controller>>();
        clazzes.add(com.wtshop.controller.shop.BaseController.class);
        clazzes.add(com.wtshop.controller.admin.BaseController.class);
        clazzes.add(com.wtshop.controller.wap.BaseController.class);
        clazzes.add(com.wtshop.api.controller.BaseAPIController.class);
        abr.addExcludeClasses(clazzes);
        routes.add(abr);
    }

    @Override
    public void configPlugin(Plugins plugins) {
        //String publicKey = getProperty("jdbc.publicKey");
        //String password = getProperty("jdbc.password");
//mongodb  数据库注释
       String mongodb_hosts = getProperty("mongodb.hosts");
        String mongodb_ports = getProperty("mongodb.ports");
        if (StringUtils.isNotBlank(mongodb_hosts) && StringUtils.isNotBlank(mongodb_ports)){
            MongodbPlugin mongodbPlugin = new MongodbPlugin(
                    mongodb_hosts, mongodb_ports, getProperty("mongodb.name")
            );
            plugins.add(mongodbPlugin);
        }else{
            MongodbPlugin mongodbPlugin = new MongodbPlugin(
                    getProperty("mongodb.host"), Integer.parseInt(getProperty("mongodb.port")), getProperty("mongodb.name")
            );
            plugins.add(mongodbPlugin);
        }


        // 定时器插件
		Cron4jPlugin cp = new Cron4jPlugin();
		cp.addTask("* * * * *", new DoTimeController());
		plugins.add(cp);

        plugins.add(new Cron4jPlugin(PropKit.use("wtshop.properties")));


        //配置druid连接池
        DruidPlugin druidDefault = new DruidPlugin(
                getProperty("jdbc.url"),
                getProperty("jdbc.username"),
                //EncriptionKit.passwordDecrypt(publicKey, password),
                getProperty("jdbc.password"),
                getProperty("jdbc.driver"));
        // StatFilter提供JDBC层的统计信息
        druidDefault.addFilter(new StatFilter());

        // WallFilter的功能是防御SQL注入攻击
        WallFilter wallDefault = new WallFilter();

        wallDefault.setDbType("mysql");
        druidDefault.addFilter(wallDefault);

        druidDefault.setInitialSize(getPropertyToInt("db.default.poolInitialSize"));
        druidDefault.setMaxPoolPreparedStatementPerConnectionSize(getPropertyToInt("db.default.poolMaxSize"));
//		druidDefault.setTimeBetweenConnectErrorMillis(getPropertyToInt("db.default.connectionTimeoutMillis"));
        plugins.add(druidDefault);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidDefault);
        plugins.add(arp);
        String sqlpath = PathKit.getRootClassPath() + "/sql";
        arp.setBaseSqlTemplatePath(sqlpath);
        arp.addSqlTemplate("act.sql");
        arp.addSqlTemplate("statistic.sql");
        // 配置属性名(字段名)大小写不敏感容器工厂
        arp.setContainerFactory(new CaseInsensitiveContainerFactory());

        // 显示SQL
        arp.setShowSql(true);

        //  工作流Activiti插件
//        ActivitiPlugin ap = new ActivitiPlugin();
//        plugins.add(ap);

        // 所有配置在 MappingKit
        _MappingKit.mapping(arp);

        //Ehcache缓存
        plugins.add(new EhCachePlugin());

        //shiro权限框架，添加到plugin
        plugins.add(new ShiroPlugin(routes, new MyJdbcAuthzService()));

        // 参数校验插件
        plugins.add(new ValidationPlugin("validation.properties"));

        //redis
        // 用于缓存news模块的redis服务
        RedisPlugin newsRedis = new RedisPlugin("act", getProperty("redis.url"), getPropertyToInt("redis.port"), getPropertyToInt("redis.timeout"), getProperty("redis.password"));
        JedisPoolConfig config = newsRedis.getJedisPoolConfig();
        config.setMaxTotal(2000);
        config.setMaxIdle(500);
        config.setMaxWaitMillis(5000);
        plugins.add(newsRedis);

        //  设置倒拍使用的redis
        setupQueueRedis(plugins);

    }

    private void setupQueueRedis(Plugins plugins) {
        String cacheName = "queue";
        String host = getProperty("redis.url");
        int port = getPropertyToInt("redis.port");
        int timeout = getPropertyToInt("redis.timeout");
        String password = getProperty("redis.password");
        int database = getPropertyToInt("redis.database.queue", 4);
        RedisPlugin queueRedis = new RedisPlugin(cacheName, host, port, timeout, password, database);
        plugins.add(queueRedis);
    }

    @Override
    public void configInterceptor(Interceptors interceptors) {
        // 添加shiro的过滤器到interceptor
        interceptors.add(new ShiroInterceptor());
        // 参数校全局拦截
        interceptors.add(new ValidatorInterceptor());
        interceptors.add(new TxByActionKeyRegex("(.*save.*|.*update.*)"));
    }

    @Override
    public void configHandler(Handlers handlers) {
        handlers.add(new FakeStaticHandler(".jhtml"));
        DruidStatViewHandler dvh = new DruidStatViewHandler("/druid", new IDruidStatViewAuth() {
            public boolean isPermitted(HttpServletRequest request) {
                if (SubjectKit.hasRoleAdmin()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        handlers.add(dvh);
    }

    @Override
    public void afterJFinalStart() {
        try {
            Configuration cfg = FreeMarkerRender.getConfiguration();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("base", JFinal.me().getContextPath());
            map.put("showPowered", getProperty("show_powered"));
            map.put("setting", SystemUtils.getSetting());
            map.put("message", new MessageMethod());
            map.put("abbreviate", new AbbreviateMethod());
            map.put("currency", new CurrencyMethod());
            map.put("flash_message", new FlashMessageDirective());
            map.put("current_member", new CurrentMemberDirective());
            map.put("pagination", new PaginationDirective());
            map.put("seo", new SeoDirective());
            map.put("ad_position", new AdPositionDirective());
            map.put("member_attribute_list", new MemberAttributeListDirective());
            map.put("navigation_list", new NavigationListDirective());
            map.put("tag_list", new TagListDirective());
            map.put("friend_link_list", new FriendLinkListDirective());
            map.put("brand_list", new BrandListDirective());
            map.put("attribute_list", new AttributeListDirective());
            map.put("article_list", new ArticleListDirective());
            map.put("article_category_root_list", new ArticleCategoryRootListDirective());
            map.put("article_category_parent_list", new ArticleCategoryParentListDirective());
            map.put("article_category_children_list", new ArticleCategoryChildrenListDirective());
            map.put("goods_list", new GoodsListDirective());
            map.put("product_category_root_list", new ProductCategoryRootListDirective());
            map.put("product_category_parent_list", new ProductCategoryParentListDirective());
            map.put("product_category_children_list", new ProductCategoryChildrenListDirective());
            map.put("review_list", new ReviewListDirective());
            map.put("consultation_list", new ConsultationListDirective());
            map.put("promotion_list", new PromotionListDirective());
            map.put("compress", StandardCompress.INSTANCE);
            map.put("fileServer", ReadProper.getResourceValue("fileServer"));
            cfg.setSharedVaribles(map);

            cfg.setDefaultEncoding(getProperty("template.encoding"));
            cfg.setURLEscapingCharset(getProperty("url_escaping_charset"));
            cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);//配置freemarker标签
            cfg.setWhitespaceStripping(true);
            cfg.setClassicCompatible(true);
            cfg.setNumberFormat(getProperty("template.number_format"));
            cfg.setBooleanFormat(getProperty("template.boolean_format"));
            cfg.setDateTimeFormat(getProperty("template.datetime_format"));
            cfg.setDateFormat(getProperty("template.date_format"));
            cfg.setTimeFormat(getProperty("template.time_format"));
            cfg.setObjectWrapper(new BeansWrapper(Configuration.VERSION_2_3_23));
            cfg.setTemplateUpdateDelayMilliseconds(getPropertyToLong("template.update_delay"));
            cfg.setTemplateExceptionHandler(new FreeMarkerExceptionHandler());
            cfg.setSharedVariable("shiro", new ShiroTags());

            cfg.setServletContextForTemplateLoading(JFinal.me().getServletContext(), getProperty("template.loader_path"));
            RedisUtil.initialPool();

        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        super.afterJFinalStart();
    }

    @Override
    public void configEngine(Engine me) {

    }


    @Override
    public void beforeJFinalStop() {
        super.beforeJFinalStop();
    }
}
