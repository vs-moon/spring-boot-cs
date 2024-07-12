import cn.hutool.core.util.IdUtil;

public class CommonTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(IdUtil.getSnowflakeNextId());
        }
    }
}
