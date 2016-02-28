package yetchina.xml;


/*
 * @(#)B2B_EDI_Validator.java        1.0 2002/05/21
 *
 * Copyright (c) 2002 Legendary Systems.
 * 300, Frank H. Ogawa, Oakland, CA, 94612, U.S.A.
 * (510) 587-0900
 *
 * All rights reserved.  This software is the confidential and
 * proprietary information of Legendary Systems. and is protected by
 * copyright laws and regulations. Do not disclose or use this
 * software except as authorized in writing by Legendary, Inc.
 */
import java.util.*;
import java.io.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
//import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * Description:  This Class takes the UEX File with a reference to its Schema
 * embedded in it. The UEXSchemaParser Method will parse that UEX file for its
 * Well formedness and Validness with respect to the underlying Schema.
 *
 * @author     Sreeni Yetchina
 * @version    May 21, 2002
 */
public class XMLProcessHelper {
    String    conditionalElementsGxmlPath = null;
    String    uexAsString = null;
    String    UEXSchemaFilePath = null;
    Document  UEXDOMDoc = null;
    DOMParser UEXParser = null;

    /**
     * Constructor of the B2B_EDI_Validator class which takes UEX file as String and ConditionalXML File Location and THe gXMl Schema Location
     * @param uexString THe UEX file in the form of valid Java String
     * @param conditionalElements_gXMLPath THe ConditionalElements XML file Path
     * @param UEXSchemaPath THe gXML Schema path for the underlaying UEX standard
     */
    public XMLProcessHelper(String uexString, String conditionalElements_gXMLPath, String UEXSchemaPath) throws IOException {
        String inString = uexString;
        //String searchString = " Val=''/";
        //String replaceString = "/";
        //uexAsString = searchReplace(inString,searchString,replaceString);
        uexAsString = uexString;

	conditionalElementsGxmlPath = conditionalElements_gXMLPath;
	UEXSchemaFilePath = UEXSchemaPath;

	try {
	    UEXParser = new DOMParser();

	    //UEXParser.setFeature("http://xml.org/sax/features/namespaces", true);
	    //UEXParser.setFeature("http://xml.org/sax/features/validation", true);
	    //UEXParser.setFeature("http://apache.org/xml/features/validation/schema", true);
	    //UEXParser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
	    //UEXParser.setErrorHandler(new MyErrorHandler());
	    //UEXParser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", UEXSchemaFilePath);

	    StringReader UEXStringReader = new StringReader(uexAsString);
	    InputSource  UEXStringInputSource =  new InputSource(UEXStringReader);
	    long StartTime = System.currentTimeMillis();

	    System.out.println("Validation Started......");
	    UEXParser.parse(UEXStringInputSource);
	    System.out.println("Time taken for only non-conditional Validation......" + (System.currentTimeMillis() - StartTime));

	    UEXDOMDoc = UEXParser.getDocument();
	} catch (SAXNotRecognizedException e) {
	    Log("Debug: " + e.getMessage());
	    System.err.println(e);
	} catch (SAXNotSupportedException e) {
	    Log("Debug: " + e.getMessage());
	    System.err.println(e);
	} catch (Exception e) {
	    Log("Debug: " + e.getMessage());
	    System.out.println("Error Occured Sreeni, Check it out ");
	}
    }



    /**
     * This Method initiates the Conditional Validation
     */
    public void validateConditions() {
	System.out.println("\nConditional XML Processing Startarted...... ");
	try {
	    Document conditionalElements_gXML = null;
	    try {
		DOMParser conditionalXmlParser = new DOMParser();
		conditionalXmlParser.parse(conditionalElementsGxmlPath);
		conditionalElements_gXML = conditionalXmlParser.getDocument();
	    } catch (Exception e) {
		Log(e.getMessage());
	    }
	    processConditionalElementsXml(conditionalElements_gXML);
	} catch (Exception ex) {
	    Log(ex.getMessage());
	}
    }

    /**
     * This method initates the traversal of the Conditonal XML Document and Selects
     * the Header/Detail and Summery Nodes
     *
     * @param conditionalElements_gXML The DOM Tree of the Conditional XML Document
     */
    public void processConditionalElementsXml(Document conditionalElements_gXML) {
	NodeList conditionalXMLNodeList =
	    conditionalElements_gXML.getElementsByTagName("UEX");
	if (conditionalXMLNodeList == null) {
	    conditionalXMLNodeList = conditionalElements_gXML.getElementsByTagName("UEX2");
	}
	for (int i = 0; i < conditionalXMLNodeList.getLength(); i++) {
	    Node conditionalXMLUexNode = conditionalXMLNodeList.item(i);
	    if (conditionalXMLUexNode.hasChildNodes()) {
		NodeList conditionalXMLHeaderNodeList = conditionalXMLUexNode.getChildNodes();
		for (int i2 = 0; i2 < conditionalXMLHeaderNodeList.getLength(); i2++) {
		    Node conditionalXMLHeaderNode = conditionalXMLHeaderNodeList.item(i2);
		    if (conditionalXMLHeaderNode != null && conditionalXMLHeaderNode.getNodeType() != conditionalXMLHeaderNode.TEXT_NODE) {
			processConditionalXmlHeaderNode(conditionalXMLHeaderNode);
		    }
		}
	    }
	}
    }

    /**
     * This method traverses thru the ConditionalXML DOM Tree with the node above the conditions node
     * @param conditionalXMLHeaderNode The Node above the Condition node it may be Header/Detail/Summery or Loop node
     */
    public void processConditionalXmlHeaderNode(Node conditionalXMLHeaderNode) {
	String   conditionString = null;
	String[] elementString = null;
	String[] brokenValue = null;
	String[] concatSegment = null;
	String   conditionValue = null;
	NodeList conditionalXMLSegmentsList = conditionalXMLHeaderNode.getChildNodes();

	for (int j = 0; j < conditionalXMLSegmentsList.getLength(); j++) {
	    Node conditionalSegment = conditionalXMLSegmentsList.item(j);

	    if (conditionalSegment != null && conditionalSegment.getNodeType() != conditionalSegment.TEXT_NODE && conditionalSegment.getNodeType()  == conditionalSegment.ELEMENT_NODE) {
		if (!conditionalSegment.getNodeName().startsWith("Loop")) {
		    processConditionalXmlSegmentNode(conditionalSegment,  conditionalXMLHeaderNode);
		} else {
		    processConditionalXmlHeaderNode(conditionalSegment);
		}
	    }
	}
    }

    /**
     * This method Prcesses the Conditional XML Each Segment
     * @param conditionalSegment
     * @param conditionalXMLHeaderNode
     */
    public void processConditionalXmlSegmentNode(Node conditionalSegment, Node conditionalXMLHeaderNode) {
	String returnString = "";
	Node   conditionTag = null;
	if (conditionalSegment != null	&& conditionalSegment.getNodeName() != "#text") {
	    NodeList elementNodeList = conditionalSegment.getChildNodes();
	    for (int innerLoop = 0; innerLoop < elementNodeList.getLength(); innerLoop++) {
		conditionTag = elementNodeList.item(innerLoop);
		if ((conditionTag != null) && conditionTag.getNodeName() != "#text" && conditionTag.getNodeName().equals("Condition")) {
		    Node conditionsString = conditionTag.getFirstChild();
		    if (conditionsString.getNodeValue() != null && conditionsString.getNodeValue() != "#text" && conditionsString.getNodeValue() != "") {
			processConditionalXmlSegmentConditionString(conditionsString.getNodeValue(), conditionalSegment, conditionalXMLHeaderNode);
		    }
		}
	    }    // end inner loop
 	 }
    }

    /**
     * This triggers the traversal of both Input/Output xml
     * @param conditionalValue
     * @param conditionalSegment
     * @param conditionalXMLHeaderNode
     */
    public void processConditionalXmlSegmentConditionString(String conditionalValue,Node conditionalSegment, Node conditionalXMLHeaderNode) {
	String   conditionString =   null;    // To hold the Condition Letter of each condition on this segment
	String[] conditionStringArray =   null;    // To Keep the Condition Letters of all the segment's conditions
	String[] elementString =   null;    // The Broken pairs of lement IDs after the condition letter in each condition of this segment
	String[] brokenValue;		  // List of Conditions on this Segment
	String[] concatSegment = null;    // The Element List in each Segment

	brokenValue = getBrokenValues(conditionalValue);
	conditionStringArray = new String[brokenValue.length];

	for (int j = 0; j < brokenValue.length; j++) {
	    if (brokenValue[j] == null) {
		break;
	    } else {
		conditionString = getConditionalChar(brokenValue[j]);
		conditionStringArray[j] = conditionString;
		elementString = getElementValues(brokenValue[j]);
		concatSegment = new String[elementString.length];

		for (int i = 0; i < elementString.length; i++) {
		    if (elementString == null) {
			break;
		    } else {
			concatSegment[i] =  conditionalSegment.getNodeName().concat(elementString[i]);
		    }
		}
		processUEXFile(conditionStringArray[j], concatSegment,  conditionalXMLHeaderNode, conditionalSegment);
	    }
	}
    }

    /**
     * This method initiates the validation against the Output xml
     * @param conditionalString(eg P,R,C etc..)
     * @param concatArray(eg L1101,G6209 etc..)
     * @param ConditionalXmlHeaderNode(eg L1101,G6209 etc..)
     * @param ConditionalElementSegmentNode(eg L1101,G6209 etc..)
     */
    public void processUEXFile(String conditionalString, String[] concatArray, Node ConditionalXmlHeaderNode, Node ConditionalElementSegmentNode) {
	try {

	    NodeList uexNodeList =    UEXDOMDoc.getElementsByTagName("UEX");
	    if (uexNodeList.getLength() == 0) {
	      uexNodeList = UEXDOMDoc.getElementsByTagName("UEX2");
	    }
	    //NodeList uexNodeList = UEXDOMDoc.getElementsByTagName("UEX2");
	    for (int i = 0; i < uexNodeList.getLength(); i++) {
		Node uexNode = uexNodeList.item(i);
		if (uexNode.hasChildNodes()) {
		    NodeList uexHeaderNodeList = uexNode.getChildNodes();
		    for (int i2 = 0; i2 < uexHeaderNodeList.getLength(); i2++) {
			Node uexHeaderNode = uexHeaderNodeList.item(i2);
			if (uexHeaderNode.getNodeType()	== uexHeaderNode.ELEMENT_NODE && uexHeaderNode.getNodeType()!= uexHeaderNode.TEXT_NODE) {
			    String uexHeaderNodeName =	uexHeaderNode.getNodeName();
			    if (uexHeaderNodeName == "Header" || uexHeaderNodeName == "Detail" || uexHeaderNodeName == "Summary") {
				processUexFileHeaderNode(uexHeaderNode,	 ConditionalXmlHeaderNode, ConditionalElementSegmentNode, conditionalString, concatArray);
			    }
			}
		    }
		}
	    }
	} catch (Exception e) {}
    }

    /**
     * This method traverses thru the UEX xml
     * @param uexHeaderNode
     * @param ConditionalXmlHeaderNode
     * @param ConditionalElementSegmentNode
     * @param conditionalString
     * @param concatArray
     */
    public void processUexFileHeaderNode(Node uexHeaderNode, Node ConditionalXmlHeaderNode, Node ConditionalElementSegmentNode, String conditionalString, String[] concatArray) {
	if (uexHeaderNode.hasChildNodes()) {
	    NodeList uexSegmentNodeList = uexHeaderNode.getChildNodes();
	    for (int j = 0; j < uexSegmentNodeList.getLength(); j++) {
		Node uexSegmentNode = uexSegmentNodeList.item(j);
		if (uexSegmentNode.getNodeType() == uexSegmentNode.ELEMENT_NODE && uexSegmentNode.getNodeName() != "#text" && uexSegmentNode.getNodeName() != null &&!uexSegmentNode.getNodeName().startsWith("Loop")) {
		    if (uexSegmentNode.getParentNode().getParentNode().getNodeName() == ConditionalElementSegmentNode.getParentNode().getParentNode().getNodeName() && uexSegmentNode.getParentNode().getNodeName() == ConditionalElementSegmentNode.getParentNode().getNodeName() && uexSegmentNode.getNodeName() == ConditionalElementSegmentNode.getNodeName()) {
			processUexFileSegmentNode(uexSegmentNode,
						  concatArray,
						  conditionalString,
						  ConditionalElementSegmentNode.getNodeName(),
						  ConditionalXmlHeaderNode);
		    }
		} else if (uexSegmentNode.getNodeName().startsWith("Loop")) {
		    processUexFileHeaderNode(uexSegmentNode,
					     ConditionalXmlHeaderNode,
					     ConditionalElementSegmentNode,
					     conditionalString, concatArray);
		}
	    }
	}
    }

    /**
     * This method gets the necessary childs of UEX file for Validation
     * @param elementNode
     * @param concatArray
     * @param conditionString
     * @param conditionalSegment
     * @param ConditionalXmlHeaderNode
     */
    public void processUexFileSegmentNode(Node elementNode,
					  String[] concatArray,
					  String conditionString,
					  String conditionalSegment,
					  Node ConditionalXmlHeaderNode) {
	String   returnString = "";
	Node     child = null;
	Node     grandChild = null;
	String[] childNodeArray = null;
	String   conditionNodeArrayString = null;

	System.out.println("\nThe UEX Element Node under consideration is..."+elementNode.getNodeName()+" "+conditionString);
	if (elementNode != null && elementNode.getNodeName() != "#text") {
	    NodeList children = elementNode.getChildNodes();
	    childNodeArray = new String[children.getLength()];
	    int p = 0;
	    for (int innerLoop = 0; innerLoop < children.getLength(); innerLoop++) {
		child = children.item(innerLoop);
		if ((child != null)&& (child.getNodeName() != null && child.getNodeName() != "#text")) {
		    if (child.getNodeName() != null && child.getNodeType() == child.ELEMENT_NODE && child.getNodeName() != "#text") {
			if (concatArray[0].startsWith(child.getNodeName().substring(0, 2)) && child.getNodeType() == child.ELEMENT_NODE) {
			    childNodeArray[p] = child.getNodeName();
			    p++;
			    if (p == children.getLength()) {

			      System.out.println("The Segment under Validation is "+elementNode.getNodeName()+" of "+elementNode.getParentNode().getNodeName()+" of "+elementNode.getParentNode().getParentNode().getNodeName());
			      System.out.print("The conditionNode Array From UEX file is   ");
			      for(int var1=0;var1<childNodeArray.length;var1++){
				System.out.print(childNodeArray[var1]+" ");
			      }
			      System.out.println("END");

			      System.out.print("The Concat Array From Conditional gXML is   ");
			      for(int var1=0;var1<concatArray.length;var1++){
				System.out.print(concatArray[var1]+" ");
			      }
			      System.out.println("END");
			      System.out.println("******************");


				boolean validity = checkConditions(conditionString, childNodeArray, concatArray);
				if (validity == false) {
				  String logMessage = "";
				  while (ConditionalXmlHeaderNode.getNodeType() != ConditionalXmlHeaderNode.DOCUMENT_NODE) {
				    logMessage = logMessage+"Inside ";
				    logMessage = logMessage+ConditionalXmlHeaderNode.getNodeName() + " ";
				    ConditionalXmlHeaderNode = ConditionalXmlHeaderNode.getParentNode();
				  }
				  Log(logMessage+"\n\n");
				  //Log("\n");
				} else {
				    innerLoop = children.getLength();
				}
				break;
			    } else {
				continue;
			    }
			}
		    }
		} else if (child.getNodeName() != "#text") {
		    ;
		}
	    }    // end inner loop
 	 }
    }

    /**
     * This method validates the Conditions present in the UEX against the X12 Specification
     * @param conditionString The Condition Character(like P, R, C etc) in the String format
     * @param conditionNodeArray
     * @param concatArray
     * @return true/false
     */
    public boolean checkConditions(String conditionString,
				   String[] conditionNodeArray,
				   String[] concatArray) {
	String[] conditionNewString = {"P", "R", "C", "E", "L", "Junk"};
	boolean  inValid = false;
	boolean  valid = true;
	boolean  validationFlag = true;

	if (conditionString.equals(conditionNewString[0])) {
	    int count = 0;
	    String errorElement = null;
	    if (concatArray.length > 1 && conditionNodeArray.length >= 1) {
		for (int i = 0; i < concatArray.length; i++) {
		    for (int j = 0; j < conditionNodeArray.length; j++) {
			if (concatArray[i].equals(conditionNodeArray[j])) {
			    count++;
			    errorElement = conditionNodeArray[j];
			    break;
			} else
			    continue;
		    }
		}

		if (count < concatArray.length && count != 0) {
		  Log("Document is Invalid at " + errorElement + " for the Condition" + conditionNewString[0]);
		    validationFlag = false;
		} else
		    validationFlag = true;
	    } else if (concatArray.length == 1) {
		Log("Invalid Condition gXml.There will not be any Single Condition");
		validationFlag = false;
	    }
	} else if (conditionString.equals(conditionNewString[1])) {
	    int count = 0;
	    if (concatArray.length > 1 && conditionNodeArray.length >= 1) {
		for (int i = 0; i < concatArray.length; i++) {
		    for (int j = 0; j < conditionNodeArray.length; j++) {
			if (concatArray[i].equals(conditionNodeArray[j])) {
			    count++;
			    //break;
			}
		    }
		}
		if (count == 0) {
		  Log("Document is Invalid at " +conditionNodeArray[1]+ " for the Condition " + conditionString);
		  validationFlag = false;
		} else
		    validationFlag = true;
	    } else if (concatArray.length == 1) {
		Log("Invalid Condition gXml.There will not be any Single Condition");
		validationFlag = false;
	    }

	} else if (conditionString.equals(conditionNewString[2])) {
	    int     count = 0;
	    boolean firstElementAvailable = false;

	    //System.out.println(conditionNodeArray[0]);
	    if (concatArray.length > 1 && conditionNodeArray.length >= 1) {
		for (int k = 0; k < conditionNodeArray.length; k++) {
		    if (concatArray[0].equals(conditionNodeArray[k])) {
			firstElementAvailable = true;
			//System.out.println("Matched for Condition C at "+conditionNodeArray[k]);
		    }
		}
	    if (firstElementAvailable == true) {
		for (int l = 1; l < (concatArray.length); l++) {
		    for (int m = 0; m < conditionNodeArray.length; m++) {
			if (concatArray[l].equals(conditionNodeArray[m]))
			    count++;
		    }
		}
		//System.out.println("The Count is "+count+" and concat array lenght is "+(concatArray.length-1));
		if (count != (concatArray.length-1)) {
		    Log("Document is Invalid at " + concatArray[0] + " for the Condition " + conditionString);
		    validationFlag = false;
		    //break;
		} else if (concatArray.length == 1) {
		    Log("Invalid Condition gXml.There will not be any Single Condition");
		    validationFlag = false;
		}
	    } else
	        validationFlag = true;
		//}
	  }
	} else if (conditionString.equals(conditionNewString[3])) {
	    int count = 0;
	    if (concatArray.length > 1 && conditionNodeArray.length >= 1) {
		for (int i = 0; i < concatArray.length; i++) {
		    for (int j = 0; j < conditionNodeArray.length; j++) {
			if (concatArray[i].equals(conditionNodeArray[j]))
			    count++;
		    }
		}
		if (count > 1) {
		  Log("Document is Invalid at " + conditionNodeArray[0]+ " for the Condition " + conditionString);
		  validationFlag = false;
		} else
		    validationFlag = true;
	    } else if (concatArray.length == 1) {
	      Log("Invalid Condition gXml.There will not be any Single Condition ");
	      validationFlag = false;
	    }
	} else if (conditionString.equals(conditionNewString[5])) {
            int count = 0;
	    if (concatArray.length > 1 && conditionNodeArray.length >= 1) {
		for (int i = 1; i < concatArray.length; i++) {
		    for (int j = 1; j < conditionNodeArray.length; j++) {
                        if (concatArray[i].equals(conditionNodeArray[j])) {
                          count++;
    		        //break;
			} else
			   continue;
		    }

		    if (count > 1) {
			break;
		    } else {
			continue;
		    }
		}

		if (concatArray[0].equals(conditionNodeArray[0]) && count > 1) {
		    for (int j = 0; j < conditionNodeArray.length; j++) {

			// Log("Document is Valid at"+ conditionNodeArray[j]+" for the Condition " + conditionString);
		    }

		    validationFlag = true;
		} else if(concatArray[0].equals(conditionNodeArray[0]) && count == 0){
		    for (int j = 0; j < conditionNodeArray.length; j++) {
                    //System.out.println("The Count is "+count);
			Log("Document is Invalid at" + conditionNodeArray[j] + " for the Condition " + conditionString);
		    }

		    validationFlag = false;
		}
	    } else if (concatArray.length == 1) {
		Log("Invalid Condition gXml.There will not be any Condition on a Single Element");

		validationFlag = false;
	    }
	  } else if (conditionString.equals(conditionNewString[4])) {
	    int     count = 0;
	    boolean firstElementAvailable = false;

	    if (concatArray.length > 1 && conditionNodeArray.length >= 1) {
		for (int k = 0; k < conditionNodeArray.length; k++) {
		    if (concatArray[0].equals(conditionNodeArray[k])) {
			firstElementAvailable = true;
		    }
		}
	    if (firstElementAvailable == true) {
		for (int l = 1; l < (concatArray.length); l++) {
		    for (int m = 0; m < conditionNodeArray.length; m++) {
			if (concatArray[l].equals(conditionNodeArray[m]))
			    count++;
			else
			  break;
		    }
		}
		if (count <= 1) {
		    Log("Document is Invalid at " + concatArray[0] + " for the Condition " + conditionString);
		    validationFlag = false;
		    //break;
		} else if (concatArray.length == 1) {
		    Log("Invalid Condition gXml.There will not be any Single Condition");
		    validationFlag = false;
		}
	    } else
	        validationFlag = true;
		//}
	  }
	} else {
	    validationFlag = false;
	}

	return validationFlag;
    }

    /**
     * This method breaks the Lengthy Condition String into pieces
     * eg P01020304 R01020304 L010203040506 from this it breaks into pieces
     * like P01020304,R01020304&L010203040506 and stores into an array for future processing
     * @param conditionalValue
     * @return stringArrayofConditionSegments
     */
    public String[] getBrokenValues(String conditionalValue) {
	String[] tokens = new String[100];
	StringTokenizer token = new StringTokenizer(conditionalValue, "\n");
	int i = 0;

	while (token.hasMoreTokens()) {
	    tokens[i] = (token.nextToken()).trim();
	    i++;
	}
	return tokens;
    }

    /**
     * This method gets the remaining String(eg P01020304 this returns 01,02,03&04)
     * @param brokeString
     * @return
     */
    public String[] getElementValues(String brokeString) {
	int      len = (brokeString.length() - 1);
	int      index = len / 2;
	String[] cString = new String[index];
	int      k = 1;

	for (int count = 0; count < index; count++) {
	    int j = k + 2;
	    cString[count] = brokeString.substring(k, j);
	    k += 2;
	    if (cString[count] == null) {
		break;
	    }
	}
	return cString;
    }

    /**
     * This method gets the ConditionalChar from each Conditional Segments
     * eg P010203 it returns P
     * @param brokeString
     * @return ConditionString(eg P,R,L etc..)
     */
    public String getConditionalChar(String brokeString) {
	char[] conChar = new char[(brokeString.length()) + 5];
	String decidingChar = null;

	conChar = brokeString.toCharArray();
	for (int i = 0; i <= conChar.length; i++) {
	    Character brokeChar = new Character(conChar[i]);
	    if (brokeChar.isLetter(conChar[i])) {
		decidingChar = brokeString.substring(0, 1);
		break;
	    }
	}
	return decidingChar;
    }

    /**
     * This Class is to Extend the Default Error Handler to be registered with the XML DOM Parser
     * it is required to handle the Parser Errors seperatey
     */
    class MyErrorHandler implements ErrorHandler {
	/**
	 * This method is required to write the Parser Errors into the ValidatorLog.txt file
	 * @param memo Error Message String
	 */
	public void Log(String memo) {
	    try {
		PrintWriter LOG =   new PrintWriter(new FileWriter("ValidatorLOG.txt", true));
		LOG.println((new Date()).toString() + " : " + memo);
		LOG.flush();
		LOG.close();
	    } catch (Exception e) {
		System.out.println("ERROR: (log file) " + e);
	    }
	}

	/**
	 * This method traverses thru the UEX xml
	 * @param uexHeaderNode
	 * @param ConditionalXmlHeaderNode
	 * @param ConditionalElementSegmentNode
	 * @param conditionalString
	 * @param concatArray
	 */
	private void print(String label, SAXParseException e) {
	    Log("** " + label + ": " + e.getMessage());
	    Log("   URI  = " + e.getSystemId());
	    Log("   line = " + e.getLineNumber());
	}
	boolean reportErrors = true;
	boolean abortOnErrors = false;

	// for recoverable errors, like validity problems

	public void error(SAXParseException e) throws SAXException {
	    if (reportErrors)
		Log("Parser Error : Ordinary :" + e.getMessage()+"\n");
	    if (abortOnErrors)
		throw e;
	}
	public void fatalError(SAXParseException e) throws SAXException {
	    if (reportErrors)
		Log("Parser Error : Fatal : " + e.getMessage()+"\n");
	    if (abortOnErrors)
		throw e;
	}
	public void warning(SAXParseException e) throws SAXException {
	    if (reportErrors)
		Log("Parser Error : Warning : " + e.getMessage()+"\n");
	    if (abortOnErrors)
		throw e;
	}
    }

    public static String searchReplace(String inString, String find, String replace) {
      if (inString==null || find == null || replace == null) {
        return inString;
      }
      if(inString.length() == 0) {
        return inString;
      }
      int i = inString.indexOf(find);
      if (i == -1) {
	return inString;
     } else {
        return inString.substring(0,i) + replace +  searchReplace(inString.substring(i + find.length()), find, replace);
     }
   }

    /**
     * This is  a Log writer method which will logs teh string into the Log file
     * @param memo  The String to be written into the Log file
     *
     */
    public static void Log(String memo) {
	try {
	    PrintWriter LOG = new PrintWriter(new FileWriter("ValidatorLOG.txt", true));
	    LOG.println((new Date()).toString() + " : " + memo);
	    LOG.flush();
	    LOG.close();
	} catch (Exception e) {
	    System.out.println("ERROR: (log file) " + e);
	}
    }

    /*
     * The Following Main Method is only for UNIT Testing Purpose, Please delete the
     * MAIN method once the testing is through
     */

    public static void main(String args[]) {
	try {
	    String	      uexString_204_1 =
		"<?xml version=" + "'1.0'" + " " + "encoding=" + "'UTF-8'"
		+ "?>"
		+ "<UEX><ISA><ISA01>00</ISA01><ISA02/><ISA03>00</ISA03><ISA04/><ISA05>01</ISA05><ISA06>009138033</ISA06><ISA07>02</ISA07><ISA08>REDP</ISA08><ISA09>001016</ISA09><ISA10>1700</ISA10><ISA11>U</ISA11><ISA12>00200</ISA12><ISA13>0</ISA13><ISA14/><ISA15>P</ISA15><ISA16>:</ISA16></ISA>"
		+ "<GS><GS01>SM</GS01><GS02>009138033</GS02><GS03>R</GS03><GS04>20001016</GS04><GS05>1700</GS05><GS06>0</GS06><GS07>004010</GS07><GS08/></GS>"
		+ "<Header>" + "<ST><ST01>204</ST01><ST02>12223</ST02></ST>"
		+ "<B2><B202>REDF</B202><B204>4768327</B204><B206>PP</B206></B2>"
		+ "<B2A><B2A01>00</B2A01><B2A02>LT</B2A02></B2A>"
		+ "<G62><G6201>10</G6201><G6202>20001019</G6202></G62>"
		+ "<AT5 count=" + "'1'" + "><AT502>XX</AT502></AT5>"
		+ "<PLD><PLD01>24</PLD01><PLD02>5</PLD02><PLD04>5.8</PLD04></PLD>"
		+ "<NTE count=" + "'1'"
		+ "><NTE01>AAA</NTE01><NTE02>Sreeni</NTE02></NTE>"
		+ "<Loop0200 count=" + "'1'"
		+ "><N7><N702>MINIMUM</N702><N715>4406</N715><N720>96.0</N720><N721>92.0</N721></N7></Loop0200>"
		+ "</Header>" + "<Detail>" + "<Loop0300 count=" + "'1'" + ">"
		+ "<S5><S501>1</S501><S502>LD</S502></S5>" + "<L11 count="
		+ "'1'"
		+ "><L1101>4768327</L1101><L1102>4768327</L1102><L1103>4768327</L1103></L11>"
		+ "<AT8><AT801>N</AT801><AT802>L</AT802><AT803>429.12</AT803><AT804>1332</AT804><AT806>E</AT806><AT807>10.07</AT807></AT8>"
		+ "<NTE count=" + "'1'"
		+ "><NTE01>AAA</NTE01><NTE02>Sreeni</NTE02></NTE>"
		+ "<Loop0310>"
		+ "<N1><N101>SF</N101><N102>THECLOROXSALESCOMPANY</N102><N103>9</N103><N104>00913803300LC</N104></N1>"
		+ "<N3 count=" + "'1'"
		+ "><N301>BRANCHWAREHOUSE</N301><N302>220INDUSTRIALSTREET</N302></N3>"
		+ "<N4><N401>BAKERSFIELD</N401><N405>CA</N405><N403>93307</N403></N4>"
		+ "</Loop0310></Loop0300>" + "<Loop0300 count=" + "'2'" + ">"
		+ "<S5><S501>2</S501><S502>UL</S502></S5>" + "<G62 count="
		+ "'1'" + "><G6201>68</G6201><G6202>20001021</G6202></G62>"
		+ "<AT8><AT801>N</AT801><AT802>L</AT802><AT803>429.12</AT803><AT804>1332</AT804><AT806>E</AT806><AT807>10.07</AT807></AT8>"
		+ "<NTE count=" + "'1'"
		+ "><NTE01>AAA</NTE01><NTE02>CARRIERMUST</NTE02></NTE>"
		+ "<Loop0310>"
		+ "<N1><N101>ST</N101><N102>WALMART</N102><N103>9</N103><N104>0519577696261</N104></N1>"
		+ "<N3 count=" + "'1'" + "><N301>10813HWY99WEST</N301></N3>"
		+ "<N4><N401>REDBLUFF</N401><N402>CA</N402><N403>96080</N403></N4>"
		+ "<G61 count=" + "'1'"
		+ "><G6101>DC</G6101><G6102>APPOINTMENTDESK</G6102><G6103>TE</G6103><G6104>9165298424</G6104></G61>"
		+ "</Loop0310>" + "<Loop0350 count=" + "'1'" + ">"
		+ "<OID><OID01>4768327</OID01><OID02>2650085775</OID02><OID03>2650085775</OID03><OID04>CA</OID04><OID05>13.32</OID05><OID06>L</OID06><OID07>429.12</OID07><OID08>E</OID08><OID09>10.07</OID09></OID>"
		+ "</Loop0350></Loop0300>" + "<Loop0300 count=" + "'3'" + ">"
		+ "<S5><S501>3</S501><S502>UL</S502></S5>" + "<G62 count="
		+ "'1'" + "><G6201>68</G6201><G6202>20001021</G6202></G62>"
		+ "<AT8><AT801>N</AT801><AT802>L</AT802><AT803>429.12</AT803><AT804>1332</AT804><AT806>E</AT806><AT807>10.07</AT807></AT8>"
		+ "<NTE count=" + "'1'"
		+ "><NTE01>AAA</NTE01><NTE02>CARRIERMUST</NTE02></NTE>"
		+ "<Loop0310>"
		+ "<N1><N101>ST</N101><N102>WALMART</N102><N104>0519577696261</N104></N1>"
		+ "<N3 count=" + "'1'" + "><N301>10813HWY99WEST</N301></N3>"
		+ "<N4><N401>REDBLUFF</N401><N402>CA</N402><N403>96080</N403></N4>"
		+ "<G61 count=" + "'1'"
		+ "><G6101>DC</G6101><G6102>APPOINTMENTDESK</G6102><G6103>TE</G6103><G6104>9165298424</G6104></G61>"
		+ "</Loop0310>" + "<Loop0350 count=" + "'1'" + ">"
		+ "<OID><OID01>4768327</OID01><OID02>2650085775</OID02><OID04>CA</OID04><OID05>13.32</OID05><OID06>L</OID06><OID07>429.12</OID07><OID08>E</OID08><OID09>10.07</OID09></OID>"
		+ "</Loop0350></Loop0300>" + "</Detail>" + "<Summary>"
		+ "<L3><L301>429.12</L301><L302>N</L302><L309>10.07</L309><L310>E</L310><L311>1332</L311><L312>L</L312></L3>"
		+ "<SE><SE01>41</SE01><SE02>14240001</SE02></SE>"
		+ "</Summary>" + "</UEX>";
	    String	      uexString_204_2 =
		"<?xml version=" + "'1.0'" + " " + "encoding=" + "'UTF-8'"
		+ "?>"
		+ "<UEX>  <ISA>    <ISA01>00</ISA01>    <ISA02/>    <ISA03>00</ISA03>    <ISA04/>    <ISA05>01</ISA05>    <ISA06>009138033</ISA06>    <ISA07>02</ISA07>    <ISA08>REDP</ISA08>    <ISA09>001009</ISA09>    <ISA10>1302</ISA10>    <ISA11>U</ISA11>    <ISA12>00200</ISA12>    <ISA13>0</ISA13>    <ISA14/>    <ISA15>P</ISA15>    <ISA16>:</ISA16>  </ISA>  <GS>    <GS01>SM</GS01>    <GS02>009138033</GS02>    <GS03>REDP</GS03>    <GS04>20001009</GS04>    <GS05>1302</GS05>    <GS06>0</GS06>    <GS07>004010</GS07>    <GS08/>  </GS>  <Header>    <ST>      <ST01>204</ST01>      <ST02>14170001</ST02>    </ST>    <B2>      <B202>REDF</B202>      <B204>4754332</B204>      <B206>PP</B206>    </B2>    <B2A>      <B2A01>05</B2A01>      <B2A02>LT</B2A02>    </B2A>    <G62>      <G6201>10</G6201>      <G6202>20001012</G6202>    </G62>    <AT5 count='1'>      <AT502>PL</AT502>    </AT5>    <PLD>      <PLD01>22</PLD01>      <PLD02>2</PLD02>    </PLD>    <NTE count='1'>      <NTE01>ZZ"
		+ "</NTE01>      <NTE02/>    </NTE>    <Loop0200 count='1'>      <N7>        <N702>MINIMUM</N702>        <N715>4406</N715>        <N720>96</N720>        <N721>92</N721>      </N7>    </Loop0200>  </Header>  <Detail>    <Loop0300 count='1'>      <S5>        <S501>1</S501>        <S502>LD</S502>      </S5>      <L11 count='1'>        <L1101>4754332</L1101>        <L1102>BM</L1102>      </L11>      <AT8>        <AT801>N</AT801>        <AT802>L</AT802>        <AT803>42540</AT803>        <AT804>1006</AT804>        <AT806>E</AT806>        <AT807>951</AT807>      </AT8>      <NTE count='1'>        <NTE01>ZZ</NTE01>        <NTE02/>      </NTE>      <Loop0310>        <N1>          <N101>SF</N101>          <N102>A&amp;M PRODUCTS MFG COMPANY</N102>          <N103>9</N103>          <N104>00913803300TL</N104>        </N1>        <N3 count='1'>          <N301>FOR A MFG SUBSIDIARY OF CLOROX</N301>          <N302>950 NORTH PETROLEUM CLUB ROAD</N302>        </N3>        <N4>          <N401>TAFT</N401>          "
		+ "<N402>CA</N402>          <N403>93268</N403>        </N4>      </Loop0310>    </Loop0300>    <Loop0300 count='2'>      <S5>        <S501>2</S501>        <S502>UL</S502>      </S5>      <G62 count='1'>        <G6201>78</G6201>        <G6202>20001013</G6202>        <G6203>Z</G6203>        <G6204>0900</G6204>      </G62>      <AT8>        <AT801>N</AT801>        <AT802>L</AT802>        <AT803>42540</AT803>        <AT804>1006</AT804>        <AT806>E</AT806>        <AT807>951</AT807>      </AT8>      <NTE count='1'>        <NTE01>ZZ</NTE01>        <NTE02>CUTS OR QUESTION  ROCKY @ 707 545-1677</NTE02>      </NTE>      <Loop0310>        <N1>          <N101>ST</N101>          <N102>WESTERN FARM CENTER</N102>          <N103>9</N103>          <N104>B32624</N104>        </N1>        <N3 count='1'>          <N301>21 W 7TH ST</N301>        </N3>        <N4>          <N401>SANTA ROSA</N401>          <N402>CA</N402>          <N403>95401</N403>        </N4>      </Loop0310>      <Loop0350 count='1'>        "
		+ "<OID>          <OID01>4754332</OID01>          <OID02>3878</OID02>          <OID04>CA</OID04>          <OID05>1006</OID05>          <OID06>L</OID06>          <OID07>42540</OID07>          <OID08>E</OID08>          <OID09>951</OID09>        </OID>      </Loop0350>    </Loop0300>  </Detail>  <Summary>    <L3>      <L301>42540</L301>      <L302>N</L302>      <L309>951</L309>      <L310>E</L310>      <L311>1006</L311>      <L312>L</L312>    </L3>    <SE>      <SE01>23</SE01>      <SE02>14170001</SE02>    </SE>  </Summary></UEX>";

	    String	      uexString_850_1 =
		"<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>"
		+ "<UEX>"
		+ "<ISA><ISA01>00</ISA01><ISA02/><ISA03>00</ISA03><ISA04/><ISA05>01</ISA05><ISA06>HUB_QA</ISA06><ISA07>01</ISA07><ISA08>ABCTC0123</ISA08><ISA09>010919</ISA09><ISA10>0303</ISA10><ISA11>U</ISA11><ISA12>00401</ISA12><ISA13>0</ISA13><ISA14/><ISA15>T</ISA15><ISA16>:</ISA16></ISA>"
		+ "<GS><GS01>PO</GS01><GS02>HUBQA99</GS02><GS03>XYZ99</GS03><GS04>010919</GS04><GS05>0303</GS05><GS06>0</GS06><GS07>004010</GS07><GS08/></GS>"
		+ "<Header>"
		+ "<ST><ST01>850</ST01><ST02>5181</ST02></ST>"
		+ "<BEG><BEG01>00</BEG01><BEG02>SA</BEG02><BEG03>MWZ629623</BEG03><BEG04>320920</BEG04><BEG05>20010919</BEG05></BEG>"
		+ "<CUR><CUR01>BY</CUR01><CUR02>USD</CUR02></CUR>"
		+ "<PER count=" + "'1'"+ "><PER01>BD</PER01><PER02>CHARLES HALLA</PER02><PER03>TE</PER03><PER04>256-882-4375</PER04></PER>"
		+ "<TD5 count=" + "'1'"	+ "></TD5>"
		+ "<LoopN1 count=" + "'1'"+ "><N1><N101>ST</N101><N104>SC0W</N104></N1>"
		+ "<N3 count=" + "'1'"+ "><N301>SCI LOGISTICS CENTER-PLT 16</N301></N3>"
		+ "<N4 count=" + "'1'"+ "><N401>10501 FISCHER RD</N401></N4></LoopN1>"
		+ "<LoopN1 count=" + "'2'"+ "><N1><N101>BT</N101><N102>SCI SYSTEMS INC</N102><N103>92</N103><N104>SC1B</N104></N1>"
		+ "<N3 count="+ "'1'" + "><N301>P. O. BOX 1000</N301></N3><N4 count="+ "'1'"+ "><N401>HUNTSVILLE</N401><N402>AL</N402><N403>35807</N403></N4></LoopN1>"
		+ "</Header>"
		+ "<Detail>" + "<LoopPO1 count=" + "'1'"+ "><PO1><PO101>1</PO101><PO102>12.12</PO102><PO103>EA</PO103><PO104>0.79</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>MEX01K3127</PO107><PO108>VP</PO108><PO109>W40S11-23GT</PO109></PO1>"
		+ "<LoopSCH count=" + "'1'"+ "><SCH><SCH01>20.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010926</SCH06></SCH></LoopSCH></LoopPO1>"
		+ "<LoopPO1 count=" + "'2'"+ "><PO1><PO101>2</PO101><PO102>12.12</PO102><PO103>EA</PO103><PO104>0.93</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>LSC01K3127</PO107><PO108>VP</PO108><PO109>VPART-NO2</PO109></PO1>"
		+ "<LoopSCH count=" + "'1'"+ "><SCH><SCH01>15.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010927</SCH06></SCH></LoopSCH></LoopPO1>"
		+"</Detail>"
		+ "<Summary>"
		+ "<LoopCTT><CTT><CTT01>2</CTT01></CTT></LoopCTT>"
		+ "<SE><SE01>17</SE01><SE02>5181</SE02></SE>"
		+ "</Summary>"
		+ "</UEX>";


	    String	      uexString_850_1_Invalid =
		"<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>"
		+ "<UEX>"
		+ "<ISA><ISA01>00</ISA01><ISA02/><ISA03>00</ISA03><ISA04/><ISA05>01</ISA05><ISA06>HUB_QA</ISA06><ISA07>01</ISA07><ISA08>ABCTC0123</ISA08><ISA09>010919</ISA09><ISA10>0303</ISA10><ISA11>U</ISA11><ISA12>00401</ISA12><ISA13>0</ISA13><ISA14/><ISA15>T</ISA15><ISA16>:</ISA16></ISA>"
		+ "<GS><GS01>PO</GS01><GS02>HUBQA99</GS02><GS03>XYZ99</GS03><GS04>010919</GS04><GS05>0303</GS05><GS06>0</GS06><GS07>004010</GS07><GS08/></GS>"
		+ "<Header>" + "<ST><ST01>850</ST01><ST02>5181</ST02></ST>"
		+ "<BEG><BEG01>00RE</BEG01><BEG02>SA</BEG02><BEG03>MWZ629623</BEG03><BEG04>320920</BEG04><BEG05>20010919</BEG05></BEG>"
		+ "<CUR><CUR01>BY</CUR01><CUR09>USD</CUR09></CUR>"
		+ "<PER count=" + "'1'"
		+ "><PER01>BD</PER01><PER02>CHARLES HALLA</PER02><PER03>TE</PER03><PER04>256-882-4375</PER04></PER>"
		+ "<TD5 count=" + "'1'"
		+ "><TD504>ZZ</TD504><TD505>207 FOB S/P UPS GROUND CO</TD505></TD5>"
		+ "<LoopN1 count=" + "'1'"
		+ "><N1><N101>ST</N101><N102>SCI SYSTEMS INC</N102><N103>92</N103><N104>SC0W</N104></N1>"
		+ "<N3 count=" + "'1'"
		+ "><N301>SCI LOGISTICS CENTER-PLT 16</N301></N3>"
		+ "<N4 count=" + "'1'"
		+ "><N401>10501 FISCHER RD</N401></N4></LoopN1>"
		+ "<LoopN1 count=" + "'2'"
		+ "><N1><N101>BT</N101><N102>SCI SYSTEMS INC</N102><N103>92</N103><N104>SC1B</N104></N1><N3 count="
		+ "'1'" + "><N301>P. O. BOX 1000</N301></N3><N4 count="
		+ "'1'"
		+ "><N401>HUNTSVILLE</N401><N402>AL</N402><N403>35807</N403></N4></LoopN1>"
		+ "</Header>" + "<Detail>" + "<LoopPO1 count=" + "'1'"
		+ "><PO1><PO101>1</PO101><PO102>20.00</PO102><PO103>EA</PO103><PO104>0.79</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>MEX01K3127</PO107><PO108>VP</PO108><PO109>W40S11-23GT</PO109></PO1>"
		+ "<LoopSCH count=" + "'1'"
		+ "><SCH><SCH01>20.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010926</SCH06></SCH></LoopSCH></LoopPO1>"
		+ "<LoopPO1 count=" + "'2'"
		+ "><PO1><PO101>2</PO101><PO102>15.00</PO102><PO103>EA</PO103><PO104>0.93</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>LSC01K3127</PO107><PO108>VP</PO108><PO109>VPART-NO2</PO109></PO1>"
		+ "<LoopSCH count=" + "'1'"
		+ "><SCH><SCH01>15.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010927</SCH06></SCH></LoopSCH></LoopPO1></Detail>"
		+ "<Summary>"
		+ "<LoopCTT><CTT><CTT01>2</CTT01></CTT></LoopCTT>"
		+ "<SE><SE01>17</SE01><SE02>5181</SE02></SE>" + "</Summary>"
		+ "</UEX>";
	    String	      uexString_850_2 =
		"<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>"
		+ "<UEX><ISA><ISA01>00</ISA01><ISA02/><ISA03>00</ISA03><ISA04/><ISA05>01</ISA05><ISA06>HUB_QA</ISA06><ISA07>01</ISA07><ISA08>ABCTC0123</ISA08><ISA09>010919</ISA09><ISA10>0303</ISA10><ISA11>U</ISA11><ISA12>00401</ISA12><ISA13>0</ISA13><ISA14/><ISA15>T</ISA15><ISA16>:</ISA16></ISA><GS><GS01>PO</GS01><GS02>HUBQA99</GS02><GS03>XYZ100</GS03><GS04>010919</GS04><GS05>0303</GS05><GS06>0</GS06><GS07>004010</GS07><GS08/></GS>"
                +"<Header><ST><ST01>850</ST01><ST02>5181</ST02></ST><BEG><BEG01>00</BEG01><BEG02>SA</BEG02><BEG03>MWZ629623</BEG03><BEG04>320920</BEG04><BEG05>20010919</BEG05></BEG><CUR><CUR01>BY</CUR01><CUR02>USD</CUR02></CUR><PER count="
		+ "'1'"
		+ "><PER01>BD</PER01><PER03>TE</PER03><PER04>256-882-4375</PER04></PER><TD5 count="
		+ "'1'"
		+ "><TD504>ZZ</TD504><TD505>207 FOB S/P UPS GROUND CO</TD505></TD5><LoopN1 count="
		+ "'1'"
		+ "><N1><N101>ST</N101><N104>SC0W</N104></N1><N3 count="
		+ "'1'"
		+ "><N301>SCI LOGISTICS CENTER-PLT 16</N301></N3><N4 count="
		+ "'1'" + ">"
		+ "<N401>10501 FISCHER RD</N401></N4></LoopN1><LoopN1 count="
		+ "'2'"
		+ "><N1><N101>BT</N101><N102>SCI SYSTEMS INC</N102><N103>92</N103><N104>SC1B</N104></N1><N3 count="
		+ "'1'" + "><N301>P. O. BOX 1000</N301></N3><N4 count="
		+ "'1'"
		+ "><N401>HUNTSVILLE</N401><N402>AL</N402><N403>35807</N403></N4></LoopN1></Header><Detail><LoopPO1 count="
		+ "'1'"
		+ "><PO1><PO101>1</PO101><PO102>20.00</PO102><PO103>EA</PO103><PO104>0.79</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>MEX01K3127</PO107><PO108>VP</PO108><PO109>W40S11-23GT</PO109></PO1><LoopSCH count="
		+ "'1'"
		+ "><SCH><SCH01>20.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010926</SCH06></SCH></LoopSCH></LoopPO1><LoopPO1 count="
		+ "'2'"
		+ "><PO1><PO101>2</PO101><PO102>15.00</PO102><PO103>EA</PO103><PO104>0.93</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>LSC01K3127</PO107><PO108>VP</PO108><PO109>VPART-NO2</PO109></PO1><LoopSCH count="
		+ "'1'"
		+ "><SCH><SCH01>15.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010927</SCH06></SCH></LoopSCH></LoopPO1><LoopPO1 count="
		+ "'1'" + "><PO1>"
		+ "<PO101>1</PO101><PO102>20.00</PO102><PO103>EA</PO103><PO104>0.79</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>MEX01K3127</PO107><PO108>VP</PO108><PO109>W40S11-23GT</PO109></PO1><LoopSCH count="
		+ "'1'"
		+ "><SCH><SCH01>20.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010926</SCH06></SCH></LoopSCH></LoopPO1><LoopPO1 count="
		+ "'2'"
		+ "><PO1><PO101>2</PO101><PO102>15.00</PO102><PO103>EA</PO103><PO104>0.93</PO104><PO105>PE</PO105><PO106>BP</PO106><PO107>LSC01K3127</PO107><PO108>VP</PO108><PO109>VPART-NO2</PO109></PO1><LoopSCH count="
		+ "'1'" + "><SCH>"
		+ "<SCH01>15.00</SCH01><SCH02>EA</SCH02><SCH05>002</SCH05><SCH06>20010927</SCH06></SCH></LoopSCH></LoopPO1></Detail><Summary><LoopCTT><CTT><CTT01>2</CTT01></CTT></LoopCTT><SE><SE01>17</SE01><SE02>5181</SE02></SE></Summary></UEX>";
	    String	      uexString_204_OutBound_1 =
		"<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>"
		+ "<UEX2>	<Header>		<ST>			<ST01 Val=?214?/>			<ST02 Val=?0000?/>		</ST>		<B10>			<B1001 Val=?5085582?/>			<B1002 Val=?5085582?/>			<B1003 Val=?ISWT?/>		</B10>		<LX>			<LX01 Val=?1?/>		</LX>		<Loop0200>			<AT7>				<AT701 Val=?X1?/>				<AT702 Val=?NS?/>				<AT703/>				<AT704/>				<AT705 Val=?20010511?/>				<AT706 Val=?0630?/>			</AT7>			<AT7>				<AT701 Val=?OA?/>				<AT702 Val=?NS?/>				<AT703/>				<AT704/>				<AT705 Val=?20010511?/>				<AT706 Val=?0630?/>			</AT7"
		+ "		</Loop0200>	</Header>	<Detail/>	<Summary>		<SE>			<SE01 Val=??/>			<SE02 Val=??/>		</SE>	</Summary></UEX2>";
             String	      uexString_204_OutBound_2 =
            "<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>"
            +"<UEX2>	<Header>	<ST><ST01 Val=''/>			<ST02 Val='0000'/>		</ST>		<B10>			<B1001 Val='5085582'/>			<B1002 Val='5085582'/>			<B1003 Val='ISWT'/>		</B10>		<LX>			<LX01 Val='1'/>		</LX>		<Loop0200>			<AT7>				<AT701 Val='X1'/>				<AT702 Val='NS'/>				<AT703/>				<AT704/>				<AT705 Val='20010511'/>				<AT706 Val='0630'/>			</AT7>			<AT7>				<AT701 Val='OA'/>				<AT702 Val='NS'/>				<AT703/>				<AT704/>				<AT705 Val='20010511'/>				"
            +"<AT706 Val='0630'/>			</AT7>		</Loop0200>	</Header>	<Detail/>	<Summary>		<SE>			<SE01 Val=''/>			<SE02 Val=''/>		</SE>	</Summary></UEX2>";
	    String	      uexString_EDIFACT_D97A_ORDERS =
		"<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>"
		+ "<UEX> <UNB> <UNB01> <UNB0101>UNOA</UNB0101> <UNB0102>1</UNB0102> </UNB01> <UNB02> <UNB0201>HUB_QA</UNB0201> <UNB0202>01</UNB0202> </UNB02> <UNB03> <UNB0301>ABCTC0123</UNB0301> <UNB0302>01</UNB0302> </UNB03> <UNB04> <UNB0401>010919</UNB0401> <UNB0402>0605</UNB0402> </UNB04> <UNB05>00000000000090</UNB05> <UNB06> <UNB0601></UNB0601> <UNB0602/> </UNB06> <UNB07>ORDERS</UNB07> <UNB08/> <UNB09/> <UNB10/> <UNB11/> </UNB> <Header> <UNH> <UNH01>1</UNH01> <UNH02> <UNH0201>ORDERS</UNH0201> <UNH0202>D</UNH0202> <UNH0203>97A</UNH0203> <UNH0204>UN</UNH0204> <UNH0205>EDPO04</UNH0205> </UNH02> </UNH> <BGM> <BGM01>220</BGM01> <BGM02>PGA292</BGM02> <BGM03>9</BGM03> </BGM> <DTM count='1'> <DTM01> <DTM0101>137</DTM0101> <DTM0102>20010918</DTM0102> <DTM0103>102</DTM0103> </DTM01> </DTM> <LoopGroup2 count='1'> <NAD> <NAD01>BY</NAD01> <NAD02> <NAD0201>SL1AT01</NAD0201> <NAD0202></NAD0202> <NAD0203>92</NAD0203> </NAD02> </NAD> </LoopGroup2> <LoopGroup2 count='2'> <NAD> <NAD01>IV</NAD01> <NAD02> <NAD0201>SL1BT01</NAD0201> <NAD0202></NAD0202>"
		+ "<NAD0203>92</NAD0203> </NAD02> </NAD> </LoopGroup2> <LoopGroup2 count='3'> <NAD> <NAD01>DP</NAD01> <NAD02> <NAD0201>SL1ST13</NAD0201> <NAD0202></NAD0202> <NAD0203>92</NAD0203> </NAD02> </NAD> </LoopGroup2> <LoopGroup2 count='4'> <NAD> <NAD01>SE</NAD01> <NAD02> <NAD0201>SL1SU01</NAD0201> <NAD0202></NAD0202> <NAD0203>92</NAD0203> </NAD02> </NAD> <LoopGroup5 count='1'> <CTA> <CTA01>PD</CTA01> <CTA02> <CTA0201>Test</CTA0201> <CTA0202>G.CANAUD</CTA0202> </CTA02> </CTA> <COM count='1'> <COM01> <COM0101>0298813380</COM0101> <COM0102>TE</COM0102> </COM01> </COM> </LoopGroup5> </LoopGroup2> <LoopGroup7 count='1'> <CUX> <CUX01> <CUX0101>2</CUX0101> <CUX0102>USD</CUX0102> <CUX0103>9</CUX0103> </CUX01> </CUX> </LoopGroup7> </Header> <Detail> <LoopGroup28 count='1'> <LIN> <LIN01>1</LIN01> <LIN03> <LIN0301>PP2090AB1</LIN0301> <LIN0302>BP</LIN0302> <LIN0303></LIN0303> <LIN0304>92</LIN0304> </LIN03> </LIN> <PIA count='1'> <PIA01>1</PIA01> <PIA02> <PIA0201>PALCE20V8-10JC</PIA0201> <PIA0202>VP</PIA0202> <PIA0203></PIA0203> <PIA0204>91</PIA0204>"
		+ "</PIA02> </PIA> <IMD count='1'> <IMD01>F</IMD01> <IMD03> <IMD0301></IMD0301> <IMD0302></IMD0302> <IMD0303></IMD0303> <IMD0304>ICPR GAL20V8 10N        PLCC28</IMD0304> </IMD03> </IMD> <QTY count='1'> <QTY01> <QTY0101>21</QTY0101> <QTY0102>629</QTY0102> <QTY0103>EA</QTY0103> </QTY01> </QTY> <LoopGroup32 count='1'> <PRI> <PRI01> <PRI0101>AAA</PRI0101> <PRI0102>0.75</PRI0102> <PRI0103>CT</PRI0103> <PRI0104></PRI0104> <PRI0105>1</PRI0105> <PRI0106>EA</PRI0106> </PRI01> </PRI> </LoopGroup32> <LoopGroup53 count='1'> <SCC> <SCC01>1</SCC01> </SCC> <LoopGroup54 count='1'> <QTY> <QTY01> <QTY0101>21</QTY0101> <QTY0102>629</QTY0102> <QTY0103>EA</QTY0103> </QTY01> </QTY> <DTM count='1'> <DTM01> <DTM0101>2</DTM0101> <DTM0102>20011120</DTM0102> <DTM0103>102</DTM0103> </DTM01> </DTM> </LoopGroup54> </LoopGroup53> </LoopGroup28> <UNS> <UNS01>S</UNS01> </UNS> </Detail> <Summary> <UNT> <UNT01>20</UNT01> <UNT02>1</UNT02> </UNT> </Summary> </UEX>";

            String	      uexString_856_Inound_1 =
		"<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>"
                +"<UEX>	<ISA>		<ISA01>00</ISA01>		<ISA02/>		<ISA03>00</ISA03>		<ISA04/>		<ISA05>ZZ</ISA05>		<ISA06>MAERSKLOG</ISA06>		<ISA07>01</ISA07>		<ISA08>02-917-4612</ISA08>		<ISA09>020513</ISA09>		<ISA10>1116</ISA10>		<ISA11>U</ISA11>		<ISA12>00300</ISA12>		<ISA13>0</ISA13>		<ISA14/>		<ISA15>T</ISA15>		<ISA16>:</ISA16>	</ISA>	<GS>		<GS01>SH</GS01>		<GS02>MAERSKLOG</GS02>		<GS03>02-917-4612</GS03>		<GS04>20020513</GS04>		<GS05>1116</GS05>		<GS06>0</GS06>		<GS07>004010</GS07>		<GS08/>	</GS>	<Header>		<ST>			<ST01>856</ST01>			<ST02>0001</ST02>		</ST>		<BSN>			<BSN01>00</BSN01>			"
                +"<BSN02>8002118798</BSN02>			<BSN03>20010406</BSN03>			<BSN04>075837</BSN04>		</BSN>	</Header>	<Detail>		<LoopHL count=‘1’>			<HL>				<HL01>1</HL01>				<HL03>S</HL03>			</HL>			<MEA count=‘1’>				<MEA02>VOL</MEA02>				<MEA03>54650</MEA03>				<MEA04>					<MEA0401/>					<MEA0402>33</MEA0402>				</MEA04>			</MEA>			<PWK count=‘1’>				<PWK01>01</PWK01>				<PWK08>1</PWK08>			</PWK>			<TD1 count=‘1’>				<TD101>CTN25</TD101>				<TD102>1</TD102>				"
                +"<TD107>13.5</TD107>				<TD108>LB</TD108>			</TD1>			<REF count=‘1’>				<REF01>BM</REF01>				<REF02>712345678910</REF02>			</REF>			<REF count=‘2’>				<REF01>IA</REF01>				<REF02>000684</REF02>			</REF>			<REF count=‘3’>				<REF01>CN</REF01>				<REF02>4712345678910</REF02>			</REF>			<DTM count=‘1’>				<DTM01>068</DTM01>				<DTM02>20010406</DTM02>			</DTM>			<DTM count=‘2’>				<DTM01>011</DTM01>				<DTM02>20010406</DTM02>			</DTM>			<DTM count=‘3’>				<DTM01>067</DTM01>"
                +"				<DTM02>20010406</DTM02>			</DTM>			<LoopN1 count=‘1’>				<N1>					<N101>ST</N101>					<N104>0255</N104>				</N1>			</LoopN1>			<LoopN1 count=‘2’>				<N1>					<N101>SF</N101>					<N102>APPLE COMPUTER INC.</N102>				</N1>				<N4>					<N401>TAO-YUAN</N401>					<N402>TA</N402>					<N404>TWN</N404>				</N4>			</LoopN1>		</LoopHL>		<LoopHL count=‘2’>			<HL>				<HL01>2</HL01>				"
                +"<HL02>1</HL02>				<HL03>O</HL03>			</HL>			<PRF>				<PRF01>0CCT03</PRF01>				<PRF04>20010409</PRF04>			</PRF>		</LoopHL>		<LoopHL count=‘3’>			<HL>				<HL01>3</HL01>				<HL02>2</HL02>				<HL03>I</HL03>			</HL>			<LIN>				<LIN01>0001</LIN01>				<LIN02>CB</LIN02>				<LIN03>IBOOKI</LIN03>				<LIN04>UP</LIN04>				<LIN05>718908331682</LIN05>			</LIN>			<SN1>				<SN102>1</SN102>				<SN103>EA</SN103>			</SN1>		</LoopHL>	</Detail>	"
                +"<Summary>		<CTT>			<CTT01>1</CTT01>		</CTT>		<SE>			<SE01>22</SE01>			<SE02>0001</SE02>		</SE>	</Summary></UEX>";

	    String uex850_out = "<?xml version='1.0' encoding='UTF-8'?>"
		+"<UEX2><ISA><ISA01/><ISA02/><ISA03/><ISA04/><ISA05/><ISA06/><ISA07/><ISA08/><ISA09/><ISA10/><ISA11/><ISA12/><ISA13/><ISA14/><ISA15/><ISA16/></ISA><GS><GS01/><GS02/><GS03/><GS04/><GS05/><GS06/><GS07/><GS08/></GS><Header><ST><ST01/><ST02/></ST>"
		+"<BEG><BEG01 Val='00'/><BEG02 Val='SA'/><BEG03 Val='400001569'/><BEG05 Val='20020509'/></BEG><DTM><DTM01 Val='002'/><DTM02 Val='20020603'/></DTM><LoopN9><N9><N901 Val='ZZ'/><N903 Val='Additional PO Information'/></N9>"+
		"<MSG><MSG01 Val='TestingMSG'/></MSG></LoopN9><LoopN1><N1><N102 Val='VN'/><N104 Val='000015214'/></N1><N2><N201 Val='P.O. Box 1567'/></N2><N4><N401 Val='San Marcos'/><N402 Val='CA'/>"+
		"<N403 Val='92079'/><N404 Val='USA'/></N4></LoopN1></Header>"+
		"<Detail><LoopPO1><PO1><PO101 Val='001'/><PO103 Val='EA'/><PO106 Val='SK'/><PO107 Val='99909'/><PO108 Val='ST'/><PO109 Val='CHROME EGG'/><PO110 Val='UP'/><PO111 Val='0000002099909'/></PO1>"+
		"<LoopPID><PID><PID01 Val='F'/><PID05 Val='EGGS WITH CHIMES CHROME'/></PID></LoopPID><PO4><PO401 Val='220'/><PO402 Val='12'/><PO403 Val='PC'/><PO405 Val='G'/><PO406 Val='0.01'/><PO407 Val='LB'/><PO408 Val='0.10'/><PO409 Val='CF'/></PO4>"+
		"<DTM><DTM01 Val='010'/><DTM02 Val='20020415'/></DTM><DTM><DTM01 Val='001'/><DTM02 Val='20020415'/></DTM></LoopPO1><LoopPO1><PO1><PO101 Val='005'/><PO103 Val='EA'/><PO107 Val='335643'/><PO108 Val='ST'/><PO109 Val='FS51'/>"+
		"<PO110 Val='UP'/><PO111 Val='0000002335643'/></PO1><LoopPID><PID><PID01 Val='F'/><PID05 Val='BA GUA FENG SHUI ND'/></PID></LoopPID><PO4><PO401 Val='80'/><PO402 Val='12'/><PO403 Val='PC'/><PO405 Val='G'/><PO406 Val='0.01'/><PO407 Val='LB'/><PO408 Val='2.40'/><PO409 Val='CF'/>"+
		"</PO4><DTM><DTM01 Val='010'/><DTM02 Val='20020415'/></DTM><DTM><DTM01 Val='001'/><DTM02 Val='20020415'/></DTM></LoopPO1>"+
		"</Detail><Summary><LoopCTT><CTT><CTT01 Val='13'/></CTT></LoopCTT><SE><SE01/><SE02/></SE></Summary></UEX2>";

            long	      StartTime = System.currentTimeMillis();

            XMLProcessHelper Validator = new XMLProcessHelper(uex850_out,
		    "E:/SREENU/PERSONAL_INTERESTS/ReVisit_Sreeni/ReVisit_Sreeni/ASCI-x12-4010-850_Conditions.xml",
		    "E:/SREENU/PERSONAL_INTERESTS/ReVisit_Sreeni/ReVisit_Sreeni/X12_V4010_850_out_Schema.xml");
	    /*B2B_EDI_Validator Validator = new B2B_EDI_Validator(uexString_850_2,
		    "C:/B2BValidator/ReVisit_Sreeni/X12_V4010_850_Conditions.xml",
		    "C:/B2BValidator/ReVisit_Sreeni/X12_V4010_850_in_Schema.xsd");*/

	    Validator.validateConditions();
	    System.out.println("Time for Total Validation"
			       + (System.currentTimeMillis() - StartTime));
	} catch (Exception e) {}
    }


}

/*--- formatting done in "Sun Java Convention" style on 05-11-2002 ---*/

