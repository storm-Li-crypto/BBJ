package cdut.rg.bbj.dao;

import cdut.rg.bbj.pojo.UserGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGoods record);

    UserGoods selectByPrimaryKey(Integer id);

    List<UserGoods> selectAll();

    int updateByPrimaryKey(UserGoods record);

    List<UserGoods> selectByGoodsId(Integer goodsId);

    List<UserGoods> selectByUserId(Integer userId);

    UserGoods selectByUG(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}