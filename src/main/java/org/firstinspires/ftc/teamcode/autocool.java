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

    // Variables

    // États
    // 0 - Bouge vers le centre
    // 1 - Atteint le centre
    private int state = 0;

    private boolean should_move = false;


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

        telemetry.addLine("Regroup");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Update Inputs
        navigation.avg_travel(lFront.getCurrentPosition(),rFront.getCurrentPosition(),lBack.getCurrentPosition(),rBack.getCurrentPosition());
        navigation.avg_sensors(dFront.getDistance(DistanceUnit.CM),dLeft.getDistance(DistanceUnit.CM),dRight.getDistance(DistanceUnit.CM));

        if(state == 0)
        {
            move_mid();
        }
        else if(state == 1)
        {
            state++;
        }
        else
        {
            telemetry.addLine("Invalid state");
            telemetry.update();
        }

        // Emergency Checks
        if(navigation.front <= 20|| navigation.left <= 15 || navigation.right <= 15)
        {
            should_move = false;
        }

        if(should_move)
        {
            lFront.setPower(mecanum.l1);
            rFront.setPower(mecanum.r1);
            lBack.setPower(mecanum.l2);
            rBack.setPower(mecanum.r2);
        }
        else
        {

        }
    }

    // Moving to mid
    public void move_mid()
    {
        double speed = 1;
        mecanum.update(0,-speed,0);
    }
}
