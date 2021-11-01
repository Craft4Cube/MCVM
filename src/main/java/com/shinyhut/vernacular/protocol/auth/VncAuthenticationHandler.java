package com.shinyhut.vernacular.protocol.auth;

import com.shinyhut.vernacular.client.VncSession;
import com.shinyhut.vernacular.client.exceptions.AuthenticationRequiredException;
import com.shinyhut.vernacular.client.exceptions.UnexpectedVncException;
import com.shinyhut.vernacular.client.exceptions.VncException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.function.Supplier;

import static com.shinyhut.vernacular.utils.ByteUtils.reverseBits;
import static java.lang.System.arraycopy;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class VncAuthenticationHandler implements SecurityHandler {

    private static final byte VNC_AUTHENTICATION_TYPE = 0x02;

    @Override
    public void authenticate(VncSession session) throws VncException, IOException {
        Supplier<String> passwordSupplier = session.getConfig().getPasswordSupplier();

        if (passwordSupplier == null) {
            throw new AuthenticationRequiredException();
        }

        InputStream in = session.getInputStream();
        OutputStream out = session.getOutputStream();

        try {
            requestVncAuthentication(out);
            byte[] challenge = readChallenge(in);
            sendResponse(out, challenge, passwordSupplier.get());
        } catch (GeneralSecurityException e) {
            throw new UnexpectedVncException(e);
        }
    }

    private static void requestVncAuthentication(OutputStream out) throws IOException {
        out.write(VNC_AUTHENTICATION_TYPE);
    }

    private static byte[] readChallenge(InputStream in) throws IOException {
        DataInputStream data = new DataInputStream(in);
        byte[] challenge = new byte[16];
        data.readFully(challenge);
        return challenge;
    }

    private static void sendResponse(OutputStream out, byte[] challenge, String password) throws IOException, GeneralSecurityException {
        Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
        des.init(ENCRYPT_MODE, buildKey(password));
        out.write(des.doFinal(challenge));
    }

    private static SecretKeySpec buildKey(String password) {
        byte[] key = keyBytes(password);
        return new SecretKeySpec(key, 0, key.length, "DES");
    }

    private static byte[] keyBytes(String password) {
        byte[] passwordBytes = password.getBytes(Charset.forName("US-ASCII"));
        byte[] key = new byte[8];
        arraycopy(passwordBytes, 0, key, 0, passwordBytes.length < key.length ? passwordBytes.length : key.length);
        return reverseBits(key);
    }

}
