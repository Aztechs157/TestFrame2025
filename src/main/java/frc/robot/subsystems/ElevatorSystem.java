// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;

public class ElevatorSystem extends SubsystemBase {

  private static SparkMax motor = new SparkMax(ElevatorConstants.ELEVATOR_MOTOR_ID, MotorType.kBrushless);
  private static AnalogInput pot = new AnalogInput(ElevatorConstants.ELEVATOR_POT_ID);
  private static DigitalInput stage1Limit = new DigitalInput(ElevatorConstants.ELEVATOR_STAGE_1_LIMIT_ID);
  private static DigitalInput stage2Limit = new DigitalInput(ElevatorConstants.ELEVATOR_STAGE_2_LIMIT_ID);
  private static DigitalInput stage3Limit = new DigitalInput(ElevatorConstants.ELEVATOR_STAGE_3_LIMIT_ID);
  private static DigitalInput stage4Limit = new DigitalInput(ElevatorConstants.ELEVATOR_STAGE_4_LIMIT_ID);
  private static PIDController PID = ElevatorConstants.ELEVATOR_PID;

  /** Creates a new ElevatorSystem. */
  public ElevatorSystem() {}

  public void runMotor(double velocity) {
    motor.set(velocity);
  }

  public static int getPos() {
    return pot.getValue();
  }

  public static boolean atStage1() {
    return stage1Limit.get();
  }

  public static boolean atStage2() {
    return stage2Limit.get();
  }

  public static boolean atStage3() {
    return stage3Limit.get();
  }

  public static boolean atStage4() {
    return stage4Limit.get();
  }

  public double getNewSpeed(double desiredPos) {
    return PID.calculate(getPos(), desiredPos);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
