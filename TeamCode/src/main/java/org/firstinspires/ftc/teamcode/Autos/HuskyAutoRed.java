package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Autos.HuskyAutoFunctions;


@Autonomous
public class HuskyAutoRed extends LinearOpMode {
    @Override
    public void runOpMode() {
        HuskyAutoFunctions Funcs = null;

        Funcs.init();

        if (Funcs.RedObjectDetection() == null){
            Funcs.DetectionRotate(2);
        }
        Funcs.RedAlign();
    }
}
