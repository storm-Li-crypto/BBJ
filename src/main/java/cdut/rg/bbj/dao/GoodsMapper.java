package cdut.rg.bbj.dao;

import cdut.rg.bbj.pojo.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    Goods selectByPrimaryKey(Integer id);

    List<Goods> selectAll();

    int updateByPrimaryKey(Goods record);

    List<Goods> selectTitle(@Param("title") String title, @Param("page") Integer page, @Param("pnumber") String pnumber, @Param("cnumber") String cnumber);

    Long countGoods();

    List<Goods> selectByKind(String kind);

    List<Goods> selectAllByTitle(String title);
}