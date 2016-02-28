package yetchina.xml;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Document;

import yetchina.xml.ParseXMLFile;

import java.io.*;
import java.util.Properties;


public class ProcessFiles{

	//GlobalVariables
	static Properties p = getFromINIFile();
	
  public static void main(String argv[]) throws	TransformerException,
						  						TransformerConfigurationException,
						  						FileNotFoundException,
						  						IOException  {  
   
   
   /* Unused variabls etc..
   int notConvertedXmlFiles=0;
   int ConvertedFiles=0;   

   long beforeTrans = 0;
   long afterTrans = 0;
   long transTime = 0;
   long totalTime = 0;
   FileOutputStream TransformedXMLFilesStream = null;
	*/  
	  String strSourceXMLFilesDir=p.getProperty("XMLFileLoc");
	  File SourceXMLFilesDir = new File(strSourceXMLFilesDir);
	  ProcessXMLDirectory(SourceXMLFilesDir);
	  System.out.println("Done");
	  
  }
  public static void ProcessXMLDirectory(File SourceXMLFilesDir){
	  
	  String sourceFile=null; 
	  if (SourceXMLFilesDir.isDirectory()){
		  FilenameFilter only = new OnlyExt_("xml");
		  String s[]  = SourceXMLFilesDir.list();

	    for (int i=0; i < s.length; i++){
	    	 System.out.println(s[i]);
	      File SourceGxmlFile = new File(s[i]);
	    
	    
	      sourceFile = SourceGxmlFile.getName();
	      
	      if (SourceGxmlFile.isDirectory()){
	        System.out.println(s[i] + " is a Sub Directory");
	      	ProcessXMLDirectory(SourceGxmlFile);
	      }
	      else {
	       ProcessXMLFile(SourceGxmlFile, SourceXMLFilesDir.getAbsolutePath());
	      }
	      /*
	      if  ( i == (s.length - 1)){
			  System.exit(1);
			  }
	        else
	   		System.out.println(strSourceXMLFilesDir + " is not a valid directory");
	        System.out.println("\n\n***************************** \n\n");
	      */
	    }
		  }
  }
public static void ProcessXMLFile(File sourceFile, String FilePath){
	String sourceFileAbsPath = FilePath+"\\"+sourceFile.getName();
	
	ParseXMLFile newXMLFile = new ParseXMLFile();
	newXMLFile.SetXMLFileName(sourceFileAbsPath);
	
	 try{
         System.out.println("Processing the "+sourceFileAbsPath+" File");
         Document doc = newXMLFile.parseFile(sourceFileAbsPath);        
	 }
	 catch (Exception ioe){
		 
	 }
     
}

public static void TransformXMLFile(File XSLTFile, File sourceXMLFile, File ConvertedFile, String TargetFileType){
		String strXSLTFilesDir=p.getProperty("gXMLStyleSheetFiles");//XSL files location
	   File XSLTFilesDir = new File(strXSLTFilesDir);
	   TransformerFactory tFactory = TransformerFactory.newInstance();
	   //String strTransformedXMLFilesDir=strSourceXMLFilesDir.concat("/TransformedXMLFiles"); //Converted fils locaion
	   //File TransformedXMLFilesDir = new File(strTransformedXMLFilesDir); 
	   //if(!(TransformedXMLFilesDir.isDirectory()))
		 //    TransformedXMLFilesDir.mkdirs();
	   
	try {
        //System.out.println("Processing the "+sourceXMLFile.getName()+" File");
        
        /* The following code is to use the XSLT transformation on each file
        beforeTrans = System.currentTimeMillis();
        GxmlInboundSchemaStream = new FileOutputStream(TransformedXMLFilesDir+"/"+s[i].substring(0, s[i].length()-4)+"_Inbound.xsd");
        Transformer GxmlInboundSchemaTransformer = tFactory.newTransformer(new StreamSource(strXSLTFilesDir+"/X12_Inbound_SchemaGenerator.xsl"));
        GxmlInboundSchemaTransformer.transform(new StreamSource(strSourceXMLFilesDir+"/"+sourceFile), new StreamResult(GxmlInboundSchemaStream));
        afterTrans = System.currentTimeMillis();
        transTime = afterTrans - beforeTrans;
        totalTime = totalTime+transTime;
        System.out.println("Total Time Taken for Inbound Schema Generation is : "+transTime+" Milli Seconds");
        ConvertedFiles++;
        
        GxmlInboundSchemaStream.close();
        System.out.println("Conversion is done");
        */

		}catch(Exception te){
		  
	          te.printStackTrace();
	        }
		
		/**catch(TransformerException te){
		  notConvertedGxmlFiles++;
		  System.out.println(sourceFile+" Was Not Converted Due to teh Transformation Error");
		  System.out.println("Transformer Exception occured");
	          te.printStackTrace();
	        }
	    catch(FileNotFoundException fne){
		  System.out.println(TransformedXMLFilesDir+"/"+s[i].substring(0, s[i].length()-4)+".txt" + "     not found.");
		}*/
}
public static Properties getFromINIFile() {

    String iniFileLoc="C:/BS/ProcessFilsConfig.ini";
    Properties p = new Properties();

    try{
      FileInputStream fis = new FileInputStream(iniFileLoc);
        p.load(fis);
    }catch (Exception e) {
      System.out.println(e);
    }
    return p;
}
}
//To Process only the XML files in the Source Directiory
class OnlyExt_ implements FilenameFilter{
	String ext;
	public OnlyExt_(String ext) {
		this.ext = "." + ext;
	}
	public boolean accept(File dir, String name) {
		return name.endsWith(ext);
	}
}