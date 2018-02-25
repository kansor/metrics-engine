package it.univaq.msa.pwrtool.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.univaq.msa.pwrtool.business.EvaluationEngine;
import it.univaq.msa.pwrtool.business.mqr.MSAService;
import mqr.msaQualityRelationships;

@Controller
public class WaveController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MSAService msal;

	@Autowired
	private EvaluationEngine ee;

	@RequestMapping("/whatif")
	@ResponseBody
	public String whatif() {

		logger.warn("--------microservice-------");
		MicroservicesArchitecture.Product systems = msal.getMicroserviceModel();
		logger.info(systems.getComposedBy().get(0).getHost());
		msaQualityRelationships weaving = ee.evaluateQuality();

		return "done";
	}

}
