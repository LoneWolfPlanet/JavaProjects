package com.dyntrace.download.metric.util;

import java.util.ArrayList;
import java.util.List;

import java.text.DecimalFormat;

public class CSVUtil {

	/**
	 * 
	 * @param data
	 * @param actualSize
	 * @return
	 */
	 public StringBuilder format(List<String> data, int actualSize) {
		 StringBuilder output = new StringBuilder();
		 try {
			 String line = "";
			 for(int i = 1 ;i<actualSize; i++) {
				 String[] parts = csvParseLine(data.get(i));
				 String timestamp = parts[1].replace("-", "/");
				 double cpu = Double.parseDouble(parts[2]);
				 DecimalFormat decimalFormat = new DecimalFormat("#.##");
				 String cpuStr = decimalFormat.format(cpu) + "%";
				 line = timestamp + "," +cpuStr+ "\n";
				 output.append(line);
			 }
			 
		 }catch(Exception e) {
			 
		 }
		 
		 return output;
	 }
	 
	 /**
	  * 
	  * @param input
	  * @return
	  */
	  private String[] csvParseLine(String input) {
	        boolean inQuotes = false;
	        StringBuilder currentValue = new StringBuilder();
	        List<String> result = new ArrayList<>();

	        for (char c : input.toCharArray()) {
	            if (c == '"') {
	                inQuotes = !inQuotes;
	            } else if (c == ',' && !inQuotes) {
	                result.add(currentValue.toString().trim());
	                currentValue = new StringBuilder();
	            } else {
	                currentValue.append(c);
	            }
	        }

	        result.add(currentValue.toString().trim());

	        return result.toArray(new String[0]);
	    }
}
