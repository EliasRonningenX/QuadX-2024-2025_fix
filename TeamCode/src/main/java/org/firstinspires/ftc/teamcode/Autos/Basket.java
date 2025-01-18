package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autos.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Autos.trajectorysequence.TrajectorySequence;

@Autonomous
public class Basket extends LinearOpMode {

    private DcMotor leftLift;
    private DcMotor rightLift;
    private ElapsedTime et;

    public void initialize() {
        leftLift = hardwareMap.dcMotor.get("leftLift");
        rightLift = hardwareMap.dcMotor.get("rightLift");

        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
        rightLift.setDirection(DcMotorSimple.Direction.FORWARD);

        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        initialize();
        Pose2d myPose = new Pose2d(-35.5, -61, Math.toRadians(270));

        drive.setPoseEstimate(myPose);

        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(myPose)
                // Scores the preload
                .strafeRight(10)
                // Returns to the original position
                .strafeLeft(11.5)
                // Heads over to the first sample
                .lineToLinearHeading(new Pose2d(-32, -110, Math.toRadians(270)))
                // Lines up with the net zone
                .turn(Math.toRadians(72))
                // Scores the first sample
                .strafeRight(45)
                // Returns to the original position
                .strafeLeft(40)
                // Lines up with the second sample
                .lineToLinearHeading(new Pose2d(-16, -117, Math.toRadians(0)))
                // Scores the second sample
                .strafeRight(40)
                .strafeLeft(45)
                .forward(15)
                .strafeRight(42)
                // Returns to the original position
                .strafeLeft(45)
                // Lines up with the third sample
                //.forward(10)
                // Scores the third sample
                //.strafeRight(8)
                // Returns to the original position and lines up for the park
                // Parks


                .addDisplacementMarker(() -> {
                    leftLift.setPower(-1);
                    rightLift.setPower(-1);
                })
                .back(38)
                .build();


        // Waits for the start button to be pressed
        waitForStart();
        //Follows the trajectory
        loop();
        drive.followTrajectorySequence(traj1);
    }
}
