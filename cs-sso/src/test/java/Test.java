import org.xiao.cs.sso.box.utils.RsaUtils;

import java.security.KeyPair;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws Exception {
        KeyPair kk = RsaUtils.generate("KK", 2048);
        System.out.println(Base64.getEncoder().encodeToString(kk.getPublic().getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(kk.getPrivate().getEncoded()));
    }
}
