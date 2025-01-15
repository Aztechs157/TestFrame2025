package frc.robot;

import edu.wpi.first.math.controller.PIDController;

public class Constants {
    public static class ControllerConstants {

        public static final int DRIVER_CONTROLLER_PORT = 0;
        public static final int OPERATOR_CONTROLLER_PORT = 1;

        // Joystick Deadband
        public static final double LEFT_X_DEADBAND = 0.1;
        public static final double LEFT_Y_DEADBAND = 0.1;
        public static final double RIGHT_X_DEADBAND = 0.1;
    }

    public static class LoggingConstants {
    
        public static final boolean DEFAULT_LOGGING_STATE = false;
    }

    public static class ElevatorConstants {

        public static final int ELEVATOR_MOTOR_ID = 21;
        public static final int ELEVATOR_POT_ID = 22;
        public static final int ELEVATOR_STAGE_1_LIMIT_ID = 23;
        public static final int ELEVATOR_STAGE_2_LIMIT_ID = 24;
        public static final int ELEVATOR_STAGE_3_LIMIT_ID = 25;
        public static final int ELEVATOR_STAGE_4_LIMIT_ID = 26;

        public static final PIDController ELEVATOR_PID = new PIDController(0, 0, 0);

        public static final int STAGE_1_POS = 4;
        public static final int STAGE_2_POS = 8;
        public static final int STAGE_3_POS = 12;
        public static final int STAGE_4_POS = 157;
    }

}
