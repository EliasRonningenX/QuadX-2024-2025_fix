package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Autos.drive.SampleMecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class RRTeleOp extends OpMode {
    private SampleMecanumDrive drive;
    //private DcMotor intake;

    private Servo wheelIn;
    //private Servo leftIn;
    private CRServo fourBar;
    private Servo leftLink;

    private Servo rightLink;
    public DcMotor leftLift;
    public DcMotor rightLift;

    @Override
    public void init() {
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //intake = hardwareMap.dcMotor.get("intake");
        leftLift = hardwareMap.dcMotor.get("leftLift");
        rightLift = hardwareMap.dcMotor.get("rightLift");

        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);
        rightLift.setDirection(DcMotorSimple.Direction.FORWARD);

        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //intake.setDirection(DcMotor.Direction.FORWARD);
        //intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        wheelIn = hardwareMap.servo.get("wheelIn");
        //leftIn = hardwareMap.servo.get("leftIn");
        //fourBar = hardwareMap.crservo.get("rightIn");
        fourBar = hardwareMap.get(CRServo.class, "rightIn");
        leftLink = hardwareMap.servo.get("leftLink");
        rightLink = hardwareMap.servo.get("rightLink");
    }

    @Override
    public void loop() {
        drive.setWeightedDrivePower(
                new Pose2d(
                        -gamepad1.left_stick_y,
                        gamepad1.left_stick_x,
                        -gamepad1.right_stick_x
                )
        );

        drive.update();

        if (gamepad2.dpad_up) {
            leftLift.setPower(-1);
            rightLift.setPower(-1);
        } else if (gamepad2.dpad_down) {
            leftLift.setPower(1);
            rightLift.setPower(1);
        } else {
            leftLift.setPower(0);
            rightLift.setPower(0);
        }
        /*
        if (gamepad2.right_trigger != 0) {
            intake.setPower(gamepad2.right_trigger);
        } else {
            intake.setPower(0);
        }
        */

        double heading = drive.getPoseEstimate().getHeading();
        telemetry.addData("Heading", Math.toDegrees(heading));
        telemetry.update();

        //double aiden = 0.0;

        if (gamepad2.a) {
            fourBar.setPower(-1);
        } else if (gamepad2.b){
            fourBar.setPower(1);
        } else {
            fourBar.setPower(0);
        }

        if (gamepad2.right_bumper) {
            rightLink.setPosition(1);
        } else if (gamepad2.left_bumper) {
            rightLink.setPosition(0);
        }

        if (gamepad2.x) {
            wheelIn.setPosition(1);
            telemetry.addData("wheelIn position: ", wheelIn.getPosition());
        } else if (gamepad2.y) {
            wheelIn.setPosition(0.3);
            telemetry.addData("wheelIn position: ", wheelIn.getPosition());
        }
        if (gamepad2.right_trigger > 0){
            wheelIn.setPosition(0);
        }

        /*
        if (gamepad2.left_trigger > 0.0) {
            intake.setPower(gamepad2.left_trigger);
        } else if (gamepad2.right_trigger > 0.0) {
            intake.setPower(-1 * gamepad2.right_trigger);
        } else {
            intake.setPower(gamepad2.right_trigger);
        }
        */
    }
}