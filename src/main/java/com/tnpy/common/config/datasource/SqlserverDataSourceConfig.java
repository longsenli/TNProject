package com.tnpy.common.config.datasource;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * mysql主库配置类
 * @日期： 2018年12月18日 下午15:00
 * @作者： hzp
 */
@Configuration
//@MapperScan(basePackages = "com.spring.boot.mapper.cluster",sqlSessionTemplateRef = "clusterSqlSessionTemplate")
@MapperScan(basePackages = SqlserverDataSourceConfig.MAPPER_LOCATION,sqlSessionTemplateRef = "clusterSqlSessionTemplate")
public class SqlserverDataSourceConfig {
	
	static final String MAPPER_LOCATION = "com.tnpy.mes.mapper.sqlserver";
    static final String MAPPING_LOCATION = "classpath:sqlservermapping/*.xml";
	
	/**
	 * 创建数据源
	 *@return DataSource
	 */
	@Bean(name = "clusterDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.cluster")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	/**
	 * 创建工厂
	 *@param dataSource
	 *@throws Exception
	 *@return SqlSessionFactory
	 */
	@Bean(name = "clusterSqlSessionFactory")
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("clusterDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(SqlserverDataSourceConfig.MAPPING_LOCATION));
		return bean.getObject();
	}
	
	/**
	 * 创建事务
	 *@param dataSource
	 *@return DataSourceTransactionManager
	 */
	@Bean(name = "clusterTransactionManager")
	public DataSourceTransactionManager masterDataSourceTransactionManager(@Qualifier("clusterDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	/**
	 * 创建模板
	 *@param sqlSessionFactory  
	 *@return SqlSessionTemplate
	 */
	@Bean(name = "clusterSqlSessionTemplate")
	public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("clusterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}
