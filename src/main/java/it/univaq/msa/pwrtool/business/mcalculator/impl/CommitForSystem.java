package it.univaq.msa.pwrtool.business.mcalculator.impl;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MicroservicesArchitecture.Product;
import QualityMetamodel.IntegerValueType;
import QualityMetamodel.Value;
import it.univaq.msa.pwrtool.business.GitService;
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service("CommitForSystem")
public class CommitForSystem implements MetricCalculator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WeavingMMService weaving;

	@Autowired
	QMService qm;

	@Autowired
	GitService gs;

	@Override
	public relationGroup calculate(Product systemMsa, Value value) {
		relationGroup label = weaving.createRelationGroup(value.getVarName());
		logger.info("CommitForSystem execution");
		int val = 0;
		relations link = weaving.createRelation();

		Iterable<RevCommit> temp = null;
		try {
			temp = gs.getListOfCommit();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (temp != null) {
			for(RevCommit rv : temp){
				
				val++;
				
			}
		}
		
		IntegerValueType element = qm.createIntValue(val);
		link.setMsaElement(systemMsa);
		link.setQualityElement(element);
		label.getReferTo().add(link);
		value.setValueType(element);

		return label;
	}

}
