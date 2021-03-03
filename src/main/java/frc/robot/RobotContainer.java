// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.Console;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants.GamePad;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.OnBoardIO.ChannelMode;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final OnBoardIO m_onboardIO = new OnBoardIO(ChannelMode.INPUT, ChannelMode.INPUT);

  // Assumes a gamepad plugged into channnel 0
  private final Joystick m_controller = new Joystick(0);

  // Create SmartDashboard chooser for autonomous routines
  private final SendableChooser<Command> m_chooser = new SendableChooser<>();

  // NOTE: The I/O pin functionality of the 5 exposed I/O pins depends on the hardware "overlay"
  // that is specified when launching the wpilib-ws server on the Romi raspberry pi.
  // By default, the following are available (listed in order from inside of the board to outside):
  // - DIO 8 (mapped to Arduino pin 11, closest to the inside of the board)
  // - Analog In 0 (mapped to Analog Channel 6 / Arduino Pin 4)
  // - Analog In 1 (mapped to Analog Channel 2 / Arduino Pin 20)
  // - PWM 2 (mapped to Arduino Pin 21)
  // - PWM 3 (mapped to Arduino Pin 22)
  //
  // Your subsystem configuration should take the overlays into account

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command is arcade drive. This will run unless another command
    // is scheduled over it.
    m_drivetrain.setDefaultCommand(getArcadeDriveCommand());

    // Example of how to use the onboard IO
    Button onboardButtonA = new Button(m_onboardIO::getButtonAPressed);
    onboardButtonA
        .whenActive(new PrintCommand("Button A Pressed"))
        .whenInactive(new PrintCommand("Button A Released"));

    new JoystickButton(m_controller, GamePad.Button.LB)
        .whenPressed(new TurnDegrees(0.6, 90, m_drivetrain))
        .whenReleased(() -> m_drivetrain.stop());

    new JoystickButton(m_controller, GamePad.Button.RB)
      .whenPressed(new TurnDegrees(-0.6, 90, m_drivetrain))
      .whenReleased(() -> m_drivetrain.stop());
      
    new JoystickButton(m_controller, GamePad.Button.Y)
      .whenPressed(() -> m_drivetrain.resetGyro());
      
    new JoystickButton(m_controller, GamePad.Button.X)
      .whenPressed(() -> m_drivetrain.resetEncoders());

    new JoystickButton(m_controller, GamePad.Button.A)
      .whenPressed(new DriveDistance(0.8, 16, m_drivetrain));

    new JoystickButton(m_controller, GamePad.Button.B)
      .whenPressed(new DriveDistance(-0.8, 16, m_drivetrain));

    new JoystickButton(m_controller, GamePad.Button.Start)
      .whenPressed(new DriveDistance(0.8, 10, m_drivetrain));

    // Setup SmartDashboard options
    m_chooser.setDefaultOption("Auto Routine Distance", new AutonomousDistance(m_drivetrain));
    m_chooser.addOption("Auto Routine Time", new AutonomousTime(m_drivetrain));
    SmartDashboard.putData(m_chooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }

  /**
   * Return the speed for the robot during teleop based on
   * the specified axis. Pass -1 to use the LT and RT combination.
   * 
   * @param speedAxis the axis to get the speed from, or -1 for LT/RT. 
   * 
   * @return the speed value
   */
  public double CalculuteSpeed(int speedAxis){
    if (speedAxis == -1) {
      // each axis goes from 0->1
      // RT is reverse, so it needs to be -1
      double reverse =  m_controller.getRawAxis(GamePad.LeftToggle);
      double forward = - m_controller.getRawAxis(GamePad.RightToggle);
  
      return (reverse + forward);
    }
    else {
      return (m_controller.getRawAxis(speedAxis));
    } 
  }

  /**
   * Return the speed for the robot during teleop based on
   * the specified axis. This is a ramp to reduce the push and
   * pull from the raw joystick value
   * 
   * @param speed the speed value from -1 to 1 that we want to fit
   *              into our speed curve  
   * 
   * @return the speed value
   * 
   * using the formula :
   *  b / (1 + exp(-ax))^{c}
   * using the fixed value:
   *  b = 1.1
   *  a = 3
   *  c = 3
   */
  public double SpeedRamp(double speed)
  {
      double curveval = 1.1 / Math.pow(1.0 + Math.exp(-3*Math.abs(speed)),3);

      if (speed > 0)
        return curveval;
      else
        return -curveval;
  }
  
  
  /*
  * Sam's speed curve a(x^{3})+x(1-a)
  * where a is the define below, and x is speed
  * input from the control axis 
  */
  static double a = .5;

  public double SpeedRamp2(double speed)
  {
      double val = (Math.pow(speed,3) * a)  + (speed * a);

      return  val;
  }

  /**
   * Use this to pass the teleop command to the main {@link Robot} class.
   *  
   * @return the command to run in teleop
   */
  public Command getArcadeDriveCommand() {
    int speedAxis = GamePad.RightStick.UpDown; //-1; //GamePad.LeftStick.UpDown;
    int rotationAxis = GamePad.RightStick.LeftRight;

    return new ArcadeDrive(
       m_drivetrain, () -> -SpeedRamp(m_controller.getRawAxis(speedAxis)), () -> SpeedRamp(m_controller.getRawAxis(rotationAxis)));
  }
}
