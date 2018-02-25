package it.univaq.msa.pwrtool.business.mcalculator.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MicroservicesArchitecture.MicroService;
import MicroservicesArchitecture.Product;
import QualityMetamodel.RealValueType;
import QualityMetamodel.Value;
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service("SystemCouplingCalculator")
public class SystemCouplingCalculator implements MetricCalculator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WeavingMMService weaving;

	@Autowired
	QMService qm;

	@Override
	public relationGroup calculate(Product systemMsa, Value value) {
		logger.info(systemMsa.getName() + systemMsa.getComposedBy().size());
		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("SystemCouplingCalculator execution");
		int sum = 0;
		for (MicroService type : systemMsa.getComposedBy()) {
			logger.info(type.getName());

			if (type.getRequire() != null)
				sum += type.getRequire().size();

			// divided by the number of service and you done

		}
		double result = 1 - 1 / (sum / systemMsa.getComposedBy().size());

		RealValueType element = qm.createRealValue(result);
		relations link = weaving.createRelationRL(systemMsa, element);
		label.getReferTo().add(link);
		value.setValueType(element);
		return label;
	}

}
