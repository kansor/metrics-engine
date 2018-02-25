package it.univaq.msa.pwrtool.business.mqr.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import MicroservicesArchitecture.Element;
import QualityMetamodel.ValueType;
import it.univaq.msa.pwrtool.business.mqr.WeavingMMService;
import mqr.MqrFactory;
import mqr.MqrPackage;
import mqr.msaQualityRelationships;
import mqr.relationGroup;
import mqr.relations;

@Service
public class WeavingMMServiceImpl implements WeavingMMService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private msaQualityRelationships weaving;

	@PostConstruct
	public void initializeWeavingModel() {
		MqrPackage.eINSTANCE.eClass();
		MqrFactory factory = this.getFactory();
		msaQualityRelationships myWeaving = factory.createmsaQualityRelationships();
		myWeaving.setMqrName("weaving model instance");
		this.weaving = myWeaving;
		logger.info("weaving model initialized");
	}

	public MqrFactory getFactory() {
		return MqrFactory.eINSTANCE;
	}

	@Override
	public msaQualityRelationships getWeavingModel() {
		return this.weaving;
	}

	@Override
	public void saveWeaving() {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("mqr", new XMIResourceFactoryImpl());
		
		// Obtain a new resource set
		ResourceSet resSet = new ResourceSetImpl();
		
		
		// create a resource
		Resource resource = resSet.createResource(URI.createFileURI("src/main/model/weaving.mqr"));
		
		resource.getContents().add(this.weaving);
		
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public relationGroup createRelationGroup(String name) {
		MqrFactory factory = getFactory();
		relationGroup label = factory.createrelationGroup();
		label.setMqrName(name);
		return label;
	}

	@Override
	public relations createRelation() {
		MqrFactory factory = getFactory();
		relations link = factory.createrelations();
		return link;
	}

	@Override
	public void addRelationGroup(relationGroup label) {
		this.weaving.getContain().add(label);

	}

	@Override
	public relations createRelationRL(Element e, ValueType v) {
		MqrFactory factory = getFactory();
		relations link = factory.createrelations();
		link.setMsaElement(e);
		link.setQualityElement(v);
		return link;
	}

}
