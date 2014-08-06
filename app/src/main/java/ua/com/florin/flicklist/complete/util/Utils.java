package ua.com.florin.flicklist.complete.util;

import android.os.Build;

/**
 * Contains static auxiliary methods
 * <p/>
 * Created by florin on 22.07.14.
 */
public class Utils {

    /**
     * Checks whether device is running on Froyo+
     *
     * @return true if device is running on Froyo or newer version
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * Checks whether device is running on Gingerbread or newer
     *
     * @return true if device is running on Gingerbread or newer version
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Checks whether device is running on Honeycomb or newer
     *
     * @return true if device is running on Honeycomb or newer version
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Checks whether device is running on Honeycomb or newer
     *
     * @return true if device is running on Honeycomb or newer version
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Checks whether device is running on Jelly Bean or newer
     *
     * @return true if device is running on Jelly Bean or newer version
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Checks whether device is running on Kitkat or newer
     *
     * @return true if device is running on Kutkat or newer version
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
