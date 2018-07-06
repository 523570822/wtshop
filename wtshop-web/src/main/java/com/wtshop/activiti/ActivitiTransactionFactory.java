package com.wtshop.activiti;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public class ActivitiTransactionFactory implements TransactionFactory {


    public void setProperties(Properties properties) {

    }

    public Transaction newTransaction(Connection connection) {
        return new ActivitiTransaction(connection);
    }

    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean b) {
        return new ActivitiTransaction(dataSource, transactionIsolationLevel, b);
    }
}
