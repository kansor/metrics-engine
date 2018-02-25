package it.univaq.msa.pwrtool.business;

import org.eclipse.ocl.ParserException;

import QualityMetamodel.AggregatedValue;
import QualityMetamodel.EnumerationItem;
import QualityMetamodel.QualityModel;
import QualityMetamodel.ValueType;
import mqr.relationGroup;

public interface OclService {


	relationGroup evaluateOCL(AggregatedValue av) throws ParserException;

}
