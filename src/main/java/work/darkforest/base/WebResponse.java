package work.darkforest.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WebResponse {
    private String state;
    private String msg;
    private Object data;

    public static class Builder{
        public static WebResponse success(){
            return new WebResponse().setState("success");
        }

        public static WebResponse success(String msg){
            return new WebResponse().setState("success").setMsg(msg);
        }

        public static WebResponse success(Object data){
            return new WebResponse().setState("success").setData(data);
        }

        public static WebResponse success(String msg, Object data){
            return new WebResponse().setState("success").setMsg(msg).setData(data);
        }

        public static WebResponse fail(String msg){
            return new WebResponse().setState("fail").setMsg(msg);
        }
    }
}
