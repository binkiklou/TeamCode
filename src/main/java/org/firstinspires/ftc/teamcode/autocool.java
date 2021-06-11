package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
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
    private ColorRangeSensor cGround = null;

    // Variables

    // États
    // 0 - Bouge vers le centre
    // 1 - Atteint le centre
    // 2 - Bouge vers cible
    // 3 - Atteint Cible
    // 4 - Recule vers la ligne blanche
    // 5 - Atteint la ligne
    private int state = 0;

    private boolean should_move = false;

    private boolean claw_open = false;

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
        cGround = hardwareMap.get(ColorRangeSensor.class, "GroundColor");
        int soundID = hardwareMap.appContext.getResources().getIdentifier("dababy", "raw", hardwareMap.appContext.getPackageName());

        telemetry.addLine("v0");
        telemetry.update();

        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, soundID);

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
            // Update le mask
            navigation.maskv += navigation.traveled;
            state++;
        }
        else if(state == 2)
        {
            move_target();
        }
        else if(state == 3)
        {
            claw_open = true;
            state++;
        }
        else if(state == 4)
        {
            move_back();
        }
        else
        {
            telemetry.addLine("Invalid state");
        }

        // Emergency Checks
        if(navigation.front <= 20|| navigation.left <= 15 || navigation.right <= 15)
        {
            //should_move = false;
            telemetry.addLine("EMERGENCY STOP PAS SECURITAIRE");
        }

        // Moving
        if(should_move)
        {
            lFront.setPower(-mecanum.l1);
            rFront.setPower(mecanum.r1);
            lBack.setPower(-mecanum.l2);
            rBack.setPower(mecanum.r2);
        }
        else
        {
            lBack.setPower(0);
            rBack.setPower(0);
            lFront.setPower(0);
            rFront.setPower(0);
        }

        if(claw_open == true)
        {
            claw.setPosition(0.6);
        }
        else
        {
            claw.setPosition(0.15);
        }

        telemetry.addData("Should_Move:", should_move);
        telemetry.addData("State:", state);
        telemetry.addData("Traveled",navigation.traveled);
        telemetry.addData("Left:",navigation.left);
        telemetry.addData("Front:",navigation.front);
        telemetry.addData("Right:",navigation.right);
        //telemetry.addData("Colors:",String.format("(%d,%d,%d)",cGround.red(),cGround.green(),cGround.blue()));
        //telemetry.addData("Sensors:", String.format("(%d,%d,%d)",navigation.left,navigation.front,navigation.right));
        telemetry.update();
    }

    // Moving to mid
    public void move_mid()
    {
        // Hard limit
        if(navigation.traveled > 4500)
        {
            telemetry.addLine("Help I'm lost UwU");
            should_move = false;

            return;
        }

        if(navigation.traveled < 3000 )
        {
            double speed = (((10000f-navigation.traveled) / 10000f)/1.5);
            if(navigation.traveled > 2750)
            {
                speed /= 2;
            }
            mecanum.update(0,-speed,0);
            should_move = true;
            telemetry.addLine("HERE");
        }
        else
        {
            should_move = false;
            state++;
            telemetry.addLine("NOT HERE");
        }
    }

    public void move_target()
    {
        if(navigation.traveled < 2350 && navigation.front >= 25)
        {
            mecanum.update(0,-0.5,0);
            should_move = true;
        }
        else
        {
            should_move = false;
            state++;
        }
    }

    public void move_back()
    {
        if(navigation.traveled > 0)
        {
            mecanum.update(0,0.5,0);
            should_move = true;
        }
        else
        {
            should_move = false;
            state++;
        }
    }
}