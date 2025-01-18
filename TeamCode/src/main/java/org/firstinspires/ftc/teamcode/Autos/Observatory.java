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
public class Observatory extends LinearOpMode {

    private DcMotor leftLift;
    private DcMotor rightLift;


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
        Pose2d myPose = new Pose2d(-33, -61, Math.toRadians(270));

        drive.setPoseEstimate(myPose);

        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(myPose)
                // Scores the preload
                .strafeLeft(25)
                // Heads over to the first sample
                .lineToLinearHeading(new Pose2d(-57, -110, Math.toRadians(270)))
                // Lines up with the observatory
                .turn(Math.toRadians(-75))
                // Scores the first sample
                .strafeLeft(45)
                // Returns to the original position
                .strafeRight(45)
                // Lines up with the second sample
                .lineToLinearHeading(new Pose2d(-69, -110, Math.toRadians(180)))
                // Scores the second sample
                .strafeLeft(45)
                // Returns to the original position
                .strafeRight(50)
                // Lines up with the third sample
                .forward(13)
                // Scores the third sample
                .strafeLeft(50)
                // Returns to the original position and lines up with the park
                .strafeRight(50)
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
