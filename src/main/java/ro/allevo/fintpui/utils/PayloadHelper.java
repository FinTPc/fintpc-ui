package ro.allevo.fintpui.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ro.allevo.fintpui.model.TemplateConfigDetailed;

public class PayloadHelper {
	
	private static final String XML_FRIENDLY_TAGS = "XMLFriendlyTags.xlsx";
	
	public static void friendlyPayload(Document payload) {
		//load xlsx
		String path = PayloadHelper.class.getClassLoader().getResource(XML_FRIENDLY_TAGS).getPath();
		
		Map<String, String> friendlyTags = new HashMap<String, String>();
		
		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			Sheet firstSheet = workbook.getSheetAt(0);
			
			DataFormatter dataFormatter = new DataFormatter();
			
			for (Row row : firstSheet) {
				String firstCell = dataFormatter.formatCellValue(row.getCell(0));
				String secondCell = dataFormatter.formatCellValue(row.getCell(1));
				
				friendlyTags.put(secondCell, firstCell);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//rename nodes
		NodeList nodes = payload.getElementsByTagName("*");
		
		for (int i = 0; i < nodes.getLength(); i++) {
	        Node node = nodes.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	String nodeName = node.getNodeName();
	        	
	        	//split by case
	        	String[] tokens = StringUtils.splitByCharacterTypeCamelCase(nodeName);
	        	
	        	String newNodeName = "";
	        	
	        	for (String token : tokens)
	        		newNodeName += friendlyTags.getOrDefault(token, token);
	        	
	        	 payload.renameNode(node, node.getNamespaceURI(), newNodeName);
	        }
	    }
	}
	
	public static void editPayload(Document payload, TemplateConfigDetailed[] configOptions){
		
		List<TemplateConfigDetailed> templatesConfig = new ArrayList<TemplateConfigDetailed>(Arrays.asList(configOptions));
		
		//rename nodes
		NodeList nodes = payload.getElementsByTagName("*");
		
		for (int i = 0; i < nodes.getLength(); i++) {
	        Node node = nodes.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	String xPath = getXPath(node);
	        	((Element)node).setAttribute("xpath", xPath);
	        	String id = "-1";
	        	Optional<TemplateConfigDetailed> templateConfig = templatesConfig.stream().filter(o -> o.getFieldxpath().equals(xPath)).findFirst();
	        	if(templateConfig.isPresent() && templateConfig.get().getEditable()){
	        		id = templateConfig.get().getTxtemplatesconfigoption().getDatasource();
	        	}
	        	((Element)node).setAttribute("optionId", String.valueOf(id));
	        }
	    }
		friendlyPayload(payload);
	}
	
	public static String savePayload(Document payload, Map<String, String> xPaths){
		
		//rename nodes
		NodeList nodes = payload.getElementsByTagName("*");
		for (int i = 0; i < nodes.getLength(); i++) {
	        Node node = nodes.item(i);
	        if (node.getNodeType() == Node.ELEMENT_NODE) {
	        	String xPath = getXPath(node);
	        	if(xPaths.containsKey(xPath)) {
	        		node.setTextContent(xPaths.get(xPath));
	        	}
	        }
	    }
		try {
			return getStringFromDoc(payload);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static void createElement( Document doc, Node parent, String[] segments, int index, String value) throws Exception {
   	 String xpath = String.join("/", Arrays.copyOfRange(segments, 0, index) );
   	 Node node = getElementByXpath(doc, xpath);
   	 if( (node == null) || segments[index-1].startsWith("@") ){
   		 if(segments[index-1].startsWith("@")){
   			 ((Element)parent).setAttribute(StringUtils.substringAfter(segments[index-1], "@"), value);
   		 }else{
   			 Element itemElement = doc.createElement( segments[index-1] );
       		 if(index == segments.length) {
       			 Text newContent = doc.createTextNode(value); 
       			 itemElement.appendChild(newContent);
       		 }
   	 		 parent.appendChild(itemElement);
   	 		 parent = itemElement;
   		 }
   	 }else{
   		 parent = node;
   	 }
   	 if(index < segments.length)
   		 createElement(doc, parent, segments, ++index, value); 
   }
   
   private static Node getElementByXpath(Document document, String xpathExpression) throws Exception {
       XPathFactory xpathFactory = XPathFactory.newInstance();
       XPath xpath = xpathFactory.newXPath();
       try {
           XPathExpression expr = xpath.compile(xpathExpression);
           NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
           if(nodes != null  && nodes.getLength() > 0) { 
           	 return nodes.item(0);
           }
       } catch (XPathExpressionException e) {
           e.printStackTrace();
       }
       return null;
   }
	
	public static String getStringFromDoc(Document doc) throws TransformerException {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(domSource, result);
		writer.flush();
		return writer.toString();
	}
	
	private static String getXPath(Node node)
	{
	    Node parent = node.getParentNode();
	    if (parent == null)
	    {
	        return "";
	    }
	    return getXPath(parent) + "/" + node.getNodeName();
	}

	public static Document parsePayload(String message) throws ParserConfigurationException, SAXException, IOException  {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        Document doc = null;
        
        builder = factory.newDocumentBuilder();  
        doc = builder.parse(new InputSource(new StringReader(message)));
        
        return doc;
	}
}
