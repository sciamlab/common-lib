package com.sciamlab.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CronHelper {

	public static final String CURRENT_USER = getCurrentUser();
//	public static final String TMP_LOG_NAME = "REPLACE_LOG_NAME";
//	public static final DateFormat dateFormatForLog = new SimpleDateFormat("yyyyMMddHHmmss"); // 19990108040506

//	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//
//		emptyCronJobList(CURRENT_USER);
////		System.out.println(fixCronCommandSyntax("\""));
//	}
	
	private static String fixCronCommandSyntax(String command) {
//		command = command.replaceAll("\\\"", "\\\\\"");
		String tmp = "";
		String[] array = command.split("\"");
		boolean odd = true;
		for(String p : array){
//			logger.info(p);
			if(odd)
				p = p.replaceAll("\\%", "\\\\%").replaceAll("\\&", "\\\\&");
			else
				tmp+="\\\"";
			tmp+=p;
			if(odd){
				odd=false;
			}else{
				tmp+="\\\"";
				odd = true;
			}	
		}
		return tmp;
	}

	private static String getCurrentUser() {
		String user = execUnixCommand("echo \"$USER\"");
		// removing \n at the end
		return user.substring(0, user.length() - 1);
	}

	public static boolean emptyCronJobList(String user) {
		try {
			execUnixCommand("crontab -r -u " + user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean removeCronJob(String command, String user) {
		try {
			if (command == null || "".equals(command)) {
				throw new Exception("Command cannot be null!");
			}

			Map<String, String> jobs = getCronJobsAsMap(user);

			// empty the cron job list
			boolean ok = emptyCronJobList(user);
			if (!ok)
				return false;

			for (String job_name : jobs.keySet()) {
				String job_schedule = jobs.get(job_name);
				if (!job_name.equals(command)) {
					addCronJob(fixCronCommandSyntax(job_name), job_schedule, user);
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean editCronJobSchedule(String command, String new_schedule, String user) {
		try {
			
			if (command == null || "".equals(command)) {
				throw new Exception("Command cannot be null!");
			} else if (new_schedule == null || "".equals(new_schedule)) {
				throw new Exception("Schedule cannot be null!");
			}

			Map<String, String> jobs = getCronJobsAsMap(user);

			// empty the cron job list
			if (!emptyCronJobList(user))
				return false;

			for (String job_name : jobs.keySet()) {
				String job_schedule = jobs.get(job_name);
				if (job_name.equals(command)) {
					job_schedule = new_schedule;
				}
				addCronJob(fixCronCommandSyntax(job_name), job_schedule, user);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<String> getCronJobsAsList(String user) {
		String s = execUnixCommand("crontab -u " + user + " -l");
//		System.out.println("#"+s+"#");
		if ("".equals(s)) {
			return new ArrayList<String>();
		} else {
//			String[] jobs = s.split(System.getProperty("line.separator"));
			List<String> crons = new ArrayList<String>();
			for(String ss : s.split(System.getProperty("line.separator"))){
				if(!ss.startsWith("#"))
					crons.add(ss);
			}
			return crons;
		}
	}

	public static String getJobSchedule(String command, String user) {
		return getCronJobsAsMap(user).get(command);
	}

	public static List<String> getJobCommandByPrefix(String command_prefix, String user) {
//		System.out.println("command_prefix: "+command_prefix);
		Map<String, String> map = CronHelper.getCronJobsAsMap(user);
//		System.out.println("map: "+map);
		List<String> commands_found = new ArrayList<String>();
		for (String c : map.keySet()) {
			if (c.startsWith(command_prefix)) {
				commands_found.add(c);
			}
		}
		return commands_found;
	}

	public static Map<String, String> getCronJobsAsMap(String user) {
		Map<String, String> jobs = new HashMap<String, String>();
		for (String j : getCronJobsAsList(user)) {
//			System.out.println("$"+j+"$");
			String[] tokens = j.split(" ");
			int i = 1;
			String job_name = "";
			String job_schedule = "";
			for (String t : tokens) {
				if (i > 5) {
					job_name += (t + " ");
				} else {
					job_schedule += (t + " ");
				}
				i++;
			}

			// remove space at the end
			job_name = job_name.substring(0, job_name.length() - 1);
			job_schedule = job_schedule.substring(0, job_schedule.length() - 1);

			jobs.put(job_name, job_schedule);
		}

		return jobs;
	}

	public static boolean doesJobExist(String command, String user) {
		String job_schedule = getJobSchedule(command, user);
		return (job_schedule == null || "".equals(job_schedule)) ? false : true;
	}

	public static boolean addCronJob(String command, String schedule, String user) {
		try {

			if (command == null || "".equals(command)) {
				throw new Exception("Command cannot be null!");
			} else if (schedule == null || "".equals(schedule)) {
				throw new Exception("Schedule cannot be null!");
			} else if (doesJobExist(command, user)) {
				throw new Exception("Command execution already scheduled: "
						+ getJobSchedule(command, user));
			}
			command = fixCronCommandSyntax(command); 
			execUnixCommand("crontab -u " + user + " -l | { cat; echo \""
					+ schedule + " " + command + "\"; } | crontab -");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void killProcessList(List<Integer> pids) {
		String c = "kill -9";
		for (Integer pid : pids) {
			c += (" " + pid);
		}
		execUnixCommandNOWAIT(c);
	}
	
	public static void killProcess(Integer pid) {
		execUnixCommandNOWAIT("kill -9 "+pid);
	}
	
	public static Integer getProcessPID(String name) {
		String c = "ps aux | grep ' "+name+"'";
		String[] pids = execUnixCommand(c)
				//.replace(getCurrentUser(), "")
				.split(System.getProperty("line.separator"));
		for(String pid : pids){
			if(!pid.contains(" grep ")){ //grep is ignored as it is present in other commands related to ps
				while(pid.contains("  ")){
					pid = pid.replaceAll("  ", " ");
				}
				System.out.println(Arrays.asList(pid.split(" ")));
				return Integer.parseInt(pid.split(" ")[1]); 
//				while(pid.startsWith(" ")){
//					pid = pid.replaceFirst(" ", "");
//				}
//				return Integer.parseInt(pid.substring(0, pid.indexOf(" ")));
			}
				
		}
		return null;		
	}

	public static String execUnixCommand(String command) {// , boolean
															// console_out){
		try {

			Process p = execUnixCommandNOWAIT(command);

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
				// System.out.println(line);
			}

			p.waitFor();
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Process execUnixCommandNOWAIT(String command) {
		try {

			ProcessBuilder pb = new ProcessBuilder((Arrays.asList(new String[] {
					"/bin/bash", "-c", command })));

			Process p = pb.start();

			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Integer getPID(Process p){
		Field f;
		try {
			f = p.getClass().getDeclaredField("pid");
			f.setAccessible(true);
			return (Integer) f.get(p);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	
}