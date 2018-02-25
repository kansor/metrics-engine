package it.univaq.msa.pwrtool.business.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.Query;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import QualityMetamodel.AggregatedValue;
import QualityMetamodel.EnumerationItem;
import QualityMetamodel.EnumerationMetric;
import QualityMetamodel.MetricProvider;
import QualityMetamodel.QualityMetamodelPackage;
import QualityMetamodel.QualityModel;
import QualityMetamodel.SingleValue;
import QualityMetamodel.Value;
import QualityMetamodel.ValueType;
import it.univaq.msa.pwrtool.business.EvaluationEngine;
import it.univaq.msa.pwrtool.business.GitService;
import it.univaq.msa.pwrtool.business.ModelinkService;
import it.univaq.msa.pwrtool.business.OclService;
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.MSAService;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.msaQualityRelationships;
import mqr.relationGroup;
import mqr.relations;

@Service
public class EvaluationEngineImpl implements EvaluationEngine {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WeavingMMService weavingService;

	@Autowired
	private QMService qm;

	@Autowired
	private MSAService msa;

	@Autowired
	GitService gs;

	@Autowired
	ModelinkService ms;
	
	@Autowired
	OclService os;

	private Map<String, MetricCalculator> metricCalculator;

	@Autowired
	public void setMetricCatalogs(Map<String, MetricCalculator> metricCatalogs) {
		this.metricCalculator = metricCatalogs;
	}

	@Override
	public msaQualityRelationships evaluateQuality() {
		QualityModel qualityModel = qm.getQualityModel();

		logger.info("inside the engine");
		for (Value value : qualityModel.getQualityValues()) {

			if (value instanceof SingleValue) {

				MetricProvider mp = ((SingleValue) value).getMeasuredBy();
				logger.info(mp.getName());
				MetricCalculator mc = metricCalculator.get(mp.getName());

				logger.info("start the evaluation list type name : " + value.getVarName());
				relationGroup label = mc.calculate(msa.getMicroserviceModel(), value);
				weavingService.addRelationGroup(label);

			} else if (value instanceof AggregatedValue) {
				AggregatedValue av = (AggregatedValue) value;

				String body = av.getCalculatedBy().getBody();
				if (av.getValueType() instanceof EnumerationMetric){
					
					relationGroup label= null;
					try {
//						
						 label = os.evaluateOCL(av); 
					} catch (ParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    weavingService.addRelationGroup(label);
				}

			}
		}
		qm.saveQM();
		weavingService.saveWeaving();

		// try {
		// gs.deleteRepository();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		ms.finalizeModelink("src/main/model/acmeair2.microservicesarchitecture","src/main/model/weaving.mqr","src/main/model/qmevaluated.qualitymetamodel");
		logger.info("end");
		return weavingService.getWeavingModel();
	}

	

}
