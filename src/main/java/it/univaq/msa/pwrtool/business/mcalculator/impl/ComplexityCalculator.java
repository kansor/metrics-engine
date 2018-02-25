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
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service("ComplexityCalculator")
public class ComplexityCalculator implements MetricCalculator{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WeavingMMService weaving;

	@Autowired
	QMService qm;
	
	@Override
	public relationGroup calculate(Product systemMsa, Value value) {
		logger.info(systemMsa.getName()+systemMsa.getComposedBy().size());
		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("icos calculator execution");
		ListValue lv = qm.createListValue();
		for (MicroService type : systemMsa.getComposedBy()){
			logger.info(type.getName());
			
			
			int sum = 0;
			if (type.getExpose() != null) 
				 sum += type.getExpose().size();
			
			if (type.getRequire() != null) 
				 sum += type.getRequire().size();
			
			//divided by the number of service and you done

			IntegerValueType element = qm.createIntValue(sum);
			relations link = weaving.createRelationRL(type, element);
			label.getReferTo().add(link);
			logger.info(type.getName());
			lv.getElements().add(element);
		}
		value.setValueType(lv);
		return label;
	}

}
