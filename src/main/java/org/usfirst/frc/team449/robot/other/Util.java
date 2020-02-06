package org.usfirst.frc.team449.robot.other;

/**
 * Stuff that doesn't fit anywhere else
 */
public class Util {

    /**
     * Make a runnable out of a method
     * TODO log the errors instead of just throwing them
     * @param object
     * @param methodName
     * @return
     */
    public static Runnable getMethod(Object object, String methodName) {
        return () -> {
            try {
                object.getClass().getMethod(methodName).invoke(object);
            } catch (Exception e) {
                sneakyThrow(e);
            }
        };
    }

    /**
     * Do not use this
     * @param e
     * @param <T>
     * @throws T
     */
    public static <T extends Throwable> void sneakyThrow(Exception e) throws T {
        throw (T) e;
    }

}
