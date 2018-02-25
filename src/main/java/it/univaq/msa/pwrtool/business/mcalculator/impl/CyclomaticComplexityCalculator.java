package it.univaq.msa.pwrtool.business.mcalculator.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.jgit.api.Git;
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
import it.univaq.msa.pwrtool.business.mcalculator.MetricCalculator;
import it.univaq.msa.pwrtool.business.mqr.QMService;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.relationGroup;
import mqr.relations;

@Service("CyclomaticComplexityCalculator")
public class CyclomaticComplexityCalculator implements MetricCalculator {
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
		logger.info("CyclomaticComplexityCalculator execution");
		ListValue lv = qm.createListValue();
		Git git = gs.getGit();
		File f = git.getRepository().getWorkTree();
		for (MicroService type : systemMsa.getComposedBy()) {
			int val = 0;
			relations link = weaving.createRelation();
			for (File t : f.listFiles()) {
				if (t.isDirectory()) {
					logger.info("nome directory: " + t.getName());
					logger.info("nome microservice:" + type.getName());
					logger.info(t.getName() + "||contain||" + type.getName());
					if (t.getName().contains(type.getName())) {
						try {
							StringBuffer output = new StringBuffer();
							ProcessBuilder ps = new ProcessBuilder(
									new String[] { "CMD", "/C", "cr", "-f", "json", "-e", t.getAbsolutePath() });
							ps.redirectErrorStream(true);
							Process pr = ps.start();
							JsonReader rdr = Json.createReader(pr.getInputStream());
							JsonObject obj = rdr.readObject();
							JsonArray results = obj.getJsonArray("reports");
							for (JsonObject result : results.getValuesAs(JsonObject.class)) {
								JsonObject j = result.getJsonObject("aggregate");
//								logger.info( String.valueOf(j.getInt("cyclomatic")));
//								logger.info(result.toString());
								val = j.getInt("cyclomatic");
							}
							pr.waitFor();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			IntegerValueType element = qm.createIntValue(val);
			link.setMsaElement(type);
			link.setQualityElement(element);
			label.getReferTo().add(link);
			lv.getElements().add(element);
		}
		return label;
	}
}
