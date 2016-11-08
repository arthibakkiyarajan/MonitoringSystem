package com.simulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * @file       ServerNetwork.java
 * @author     Arthi Bakkiyarajan, arthi.bakkiarajan89@gmail.com
 * @date       October 23, 2010
 * @version    1.0
 */

/**
 * @class      ServerNetwork
 *
 * @brief      Validates user input and generates Log.
 * @details    The class takes number of servers parameter from calling object and
 * 	       creates network of servers. It assigns ip address and log file, where
 * 	       where each server can write its log content.
 */

public class ServerNetwork {

	private int numberOfServers = 1000;
	
	/**
	 * @brief      Add servers to network.
	 *
	 * @details    The function assigns ip address and log file for each server
	 *
	 * @param      serverDetails A file which points to serverdetails.txt
	 * 	       logfile 	     A string which specifies path of log file
	 * 
	 * @exception  Exception
	 *             General Exception.
	 */
	
	public void addServerToNetwork(File serverDetails, String logfile){
		
		int count = 0,subnet =0;
		try{
		    if (!serverDetails.exists()) {
		    	serverDetails.createNewFile();
		    }
		    FileWriter fileWriter = new FileWriter(serverDetails.getAbsoluteFile(),true);
		    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			while(count < numberOfServers){
				for(int i = 0; i <= 255;i++){
					String ip="192.168."+subnet;
					if(count >= numberOfServers){
						break;
					}
					count++;
					ip += "."+i;
					String serverContent = ip+" "+logfile+getLogFileName(ip)+"\n";
				    bufferedWriter.write(serverContent);
				}
				subnet++;
			}
			bufferedWriter.close();
		
		}catch(Exception e){
            System.out.println(e);
       }
	}
	
	/**
	 * @brief      Convert to proper filename format.
	 *
	 * @details    replaces . with _
	 *
	 * @param      ip a string which takes ip address.
	 *
	 * @return     File Name
	 */
	
	private String getLogFileName(String ip) {

		String ipString = ip.replaceAll("\\.", "_")+".txt";
		return ipString;
	}
	
}
