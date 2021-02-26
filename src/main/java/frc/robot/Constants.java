// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    
    // Constants for the gamepad joysticks & buttons
    public static final class GamePad {
        // Joysticks and their axes
        public final class LeftStick {
            public static final int LeftRight = 0;
            public static final int UpDown = 1;
        }
        public final class RightStick {
            public static final int LeftRight = 4;
            public static final int UpDown = 5;
        }
        public final static int LeftToggle = 2;
        public final static int RightToggle = 3;
    
        public final class Button {
            public static final int A = 1;
            public static final int B = 2;
            public static final int X = 3;
            public static final int Y = 4;
            public static final int LB = 5;
            public static final int RB = 6;
            public static final int Back = 7;
            public static final int Start = 8;
            // public static final int LeftJoyStickClick = 9;
            // public static final int RightJoyStickClick = 10;
        }    
    }

}
