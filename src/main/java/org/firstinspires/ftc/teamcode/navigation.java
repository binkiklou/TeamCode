package org.firstinspires.ftc.teamcode;

public class navigation {

    // Traveled distance
    public static int traveled = 0;

    // Oh boi
    public static void update()
    {
        // Bouger jusqua ligne blance,
        // Doit checker pour ligne blanche
    }

    public static void avg_travel(int a,int b, int c, int d)
    {
        // Ya deux qui doivent être reversed
        b = -b;
        d = -d;

        traveled = (a+b+c+d)/4;
    }
}
