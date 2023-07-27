package com.dyntrace.download.metric.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class DynatraceDataExpUtil {
         private String urlAPI = "";
         private final int MIN_SIZE = 1000000;
         private List<String> data = new ArrayList<String>(MIN_SIZE);
         private int statusCode = 200;
         private String error = "";
         private int responseSize = 0;
         
         public int getResponseSize() {
        	 return this.responseSize;
         }

         /**
          * 
          * @return
          */
         public List<String> getData(){
        	 return this.data;
         }
         /**
          * 
          * @return
          */
         public int getStatusCode() {
        	 return this.statusCode;
         }
         
         /**
          * 
          * @return
          */
         public String getError() {
        	return this.error; 
         }
         /**
          * 
          * @param url
          * @param metricSelector
          * @param resolution
          * @param fromTimeStamp
          * @param toTimeStamp
          * @param entitySelector
          * @param mzSelector
          */
	     public boolean setURLAPI(String url,
	    		 			   String metricSelector,
	    		 			   String resolution,
	    		 			   long fromTimeStamp,
	    		 			   long toTimeStamp,
	    		 			   String entitySelector,
	    		 			   String mzSelector) {
	    	 boolean result = false;
	    	 try {
	 	    	String builder = "";
		    	 int noOfParamAppended = 0;
		    	 builder += url;
		    	
		    	 //Append metric selector
		    	if(!metricSelector.isEmpty()) {
		    		builder += "?metricSelector=" + metricSelector;
		    		noOfParamAppended +=1;
		    	}
		    	
		    	//Append resolution
		    	if(!resolution.isEmpty()) {
		    		if(noOfParamAppended > 0) {
		    			builder += "&resolution=" + resolution;
		    		}else {
		    			builder += "?resolution=" + resolution;
		    		}
		    		noOfParamAppended +=1;
		    	}
		    	
		    	
		    	//Append fromTimeStamp
		    	if(fromTimeStamp > -1) {
		    		if(noOfParamAppended > 0) {
		    			builder += "&from=" + fromTimeStamp;
		    		}else {
		    			builder += "?from=" + fromTimeStamp;
		    		}
		    		noOfParamAppended +=1;			
		    	}
		    	
		    	//Append toTimeStamp
		    	if(toTimeStamp > -1) {
		    		if(noOfParamAppended > 0) {
		    			builder += "&to=" + toTimeStamp;
		    		}else {
		    			builder += "?to=" + toTimeStamp;
		    		}
		    		noOfParamAppended +=1;
		    	}
		    	
		    	
		    	//Append entitySelector
		    	if(!entitySelector.isEmpty()) {
		    		if(noOfParamAppended > 0) {
		    			builder += "&entitySelector=" + entitySelector;
		    		}else {
		    			builder += "?entitySelector=" + entitySelector;
		    		}
		    		noOfParamAppended +=1;	
		    	}
		    	
		    	//Append mzSelector
		    	if(!mzSelector.isEmpty()) {
		    		if(noOfParamAppended > 0) {
		    			builder += "&mzSelector=" + mzSelector;
		    		}else {
		    			builder += "?mzSelector=" + mzSelector;
		    		}
		    		noOfParamAppended +=1;		
		    	}
		    	 
		    	this.urlAPI = builder;
		    	result = true;
	    	 }catch(Exception e) {
	    		 
	    		 this.error = e.getMessage();
	    	 }
	    	 return result;
	     }
	     
	     /**
	      * 
	      * @param url
	      */
	     public void setURLAPI(String url) {
	    	 this.urlAPI = url;
	     }
	     public boolean download(  
	    		 				 String token,
	    		 				 FileType fileType
	    		 				 ) {	 
		 boolean result = false;
		 try {
	            URL url = new URL(this.urlAPI);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");
	            if(fileType == FileType.CSV) {
	            	conn.setRequestProperty("Accept", "text/csv, application/json; q=0.1");
	            }else {
	            	conn.setRequestProperty("Accept", "application/json");
	            }
	            
	            conn.setRequestProperty("Authorization", "Api-Token " + token);
	            int responseCode = conn.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) {
	                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                String inputLine;

	                while ((inputLine = in.readLine()) != null) {
	                	data.add(inputLine);
	                	this.responseSize +=1;
	                }
	                in.close(); 
	            } else {

	            	this.statusCode = responseCode;
	            } 
			 result = true;
		 }catch(Exception e) {
			 this.statusCode = 400;
			 this.error = e.getMessage();
		 }
		 return result;
	 }
}
