package me.dags.daflightmanager.messaging;

/**
 * @author dags_ <dags@dags.me>
 */

public class DFData
{
    // state
    public static final byte DISABLED = 0;
    public static final byte ENABLED = 1;
    // settings
    public static final byte NOCLIP = 0;
    public static final byte FULL_BRIGHT = 1;
    public static final byte FLY_MOD = 2;
    public static final byte NO_FALL_DAMAGE = 3;
    public static final byte REFRESH = 4;
    public static final byte SPEED = 100;

    public static byte[] getBooleanData(byte setting, boolean state)
    {
        return new byte[] {setting, (state ? ENABLED : DISABLED)};
    }

    public static byte[] getValueData(byte setting, byte value)
    {
        return new byte[] {setting, value};
    }
}
