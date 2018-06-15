package com.dashboard.restservicedashboard.selector;

import com.dashboard.commondashboard.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationService {
	
	@Autowired
	private DefectConfRepository defectConfRepository;
	
	@Autowired
	private TestsetConfRepository testsetConfRepository;
	
	
	private TestsetConf getTestsetConf() {
		return testsetConfRepository.findAll().get(0);
	}
	private DefectConf getDefectConf() {
		return defectConfRepository.findAll().get(0);
	}
	
	
	public EntityConfAbstract getEntityConf(Entity.EntityType entityType) {
		if(entityType.equals(Entity.EntityType.DEFECT)) {
			return getDefectConf();
		} else if(entityType.equals(Entity.EntityType.TESTSET)) {
			return getTestsetConf();
		}
		throw new RuntimeException("No Entity Found");
	}

}
