import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RsaTest
 * @Author tao.he
 * @Since 2022/8/15 10:19
 */
public class RsaTest {

    private static final String PUBLIC_KEY = "PublicKey";
    private static final String PRIVATE_KEY = "PrivateKey";

    public static void main(String[] args) {
        Map<String, Object> map = genKeyPair();
        System.out.println("PublicKey = " + map.get(PUBLIC_KEY));
        System.out.println("PrivateKey = " + map.get(PRIVATE_KEY));
    }


    /**
     * 生成秘钥对（公钥和私钥）
     *
     * @return
     */
    public static Map<String, Object> genKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            if (keyPairGen != null) {
                keyPairGen.initialize(2048);

                KeyPair generateKeyPair = keyPairGen.generateKeyPair();

                PublicKey publicKey = generateKeyPair.getPublic();
                PrivateKey privateKey = generateKeyPair.getPrivate();

                Base64.encode(publicKey.getEncoded());

                Map<String, Object> keyPairMap = new HashMap<String, Object>();
                keyPairMap.put(PUBLIC_KEY, Base64.encode(publicKey.getEncoded()));
                keyPairMap.put(PRIVATE_KEY, Base64.encode(privateKey.getEncoded()));

                return keyPairMap;
            }
        } catch (Exception e) {
            throw e;
        }
        return null;
    }
}
