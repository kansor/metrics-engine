package it.univaq.msa.pwrtool.business.mqr.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import MicroservicesArchitecture.MicroservicesArchitectureFactory;
import MicroservicesArchitecture.MicroservicesArchitecturePackage;
import MicroservicesArchitecture.Product;
import it.univaq.msa.pwrtool.business.mqr.MSAService;

@Service
public class MSAServiceImpl implements MSAService {
	
	@Value("${msapath}")
	private String msaPath;
	
	private Product msaModel;
	
	
	@PostConstruct
	public void initializeMsa(){
		MicroservicesArchitecturePackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("microservicesarchitecture", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createFileURI(msaPath), true);
		this.msaModel = (Product) resource.getContents().get(0);
		
		
		
	}
	public MicroservicesArchitectureFactory getFactory(){
		return MicroservicesArchitectureFactory.eINSTANCE;
		
	}

	public Product getMicroserviceModel() {
		return this.msaModel;
	}

}
