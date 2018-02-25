package it.univaq.msa.pwrtool.business.mcalculator.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MicroservicesArchitecture.MicroService;
import MicroservicesArchitecture.Product;
import QualityMetamodel.BooleanValueType;
import QualityMetamodel.ListValue;
import QualityMetamodel.Value;
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service("IsStatefulCalculator")
public class IsStatefulCalculator implements MetricCalculator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WeavingMMService weaving;

	@Autowired
	QMService qm;

	@Override
	public relationGroup calculate(Product systemMsa, Value value) {

		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("IsStatefulCalculator execution");
		ListValue lv = qm.createListValue();

		for (MicroService type : systemMsa.getComposedBy()) {

			relations link = weaving.createRelation();
			BooleanValueType element;
			char ch = 'f';
//			do{
//				try {
//					logger.info("is " + type.getName() + " stateful? (t for true, f"
//							+" for false)");
//					ch = (char) System.in.read();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}while(ch != 't' & ch!= 'f');
			
			// String s =null;
			// Scanner sc = new Scanner(System.in);
			// do{
			//
			// logger.info("is " + type.getName() + " stateful? (t for true, f
			// for false)");
			//
			//
			// }while (!s.matches("[t|T|f|F]"));
			//
			// sc.close();
			 boolean t = false;
			 if (ch == 't') t = true;
			element = qm.createBooleanValueType(t);
			link.setMsaElement(type);
			link.setQualityElement(element);
			label.getReferTo().add(link);
			lv.getElements().add(element);
		}
		value.setValueType(lv);
		return label;
	}

}
