import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName Test
 * @Author tao.he
 * @Since 2022/4/20 17:01
 */
public class Test {
    public static void main(String[] args) {
        String str = "[TradeBillSynInfoReq(id=718772604421115904, synBillNo=Order2112-0125, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq" +
                "(id=718776408889720832, synBillNo=Order2112-0124, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718783577529880577, synBillNo=Order2112-0110, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718785353821814784, synBillNo=Order2112-0108, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718786923057737729, synBillNo=Order2112-0107, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718791400808620032, synBillNo=Order2112-0130, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718801773561475073, synBillNo=Order2112-0117, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718778543190675456, synBillNo=Order2112-0117, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718779484711256064, synBillNo=Order2112-0136, synCount=2, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718779891583918081, synBillNo=Order2112-0107, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718780740880146433, synBillNo=Order2112-0135, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718781283430133760, synBillNo=Order2112-0131, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718783050649800704, synBillNo=Order2112-0130, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718790558223273985, synBillNo=Order2112-0108, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718774363822587905, synBillNo=Order2112-0124, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718775448490573825, synBillNo=Order2112-0124, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718789344584634369, synBillNo=Order2112-0109, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718792545836834816, synBillNo=Order2112-0110, synCount=3, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718792900456841217, synBillNo=Order2112-0111, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=718800669499031553, synBillNo=Order2112-0112, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720661974922461185, synBillNo=Order2109-0613, synCount=5, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720661976293998593, synBillNo=Order2109-0667, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720653841205186561, synBillNo=Order2112-0140, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720654454454370305, synBillNo=Order2112-0138, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672726328705024, synBillNo=Order2109-0648, synCount=5, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672728232919041, synBillNo=Order2109-0613, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672729906446337, synBillNo=Order2109-0673, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672731345092609, synBillNo=Order2109-0695, synCount=5, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672732725018625, synBillNo=Order2109-0642, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672734033641473, synBillNo=Order2109-0637, synCount=5, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672735354847233, synBillNo=Order2109-0667, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720672736835436545, synBillNo=Order2109-0627, synCount=5, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=723575765396869120, synBillNo=Order2112-0152, synCount=2, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=723576003457179649, synBillNo=Order2112-0148, synCount=2, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=720654757987770368, synBillNo=Order2112-0141, synCount=4, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=723531357637042176, synBillNo=Order2110-0101, synCount=9, synMessage=同步异常, synStatus=4), TradeBillSynInfoReq(id=723575249111592960, synBillNo=Order2112-0147, synCount=2, synMessage=同步异常, synStatus=4)]";
        final Pattern compile = Pattern.compile("[id]{2}[=](?<k3>\\d+)");
        final Matcher matcher = compile.matcher(str);
        int matcher_start = 0;
        StringBuffer sb = new StringBuffer();
        while (matcher.find(matcher_start)) {
            final String k3 = matcher.group("k3");
            sb.append(k3).append(",");
            matcher_start = matcher.end();
        }
        System.out.println(sb.toString());
    }

    @org.junit.Test
    public void test() {
        AtomicLong atomicLong = new AtomicLong(10);
        System.out.println(atomicLong.incrementAndGet());
    }

    @org.junit.Test
    public void test2() throws IOException {
        final ClassPathResource resource = new ClassPathResource("goods.json");
        final InputStream inputStream = resource.getInputStream();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        final List<ReceiveSkuDTO> receiveSkuDTOS = JSONObject.parseArray(responseStrBuilder.toString(), ReceiveSkuDTO.class);
        final Set<String> goods = receiveSkuDTOS.stream().map(ReceiveSkuDTO::getGoodsCode).collect(Collectors.toSet());
        final Set<String> skuCodes = receiveSkuDTOS.stream().map(ReceiveSkuDTO::getCode).collect(Collectors.toSet());
        System.out.println("skuCodes = " + skuCodes);
        System.out.println("goods = " + goods);
    }
}
