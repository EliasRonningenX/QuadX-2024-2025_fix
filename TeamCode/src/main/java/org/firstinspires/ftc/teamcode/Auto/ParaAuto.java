package org.firstinspires.ftc.teamcode.Auto;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

// Non-RR imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teamcode.MecanumDrive;

@Config
@Autonomous(name = "ParaAuto", group = "Autonomous")
public class ParaAuto extends LinearOpMode {
    public class Lift {
        private CRServo left;
        private CRServo right;

        public Lift(HardwareMap hardwareMap) {
            left = hardwareMap.crservo.get("outLeft");
            right = hardwareMap.crservo.get("outRight");
        }

        public class LiftUp implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                left.setPower(1);
                right.setPower(-1);
                return false;
            }
        }
        public Action liftUp() {
            return new LiftUp();
        }

        public class LiftStop implements Action {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                left.setPower(0);
                right.setPower(0);
                return false;
            }
        }
        public Action liftStop() {
            return new LiftStop();
        }

        public class LiftDown implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                ElapsedTime time = new ElapsedTime();
                time.startTime();
                left.setPower(-1);
                right.setPower(1);
                while (time.seconds() != 0.5) {
                    if(time.seconds() >= 0.5){
                        left.setPower(0);
                        right.setPower(0);
                        return false;
                    }
                }
                return false;
            }
        }
        public Action liftDown(){
            return new LiftDown();
        }

        public class LiftDownFull implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                ElapsedTime time = new ElapsedTime();
                time.startTime();
                left.setPower(-1);
                right.setPower(1);
                while (time.seconds() != 2 ){
                    if(time.seconds() >= 2){
                        left.setPower(0);
                        right.setPower(0);
                        return false;
                    }
                }
                return false;
            }
        }
        public Action liftDownFull(){
            return new LiftDownFull();
        }
    }
    public class Claw {
        private Servo claw;

        public Claw(HardwareMap hardwareMap) {
            claw = hardwareMap.servo.get("axonServo");
        }

        public class CloseClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                claw.setPosition(1);
                return false;
            }
        }
        public Action closeClaw() {
            return new CloseClaw();
        }

        public class OpenClaw implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                claw.setPosition(0);
                return false;
            }
        }
        public Action openClaw() {
            return new OpenClaw();
        }
    }

    public class Stand {
        private Servo standLeft;
        private Servo standRight;

        public Stand(HardwareMap hardwareMap) {
            standLeft = hardwareMap.servo.get("standLeft");
            standRight = hardwareMap.servo.get("standRight");
        }

        public class Down implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                standRight.setPosition(0);
                standLeft.setPosition(0.5);
                return false;
            }
        }
        public Action down() {
            return new Down();
        }

        public class Up implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                standRight.setPosition(0.5);
                standLeft.setPosition(0);
                return false;
            }
        }
        public Action up() {
            return new Up();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d initialPose = new Pose2d(-12, -72, Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Claw claw = new Claw(hardwareMap);
        Lift lift = new Lift(hardwareMap);
        Stand stand = new Stand(hardwareMap);

        TrajectoryActionBuilder traj1 = drive.actionBuilder(initialPose)
                // Score Pre load
                .lineToY(-44);

        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh()
                // Score Pre load
                .lineToY(-55);



        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        traj1.build(),
                        new SequentialAction(
                                lift.liftUp(),
                                new SleepAction(8),
                                claw.closeClaw(),
                                lift.liftStop()
                        )
                )


        );
    }
}
