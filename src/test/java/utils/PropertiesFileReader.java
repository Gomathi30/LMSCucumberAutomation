package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesFileReader {

	 Properties prop = new Properties();
	 FileInputStream input;
	public PropertiesFileReader(){
	try {
		input=   new FileInputStream("/Users/suresh/eclipse-workspace/LMSCucumberAutomation/src/test/resources/properties/config.properties");
		prop.load(input);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	public  String getUsername()
	{
		
		return (prop.getProperty("username"));
		
	}
	
	public  String getPassword()
	{	
		return (prop.getProperty("password"));		
	}
	
	public  String getBaseURI()
	{
	return (prop.getProperty("baseURI"));
		
	}
	
	/*usersEndPoint= /Users
			skillsEndpoint= /Skills
			userSkillsEndPoint = /UserSkills
			userSkillMapEndPoint = /UserSkillsMap */
	public  String getUsersEndPoint()
	{
		return (prop.getProperty("usersEndPoint"));
		
	}
	
	public  String getSkillsEndpoint()
	{
		return (prop.getProperty("skillsEndpoint"));
		
	}
	public  String getuserSkillsEndPoint()
	{
		return (prop.getProperty("userSkillsEndPoint"));
		
	}
	public  String getuserSkillMapEndPoint()
	{
		return (prop.getProperty("userSkillMapEndPoint"));
		
	}
	
	
	public  String getExcelPath()
	{
		return (prop.getProperty("excelPath"));
		
	}
	public  String getSkillsExcelPath()
	{
		return (prop.getProperty("skillsExcelPath"));
		
	}
	
	public  String getSheetNameGet()
	{
	return (prop.getProperty("sheetNameGet"));
	}
	
	public  String getSheetNamePost()
	{

		return (prop.getProperty("sheetNamePost"));
		
	}
	
	public String getSheetNamePut()
	{
	
		return (prop.getProperty("sheetNamePut"));
		
	}
	
	public  String getSheetNameDelete()
	{
		
		return (prop.getProperty("sheetNameDelete"));
		
	}
	
	
	public static void main(String[] args) {
		PropertiesFileReader prop = new PropertiesFileReader();
		System.out.println("users:"+prop.getUsersEndPoint());
		System.out.println(prop.getSkillsEndpoint());
		System.out.println(prop.getuserSkillsEndPoint());
		System.out.println(prop.getuserSkillMapEndPoint());
		/*
		 * System.out.println(getUsername()); System.out.println(getPassword());
		 * System.out.println( getBaseURI()); System.out.println(getEndPoint());
		 * System.out.println(getExcelPath()); System.out.println(getSheetNameGet());
		 * System.out.println(getSheetNamePost());
		 * System.out.println(getSheetNamePut());
		 * System.out.println(getSheetNameDelete());
		 */
	}

	
}
