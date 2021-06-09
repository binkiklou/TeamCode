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
    // 3 - Atteint Cible(drop le chose)
    // 4 - Recule vers la ligne blanche
    private int state = 0;

    private boolean should_move = false;

    private boolean claw_open = false;

    private boolean wait_for_something = false; // Terrible


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

        telemetry.addLine("danger publique: le retour 2");
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
            // Fait rien
            state++;
        }
        else if(state == 2)
        {
            move_target();
        }
        else if(state == 3)
        {
            lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            state++;
            wait_for_something = true;
        }
        else if(state == 4)
        {
            if(wait_for_something)
            {
                lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                wait_for_something = false;
            }
            move_back();
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
            claw.setPosition(0.15);
        }

        telemetry.addData("State:", state);
        telemetry.addData("Traveled",navigation.traveled);
        telemetry.addData("Colors:",String.format("(%d,%d,%d)",cGround.red(),cGround.green(),cGround.blue()));
        telemetry.update();
    }

    // Moving to mid
    public void move_mid()
    {
        // Hard limit
        if(navigation.traveled > 4500)
        {
            telemetry.addLine("Help I'm lost UwU");
            telemetry.update();
            should_move = false;
            return;
        }

        if(
                navigation.traveled <= 4200 //||
        //                (navigation.traveled > 2500 && (cGround.red() >= 1000 || cGround.green() >= 1000 || cGround.blue() >= 1000))
        )
        {
            double speed = (((10000f-navigation.traveled) / 10000f)/1.5) * 2;
            if(navigation.traveled > 2750)
            {
                speed /= 2;
            }
            mecanum.update(0,-speed,0);
            should_move = true;
        }
        else
        {
            if(navigation.traveled >= 4200)
            {
                state = 5;
            }

            should_move = false;
            state++;
        }
    }

    public void move_target()
    {
        if(navigation.traveled < 1000)
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
        if(navigation.traveled > 3000)
        {
            mecanum.update(0,1,0);
        }
        else
        {
            state++;
        }
    }
}
