package it.univaq.msa.pwrtool.business.mcalculator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MicroservicesArchitecture.MicroService;
import MicroservicesArchitecture.Product;
import QualityMetamodel.IntegerValueType;
import QualityMetamodel.ListValue;
import QualityMetamodel.Value;
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.MSAService;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;
@Service("CohesionCalculator")
public class CohesionCalculator implements MetricCalculator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	WeavingMMService weaving;
	
	@Autowired
	QMService qm;
	
	@Autowired
	MSAService msa;
	
	
	@Override
	public relationGroup calculate(Product systemMsa, Value value) {
		logger.info(systemMsa.getName()+systemMsa.getComposedBy().size());
		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("cohesion execution");
		ListValue lv = qm.createListValue();
		for (MicroService type : systemMsa.getComposedBy()){
			logger.info(type.getName());
			//LinkW link = weaving.createLinkW();
			IntegerValueType element;
		
			if (type.getExpose() != null) {
				 element = qm.createIntValue(type.getExpose().size());
			} else element = qm.createIntValue(0);
			
//			link.setLinkLeft(type);
//			link.setLinkRight(element);
			
			relations link = weaving.createRelationRL(type, element);
			label.getReferTo().add(link);
			logger.info(type.getName());
			lv.getElements().add(element);
		}
		value.setValueType(lv);
		return label;
	}

}
