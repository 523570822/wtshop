package com.wtshop.activiti;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.SessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作流插件
 */
public class ActivitiPlugin implements IPlugin {

    private static ProcessEngine processEngine = null;
    private static ProcessEngineConfiguration processEngineConfiguration = null;
    private boolean isStarted = false;

    public boolean start() {
        try {
            createProcessEngine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean stop() {
        ProcessEngines.destroy();
        isStarted = false;
        return true;
    }

    private Boolean createProcessEngine() throws Exception {
        if (isStarted) {
            return true;
        }
        StandaloneProcessEngineConfiguration conf = (StandaloneProcessEngineConfiguration) ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        conf.setDataSource(DbKit.getConfig().getDataSource())
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setDbHistoryUsed(true);
//		conf.setTransactionsExternallyManaged(true); // 使用托管事务工厂
        conf.setTransactionFactory(new ActivitiTransactionFactory());

        List<SessionFactory> sessionFactoryList = new ArrayList<SessionFactory>();
        sessionFactoryList.add(new WtGroupSessionFactory());
        sessionFactoryList.add(new WtUserSessionFactory());
        conf.setCustomSessionFactories(sessionFactoryList);

        ActivitiPlugin.processEngine = conf.buildProcessEngine();
        isStarted = true;
        //开启流程引擎
        System.out.println("启动流程引擎.......");

        /**
         * 部署流程定义
         * 以后可以拿出去
         * */
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService().createDeployment()
                .name("RXM")
                .addClasspathResource("/diagrams/reverseAuction.bpmn")
                .deploy();
//		convertToModel(ActivitiPlugin.processEngine,"Urge:4:17504");
//		createModel(ActivitiPlugin.processEngine);
        return isStarted;
    }

    // 开启流程服务引擎
    public static ProcessEngine buildProcessEngine() {
        if (processEngine == null)
            if (processEngineConfiguration != null) {
                processEngine = processEngineConfiguration.buildProcessEngine();
            }
        return processEngine;
    }


}