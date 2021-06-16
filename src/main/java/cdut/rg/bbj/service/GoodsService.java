package cdut.rg.bbj.service;

import java.util.Map;

public interface GoodsService {



    Map<String, Object> getCompare(Integer goodId);

    Map<String, Object> getAll(String title, Integer page, String pnumber, String cnumber);

}
