package it.univaq.msa.pwrtool.business.mcalculator;

import MicroservicesArchitecture.Product;
import QualityMetamodel.Value;
import mqr.relationGroup;

public interface MetricCalculator {

	relationGroup calculate(Product systemMsa, Value value);
	
}
