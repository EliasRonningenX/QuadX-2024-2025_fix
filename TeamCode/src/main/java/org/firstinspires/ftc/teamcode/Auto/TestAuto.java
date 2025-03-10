package org.firstinspires.ftc.teamcode.Auto;

import androidx.annotation.NonNull;

// RR-specific imports
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
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
@Autonomous(name = "TestAuto", group = "Autonomous")
public class TestAuto extends LinearOpMode {
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
                ElapsedTime time = new ElapsedTime();
                time.startTime();
                left.setPower(1);
                right.setPower(-1);
                while (time.seconds() != 1) {
                    if(time.seconds() >= 1){
                        left.setPower(0);
                        right.setPower(0);
                        return false;
                    }
                }
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
            return new LiftUp();
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
                left.setPower(-1);
                right.setPower(1);
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
                //Sample 1 alignment part 1/3
                .strafeTo(new Vector2d(-12, -50))
                //Sample 1 alignment part 2/3
                .splineToLinearHeading(new Pose2d(15, -20, 0), Math.PI/2)
                //Sample 1 alignment part 3/3
                .splineToLinearHeading(new Pose2d(24.5, -25, 0), Math.toRadians(270.00))
                //Sample 1 in observatory
                .strafeTo(new Vector2d(24.5,-68))
                //Sample 2 alignment part 1/2
                .strafeTo(new Vector2d(26,-30))
                //Sample 2 alignment part 2/2
                .splineToLinearHeading(new Pose2d(36, -14, Math.toRadians(90)), Math.PI/2)
                //Sample 2 in observatory
                .lineToY(-61)
                // Sample 1 Pick up alignment part 1/2
                .strafeTo(new Vector2d(29.5,-50))
                .waitSeconds(0.01)
                // Sample 1 Pick up alignment part 2/2
                .lineToY(-62.5)
                .waitSeconds(0.5);

        TrajectoryActionBuilder traj3 = traj2.endTrajectory().fresh()
                .lineToY(-54);

        TrajectoryActionBuilder traj4 = traj3.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(-20, -43.5, Math.toRadians(271.00)), Math.toRadians(90.00));
                //.strafeTo(new Vector2d(-20,-60))
                //.turn(Math.toRadians(190))
                //.lineToY(-53)

        TrajectoryActionBuilder traj5 = traj4.endTrajectory().fresh()
                .waitSeconds(0.01)
                .splineToSplineHeading(new Pose2d(29.5, -62, Math.toRadians(90)), Math.toRadians(0))
                //-50 & -55
                //.waitSeconds(0.01)
                //.lineToY(-62)
                .waitSeconds(0.5);

        TrajectoryActionBuilder traj7 = traj5.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(-15, -43.5, Math.toRadians(270.05)), Math.toRadians(90.00));
                //.turn(Math.toRadians(190))
                //.lineToY(-44);

        TrajectoryActionBuilder traj8 = traj7.endTrajectory().fresh()
                .strafeTo(new Vector2d(-12, -50))
                .splineToLinearHeading(new Pose2d(15, -20, 0), Math.PI/2);

        Action trajectoryActionCloseOut = traj8.endTrajectory().fresh()
                .strafeTo(new Vector2d(-10, -13))
                .build();

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        stand.down(),
                        traj1.build(),
                        lift.liftUp(),
                        claw.closeClaw(),
                        lift.liftStop(),
                        stand.up(),
                        traj2.build(),
                        claw.openClaw(),
                        new ParallelAction(
                                lift.liftDownFull(),
                                stand.down(),
                                traj4.build()
                        ),
                        stand.up(),
                        lift.liftUp(),
                        claw.closeClaw(),
                        traj5.build(),
                        claw.openClaw(),
                        new ParallelAction(
                                lift.liftDownFull(),
                                stand.down(),
                                traj7.build()
                        ),
                        lift.liftUp(),
                        claw.closeClaw(),
                        stand.up(),
                        new ParallelAction(
                                traj8.build(),
                                lift.liftDownFull()
                        ),
                        trajectoryActionCloseOut,
                        lift.liftUp()
                )

        );
    }
}
