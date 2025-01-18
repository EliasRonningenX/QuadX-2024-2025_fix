package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Autos.drive.SampleMecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;

@TeleOp
public class TestTeleOp extends OpMode {
    private SampleMecanumDrive drive;
    private DcMotor intake;

    @Override
    public void init() {
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake = hardwareMap.dcMotor.get("intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {
        drive.setWeightedDrivePower(
                new Pose2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x
                )
        );
        drive.update();

        if (gamepad2.left_bumper) {
            intake.setPower(1);
        } else if (gamepad2.right_bumper) {
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }

        double heading = drive.getPoseEstimate().getHeading();
        telemetry.addData("Heading", heading);
        telemetry.update();
    }
}
