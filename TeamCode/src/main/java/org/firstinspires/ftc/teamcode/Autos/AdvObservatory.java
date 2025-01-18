package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autos.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Autos.trajectorysequence.TrajectorySequence;

@Autonomous
public class AdvObservatory extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d myPose = new Pose2d(-35.5, -61, Math.toRadians(270));

        drive.setPoseEstimate(myPose);

        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(myPose)
                // Lines up with the trusses
                .lineToLinearHeading(new Pose2d(-42, -37, Math.toRadians(270)))
                // Scores the preloaded specimen
                .waitSeconds(1)
                // Lines up with the first sample
                .lineToLinearHeading(new Pose2d(-5, -40, Math.toRadians(180)))
                .turn(Math.toRadians(-108.5))
                // Intakes the first sample
                .waitSeconds(1)
                // Lines up with the observatory
                .lineToLinearHeading(new Pose2d(-3, -50, Math.toRadians(270)))
                // Outtakes the first sample
                .waitSeconds(1)
                // Lines up with the second sample
                .lineToLinearHeading(new Pose2d(7, -40, Math.toRadians(90)))
                // Intakes the second sample
                .waitSeconds(1)
                // Lines up with the observatory
                .lineToLinearHeading(new Pose2d(-3, -50, Math.toRadians(270)))
                // Outtakes the second sample
                .waitSeconds(1)
                // Lines up with the third sample
                .lineToLinearHeading(new Pose2d(10, -40, Math.toRadians(72)))
                // Intakes the third sample
                .waitSeconds(1)
                // Lines up with the observatory
                .lineToLinearHeading(new Pose2d(-3, -50, Math.toRadians(269)))
                // Outtakes the third sample
                .waitSeconds(1)
                .build();
        ;

        // Waits for the start button to be pressed
        waitForStart();
        //Follows the trajectory
        loop();
        drive.followTrajectorySequence(traj1);
    }
}
