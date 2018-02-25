package it.univaq.msa.pwrtool.business.impl;

import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import QualityMetamodel.AggregatedValue;
import QualityMetamodel.EnumerationItem;
import QualityMetamodel.EnumerationMetric;
import QualityMetamodel.QualityMetamodelPackage;
import it.univaq.msa.pwrtool.business.OclService;
import it.univaq.msa.pwrtool.business.mqr.MSAService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service
public class OclServiceImpl implements OclService {

	@Autowired
	WeavingMMService ws;

	@Autowired
	MSAService msa;

	@Override
	public relationGroup evaluateOCL(AggregatedValue av) throws ParserException {
		relationGroup label = ws.createRelationGroup(av.getVarName());
		OCL<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, Constraint, EClass, EObject> ocl;
		OCLHelper<EClassifier, ?, ?, Constraint> helper;

		// INSTANCIATE OCL
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		// INSTANCIATE NEW HELPER FROM OCLEXPRESSION
		helper = ocl.createOCLHelper();
		// SET HELPER CONTEXT
		helper.setContext(QualityMetamodelPackage.eINSTANCE.getAggregatedValue());

		// CREATE OCLEXPRESSION
		OCLExpression<EClassifier> expression = helper.createQuery(av.getCalculatedBy().getBody());
		// CREATE QUERY FROM OCLEXPRESSION
		// CREATE QUERY FROM OCLEXPRESSION
		Query<EClassifier, EClass, EObject> query = ocl.createQuery(expression);

		// EVALUATE OCL
		List<EnumerationItem> success = (List<EnumerationItem>) query.evaluate(av);
		for (EnumerationItem ei : success) {
			System.out.println(ei.getName());

		}
		EnumerationMetric em = (EnumerationMetric) av.getValueType();
		em.setValue(success.get(0));
		av.setValueType(em);
		relations rel = ws.createRelationRL(msa.getMicroserviceModel(), av.getValueType());
		label.getReferTo().add(rel);
		return label;
	}

}
