package xyz;

public class PointRotater {

    //point - tablica z punktem który ma zostać obrócony (x, y)
    //center - tablica z punktem wokół którego punkt ma zostać obrócony (x, y)
    //angle - kąt w stopniach
    public static double[] rotatePointDeg(double[] point, double center[], double angle) {
        return rotatePoint(point, center, Math.toRadians(angle));
    }

    //point - tablica z punktem który ma zostać obrócony (x, y)
    //center - tablica z punktem wokół którego punkt ma zostać obrócony (x, y)
    //angle - kąt w radianach
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
