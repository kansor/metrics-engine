package it.univaq.msa.pwrtool.business.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import java.io.File;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import it.univaq.msa.pwrtool.business.ModelinkService;

@Service
public class ModelinkServiceImpl implements ModelinkService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void finalizeModelink(String left, String center, String right) {
		try {

			/*
			 * <?xml version="1.0" encoding="UTF-8"?> <modelink threeWay="true"
			 * forceExeedL="false" forceExeedM="false" forceExeedR="false">
			 * <model
			 * path="\msaWquality\workbench\acmeair.microservicesarchitecture"
			 * position="LEFT" /> <model
			 * path="\msaWquality\workbench\weaving.weavingmodelmm"
			 * position="MIDDLE" /> <model
			 * path="\msaWquality\workbench\qmevaluated.qualitymetamodel"
			 * position="RIGHT" /> </modelink>
			 */

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("modelink");
			// threeway attr
			Attr attr = doc.createAttribute("threeWay");
			attr.setValue("true");
			rootElement.setAttributeNode(attr);
			// forceExeedL
			Attr attrforceExeedL = doc.createAttribute("forceExeedL");
			attrforceExeedL.setValue("false");
			rootElement.setAttributeNode(attrforceExeedL);
			// forceExeedM
			Attr forceExeedM = doc.createAttribute("forceExeedM");
			forceExeedM.setValue("false");
			rootElement.setAttributeNode(forceExeedM);
			// forceExeedR
			Attr forceExeedR = doc.createAttribute("forceExeedR");
			forceExeedR.setValue("false");
			rootElement.setAttributeNode(forceExeedR);

			doc.appendChild(rootElement);

			// element SX
			Element elsx = doc.createElement("model");

			Attr pathl = doc.createAttribute("path");
			pathl.setValue(left);
			elsx.setAttributeNode(pathl);

			Attr positionl = doc.createAttribute("position");
			positionl.setValue("LEFT");
			elsx.setAttributeNode(positionl);

			rootElement.appendChild(elsx);

			// element M

			Element elc = doc.createElement("model");

			Attr pathc = doc.createAttribute("path");
			pathc.setValue(center);
			elc.setAttributeNode(pathc);

			Attr positionc = doc.createAttribute("position");
			positionc.setValue("CENTER");
			elc.setAttributeNode(positionc);

			rootElement.appendChild(elc);
			
			//element DX 
			
			Element eldx = doc.createElement("model");

			Attr pathd = doc.createAttribute("path");
			pathd.setValue(right);
			eldx.setAttributeNode(pathd);

			Attr positiond = doc.createAttribute("position");
			positiond.setValue("RIGHT");
			eldx.setAttributeNode(positiond);

			rootElement.appendChild(eldx);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("src/main/model/result.modelink"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			logger.info("modelink saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}


