package com.simulator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @file       GenerateLogs.java
 * @author     Arthi Bakkiyarajan, arthi.bakkiarajan89@gmail.com
 * @date       October 23, 2010
 * @version    1.0
 */

/**
 * @class      GenerateLogs
 *
 * @brief      Validates user input and generates Log.
 * @details    The file takes LOG_PATH from the user and verifies if the path is a valid path or not. 
 * 	       It also gets date from user for which log has to be generated. If the user doesn't give
 * 	       a valid date, it uses default date to generate logs.
 */

public class GenerateLogs {

	
	static String logfile;
	File serverDetails;
	static Date date;
	static SimpleDateFormat format;
	static String dateString = "2014-10-31 00:00";
	
	static{
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			date = format.parse("2014-10-31 00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public GenerateLogs(){
		File file = new File(logfile);
		for(File f: file.listFiles()){
			f.delete();
		}
		serverDetails = new File(logfile+"serverDetails.txt");
		
	}
	
	/**
	 * @brief      Add servers to network.
	 *
	 * @details    It constructs server network by instantiating ServerNetwork object.
	 *             It passes two arguments to the function: File and Logpath
	 *
	 */
	
	private void addServers(){

		ServerNetwork serverNetwork = new ServerNetwork();
		serverNetwork.addServerToNetwork(serverDetails, logfile);
	}
	
	/**
	 * @brief      Add logs to log files.
	 *
	 * @details    It instantiates object which generates log content and adds it
	 * 	       to log files.
	 *
	 */
	
	private void addLogs(){
		
		LogGenerator logGenerator = new LogGenerator(date,serverDetails,dateString);
		logGenerator.addLogsToLogFiles();
	}

	/**
	 * @brief      Adds server to network and adds logs.
	 *
	 * @details    Instantiates object to simulate servers and to add log content.
	 * 	       Generates log and calculates time taken for log generation
	 *
	 * @param      Scanner object.
	 */
	
	private static void generateLogs(Scanner in) {
		
		System.out.println("Please enter date for which logs have to be generated(Format: yyyy-MM-dd). If date is not specified or if the format is wrong , then log will be generated for '2014-10-31'");
		String input = in.nextLine();
		boolean isValid = true;
		if(input.trim().length()!=0){
			try{
				date = format.parse(input.trim()+" 00:00");
				dateString = input.trim()+" 00:00";
				isValid = validateDate(input.trim());
				if(!isValid){
					System.out.println("Wrong Format/Date. Logs are generated for '2014-10-31'");
				}
			} catch (ParseException e) {
				System.out.println("Wrong Format/Date. Logs are generated for '2014-10-31'");
				System.out.println(date);
			}
		}
		setDateForLogs();
	}

	/**
	 * @brief      Sets the date.
	 *
	 * @details    Adds logs and servers
	 */
	
	private static void setDateForLogs() {
		System.out.println("Generating Logs....");
		GenerateLogs gl = new GenerateLogs();
		long start = System.currentTimeMillis();
		gl.addServers();
		gl.addLogs();
		long end = System.currentTimeMillis();
		float time = (end-start)/1000;
		System.out.println("Done");
		
	}

	/**
	 * @brief      Validates Date.
	 *
	 * @details    Checks if date is in proper format. Displays error message
	 * 	       if date is null or contains special characters like @,%$ etc.
	 *
	 * @param      input String containing the input
	 *
	 */
	
	private static boolean validateDate(String input) {
		String [] dateArr = input.split("-");
		if(dateArr[0].trim().length()==0 ||dateArr[1].trim().length()==0||dateArr[2].trim().length()==0){
			return false;
		}
		int year = Integer.parseInt(dateArr[0]);
		int month = Integer.parseInt(dateArr[1]);
		int day = Integer.parseInt(dateArr[2]);
		
		if(year <  0){
			return false;
		}
		if(month < 0 || month >12){
			return false;
		}
		if(day < 0 || day >31){
			return false;
		}
		return true;
	}
		

	/**
	 * @brief      Gets user Input and calls log generator.
	 *
	 * @details    Gets log path and date from user and performs validation.
	 * 			   If inputs are valid generator is called or else error message
	 * 			   is displayed. 
	 *
	 * @param      args Arguments input by user
	 *
	 * @exception  Exception
	 *             General exceptions.
	 */
	
	public static void main(String [] args){
		
		Scanner in = new Scanner(System.in);
		String input = null;
		while(true){
			try{
				if(args.length==1 || (input!=null && input.trim().length()>0)){
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
					if (file.isDirectory() && file.canWrite()){
						generateLogs(in);
						break;
					}
					else{
						System.out.println("Invalid path/No permission. Please enter a valid file path to generate logs.");
					}
				}
				else{
					System.out.println("Please enter a file path to generate logs.");
				}
			}catch(Exception ex){
				System.out.println("Invalid path. Please enter a valid file path to generate logs.");
			}
			input = in.nextLine();
		}
	}
}
