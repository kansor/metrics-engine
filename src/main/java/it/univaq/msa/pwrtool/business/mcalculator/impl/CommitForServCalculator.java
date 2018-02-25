package it.univaq.msa.pwrtool.business.mcalculator.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MicroservicesArchitecture.MicroService;
import MicroservicesArchitecture.Product;
import QualityMetamodel.IntegerValueType;
import QualityMetamodel.ListValue;
import QualityMetamodel.Value;
import it.univaq.msa.pwrtool.business.GitService;
import it.univaq.msa.pwrtool.business.impl.LogFollowCommand;
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service("CommitForServCalculator")
public class CommitForServCalculator implements MetricCalculator {

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
		logger.info("CommitForServCalculator execution");
		ListValue lv = qm.createListValue();

		Git git = gs.getGit();
		File f = git.getRepository().getWorkTree();

		for (MicroService type : systemMsa.getComposedBy()) {

			relations link = weaving.createRelation();
			IntegerValueType element;
			int val = 0;
			for (File t : f.listFiles()) {

				if (t.isDirectory()) {
					logger.info("nome directory: "+t.getName());
					logger.info("nome microservice:"+type.getName());
					logger.info(t.getName()+"||contain||"+type.getName());
					if (t.getName().contains(type.getName())) {
						List<RevCommit> temp= new ArrayList<RevCommit>();
						
						
						try {
							logger.info("prima di log follow");
							LogFollowCommand lgc = new LogFollowCommand(git.getRepository(), t.getName());
							temp = lgc.call();
							logger.info("lunghezza out"+String.valueOf(temp.size()));
						} catch (NoHeadException e) {
							e.printStackTrace();
						} catch (GitAPIException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
//						
						
						if(temp!=null){
							
							
//								logger.info(rv.getName());
//								logger.info(rv.getFullMessage());
								val= temp.size();
								logger.info("value of size commit in a path"+String.valueOf(val));
								
							
							
						}

					}

				}

			}

			element = qm.createIntValue(val);

			link.setMsaElement(type);
			link.setQualityElement(element);
			label.getReferTo().add(link);
			lv.getElements().add(element);
		}
		value.setValueType(lv);
		return label;
	}

}
