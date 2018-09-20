package com.dfire.core.tool.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.dfire.core.config.HeraGlobalEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

/**
 * @Description : Druid连接池
 * @Author ： HeGuoZi
 * @Date ： 10:04 2018/8/24
 * @Modified :
 */
@Slf4j
public abstract class AbstractDataSourcePool {

    private volatile boolean isClose;

    private DruidDataSource dataSource;

    public AbstractDataSourcePool() {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(HeraGlobalEnvironment.getSparkDriver());
        dataSource.setUrl(HeraGlobalEnvironment.getSparkAddress());
        dataSource.setUsername(HeraGlobalEnvironment.getSparkUser());
        dataSource.setPassword(HeraGlobalEnvironment.getSparkPassword());
        dataSource.setInitialSize(10);
        dataSource.setMaxActive(20);
        dataSource.setMinIdle(20);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(600 * 1000);
        isClose = false;
    }

    public Connection getConnection() {
        if (isClose || dataSource == null) {
            log.error("空连接池或连接池已关闭");
            return null;
        }
        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取连接失败");
            return null;
        }
    }

    public void close() {
        if (!isClose) {
            isClose = true;
            if (dataSource != null) {
                dataSource.close();
                dataSource = null;
            }
        }
    }

    public boolean isClose() {
        return isClose;
    }

}