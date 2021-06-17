package cdut.rg.bbj.service;

import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;

public interface UserGoodsService {
    Result getRecommendation(String account);

    Result change(User user, Integer goodsId, Integer isLinks);

    Result getLinkList(User user);
}
