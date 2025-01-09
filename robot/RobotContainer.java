// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.HashMap;
import java.util.Map;

import org.assabet.aztechs157.input.layouts.Layout;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.Orchestra;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.DriveSystem;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.data.LoggingSystem;

public class RobotContainer {
  private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
  private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
  private Map<String,Subsystem> SystemMap = new HashMap<String, Subsystem>();

  private final CommandXboxController joystick = new CommandXboxController(0); // My joystick
  public final DriveSystem drivetrain = TunerConstants.createDrivetrain(); // My drivetrain

  {SystemMap.put("Drive",drivetrain);}
  private final Layout inputs = Inputs.createFromChooser();
  private final LoggingSystem loggingSystem = new LoggingSystem(SystemMap);
  private final SendableChooser<Command> autoChooser;

/* Setting up bindings for necessary control of the swerve drive platform */
private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
.withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
.withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric()
.withDriveRequestType(DriveRequestType.OpenLoopVoltage);

  private final Telemetry logger = new Telemetry(MaxSpeed);

  // Slew Rate Limiters to limit acceleration of joystick inputs
  // private final SlewRateLimiter xLimiter = new SlewRateLimiter(25);
  // private final SlewRateLimiter yLimiter = new SlewRateLimiter(25);
  // private final SlewRateLimiter rotLimiter = new SlewRateLimiter(1570);

  private Orchestra soundSystem = new Orchestra();

  public RobotContainer() {
    configureBindings();

    autoChooser = AutoBuilder.buildAutoChooser("NothingAuto");
    SmartDashboard.putData("Auto Chooser", autoChooser);
    for(int i = 0; i<4;i++){
      soundSystem.addInstrument(drivetrain.getModule(i).getDriveMotor(), 0);
      soundSystem.addInstrument(drivetrain.getModule(i).getSteerMotor(), 1);
    }
    soundSystem.loadMusic("music/e1m1.chrp");
  }

      /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
      // An example command will be run in autonomous
      return autoChooser.getSelected();
  }

  public double modifySpeed(final double speed) {
    final var modifier = 1 - inputs.axis(Inputs.precisionDrive).get();
    return speed * modifier;
}

  private void configureBindings() {
    // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
    drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
        drivetrain.applyRequest(() -> drive.withVelocityX(-joystick.getLeftY() /* TODO: check inversion */ * modifySpeed(MaxSpeed)) // Drive forward with
                                                                                           // negative Y (forward)
            .withVelocityY(-joystick.getLeftX() /* TODO: check inversion */ * modifySpeed(MaxSpeed)) // Drive left with negative X (left)
            .withRotationalRate(-joystick.getRightX() /* TODO: check inversion */ * MaxAngularRate) // Drive counterclockwise with negative X (left)
        ));

    joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
    joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

    joystick.pov(0).whileTrue(drivetrain.applyRequest(() ->
        forwardStraight.withVelocityX(0.5).withVelocityY(0))
    );
    joystick.pov(180).whileTrue(drivetrain.applyRequest(() ->
        forwardStraight.withVelocityX(-0.5).withVelocityY(0))
    );

    // Run SysId routines when holding back/start and X/Y.
    // Note that each routine should be run exactly once in a single log.
    joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
    joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
    joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
    joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

    // reset the field-centric heading on left bumper press
    joystick.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

    joystick.start().onTrue(drivetrain.runOnce(()-> {soundSystem.play();}));
    joystick.back().onTrue(drivetrain.runOnce(()->{soundSystem.pause();}));
    joystick.y().onTrue(drivetrain.runOnce(()->{soundSystem.stop();}));

    drivetrain.registerTelemetry(logger::telemeterize);
  }
  
}
