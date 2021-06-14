package cdut.rg.bbj.pojo;

import java.util.List;

/*专门封装返回的数据信息*/
public class Result {

    private Integer code;

    private String msg;

    //数据总量，当前查询对象
    private Long count;

    private List data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
