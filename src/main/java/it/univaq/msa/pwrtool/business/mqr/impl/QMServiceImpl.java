package it.univaq.msa.pwrtool.business.mqr.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import QualityMetamodel.BooleanValueType;
import QualityMetamodel.IntegerValueType;
import QualityMetamodel.ListValue;
import QualityMetamodel.QualityMetamodelFactory;
import QualityMetamodel.QualityMetamodelPackage;
import QualityMetamodel.QualityModel;
import QualityMetamodel.RealValueType;
import QualityMetamodel.TextValueType;
import it.univaq.msa.pwrtool.business.mqr.QMService;

@Service
public class QMServiceImpl implements QMService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${qmpath}")
	private String qmPath;
	
	private QualityModel qualityModel;

	@PostConstruct
	private void qualityLoader(){
		logger.info("inside the quality post construct");
		QualityMetamodelPackage.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("qualitymetamodel", new XMIResourceFactoryImpl());
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(URI.createFileURI(qmPath), true);
		this.qualityModel = (QualityModel) resource.getContents().get(0);
	}
	
	public QualityModel getQualityModel() {
		return this.qualityModel;
	}
	
	public QualityMetamodelFactory getFactory(){
		return QualityMetamodelFactory.eINSTANCE;
	}
	
	@Override
	public void saveQM() {
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> m = reg.getExtensionToFactoryMap();
        m.put("qualitymetamodel", new XMIResourceFactoryImpl());
        // Obtain a new resource set
        ResourceSet resSet = new ResourceSetImpl();

        // create a resource
        Resource resource = resSet.createResource(URI.createURI("src/main/model/qmevaluated.qualitymetamodel"));
        
        resource.getContents().add(this.qualityModel);
        
        try {
            resource.save(Collections.EMPTY_MAP);
    } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
    }
		
	}

	@Override
	public TextValueType createTextValueType(String s) {
		QualityMetamodelFactory factory = getFactory();
		TextValueType result = factory.createTextValueType();
		result.setValue(s);
		this.qualityModel.getQualityTypes().add(result);
		return result;
	}

	@Override
	public ListValue createListValue() {
		QualityMetamodelFactory factory = getFactory();
		ListValue result = factory.createListValue();
		this.qualityModel.getQualityTypes().add(result);
		return result;
	}

	@Override
	public IntegerValueType createIntegerValueType(Integer value) {
		QualityMetamodelFactory factory = getFactory();
		IntegerValueType result = factory.createIntegerValueType();
		result.setValue(value);
		this.qualityModel.getQualityTypes().add(result);
		return result;
	}

	@Override
	public IntegerValueType createIntValue(int x) {
		QualityMetamodelFactory factory = getFactory();
		IntegerValueType result = factory.createIntegerValueType();
		result.setValue(new Integer(x));
		this.qualityModel.getQualityTypes().add(result);
		
		return result;
	}

	@Override
	public BooleanValueType createBooleanValueType(boolean b) {
		QualityMetamodelFactory factory = getFactory();
		BooleanValueType bvt = factory.createBooleanValueType();
		bvt.setValue(b);
		this.qualityModel.getQualityTypes().add(bvt);
		return bvt;
	}

	@Override
	public RealValueType createRealValue(double value) {
		QualityMetamodelFactory factory = getFactory();
		RealValueType result = factory.createRealValueType();
		result.setValue( value);
		this.qualityModel.getQualityTypes().add(result);
		return result;
	}

	
	
}
