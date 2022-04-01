package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;


public class ExcelHandler {
	File file;
	FileOutputStream fos;
	FileInputStream fis;
	
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	Row row;
	Cell cell;
	
	int noOfRows;
	int noOfColumns;
	String cellData;
	
	//constructor
	public ExcelHandler(String path,String sheetName) {
		file = new File(path) ;
					
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			 workbook = new XSSFWorkbook(fis);
			 System.out.println("workbook created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	  sheet = workbook.getSheet(sheetName);
	  System.out.println("sheet name is"+sheetName);
	  
	}
	public void setCellData(int r,int col,String data )
	{
		  row =sheet.getRow(r);
		  if (row==null)
		  {
			  row=sheet.createRow(r);
		  }
		   cell= row.getCell(col);
		  if (cell==null)
		  {
			  cell=row.createCell(col);
		  }
		  cell.setCellValue(data);
		//sheet.getRow(row).createCell(col).setCellValue(data);

	}
	
	
	

	//get the no of rows in excel
	public int getNoOfRows()
	{
		noOfRows=sheet.getLastRowNum();
		System.out.println(noOfRows);
		//noOfRows = sheet.getPhysicalNumberOfRows();
		//System.out.println(noOfRows);
		
		return noOfRows;
	}
	
	//get no of columns in excel
	public int getNoOfColumns()
	{
		noOfColumns= sheet.getRow(0).getLastCellNum();
		System.out.println(noOfColumns);
		noOfColumns= sheet.getRow(0).getPhysicalNumberOfCells();
		System.out.println(noOfColumns);
		return noOfColumns;
	}
	public Object getCellData(int row, int col) {
		Object value=null;
		DataFormatter formatter = new DataFormatter();
		   value= formatter.formatCellValue(sheet.getRow(row).getCell(col));
			
		//cellData = sheet.getRow(row).getCell(col).getStringCellValue();
		
		System.out.println("Data: "+ value);
		return value;
	}
	//assign the values
	public void setCellData(int r, int col, String data, String sheetName) {
		 sheet = workbook.getSheet(sheetName);
		  System.out.println("sheet name is"+sheetName);
		  //sheet.getRow(row).createCell(col).setCellValue(data);
		  row =sheet.getRow(r);
		  if (row==null)
		  {
			  row=sheet.createRow(r);
		  }
		  cell= row.getCell(col);
		  if (cell==null)
		  {
			  cell=row.createCell(col);
		  }
		  cell.setCellValue(data);
		  //row.createCell(col,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).setCellValue(data);
		 
	}
	
	//Write and close the excel
	public void close() {
		
		 try {
	         fos = new FileOutputStream(file);
	        workbook.write(fos);
	        workbook.close();
	        System.out.println("vales are written in sheet");
	    } catch (Exception e) {
           e.printStackTrace();
	}
	}

	public static Object[][] getTestData(String path,String sheetName) {
		String projectPath=System.getProperty("user.dir");
		String filepath = "/src/test/resources/data/UsersTestData.xlsx";
		ExcelHandler excel= new 
				ExcelHandler("/Users/suresh/eclipse-workspace/LMSCucumberAutomation/src/test/resources/data/UsersTestData.xlsx","Get");
		//ExcelHandler excel= new ExcelHandler(path, sheetName);
		int rowCount=excel.getNoOfRows();
		int columnCount=excel.getNoOfColumns();
		Object data[][]=new Object[rowCount-1][columnCount];
		for(int i=1;i<rowCount;i++) {
			for (int j=0;j<columnCount;j++)
			{
				Object cellData= excel.getCellData(i,j);
				
				System.out.print( cellData +"|");
				data[i-1][j]=cellData;
			}
			System.out.println();
		}
		return data;
	}
	
	public static void main(String[] args) throws IOException {
		//Object data[][];
		ExcelHandler excel= new 
				ExcelHandler("/Users/suresh/eclipse-workspace/LMSCucumberAutomation/src/test/resources/data/Book5.xlsx","sheet");
	//	int r= excel.getNoOfRows();
	//	int col= excel.getNoOfColumns();
		excel.setCellData(1, 0, "success");
		/*excel.getCellData(r, col-1);
		for (int i=1;i<=r;i++) {
			for(int j=0;j<col;j++) {
				
				Object dat = excel.getCellData(i,j);
				System.out.println( dat + "|");
				
			}
		}*/
			
		
		//String id = sheet.getRow(1).getCell(0).getStringCellValue();
		//System.out.println("ID"+ id);
	}
	public void setCellData(int r, int col, int data, String sheetName) {
		sheet = workbook.getSheet(sheetName);
		  System.out.println("sheet name is"+sheetName);
		  //sheet.getRow(row).createCell(col).setCellValue(data);
		  row =sheet.getRow(r);
		  if (row==null)
		  {
			  row=sheet.createRow(r);
		  }
		  cell= row.getCell(col);
		  if (cell==null)
		  {
			  cell=row.createCell(col);
		  }
		  cell.setCellValue(data);
		  //row.createCell(col,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).setCellValue(data);
		 
		
	}
	
	
}
