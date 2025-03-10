package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp

public class MyTeleOp extends OpMode {
    private DcMotor fr;
    private DcMotor fl;
    private DcMotor br;
    private DcMotor bl;
    private CRServo outRight;
    private CRServo outLeft;
    private Servo claw;
    private Servo standRight;
    private Servo standLeft;

    @Override
    public void init() {
        fr = hardwareMap.dcMotor.get("fr");
        fl = hardwareMap.dcMotor.get("fl");
        br = hardwareMap.dcMotor.get("br");
        bl = hardwareMap.dcMotor.get("bl");

        outRight = hardwareMap.crservo.get("outRight");
        outLeft = hardwareMap.crservo.get("outLeft");

        claw = hardwareMap.servo.get("axonServo");

        standRight = hardwareMap.servo.get("standRight");
        standLeft = hardwareMap.servo.get("standLeft");


        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.FORWARD);

        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        float drive = gamepad1.left_stick_y;
        float turn = gamepad1.right_stick_x;
        float strafe = gamepad1.left_stick_x;


        double frPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
        double flPower = Range.clip(drive - turn + strafe, -1.0, 1.0);
        double brPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
        double blPower = Range.clip(drive - turn - strafe, -1.0, 1.0);

        /*
        if (gamepad2.right_trigger != 0) {
            intakeR.setPower(gamepad2.right_trigger);
            intakeL.setPower(-gamepad2.right_trigger);
        }

        if (gamepad2.left_trigger != 0) {
            intakeR.setPower(gamepad2.left_trigger);
            intakeL.setPower(-gamepad2.left_trigger);
        }
         */

        if (gamepad2.left_bumper) {
            telemetry.addLine("Servo position 1");
            telemetry.update();
            claw.setPosition(1);
        }

        if (gamepad2.right_bumper) {
            telemetry.addLine("Servo position 0");
            telemetry.update();
            claw.setPosition(0);
        }

        //up
        if (gamepad1.right_trigger != 0) {
            double rightPow = 0;
            double leftPow = 0.5;
            standRight.setPosition(rightPow);
            telemetry.addData("Right Servo Power: ", rightPow);
            standLeft.setPosition(leftPow);
            telemetry.addData("Left Servo Power: ", leftPow);
        }

        //down
        if (gamepad1.left_trigger != 0) {
            double rightPow = 0.5;
            double leftPow = 0;
            standRight.setPosition(rightPow);
            telemetry.addData("Right Servo Power: ", rightPow);
            standLeft.setPosition(leftPow);
            telemetry.addData("Left Servo Power: ", leftPow);
        }

        if (gamepad2.a) {
            outLeft.setPower(-1);
            outRight.setPower(1);
        } else if (gamepad2.b) {
            outLeft.setPower(1);
            outRight.setPower(-1);
        } else {
            outLeft.setPower(0);
            outRight.setPower(0);
        }


        //intakeR.setPower(0);
        //intakeL.setPower(0);

        fr.setPower(frPower);
        fl.setPower(flPower);
        br.setPower(brPower);
        bl.setPower(blPower);
    }
}

