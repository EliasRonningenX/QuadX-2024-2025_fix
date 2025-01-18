package org.firstinspires.ftc.teamcode.testing;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Testing: Color", group = "Testing")
public class ColorSensorTesting extends LinearOpMode {
    NormalizedColorSensor colorSensor;

    View relativeLayout;
    private DcMotor bl;
    @Override
    public void runOpMode() {

        // "relativeLayoutId" is a reference to the layout on the driver hub.
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        try {
            runSample(); // actually run "runSample"
        } finally {
            // Sets the panel back to the default color
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.WHITE);
                }
            });
        }
    }

    protected void runSample(){
        // Used to multiply the rgb color values but depends on lighting conditions.
        float gain = 5;
        boolean red_blue = true;

        // Resets the array each loop [0] being hue, [1] being saturation, and [2] being value.
        final float[] hsvValues = new float[3];

        // Keeps track of when X was pressed last.
        boolean xButtonPreviouslyPressed = false;
        boolean xButtonCurrentlyPressed = false;

        // Checks if the color sensor in mapped on the driver hub.
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
        bl = hardwareMap.dcMotor.get("bl");

        // Checks if the light on the color sensor is on, if it isn't then this turns it on.
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(true);
        }

        // Waits until the teleop is ran.
        waitForStart();

        while (opModeIsActive()) {
            // Explain basic gain information via telemetry
            telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n");
            telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

            // Increases or decreases gain if A or B is pressed.
            if (gamepad1.a) {
                // Increases gain, can be held down but increases multiple times a second.
                gain += 0.005;
            } else if (gamepad1.b && gain > 1) {
                // Reduces gain, can be held down but increases multiple times a second.
                gain -= 0.005;
            }

            // Show the gain value via telemetry.
            telemetry.addData("Gain", gain);

            // Sets the gain value.
            colorSensor.setGain(gain);

            // Checks if X has been pressed or not.
            xButtonCurrentlyPressed = gamepad1.x;

            // If the button state is different than what it was, then act
            if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
                // If the button is (now) down, then toggle the light
                if (xButtonCurrentlyPressed) {
                    if (colorSensor instanceof SwitchableLight) {
                        SwitchableLight light = (SwitchableLight)colorSensor;
                        light.enableLight(!light.isLightOn());
                    }
                }
            }

            if (gamepad1.y){
                if (red_blue){
                    red_blue = false;
                }
                else{
                    red_blue = true;
                }
            }
            // Resets what button was previously pressed.
            xButtonPreviouslyPressed = xButtonCurrentlyPressed;

            // Get the color of the sensor.
            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
             * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent*/

            // Update the hsvValues array by passing it to Color.colorToHSV()
            Color.colorToHSV(colors.toColor(), hsvValues);

            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            telemetry.addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", colors.alpha);

            if (colors.red > 0.03 &&  colors.green > 0.04){
                telemetry.addLine("The color is: Yellow");
                bl.setPower(0);
            }
            else if(colors.blue > 0.03){
                telemetry.addLine("The color is: Blue");
                if (red_blue){
                    bl.setPower(-0.1);
                }
                else{
                    bl.setPower(0);
                }
            }
            else if(colors.red > 0.03) {
                telemetry.addLine("The color is: Red");
                if (!red_blue){
                    bl.setPower(-0.1);
                }
                else{
                    bl.setPower(0);
                }
            }
            else{
                telemetry.addLine("The color is: None");
                bl.setPower(0.1);
            }
            if (red_blue){
                telemetry.addLine("Your Team is: Red Team");
            }
            else{
                telemetry.addLine("Your Team is: Blue Team");
            }

            telemetry.update();

            // Change the background color of the driver hub.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(hsvValues));
                }
            });
        }

    }
}