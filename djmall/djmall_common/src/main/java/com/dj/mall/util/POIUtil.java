package com.dj.mall.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

public class POIUtil {

	/**
	 * 
	 * @param file 待解析的Excel
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Workbook getExcelWorkBook(InputStream file, String fileName) throws Exception {
		// 获取一个工作簿
		Workbook workbook = null;
		// 判断excel的版本 2007以前版本需要使用HSSFWorkbook
		if(fileName.endsWith(".xls")){
			workbook = new HSSFWorkbook(file);
		}else if(fileName.endsWith(".xlsx")){
			workbook = new XSSFWorkbook(file);
		}
		return workbook;
	}
	
	/**
	 * 数据转换
	 * @param cell
	 * @return
	 */
	public static String getStringCellValue(Cell cell){
		String strCell = "";  
        if(cell==null) return strCell;  
        
        switch (cell.getCellTypeEnum()) {  
            case STRING: // get String data   
                strCell = cell.getRichStringCellValue().getString().trim();  
                break;  
            case NUMERIC:    // get date or number data   
                if (DateUtil.isCellDateFormatted(cell)) {  
                    strCell = com.dj.mall.util.DateUtil.format(cell.getDateCellValue(), com.dj.mall.util.DateUtil.hour24HPatternMillisecond);
                } else {  
                    strCell = String.valueOf(cell.getNumericCellValue());  
                }  
                break;  
            case BOOLEAN:    // get boolean data   
                strCell = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case FORMULA:    // get expression data   
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();  
//                evaluator.evaluateFormulaCell(cell);
                CellValue cellValue = evaluator.evaluate(cell);  
                strCell = String.valueOf(cellValue.getNumberValue()) ;  
                break;  
            default:  
                strCell = "";  
        }  
        return strCell;  
	}
	
	public static void main(String[] args) throws Exception {
		// 读取
//		InputStream file = new FileInputStream("C:/Users/turingdj/Desktop/1.xlsx");
//		// 拿到工作表 相当于打开Excel
//		Workbook workbook = getExcelWorkBook(file, "1.xlsx");
//		// excel全部sheet个数
//		Integer sheetCount = workbook.getNumberOfSheets();
//		for (int i = 0; i < sheetCount; i++) {
//			// 获取单个Sheet页 下标从0开始
//			Sheet sheet = workbook.getSheetAt(i);
//			// 获取当前sheet总行数
//			Integer rowCount = sheet.getPhysicalNumberOfRows();
//			// 得到一行 下标从0开始
//			for (int j = 1; j < rowCount; j++) {
//				Row row = sheet.getRow(j);
//				// 当前行的总列数
//				Integer cellCount = row.getPhysicalNumberOfCells();
//				for (int k = 0; k < cellCount; k++) {
//					Cell cell = row.getCell(k);
//					System.out.print(getStringCellValue(cell) + "\t");
//				}
//				System.out.println();
//			}
//		}
		// 写入
//		List<User> list =  new ArrayList<>();
//		for (int i = 0; i < 5; i++) {
//			User user = new User();
//			user.setId(i);
//			user.setUsername("dj" + i);
//			list.add(user);
//		}
//		// 创建一个空白Excel
//		Workbook wookbook = new XSSFWorkbook();
//		// 创建一个sheet
//		Sheet sheet = wookbook.createSheet("user");
//		// 插入表头
//		Row titleRow = sheet.createRow(0);
//		titleRow.createCell(0).setCellValue("编号");
//		titleRow.createCell(1).setCellValue("用户名");
//		// 插入table
//		for (int i = 0; i < list.size(); i++) {
//			Row tableRow = sheet.createRow(i + 1);
//			tableRow.createCell(0).setCellValue(list.get(i).getId());
//			tableRow.createCell(1).setCellValue(list.get(i).getUsername());
//		}
//		// 输出
//		OutputStream out = new FileOutputStream("C:/Users/turingdj/Desktop/user.xlsx");
//		wookbook.write(out);
//		out.close();
	}
}
