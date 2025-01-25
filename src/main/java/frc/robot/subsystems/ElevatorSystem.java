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

  private static double getMotorVelocity() {
    return motor.getEncoder().getVelocity();
  }

  private static int getPos() {
    return pot.getValue();
  }

  public boolean atStage(int stage) {
    switch (stage) {
      case 1: return atStage1();
      case 2: return atStage2();
      case 3: return atStage3();
      case 4: return atStage4();
      default: return false;
    }
  }

  public boolean atStage1() {
    return stage1Limit.get();
  }

  public boolean atStage2() {
    return stage2Limit.get();
  }

  public boolean atStage3() {
    return stage3Limit.get();
  }

  public boolean atStage4() {
    return stage4Limit.get();
  }

  public double getNewSpeed(double desiredPos) {
    return PID.calculate(getPos(), desiredPos);
  }

  public boolean isOscillating(double desiredPos) { // TODO: Consider generalizing this
    boolean retval = false;

    if ((getPos() >= desiredPos - ElevatorConstants.ELEVATOR_POS_TOLERANCE) & ((getMotorVelocity() >= 0) & (getMotorVelocity() <= ElevatorConstants.ELEVATOR_MOTOR_VELOCITY_TOLERANCE))) {
      // checks if the elevator's current position is below the desired position within ELEVATOR_POS_TOLERANCE and the elevator is moving up at a speed below ELEVATOR_MOTOR_VELOCITY_TOLERANCE
      retval = true;
    } 
    else if ((getPos() <= desiredPos + ElevatorConstants.ELEVATOR_POS_TOLERANCE) & ((getMotorVelocity() <= 0) & (getMotorVelocity() >= - ElevatorConstants.ELEVATOR_MOTOR_VELOCITY_TOLERANCE))) {
      // checks if the elevator's current position is above the desired position within ELEVATOR_POS_TOLERANCE and the elevator is moving down at a speed below ELEVATOR_MOTOR_VELOCITY_TOLERANCE
      retval = true;
    }

    return retval;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
