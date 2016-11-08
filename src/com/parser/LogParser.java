package com.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @mainpage   MONITORING SYSTEM
 *
 * System to monitor log files to determine CPU usage of the servers
 * 
 **/

/**
 * @file	   LogParser.java
 * @author     Arthi Bakkiyarajan, arthi.bakkiarajan89@gmail.com
 * @date       October 23, 2010
 * @version    1.0
 */

/**
 * @class	   LogParser
 *
 * @brief      Parses Log files and displays result to user.
 * @details    Parses log files and provides a tool to user. The
 * 			   user can query using the tool to display resuls
 */

public class LogParser {
	
	HashMap<String,TreeMap> logHashMap = new HashMap<String,TreeMap>();
	static String logfile;
	
	
	/**
	 * @brief      Parses log files.
	 *
	 * @details    Parses all log files and constructs Map which
	 * 			   stores timestamp and cpu usage
	 *	 
	 * @return     status A boolean value.
	 *
	 * @exception  IOException
	 *             If the file doesn't exist or path is wrong.
	 */

	private boolean parseLogs(){
		String line;
		String logPath;
		String ip = null;
		String [] serverDetail;

		try{
			File file = new File(logfile+"serverDetails.txt");
			if(file.exists()){
				BufferedReader logBufferedReader = new BufferedReader(new FileReader(file));
				line = logBufferedReader.readLine();
				while (line != null) {
			    	serverDetail = line.split(" ");
					logPath = serverDetail[1];
					TreeMap<Date,String> cpu0UsageTreeMap = new TreeMap<Date,String>();
					TreeMap<Date,String> cpu1UsageTreeMap = new TreeMap<Date,String>();
					try {
						
						BufferedReader logReader = new BufferedReader(new FileReader(logPath));
						String log = logReader.readLine();
					    while (log != null) {

					    	String [] logParam = log.split(" ");
					        if(logParam[2].equals("0")){
					        	Long id = Long.parseLong(logParam[0]);
					        	Date date = new Date();
					        	date.setTime(id);
					        	cpu0UsageTreeMap.put(date, logParam[3]+"%");
					        }
					        else{
					        	Long id = Long.parseLong(logParam[0]);
					        	Date date = new Date();
					        	date.setTime(id);
					        	cpu1UsageTreeMap.put(date, logParam[3]+"%");
					        }
					        
					        log = logReader.readLine();
					    }
					    logReader.close();
					    ip = serverDetail[0]+".0";
						logHashMap.put(ip, cpu0UsageTreeMap);
						ip = serverDetail[0]+".1";
						logHashMap.put(ip, cpu1UsageTreeMap);
					} 
					catch (IOException e) {
							e.printStackTrace();
					}
					line = logBufferedReader.readLine();
			    }
			    
				logBufferedReader.close();
				return true;
			}else{
				System.out.println("File don't Exist!");
				System.out.println("Please enter a valid log path.");
				return false;
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @brief      Filters HashMap based on Query.
	 *
	 * @details    It gets server ip, start date and end
	 * 			   date and applies the filter on Hashmap
	 *
	 * @param      serverIp A string which stores ip address
	 * 	       start 	A date which stores start date
	 *             end 	A date object which stores end date
	 */

	public void findLogsWithinInterval(String serverIp, Date start, Date end){
		
			TreeMap <Date,String>fullTreeMap = logHashMap.get(serverIp);
			SortedMap requiredTreeMap = fullTreeMap.subMap(start, end);
			System.out.println(requiredTreeMap);
	
	}

	/**
	 * @brief      Accepts User input.
	 *
	 * @details    Accepts user input and validates it. Displays
	 * 			   error message if the input is invalid and calls parselogs()
	 * 			   method if input is valid
	 *
	 * @param      args Arguments input by user
	 */

	public static void main(String [] args){
		Scanner in = new Scanner(System.in);
		String input = null;
		while(true){
			if(args.length==1 || input!=null){
				if(input!=null){
					logfile = input;
				}
				else{
					logfile = args[0];	
				}
				if(!logfile.endsWith("///")){
					logfile+="///";
				}
				File file = new File(logfile);
				if (file.isDirectory()){
					LogParser lp = new LogParser();
					Long s = System.currentTimeMillis();
					System.out.println("Please wait while loading the tool...");
					boolean status = lp.parseLogs();
					if(status){
						Long e = System.currentTimeMillis();
						float time = (e-s)/1000;
						boolean quit = parseQueryString(in,lp);
						if(quit){
							break;
						}
							
					}
				}
				else{
					System.out.println("Please enter a valid log path.");
				}
			}
			else{
				System.out.println("Please enter the log path.");
			}
			input = in.nextLine();
		}
	}

	/**
	 * @brief      Parses query string from user.
	 *
	 * @details    Validates if query string is in proper format. Checks if ip
	 * 			   address, cpu id and dates are valid
	 *
	 * @param      in Scanner object to read from stdin
	 * 			   LogParser object
         *             lp LogParser object
	 *
	 *
	 * @exception  ParseException
	 *             Exception while parsing date.
	 */

	private static boolean parseQueryString(Scanner in, LogParser lp) {
		
		String input;
		String [] queryString;
		String ip;
		String startDate;
		String startTime;
		String endDate;
		String endTime;
		String cpu;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		while(true){
			System.out.println("Please enter the query string.");
			input = in.nextLine();
			if(input!=null && input.trim().length()!=0){
				queryString = input.split(" ");
				if(input.equalsIgnoreCase("EXIT")){
					break;
				}
				if(queryString.length!=7 || !queryString[0].equals("QUERY")){
					System.out.println("Query should be in QUERY [IP] [CPU] [START_DATE] [START_TIME] [END_DATE] [END_TIME] format.");
				}
				else{
					ip = queryString[1];
					cpu = queryString[2];
					startDate = queryString[3];
					startTime = queryString[4];
					endDate = queryString[5];
					endTime = queryString[6];
					
					if(!ip.startsWith("192.168.0") && !ip.startsWith("192.168.1") && !ip.startsWith("192.168.2") && !ip.startsWith("192.168.3")){
						System.out.println("Please enter a valid IP");
					}
					
					else if(!cpu.equals("0") && !cpu.equals("1")){
						System.out.println("Please enter a valid CPU Id");
					}

					else{
						try {
							Date start = format.parse(startDate+" "+startTime);
							Date end = format.parse(endDate+" "+endTime);
							if(validate(startDate,startTime) || validate(endDate,endTime)){
								System.out.println("Start date/End date is not valid.");
							}
							else if(end.getTime() > start.getTime()){
								String serverIp = ip+"."+cpu;
								lp.findLogsWithinInterval(serverIp, start, end);
							}
							else{
								System.out.println("End date and time should be greater than Start date and time");
							}
		
						} catch (ParseException e) {
							System.out.println("Start/End Date should be in YYYY-MM-DD HH:MM format");
						}
					}
				}
				
			}
			
		}
		return true;
	}

        /**
	 * @brief      Validates date
	 *
	 * @details    Validates if day, month and year are within bounds
	 *
	 * @param      date A string which has the date
         *             time A string which has the time
	 *
	 * @return     boolean Returns if date is valid or not
	 *
	 */

	private static boolean validate(String date,String time) {
		
		String [] dateArr = date.split("-");
		String [] timeArr = time.split(":");
		if(dateArr[0].trim().length()==0 ||dateArr[1].trim().length()==0||dateArr[2].trim().length()==0||timeArr[0].trim().length()==0||timeArr[1].trim().length()==0){
			return true;
		}
		int year = Integer.parseInt(dateArr[0]);
		int month = Integer.parseInt(dateArr[1]);
		int day = Integer.parseInt(dateArr[2]);
		int hr = Integer.parseInt(timeArr[0]);
		int min = Integer.parseInt(timeArr[1]);
		
		if(year <  0){
			return true;
		}
		if(month < 0 || month >12){
			return true;
		}
		if(day < 0 || day >31){
			return true;
		}
		if(hr < 0 || hr > 23){
			return true;
		}
		if(min < 0 || min >60){
			return true;
		}
		return false;
	}
	
}
