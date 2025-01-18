package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class HuskyTeleOpRed extends OpMode {
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor bl;
    private DcMotor br;
    //private Servo IntakeServoR;
    HuskyLens Husk;


    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");
        //IntakeServoR = hardwareMap.servo.get("IntakeR");


        Husk = hardwareMap.get(HuskyLens.class,"Husky");

        /*
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

         */

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

    //This checks if there is an object. If there is, return how many.
    //Mind you, this only checks for BLUE objects. TODO: DOES NOT WORK ON RED OR YELLOW YET.
    int Num;
    public HuskyLens.Block RedObjectDetection(){
        HuskyLens.Block[] BlockList = Husk.blocks(2);
        Num = BlockList.length;
        if (Num >= 1){
            return(BlockList[0]);
        } else {
            return (null);
        }

    }

    //This returns the X position of a detected object (only blue at the moment).
    //For usage of the values, use Posi.
    int Xpos;

    public int RedObjectOrientation() {
        Husk.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);
        for (HuskyLens.Block b : Husk.blocks(2)) {
                if (b.id == 2){
                    Xpos = RedObjectDetection().x;
                }

        }
        return Xpos;
    }

    HuskyLens.Block Object;
    int Posi;

    @Override
    public void loop() {

        float drive = gamepad1.left_stick_y;
        float turn = -gamepad1.right_stick_x;
        float strafe = gamepad1.left_stick_x;

        double flPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
        double frPower = Range.clip(drive - turn + strafe, -1.0, 1.0);
        double blPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
        double brPower = Range.clip(drive - turn - strafe, -1.0, 1.0);


        Object = RedObjectDetection();

            if (Num >= 1){
                telemetry.addData("Vision", Num);
            }

            if (Num == 0){
                telemetry.addData("Vision",0);
            }

            Posi = RedObjectOrientation();
            telemetry.addData("X Position",Posi);
            updateTelemetry(telemetry);

            //TODO: THIS IS EXPERIMENTAL AND NEEDS TO BE TESTED/FIXED.
            //This function allows for alignment with detected objects. If there aren't any to align with, it attempts to find one.
            //NOTE - This might override all other Driver 1 functions temporarily. Whether or not this is bad or good is yet to be determined.
            if(gamepad1.b) {

                if (Num >= 1) {
                    //TODO: Check if the turns are proper. They may be skewed by other values. Delete this after.
                        double error = 160 - Posi;
                        double rotate = error * 0.004;
                        telemetry.addData("Posi",Posi);
                            flPower = rotate;
                            frPower = -rotate;
                            blPower = rotate;
                            brPower = -rotate;
                        Posi = RedObjectOrientation();
                }
            }


        telemetry.addData("flpower",flPower);

        fl.setPower(flPower);
        fr.setPower(frPower);
        bl.setPower(blPower);
        br.setPower(brPower);


    }
}