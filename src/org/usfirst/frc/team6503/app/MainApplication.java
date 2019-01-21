package org.usfirst.frc.team6503.app;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;

import org.usfirst.frc.team6503.app.JSONRobotReader.JSONPeripheral;
import org.usfirst.frc.team6503.app.JSONRobotReader.JSONRobot;

public class MainApplication extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Defaults
	private static final File DESTINATION = new File(
			"C:/JSONRobot/" + LocalDate.now().toString() + "/export/Robot.json");
	private static final File SAVE_DESTINATION = new File("C:/JSONRobot/" + LocalDate.now().toString() + "/Robot.json");

	// private static final String MATCHES =
	// "(\\W)*(joysticks|controllers|gyros|camera|port|name|[{}]|\\d)";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DESTINATION.getParentFile().mkdirs();
		SAVE_DESTINATION.getParentFile().mkdirs();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainApplication frame = new MainApplication();
					// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainApplication() {
		setTitle("JSONRobot");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mnFile.add(mntmLoad);

		JMenuItem mntmGenerate = new JMenuItem("Generate");
		mntmGenerate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
		mnFile.add(mntmGenerate);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmWiki = new JMenuItem("Wiki");
		mntmWiki.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/rajawilliams/JSONRobotics/wiki"));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});

		JMenuItem mntmGithub = new JMenuItem("Github");
		mntmGithub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/rajawilliams/JSONRobotics"));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmGithub);
		mnHelp.add(mntmWiki);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JTextPane editorPane = new JTextPane();
		scrollPane.setViewportView(editorPane);
		((AbstractDocument) editorPane.getDocument())
				.setDocumentFilter(new StylizedDocumentFilter(editorPane, DefaultStyleFilter.getInstance()));
		editorPane.setText("{\r\n\r\n}");
		mntmGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process(editorPane.getText());
			}
		});
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				processSave(editorPane.getText());
			}
		});
		mntmLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				processLoad(editorPane);
			}
		});
	}

	public void processLoad(JTextPane editorPane) {
		try {
			load(editorPane);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public void processSave(String text) {
		try {
			save(text);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public void process(String text) {
		try {
			JSONRobot robot = JSONRobotReader.toJson(text);
			generate(robot);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	// import edu.wpi.first.wpilibj.IterativeRobot;
	// import edu.wpi.first.wpilibj.Joystick;
	// import edu.wpi.first.wpilibj.CameraServer;
	// import edu.wpi.first.wpilibj.Spark;
	// import edu.wpi.first.wpilibj.XboxController;
	// import edu.wpi.first.wpilibj.drive.DifferentialDrive;
	// import edu.wpi.first.wpilibj.GenericHID;

	public void load(JTextPane editorPane) throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(SAVE_DESTINATION.getParentFile());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File importe = chooser.getSelectedFile();
			Scanner scanner = new Scanner(importe);
			StringBuilder builder = new StringBuilder();
			while (scanner.hasNextLine()) {
				String str = scanner.nextLine();
				builder.append(str + "\n");
				System.out.println(str);
			}
			scanner.close();
			editorPane.setText(builder.toString());
		}
	}

	public void save(String text) throws FileNotFoundException {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(SAVE_DESTINATION.getParentFile());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String name = JOptionPane.showInputDialog("What is the name of the file?");
			if (name != null) {
				name = name.replaceAll(".json", "");
				File dest = new File(chooser.getSelectedFile().getPath() + "/" + name + ".json");
				PrintWriter writer = new PrintWriter(dest);
				writer.write(text);
				writer.flush();
				writer.close();
				JOptionPane.showMessageDialog(this, "Saved " + name + ".json at " + dest.getAbsolutePath());
			}
		}
	}

	public void generate(JSONRobot robot) throws FileNotFoundException {
		if (robot == null) {
			JOptionPane.showMessageDialog(this, "No file has been created.");
			return;
		}
		File export = new File(DESTINATION.getPath() + "/Robot.java");
		PrintWriter writer = new PrintWriter(export);
		writer.println("package org.usfirst.frc.team6503.robot;");
		writer.println();
		writer.println("import edu.wpi.first.wpilibj.IterativeRobot;");
		writer.println("import edu.wpi.first.wpilibj.Joystick;");
		writer.println("import edu.wpi.first.wpilibj.CameraServer;");
		writer.println("import edu.wpi.first.wpilibj.Spark;");
		writer.println("import edu.wpi.first.wpilibj.XboxController;");
		writer.println("import edu.wpi.first.wpilibj.drive.DifferentialDrive;");
		writer.println("import edu.wpi.first.wpilibj.GenericHID;");
		writer.println();
		writer.println("import edu.wpi.cscore.UsbCamera;");
		writer.println();
		writer.println("public class Robot extends IterativeRobot {");

		// Initialize variables
		HashMap<String, Integer> map = new HashMap<>();
		final String varFormat = "\tpublic %s %s;";
		putPeripherals("Joystick", robot.joysticks, map, writer, varFormat);
		putPeripherals("XboxController", robot.controllers, map, writer, varFormat);
		putPeripherals("ADXRS450_Gyro", robot.gyros, map, writer, varFormat);
		if (robot.diffdrive != null) {
			writer.println("\tpublic DifferentialDrive diffdrive;");
		}

		// robotInit
		writer.println();
		writer.println("\t@Override");
		writer.println("\tpublic void robotInit() {");
		final String varDecFormat = "\t\t%s = new %s(%s);";
		HashMap<String, Integer> varDecMap = new HashMap<>();
		putPeripherals("Joystick", robot.joysticks, varDecMap, writer, varDecFormat, true);
		putPeripherals("XboxController", robot.controllers, varDecMap, writer, varDecFormat, true);
		putPeripherals("ADXRS450_Gyro", robot.gyros, varDecMap, writer, varDecFormat, true);
		if (robot.camera != null) {
			writer.println();
			writer.println(String.format("\t\t// Initialize camera and set resolution to %s by %s", robot.camera.resx,
					robot.camera.resy));
			writer.println("\t\tUsbCamera camera = CameraServer.getInstance().startAutomaticCapture();");
			writer.println(String.format("\t\tcamera.setResolution(%s, %s);", robot.camera.resx, robot.camera.resy));
			writer.println();
		}
		if (robot.diffdrive != null) {
			final String spark = "Spark";
			final String victor = "Victor";
			JSONPeripheral motor1 = null;
			String type1 = spark;
			JSONPeripheral motor2 = null;
			String type2 = spark;
			if (robot.diffdrive.motors.spark1 != null) {
				motor1 = robot.diffdrive.motors.spark1;
			} else if (robot.diffdrive.motors.victor1 != null) {
				motor1 = robot.diffdrive.motors.victor1;
				type1 = victor;
			}
			if (robot.diffdrive.motors.spark2 != null) {
				motor2 = robot.diffdrive.motors.spark2;
			} else if (robot.diffdrive.motors.victor2 != null) {
				motor2 = robot.diffdrive.motors.victor2;
				type2 = victor;
			}
			if (motor1 != null && motor2 != null) {
				writer.println("\t\t// Initialize DifferentialDrive");
				writer.println(String.format("\t\tdiffdrive = new DifferentialDrive(new %s(%s), new %s(%s));", type1,
						motor1.port, type2, motor2.port));
			}
		}
		writer.println("\t}");
		putPeriods(robot, writer);
		writer.println("}");
		writer.flush();
		writer.close();
		JOptionPane.showMessageDialog(this, "Saved Robot.java at " + export.getAbsolutePath());
	}

	private void putPeripherals(String type, ArrayList<JSONPeripheral> peripherals, HashMap<String, Integer> map,
			PrintWriter writer, String format) {
		for (JSONPeripheral peripheral : peripherals) {
			String name;
			if (!map.containsKey(peripheral.name)) {
				map.put(peripheral.name, 1);
				name = peripheral.name;
			} else {
				name = peripheral.name + map.get(peripheral.name);
				map.put(peripheral.name, map.get(peripheral.name) + 1);
			}
			// "\tpublic " + type + " " + name + ";"
			writer.println(String.format(format, type, name));
		}
	}

	private void putPeripherals(String type, ArrayList<JSONPeripheral> peripherals, HashMap<String, Integer> map,
			PrintWriter writer, String format, boolean includePort) {
		for (JSONPeripheral peripheral : peripherals) {
			String name;
			if (!map.containsKey(peripheral.name)) {
				map.put(peripheral.name, 1);
				name = peripheral.name;
			} else {
				name = peripheral.name + map.get(peripheral.name);
				map.put(peripheral.name, map.get(peripheral.name) + 1);
			}
			// "\tpublic " + type + " " + name + ";"
			if (includePort) {
				writer.println();
				writer.println(String.format("\t\t// Initalize %s %s", type, name));
				writer.println(String.format(format, name, type, peripheral.port));
			} else {
				writer.println(String.format(format, type, name));
			}
		}
	}

	private void putPeriods(JSONRobot robot, PrintWriter writer) {
		if (robot.autonomous != null) {
			if (robot.autonomous.init != null) {
				writer.println();
				writer.println("\t@Override");
				writer.println("\tpublic void autonomusInit() {");
				for (String str : robot.autonomous.init) {
					writer.println("\t\t" + str);
				}
				writer.println("\t}");
			}
			if (robot.autonomous.periodic != null) {
				writer.println();
				writer.println("\t@Override");
				writer.println("\tpublic void autonomusPeriodic() {");
				for (String str : robot.autonomous.periodic) {
					writer.println("\t\t" + str);
				}
				writer.println("\t}");
			}
		}
		if (robot.teleOperated != null) {
			if (robot.teleOperated.init != null) {
				writer.println();
				writer.println("\t@Override");
				writer.println("\tpublic void teleopInit() {");
				for (String str : robot.teleOperated.init) {
					writer.println("\t\t" + str);
				}
				writer.println("\t}");
			}
			if (robot.teleOperated.periodic != null) {
				writer.println();
				writer.println("\t@Override");
				writer.println("\tpublic void teleopPeriodic() {");
				for (String str : robot.teleOperated.periodic) {
					writer.println("\t\t" + str);
				}
				writer.println("\t}");
			}
		}
	}

}
