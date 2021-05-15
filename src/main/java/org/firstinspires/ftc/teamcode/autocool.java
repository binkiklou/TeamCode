package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Mode Automatique",group = "yeet")
public class autocool extends OpMode {
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

    // Values
    private boolean reached = false;
    private boolean force_stop = false;

    @Override
    public void init() {
        // Hardware definitions
        lFront = hardwareMap.get(DcMotor.class, "LeftFront");
        rFront = hardwareMap.get(DcMotor.class, "RightFront");
        lBack = hardwareMap.get(DcMotor.class, "LeftBack");
        rBack = hardwareMap.get(DcMotor.class, "RightBack");
        claw = hardwareMap.get(Servo.class, "Claw");
        dFront = hardwareMap.get(DistanceSensor.class, "DistanceFront");
        dLeft = hardwareMap.get(DistanceSensor.class, "DistanceLeft");
        dRight = hardwareMap.get(DistanceSensor.class, "DistanceRight");

        // Reset every units
        lFront.resetDeviceConfigurationForOpMode();
        rFront.resetDeviceConfigurationForOpMode();
        lBack.resetDeviceConfigurationForOpMode();
        rBack.resetDeviceConfigurationForOpMode();
        reached = false;
        force_stop = false;

        telemetry.addLine("Welcome to the Auto Zone!");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Emergency Checks
        if(dFront.getDistance(DistanceUnit.CM) <= 15|| dLeft.getDistance(DistanceUnit.CM) <= 15 || dRight.getDistance(DistanceUnit.CM) <= 15)
        {
            force_stop = true;
        }

        if(navigation.traveled >= 3500)
        {
            reached = true;
        }

        // Drive for around 3500 encoder
        if(!reached)
        {
            mecanum.update(0,-1,0);
        }
        else
        {
            mecanum.update(0,0,0);
        }

        if(!force_stop)
        {
            lFront.setPower(-mecanum.l1);
            rFront.setPower(mecanum.r1);
            lBack.setPower(-mecanum.l2);
            rBack.setPower(mecanum.r2);
        }
        else
        {
            lFront.setPower(0);
            rFront.setPower(0);
            lBack.setPower(0);
            rBack.setPower(0);

            telemetry.addLine("FORCE STOPPED!!!!");
            telemetry.update();
        }
        navigation.avg_travel(lFront.getCurrentPosition(),rFront.getCurrentPosition(),lBack.getCurrentPosition(),rBack.getCurrentPosition());
    }
}
