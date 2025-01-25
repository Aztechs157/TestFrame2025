// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElbowConstants;
import frc.robot.Constants.WristConstants;

public class JointSystem extends SubsystemBase {
  private SparkMax motor;
  private AnalogInput pot;
  private PIDController PID;

  /** Creates a new JointSystem. */
  public JointSystem(boolean isElbow) {
    motor = new SparkMax(isElbow ? ElbowConstants.ELBOW_MOTOR_ID : WristConstants.WRIST_MOTOR_ID, MotorType.kBrushless);
    pot = new AnalogInput(isElbow ? ElbowConstants.ELBOW_POT_ID : WristConstants.WRIST_POT_ID);
    PID = isElbow ? ElbowConstants.ELBOW_PID : WristConstants.WRIST_PID;
  }

  public void runMotor(double velocity) {
    motor.set(velocity);
  }

  private int getPos() {
    return pot.getValue();
  }

  public double getNewSpeed(double desiredPos) {
    return PID.calculate(getPos(), desiredPos);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
