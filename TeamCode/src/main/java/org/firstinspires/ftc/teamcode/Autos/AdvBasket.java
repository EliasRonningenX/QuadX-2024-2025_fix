package org.firstinspires.ftc.teamcode.Autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autos.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.Autos.trajectorysequence.TrajectorySequence;

@Autonomous
public class AdvBasket extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d myPose = new Pose2d(-35.5, -61, Math.toRadians(270));

        drive.setPoseEstimate(myPose);

        TrajectorySequence traj1 = drive.trajectorySequenceBuilder(myPose)
                //Lines up with the basket
                .lineToLinearHeading(new Pose2d(-19.5, -71, Math.toRadians(230)))
                //Scores the preload
                .waitSeconds(2.25)
                //Lines up with the first sample
                .lineToLinearHeading(new Pose2d(-25, -83, Math.toRadians(270)))
                //Intakes the first sample
                .waitSeconds(1.5)
                //Lines up with basket
                .lineToLinearHeading(new Pose2d(-19.25, -70.75, Math.toRadians(232)))
                //Scores the first sample
                .waitSeconds(2.25)
                //Lines up with the second sample
                .lineToLinearHeading(new Pose2d(-14.5, -83, Math.toRadians(270)))
                //Intakes the second sample
                .waitSeconds(1.5)
                //Lines up with basket
                .lineToLinearHeading(new Pose2d(-19.25, -70.75, Math.toRadians(232)))
                //Scores the second sample
                .waitSeconds(2.25)
                //Lines up with the third sample
                .lineToLinearHeading(new Pose2d(-13, -86, Math.toRadians(311)))
                //Intakes the third sample
                .waitSeconds(1.5)
                //Lines up with the basket
                .lineToLinearHeading(new Pose2d(-19.25, -70.75, Math.toRadians(230)))
                //Scores the third sample
                .waitSeconds(2.25)
                .build();
        ;
        // Waits for the start button to be pressed
        waitForStart();
        //Follows the trajectory
        loop();
        drive.followTrajectorySequence(traj1);
    }
}
