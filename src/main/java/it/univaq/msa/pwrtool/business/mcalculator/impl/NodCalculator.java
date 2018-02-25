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

@Service("NodCalculator")
public class NodCalculator implements MetricCalculator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WeavingMMService weaving;

	@Autowired
	QMService qm;

	@Override
	public relationGroup calculate(Product systemMsa, Value value) {
		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("NodCalculator execution");
		relations link = weaving.createRelation();
		IntegerValueType element;

		if (systemMsa.getDevelopers() != null)
			element = qm.createIntValue(systemMsa.getDevelopers().size());
		else
			element = qm.createIntValue(0);

		link.setMsaElement(systemMsa);
		link.setQualityElement(element);
		label.getReferTo().add(link);
		value.setValueType(element);
		logger.info(systemMsa.getName() + systemMsa.getComposedBy().size());
		return label;
	}

}
