package it.univaq.msa.pwrtool.business.mqr;

import MicroservicesArchitecture.Element;
import QualityMetamodel.ValueType;
import mqr.msaQualityRelationships;
import mqr.relationGroup;
import mqr.relations;

public interface WeavingMMService {

	msaQualityRelationships getWeavingModel();

	void saveWeaving();

	relationGroup createRelationGroup(String name);

	relations createRelation();

	void addRelationGroup(relationGroup label);

	relations createRelationRL(Element e, ValueType v);

}
