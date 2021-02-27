// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutonomousDistance extends SequentialCommandGroup {
  /**
   * Creates a new Autonomous Drive based on distance. This will drive out for a specified distance,
   * turn around and drive back.
   *
   * @param drivetrain The drivetrain subsystem on which this command will run
   */
  public AutonomousDistance(Drivetrain drivetrain) {
    double speed = 0.6;

    //DriveSquare(drivetrain, speed);
    DriveCourse(drivetrain, speed);
  }

  private void DriveSquare(Drivetrain drivetrain, double speed) {
    addCommands(
          new DriveDistance(speed, 18.0, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(-speed, 130, drivetrain)
        , new WaitCommand(0.1)
        , new DriveDistance(speed, 18, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(-speed, 120, drivetrain)
        , new WaitCommand(0.1)
        , new DriveDistance(speed, 18.0, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(-speed, 90, drivetrain)
        , new WaitCommand(0.1)
        , new DriveDistance(speed, 18, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(-speed, 90, drivetrain)
      );
  }

  private void DriveCourse(Drivetrain drivetrain, double speed) {
    addCommands(
          new DriveDistance(speed, 17.0, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(-speed, 70, drivetrain)
        , new WaitCommand(0.1)
        , new DriveDistance(speed, 17.5, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(-speed, 122, drivetrain)
        , new WaitCommand(0.1)
        , new DriveDistance(speed, 29, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(speed, 110, drivetrain)
        , new WaitCommand(0.1)
        , new DriveDistance(speed, 17, drivetrain)
        , new WaitCommand(0.1)
        , new TurnDegrees(speed, 70, drivetrain)
        , new WaitCommand(0.16)
        , new DriveDistance(speed, 16, drivetrain)
        );
  }
}
