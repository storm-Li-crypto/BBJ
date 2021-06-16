package cdut.rg.bbj.service;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;

public interface UserGoodsService {
    Result getRecommendation(User user);
}
