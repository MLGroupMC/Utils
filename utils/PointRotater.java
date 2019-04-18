package xyz;

public class PointRotater {

    //point - array with point that will be rotated (x, y)
    //center - array with point around which point will be rotated (x, y)
    //angle - angle in degrees
    public static double[] rotatePointDeg(double[] point, double center[], double angle) {
        return rotatePoint(point, center, Math.toRadians(angle));
    }

    //point - array with point that will be rotated (x, y)
    //center - array with point around which point will be rotated (x, y)
    //angle - angle in radians
    public static double[] rotatePoint(double[] point, double center[], double angle) {
        double x1 = point[0]-center[0];
        double y1 = point[1]-center[1];
        double r = Math.hypot(x1, y1);
        double k = y1/x1;
        double p = 1/Math.sqrt(1+k*k);
        double q = x1 >= 0 ? r*p : -r*p;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        return new double[] {
                q*(cos-k*sin)+center[0],
                q*(sin+k*cos)+center[1],
        };
    }

}
