package coin.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Robin Wang  on 2018/4/20.
 */
@UtilityClass
public class CoinGetUtil {

    /**
     * coin-API 请求方法
     * @param url
     * @return
     */
    public String get(String url) {
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public boolean isAPISuccess(String res){
            return StringUtils.isNotEmpty(res) && res.startsWith("[");
    }




}
