package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class HuskyAutoFunctions extends OpMode {

    public HuskyLens Husk;
    public int Num;
    public int Xpos;
    public int Ypos;
    public int PosiX;
    public int PosiY;
    public DcMotor fl;
    public DcMotor fr;
    public DcMotor bl;
    public DcMotor br;


    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");
        Husk = hardwareMap.get(HuskyLens.class, "Husky");

        Husk.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);

        fl.setDirection(DcMotor.Direction.FORWARD);
        fr.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.FORWARD);
        br.setDirection(DcMotor.Direction.REVERSE);

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public HuskyLens.Block RedObjectDetection() {
        HuskyLens.Block[] BlockList = Husk.blocks(2);
        Num = BlockList.length;
        if (Num >= 1) {
            return (BlockList[0]);
        } else {
            return (null);
        }

    }

    public HuskyLens.Block BlueObjectDetection() {
        HuskyLens.Block[] BlockList = Husk.blocks(1);
        Num = BlockList.length;
        if (Num >= 1) {
            return (BlockList[0]);
        } else {
            return (null);
        }

    }

    public int RedObjectXOrientation() {
        Husk.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        for (HuskyLens.Block b : Husk.blocks(1)) {
            if (b.id == 1) {
                Xpos = BlueObjectDetection().x;
            }

        }
        return Xpos;
    }

    public int BlueObjectXOrientation() {
        Husk.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        for (HuskyLens.Block b : Husk.blocks(1)) {
            if (b.id == 1) {
                Xpos = BlueObjectDetection().x;
            }

        }
        return Xpos;
    }

    public int BlueObjectYOrientation() {
        Husk.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        for (HuskyLens.Block b : Husk.blocks(1)) {
            if (b.id == 1){
                Xpos = BlueObjectDetection().y;
            }

        }
        return Ypos;
    }

    public int RedObjectYOrientation() {
        Husk.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        for (HuskyLens.Block b : Husk.blocks(1)) {
            if (b.id == 2){
                Xpos = BlueObjectDetection().y;
            }

        }
        return Ypos;
    }

    public void DetectionRotate(int i) {
        if (i == 2) {
            while (RedObjectDetection() == null) {
                fl.setPower(0.1);
                fr.setPower(-0.1);
                bl.setPower(0.1);
                br.setPower(-0.1);
            }
        }
        if (i == 1) {
            while (BlueObjectDetection() == null) {
                fl.setPower(0.1);
                fr.setPower(-0.1);
                bl.setPower(0.1);
                br.setPower(-0.1);
            }
        }
    }

    public void BlueAlign(){
        PosiX = BlueObjectXOrientation();
        if (Num >= 1) {
            while (PosiX != 160) {
                double error = 160 - PosiX;
                double rotate = error * 0.004;
                telemetry.addData("Posi", PosiX);
                fl.setPower(rotate);
                fr.setPower(-rotate);
                bl.setPower(rotate);
                br.setPower(-rotate);
                PosiX = BlueObjectXOrientation();
            }
        }
    }

    public void RedAlign(){
        PosiX = RedObjectXOrientation();
        if (Num >= 1) {
            while (PosiX != 160) {
                double error = 160 - PosiX;
                double rotate = error * 0.004;
                telemetry.addData("Posi", PosiX);
                fl.setPower(rotate);
                fr.setPower(-rotate);
                bl.setPower(rotate);
                br.setPower(-rotate);
                PosiX = RedObjectXOrientation();
            }
        }
    }

    @Override
    public void loop() {

    }


}