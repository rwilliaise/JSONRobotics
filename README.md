# JSONRobotics
Made with the purpose of generating iterative robots with JSON.

## Example

An example of working JSON:
```json
{
	"controllers":[{"port":0,"name":"controller"}],
	"diffdrive":{"motors":{"spark1":{"port":0},"spark2":{"port":1}}},
	"camera":{},
	"teleOperated":{"periodic":["diffDrive.tankDrive(controller.getX(GeneralHID.Hand.kLeft),controller.getY(GeneralHID.Hand.kLeft));"]}
}
```

This should output into
```java
package org.usfirst.frc.team6503.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.cscore.UsbCamera;

public class Robot extends IterativeRobot {
	public XboxController controller;
	public DifferentialDrive diffdrive;

	@Override
	public void robotInit() {

		// Initalize XboxController controller
		controller = new XboxController(0);

		// Initialize camera and set resolution to 640 by 480
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(640, 480);

		// Initialize DifferentialDrive
		diffdrive = new DifferentialDrive(new Spark(0), new Spark(1))
	}

	@Override
	public void teleopPeriodic() {
		diffDrive.tankDrive(controller.getX(GeneralHID.Hand.kLeft),controller.getY(GeneralHID.Hand.kLeft));
	}
}
```
