Reads an .XLS or .XLSX Excel file and gives back a 2D String Array in Java Has 2 classes called ExcelReadStringArrayXSL(for .xsl file) and ExcelReadStringArrayXSLX(for .xslx file)
both have a method called getExcelStringArray() which gives back 2D String array The classes take two String parameters 1) String filepath 2) String sheetname The method can handle following cell types: String, Numeric, Blank, boolean, Error, Formula. Both the class throws: FileNotFoundException and Exception.
After getting the output from your test data 
1)if you get a 1-dimension result array then there is a method called setOutputSingletResult(String[][] inputData, String[]outputResult, String filePath)
pass the following parameters of the method and it stores an excel file with both input and output in one file in a destination path
2) if you get a 2-dimension result array then there is a method called setOutputResult(String[][] inputData, String[][] outputResult, String filePath) 
pass the following parameters of the method and it stores an excel file with both input and output in one file in a destination path

The excel_read_to_array_0.02.jar file is in dist folder. In your favourite IDE just import this external .jar file and make it use :-) 

A complete video on How to use this API can be found on the link https://www.youtube.com/playlist?list=PLC5A_d190b3ej_3iTW-rYWxCTSC3w-NFi
