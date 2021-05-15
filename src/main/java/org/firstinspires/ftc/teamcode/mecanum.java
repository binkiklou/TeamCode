package org.firstinspires.ftc.teamcode;

public class mecanum {
    public static double l1;
    public static double l2;
    public static double r1;
    public static double r2;
    public static void update(double x1, double y1, double x2)
    {
        double wheelSpeeds[] = new double[4];

        x1 = -x1;
        x2 = -x2;


        // Drive "Normale"
        wheelSpeeds[0] = x1 + y1;
        wheelSpeeds[1] = -x1 + y1;
        wheelSpeeds[2] = -x1 + y1;
        wheelSpeeds[3] = x1 + y1;

        // Laterales
        wheelSpeeds[0] += x2;
        wheelSpeeds[1] -= x2;
        wheelSpeeds[2] += x2;
        wheelSpeeds[3] -= x2;

        l1 = wheelSpeeds[0];
        r1 = wheelSpeeds[1];
        l2 = wheelSpeeds[2];
        r2 = wheelSpeeds[3];
    }

    private void normalize(double[] wheelSpeeds)
    {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);

        for (int i = 1; i < wheelSpeeds.length; i++)
        {
            double magnitude = Math.abs(wheelSpeeds[i]);

            if (magnitude > maxMagnitude)
            {
                maxMagnitude = magnitude;
            }
        }

        if (maxMagnitude > 1.0)
        {
            for (int i = 0; i < wheelSpeeds.length; i++)
            {
                wheelSpeeds[i] /= maxMagnitude;
            }
        }
    }
}
