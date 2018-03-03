package com.stackroute.activitystream.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*This class will contain the application-context for the application. 
 * Define the following annotations:
 * @Configuration - Annotating a class with the @Configuration indicates that the 
 *                  class can be used by the Spring IoC container as a source of 
 *                  bean definitions
 * @EnableTransactionManagement - Enables Spring's annotation-driven transaction management capability.
 * @EnableJpaRepositories -  Will scan the package of the annotated configuration class for Spring Data repositories by default.                
 * */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories (basePackages = {"com.stackroute.activitystream"})
public class PersistenceJPAConfig {

	/*
	 * Define the bean for EntityManagerFactory,
	 * LocalContainerEntityManagerFactoryBean gives full control over
	 * EntityManagerFactory configuration and is appropriate for environments where
	 * fine-grained customization is required. Create a new
	 * LocalContainerEntityManagerFactoryBean object. We need to create this object
	 * because it creates the JPA EntityManagerFactory.
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "com.stackroute.activitystream.model" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	/*
	 * Define the bean for DataSource. In our application, we are using MySQL as the
	 * dataSource. To create the DataSource bean, we need to know: 1. Driver class
	 * name 2. Database URL 3. Username 4. Password
	 */
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/activitystream_step5");
		dataSource.setUsername("root");
//		dataSource.setPassword("pass@word1");
		dataSource.setPassword("root");
		return dataSource;

		
		/*  DriverManagerDataSource dataSource = new DriverManagerDataSource();
		  dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		  dataSource.setUrl("jdbc:mysql://localhost:3306/" +
		  System.getenv("MYSQL_DATABASE"));
		  dataSource.setUsername(System.getenv("MYSQL_USER"));
		  dataSource.setPassword(System.getenv("MYSQL_PASSWORD")); return dataSource;*/
		 
	}

	/*
	 * Define the bean for Transaction Manager, PlatformTransactionManager
	 * implements the programmatic approach to implement transactions.
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	/*
	 * Define the bean for PersistenceExceptionTranslationPostProcessor to enable
	 * exception translation.
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/*
	 * Define all the Hinernate properties
	 */
	Properties additionalProperties() {
		Properties properties = new Properties();
//		properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return properties;
	}

}
