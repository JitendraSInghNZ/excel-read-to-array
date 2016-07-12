/*
 * Copyright (C) 2016  Jitendra Shyam Singh

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details email: jitendrasinghnz@gmail.com.
 */


package com.jitendrasinghnz.excelreadutility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author jitus_000
 */
public class ExcelReadStringArrayXSLX {
private String mFilePath;
	private File mExcelFile;
	private XSSFWorkbook mXssfWorkbook;
	private FileInputStream mFileInputStream;
	private XSSFSheet mXssfSheet;
	private int mTotalRowSourceFile;
	private int mTotalColSourceFile;
	private String[][] mExcelStringArray;
	private String mWorksheetName;
	
	public ExcelReadStringArrayXSLX(String excelFilePath, String worksheetName) throws FileNotFoundException,Exception{
		mFilePath = excelFilePath;
		mWorksheetName = worksheetName;
		mExcelFile = new File(mFilePath);
		
		if(mExcelFile.exists() && !mExcelFile.isDirectory()){
			
			try{
				mFileInputStream = new FileInputStream(mExcelFile);									
			}
			catch(FileNotFoundException fileNotFoundException){
				System.out.println("File path name is incorrect or file does not exist");
			}
			try{
				mXssfWorkbook = new XSSFWorkbook(mFileInputStream);
				mXssfSheet = mXssfWorkbook.getSheet(worksheetName);				
			}			
			catch(IOException ioe){
				System.out.println("Error in opening file "+mFilePath);
			}			
			
		}else{
			throw new FileNotFoundException("File path name is incorrect or file "+mFilePath+" does not exist\n"+"Have you typed the file path name correctly ? e.g.: For Windows user an example file path would be C:\\\\foouser\\\\foodocuments\\\\foofilename.xls or \n if you are GNU/Linux user an example file path would be /foouser/foodocuments/foofilename.xls");
		}
		if(mXssfSheet == null){
			throw new Exception("Worksheet with name "+mWorksheetName+ " does not exist in "+mFilePath);
		}
		
	}
	
	public int getRowLenght(){
		mTotalRowSourceFile = mXssfSheet.getLastRowNum() + 1;
		return mTotalRowSourceFile;
	} 
	
	public int getColumnLenght(){
		mTotalColSourceFile = mXssfSheet.getRow(0).getLastCellNum();
		return mTotalColSourceFile;
	}
	
	public String[][] getExcelStringArray() throws Exception{
		if(mXssfSheet!=null){
		mExcelStringArray = new String[getRowLenght()][getColumnLenght()];
		for(int rowIndex = 0; rowIndex < getRowLenght(); rowIndex++){
			XSSFRow row = mXssfSheet.getRow(rowIndex);
			for(int colIndex = 0; colIndex < getColumnLenght(); colIndex++){
				XSSFCell cell = row.getCell(colIndex);
				mExcelStringArray[rowIndex][colIndex] = convertCellToString(cell); 
			}
		}
		try{
			mXssfWorkbook.close();
			mFileInputStream.close();
		}
		catch(IOException ioe){
			System.out.println("Error in closing the filestream");
		}	
		return mExcelStringArray;
		}
		else{
			throw new Exception("Worksheet with name "+mWorksheetName+ " does not exist in "+mFilePath);
		}
	}
	
	public String convertCellToString(XSSFCell cell){
		int type;
		if(cell == null){
			type = Cell.CELL_TYPE_BLANK;
		}
		else{
		type = cell.getCellType();
		}
		Object result;
		switch (type){
		case Cell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			result = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			result = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			result = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_ERROR:
			result = cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;
			
		default:
			throw new RuntimeException("There are no support fot the cell type");
		}
		return result.toString();
	}
        public void setOutputSingletResult(String[][]inputData, String[] outputResult, String filePath){
            String[][] outputResultTwoDimensionArray;
            outputResultTwoDimensionArray = new String[inputData.length][inputData[0].length + 1];
            Path folderPath =null;
            try{
                folderPath = Paths.get(filePath);
            }
            catch(InvalidPathException i){
                System.out.println("Please Check whether "+filePath+" exist ");
            }
            for(int i=0; i < outputResultTwoDimensionArray.length; i++){
                for(int j=0; j < outputResultTwoDimensionArray[i].length; j++){
                    if(j==(outputResultTwoDimensionArray[i].length - 1)){
                        for(int k = j; k < outputResultTwoDimensionArray[i].length; k++){
                            outputResultTwoDimensionArray[i][k] = outputResult[i];
                        }
                    }
                    else
                        outputResultTwoDimensionArray[i][j] = inputData[i][j];                    
                }
            }
            
                CharSequence filepathWindows = "\\";
                CharSequence filepathGNULinux = "/";
                try{
                    if(filePath.contains(filepathWindows) || filePath.contains(filepathGNULinux)){
                        String filename = "test_output_singlet_"+String.valueOf(System.currentTimeMillis())+".xls";
                        String finalFileName = filePath+filename;
                        FileOutputStream outputFile = new FileOutputStream(finalFileName);
                        HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
                        HSSFSheet hSSFSheet = hSSFWorkbook.createSheet("output");
                        for(int i = 0; i < outputResultTwoDimensionArray.length;i++){
                            HSSFRow row = hSSFSheet.createRow(i);
                            for(int j = 0; j < outputResultTwoDimensionArray[i].length;j++){
                                HSSFCell cell = row.createCell(j);
                                cell.setCellValue(outputResultTwoDimensionArray[i][j]);
                            }
                        }
                        hSSFWorkbook.write(outputFile);
                        outputFile.flush();
                        outputFile.close();
                        System.out.println("An output file named \""+filename+ "\" was created at \""+filePath+"\"");
                        System.out.println("Good Bye !!!");
                    }                    
                }
                catch(FileNotFoundException fnfe){
                       System.out.println("Sorry "+filePath+" does not exist");
                }
                
                catch(IOException ioe){
                        System.out.println("Error to open/close file from path "+filePath);
                }
                
        }
        public void setOutputResult(String[][]inputData, String[][] outputResult, String filePath){
        String[][] outputResultTwoDimensionArray;
        outputResultTwoDimensionArray = new String[inputData.length][inputData[0].length + outputResult[0].length];
        Path folderPath =null;
            try{
                folderPath = Paths.get(filePath);
            }
            catch(InvalidPathException i){
                System.out.println("Please Check whether "+filePath+" exist ");
            }
            for(int i = 0; i < outputResultTwoDimensionArray.length; i++){
                for(int j = 0 ; j < outputResultTwoDimensionArray[i].length;j++){
                    if(j >= (outputResultTwoDimensionArray[i].length - outputResult[i].length)){
                        for(int k = j; k < outputResultTwoDimensionArray[i].length; k++){
                            outputResultTwoDimensionArray[i][j] = outputResult[i][k-(outputResultTwoDimensionArray[i].length - outputResult[i].length)];
                        }
                    }
                    else
                        outputResultTwoDimensionArray[i][j] = inputData[i][j];                  
                }
            }
            CharSequence filepathWindows = "\\";
                CharSequence filepathGNULinux = "/";
                try{
                    if(filePath.contains(filepathWindows) || filePath.contains(filepathGNULinux)){
                        String filename = "test_output_"+String.valueOf(System.currentTimeMillis())+".xls";
                        String finalFileName = filePath+filename;
                        FileOutputStream outputFile = new FileOutputStream(finalFileName);
                        HSSFWorkbook hSSFWorkbook = new HSSFWorkbook();
                        HSSFSheet hSSFSheet = hSSFWorkbook.createSheet("output");
                        for(int i = 0; i < outputResultTwoDimensionArray.length;i++){
                            HSSFRow row = hSSFSheet.createRow(i);
                            for(int j = 0; j < outputResultTwoDimensionArray[i].length;j++){
                                HSSFCell cell = row.createCell(j);
                                cell.setCellValue(outputResultTwoDimensionArray[i][j]);
                            }
                        }
                        hSSFWorkbook.write(outputFile);
                        outputFile.flush();
                        outputFile.close();
                        System.out.println("An output file named \""+filename+ "\" was created at \""+filePath+"\"");
                        System.out.println("Good Bye !!!");
                    }                    
                }
                catch(FileNotFoundException fnfe){
                       System.out.println("Sorry "+filePath+" does not exist");
                }
                
                catch(IOException ioe){
                    System.out.println("Error to open/close file from path "+filePath);
                }
        }
}
