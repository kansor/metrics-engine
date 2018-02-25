package it.univaq.msa.pwrtool.business.mcalculator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MicroservicesArchitecture.Product;
import QualityMetamodel.IntegerValueType;
import QualityMetamodel.Value;
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service("NosCalculator")
public class NosCalculator implements MetricCalculator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WeavingMMService weaving;
	@Autowired
	QMService qm;

	@Override
	public relationGroup calculate(Product systemMsa, Value value) {
		logger.info(value.getVarName());

		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("NosCalculator execution");
		relations link = weaving.createRelation();
		IntegerValueType element = qm.createIntValue(0);

		if (systemMsa.getComposedBy() != null)
			element.setValue(systemMsa.getComposedBy().size());
		element.setVarName("nos");
		value.setValueType(element);
		link.setMsaElement(systemMsa);
		link.setQualityElement(element);
		label.getReferTo().add(link);

		return label;
	}

}
