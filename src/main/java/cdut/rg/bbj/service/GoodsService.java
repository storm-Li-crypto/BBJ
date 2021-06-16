package cdut.rg.bbj.service;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;

import java.util.Map;

public interface GoodsService {



    Map<String, Object> getCompare(Integer goodId);

    Map<String, Object> getAll(String title, Integer page, String pnumber, String cnumber);

    Result getRecommendation(User user);
}
