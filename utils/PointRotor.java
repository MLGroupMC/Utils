package xyz;

public class PointRotor {

    //point - array with point that will be rotated (x, y)
    //center - array with point around which point will be rotated (x, y)
    //angle - angle in degrees
    public static double[] rotatePointDeg(double[] point, double[] center, double angle) {
        return rotatePointRad(point, center, Math.toRadians(angle), 0);
    }
    //length - if needed, increases distance of rotated point from center by given amount
    public static double[] rotatePointDeg(double[] point, double[] center, double angle, double length) {
        return rotatePointRad(point, center, Math.toRadians(angle), length);
    }

    //point - array with point that will be rotated (x, y)
    //center - array with point around which point will be rotated (x, y)
    //angle - angle in radians
    public static double[] rotatePointRad(double[] point, double[] center, double angle) {
        return rotatePointRad(point, center, angle, 0);
    }
    //length - if needed, increases distance of rotated point from center by given amount
    public static double[] rotatePointRad(double[] point, double[] center, double angle, double length) {
        double x1 = point[0]-center[0], y1 = point[1]-center[1];
        double r = Math.hypot(x1, y1);
        if(x1 == 0) {
            if(y1 == 0)
                return center.clone();
            if(y1 > 0)
                return new double[] {-r*Math.sin(angle)+center[0], r*Math.cos(angle)+center[1]};
            return new double[] {r*Math.sin(angle)+center[0], -r*Math.cos(angle)+center[1]};
        }
        double k = y1/x1, p = 1/Math.sqrt(1+k*k), q = x1 > 0 ? r*p : -r*p;
        double sin = Math.sin(angle), cos = Math.cos(angle);
        return new double[]{q*(cos-k*sin)+center[0], q*(sin+k*cos)+center[1]};
    }

}
