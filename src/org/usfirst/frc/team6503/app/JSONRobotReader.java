package org.usfirst.frc.team6503.app;

import java.util.ArrayList;

import com.google.gson.Gson;

public class JSONRobotReader {

	public static final Gson gson = new Gson();

	public static JSONRobot toJson(String in) {
		return gson.fromJson(in, JSONRobot.class);
	}

	public static class JSONRobot {

		public ArrayList<JSONPeripheral> joysticks = new ArrayList<>();
		public ArrayList<JSONPeripheral> controllers = new ArrayList<>();
		public ArrayList<JSONPeripheral> gyros = new ArrayList<>();

		public JSONCamera camera;
		public JSONDiffDrive diffdrive;

		public JSONPeriod autonomous;
		public JSONPeriod teleOperated;
	}

	public static class JSONPeripheral {

		public int port = 0;

		public String name = "peripheral";

	}

	public static class JSONCamera {

		public int resx = 640;
		public int resy = 480;
	}

	public static class JSONDiffDrive {

		public ArrayList<JSONPeripheral> sparks = new ArrayList<>();
	}

	public static class JSONPeriod {
		public ArrayList<String> init;
		public ArrayList<String> periodic;
	}

}
