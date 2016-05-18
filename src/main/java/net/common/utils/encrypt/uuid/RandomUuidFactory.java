package net.common.utils.encrypt.uuid;

import java.security.SecureRandom;

/**
 *
 * RandomUuidFactory is a {@linkplain UuidFactory UuidFactory} that generates
 * random UUIDs. This implementation uses the JDK's java.security.SecureRandom
 * to generate sufficiently random values for the UUIDs.
 * <br><br>
 * This class is a singleton, so it must be constructed through the static
 * getInstance() method.
 *
 * @author Dan Jemiolo (danj)
 *
 */
public class RandomUuidFactory {
    private static final RandomUuidFactory _SINGLETON = new RandomUuidFactory();

    private static final char[] _HEX_VALUES = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
    };

    //
    // Used to create all random numbers - guarantees unique strings
    // during the process lifetime
    //
    private static final SecureRandom _RNG = new SecureRandom();

    /**
     * The default constructor is explicit so we can make it private and
     * require use of getInstance() for instantiation.
     *
     * @see #getInstance()
     */
    private RandomUuidFactory() {
        //
        // this is just to prevent instantiation
        //
    }

    /**
     * @return A unique UUID of the form <em>uuid:<b>X</b></em>, where
     * <b>X</b> is the generated value.
     */
    public String createUUID() {
        //
        // first get 16 random bytes...
        //
        byte[] bytes = new byte[16];
        _RNG.nextBytes(bytes);

        StringBuffer uuid = new StringBuffer(41);
        uuid.append("uuid:");

        //
        // ...then we need to shift so they're valid hex (0-9, a-f)
        //
        for (int n = 0; n < 16; ++n) {
            //
            // (there's really no elegant way to add the dashes...)
            //
            if (n == 4 || n == 6 || n == 8 || n == 10)
                uuid.append('-');

            //
            // shift the bits so they are proper hex
            //
            int hex = bytes[n] & 255;
            uuid.append(_HEX_VALUES[hex >> 4]);
            uuid.append(_HEX_VALUES[hex & 15]);
        }

        return uuid.toString();
    }

    /**
     * @return The singleton instance of this class.
     */
    public static RandomUuidFactory getInstance() {
        return _SINGLETON;
    }
}
