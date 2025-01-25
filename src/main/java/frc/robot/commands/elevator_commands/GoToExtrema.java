// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.elevator_commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.subsystems.ElevatorSystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class GoToExtrema extends Command {
  private final ElevatorSystem elevator;
  private final boolean isTop;

  /** Creates a new GoToStage1. */
  public GoToExtrema(final ElevatorSystem elevator, final boolean isTop) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.elevator = elevator;
    this.isTop = isTop;
    addRequirements(elevator);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    elevator.runMotor(elevator.getNewSpeed(isTop ? ElevatorConstants.STAGE_1_POS : ElevatorConstants.STAGE_4_POS));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    elevator.runMotor(elevator.getNewSpeed(ElevatorConstants.STAGE_1_POS));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    elevator.runMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (elevator.isOscillating(ElevatorConstants.STAGE_1_POS) | elevator.atStage1());
  }
}
