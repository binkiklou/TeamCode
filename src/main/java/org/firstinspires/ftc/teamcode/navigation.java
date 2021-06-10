package org.firstinspires.ftc.teamcode;

public class navigation {

    // Traveled distance
    public static int traveled = 0;

    public static double front = 0;
    public static double left = 0;
    public static double right = 0;

    public static int maskv = 0;

    public static void avg_travel(int a,int b, int c, int d)
    {
        // Ya deux qui doivent être reversed
        b = -b;
        d = -d;

        traveled = ((a+b+c+d)/4) - maskv;
    }

    public static void avg_sensors(double a, double b, double c)
    {
        front = (front + a)/2;
        left = (left + b)/2;
        right = (right + c)/2;
    }
}
