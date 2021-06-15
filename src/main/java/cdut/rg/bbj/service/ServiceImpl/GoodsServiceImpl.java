package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.GoodsMapper;
import cdut.rg.bbj.dao.UserGoodsMapper;
import cdut.rg.bbj.pojo.Goods;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.UserGoods;
import cdut.rg.bbj.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UserGoodsMapper userGoodsMapper;


    @Override
    public Map<String, Object> getAll(String title, Integer page) {
        Map<String,Object> map = new HashMap<>();
        page = (page - 1) * 100;
        List<Goods> list = goodsMapper.selectKind("food", page);
//        List<Goods> list = goodsMapper.selectTitle(title, page);
        Long count = goodsMapper.countGoods();
        Result result = new Result();
        result.setCode(200);
        result.setCount(count / 100);
        result.setData(list);
        map.put("result", result);
        List link_list = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Goods goods = list.get(i);
            UserGoods userGoods = userGoodsMapper.selectByGoodsId(goods.getId());
            if (userGoods != null) {
                link_list.add(i);
            }
        }
        map.put("links", link_list);
        System.out.println(map);
        return map;
    }
}
