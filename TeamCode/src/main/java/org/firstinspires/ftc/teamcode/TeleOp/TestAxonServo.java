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
public class TestAxonServo extends OpMode{

    private Servo axonServo;

    @Override
    public void init() {
        axonServo = hardwareMap.servo.get("axonServo");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            axonServo.setPosition(1);
        } else if (gamepad1.b) {
            axonServo.setPosition(0.2);
        }
    }
}
