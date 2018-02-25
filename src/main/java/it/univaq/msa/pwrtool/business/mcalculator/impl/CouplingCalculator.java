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
@Service("CouplingCalculator")
public class CouplingCalculator implements MetricCalculator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WeavingMMService weaving;

	@Autowired
	QMService qm;

	@Override
	public relationGroup calculate(Product systemMsa, Value value) {
		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("coupling execution");
		ListValue lv = qm.createListValue();
		
		for (MicroService type : systemMsa.getComposedBy()){

			relations link = weaving.createRelation();
			IntegerValueType element;

			if (type.getRequire() != null) {
				element = qm.createIntValue(type.getRequire().size());
			} else
				element = qm.createIntValue(0);

			link.setMsaElement(type);
			link.setQualityElement(element);
			label.getReferTo().add(link);
			lv.getElements().add(element);
		}
		value.setValueType(lv);
		return label;
	}

}
