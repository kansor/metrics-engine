package it.univaq.msa.pwrtool.business.mqr;

import QualityMetamodel.BooleanValueType;
import QualityMetamodel.IntegerValueType;
import QualityMetamodel.ListValue;
import QualityMetamodel.QualityModel;
import QualityMetamodel.RealValueType;
import QualityMetamodel.TextValueType;

public interface QMService {

	QualityModel getQualityModel();

	TextValueType createTextValueType(String s);

	ListValue createListValue();

	IntegerValueType createIntegerValueType(Integer value);

	IntegerValueType createIntValue(int size);
	
	BooleanValueType createBooleanValueType(boolean b);
	
	void saveQM();


	RealValueType createRealValue(double result);

}
