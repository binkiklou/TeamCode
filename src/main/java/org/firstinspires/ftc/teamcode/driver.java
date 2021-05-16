package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREV2mDistance;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="Mode Manuel",group="yeet")
public class driver extends OpMode {
    // Motors
    private DcMotor lFront = null;
    private DcMotor rFront = null;
    private DcMotor lBack = null;
    private DcMotor rBack = null;
    // Servos
    private Servo claw = null;
    // Sensors
    private DistanceSensor dFront = null;
    private DistanceSensor dLeft = null;
    private DistanceSensor dRight = null;
    private ColorRangeSensor cGround = null;

    // Values
    private boolean sclaw = false;

    @Override
    public void init()
    {
        // Hardware definitions
        lFront = hardwareMap.get(DcMotor.class, "LeftFront");
        rFront = hardwareMap.get(DcMotor.class, "RightFront");
        lBack = hardwareMap.get(DcMotor.class, "LeftBack");
        rBack = hardwareMap.get(DcMotor.class, "RightBack");
        claw = hardwareMap.get(Servo.class, "Claw");
        dFront = hardwareMap.get(DistanceSensor.class, "DistanceFront");
        dLeft = hardwareMap.get(DistanceSensor.class, "DistanceLeft");
        dRight = hardwareMap.get(DistanceSensor.class, "DistanceRight");
        cGround = hardwareMap.get(ColorRangeSensor.class, "GroundColor");

        telemetry.addLine("Gimme the color");
        telemetry.update();
    }

    @Override
    public void loop(){
        //telemetry.addData("range", String.format("%.01f mm", dsens.getDistance(DistanceUnit.MM)));
        //telemetry.update();
        // Si ya des problemes c a cause de ca
        mecanum.update(gamepad1.left_stick_x,gamepad1.left_stick_y,gamepad1.right_stick_x);
        telemetry.addData("Mecanum",String.format("({%.01f,%.01f},{%.01f,%.01f})",mecanum.l1,mecanum.l2,mecanum.r1,mecanum.r2));
        telemetry.addData("Encoders",String.format("({%d,%d},{%d,%d})",lFront.getCurrentPosition(),rFront.getCurrentPosition(),lBack.getCurrentPosition(),rBack.getCurrentPosition()));
        telemetry.addData("Distances:",String.format("%.01f; %.01f, %.01f",dFront.getDistance(DistanceUnit.CM),dLeft.getDistance(DistanceUnit.CM),dRight.getDistance(DistanceUnit.CM)));
        telemetry.addData("Traveled: ",String.format("%d",navigation.traveled));
        telemetry.addData("Ground Color:",String.format("(%d,%d,%d)",cGround.red(),cGround.blue(),cGround.green()));
        telemetry.update();

        // Input Update
        sclaw = gamepad1.a;

        // Update Drive
        lFront.setPower(-mecanum.l1);
        rFront.setPower(mecanum.r1);
        lBack.setPower(-mecanum.l2);
        rBack.setPower(mecanum.r2);
        navigation.avg_travel(lFront.getCurrentPosition(),rFront.getCurrentPosition(),lBack.getCurrentPosition(),rBack.getCurrentPosition());


        // Update Servos
        if(sclaw)
        {
            claw.setPosition(0.6);
        }
        else
        {
            claw.setPosition(0.15);
        }
    }
}