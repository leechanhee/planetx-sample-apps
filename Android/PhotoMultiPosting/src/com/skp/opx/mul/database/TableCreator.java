package com.skp.opx.mul.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @설명 : DB Table 생성 Class
 * @클래스명 : TableCreator 
 *
 */
public final class TableCreator {

	private static String createTableString(Class <?> classTemplate) {
		
		String strMakeQuery = "create table ";
		strMakeQuery += classTemplate.getSimpleName() + " (";
		strMakeQuery += "ID INTEGER PRIMARY KEY AUTOINCREMENT, ";
		
		Field fieldArray[] = classTemplate.getFields();
		
		for(int field = 0, maxfield = fieldArray.length; field < maxfield; field++) {
			
			strMakeQuery += fieldArray[field].getName() + " ";
			
			if(fieldArray[field].getType() == int.class ||fieldArray[field].getType() == long.class ) {
				strMakeQuery += "INTEGER";
			} else if(fieldArray[field].getType() == String.class) {
				strMakeQuery += "TEXT";
			}
			
			if(field == maxfield -1) {
				strMakeQuery += " ) ";
			} else {
				strMakeQuery += ", ";
			}
		}
		
		return strMakeQuery;
	}
	
	public static List<String> getCreateTableDDL() {

		ArrayList<String> strDllArrayList = new ArrayList<String>();
		//여기에 테이블을 추가하시요... 아래와 같이
//		strDllArrayList.add(createTableString(EntitySendBox.class));
		strDllArrayList.add(createTableString(EntityPostingDB.class));	//Posting Info table
		return strDllArrayList;		
	}
}