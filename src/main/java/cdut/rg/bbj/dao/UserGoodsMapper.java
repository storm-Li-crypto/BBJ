package cdut.rg.bbj.dao;

import cdut.rg.bbj.pojo.UserGoods;
import java.util.List;

public interface UserGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGoods record);

    UserGoods selectByPrimaryKey(Integer id);

    List<UserGoods> selectAll();

    int updateByPrimaryKey(UserGoods record);

    UserGoods selectByGoodsId(Integer id);
}