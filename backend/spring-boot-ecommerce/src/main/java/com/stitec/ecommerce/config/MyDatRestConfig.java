package com.stitec.ecommerce.config;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import com.stitec.ecommerce.entity.Country;
import com.stitec.ecommerce.entity.Product;
import com.stitec.ecommerce.entity.ProductCategory;
import com.stitec.ecommerce.entity.State;


@Configuration
public class MyDatRestConfig  implements RepositoryRestConfigurer{
	
	private EntityManager entityManager;
	
	@Autowired
	public MyDatRestConfig(EntityManager theEntityManager) {
	entityManager = theEntityManager;
	}
	

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		
		HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};
		
		
		
		disableHttpMethods(Product.class, config, theUnsupportedActions);
		disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);
		disableHttpMethods(Country.class, config, theUnsupportedActions);
		disableHttpMethods(State.class, config, theUnsupportedActions);
		
		
		// appellée une methode pour exposer les Id
		exposeIds(config);
		
	}


	private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
		config.getExposureConfiguration().forDomainType(theClass).withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
		.withCollectionExposure((metdata,httpMethods) -> httpMethods.disable(theUnsupportedActions));
	}
	
	
	private void exposeIds(RepositoryRestConfiguration config) {
		//exposer les ids des entity
		
		//recuperer la liste de toutes les classe entity depuis l entity manager
		
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		
		//creer un tableau des types entity
		List<Class> entityClasses = new ArrayList<>();
		
		//recuperer le type entity des entities
		
		for(EntityType tempEntityType : entities) {
			entityClasses.add(tempEntityType.getJavaType());
		}
		
		// exposer les ids entity dans un tableau entity/domain types
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
	}

}
