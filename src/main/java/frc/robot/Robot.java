// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.VisionConstants;
import frc.utilities.PrintLimiter;

public class Robot extends TimedRobot {

  private static PIDController aimingPID = VisionConstants.AIMING_PID;

  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  private PrintLimiter limiter = new PrintLimiter(1000);
  public static double rotationValue;

  public Robot() {
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {

    // Read in relevant data from the Camera
        boolean targetVisible = false;
        double targetYaw = 0.0;
        var results = m_robotContainer.visionSystem.findTargets();
        
        if (results != null) {
            // Camera processed a new frame since last
            // Get the last one in the list.
            var result = results.get(results.size() - 1);
            if (m_robotContainer.visionSystem.getTargetID(result) == 3) {
                // Found Tag 7, record its information
                targetYaw = m_robotContainer.visionSystem.getTargetYaw(result);
                targetVisible = true;
            }
        }

        boolean joystickX = m_robotContainer.joystick.x().getAsBoolean();
        if (joystickX && targetVisible) {
            double turn_vision = -1.0 * targetYaw * 0.05 * m_robotContainer.MaxAngularRate;
            // double turn_vision = aimingPID.calculate(0, targetYaw) * m_robotContainer.MaxAngularRate;
            SmartDashboard.putNumber("yaw", targetYaw);
            SmartDashboard.putNumber("turn", turn_vision);
            rotationValue = turn_vision;
        }
        else {
          rotationValue = MathUtil.applyDeadband(-m_robotContainer.joystick.getRightX(), ControllerConstants.RIGHT_X_DEADBAND) * m_robotContainer.MaxAngularRate;

        }

        SmartDashboard.putBoolean("Vision Target Visible", targetVisible);
        SmartDashboard.putNumber("joystick command turn", MathUtil.applyDeadband(-m_robotContainer.joystick.getRightX(), ControllerConstants.RIGHT_X_DEADBAND) * m_robotContainer.MaxAngularRate);

  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {}
}
