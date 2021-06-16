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

    // 根据关键词查找并按分页返回
    @Override
    public Map<String, Object> getAll(String title, Integer page, String pnumber, String cnumber) {
        Map<String,Object> map = new HashMap<>();
        page = (page - 1) * 100;
        System.out.println(pnumber);
        System.out.println(cnumber);
//        if (pnumber == 0 && cnumber == 0) {  // 未排序
//
//        } else if (pnumber == 1) {    // 价格由低到高
//
//        } else if (pnumber == 2) {    // 价格由高到低
//
//        } else if (cnumber == 1) {    // 收藏数由低到高
//
//        } else {        // 收藏数由高到低
//
//        }
        List<Goods> list = goodsMapper.selectTitle(title, page, pnumber.toString(), cnumber.toString());
        Long count = goodsMapper.countGoods();
        Result result = new Result();
        result.setCode(200);
        result.setCount(count / 100);
        result.setData(list);
        map.put("result", result);
        List link_list = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Goods goods = list.get(i);
            System.out.println(goods);
//            // 将描述分词
//            try {
//                goods.setTitle(JieBaUtil.getStringList(goods.getTitle()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            // 获得该页用户收藏的商品下标
            UserGoods userGoods = userGoodsMapper.selectByGoodsId(goods.getId());
            if (userGoods != null) {
                link_list.add(i);
            }
        }
        map.put("links", link_list);
        System.out.println(map);
        return map;
    }

    @Override
    public Map<String, Object> getCompare(Integer goodId) {
        Map<String,Object> map = new HashMap<>();
        Goods goods = goodsMapper.selectByPrimaryKey(goodId);
        map.put("goods", goods);
        int num = 5;
        List<Goods> similarGoods = goodsMapper.selectByKind(goods.getKind());

//        try {
//            List<String> stringList = JieBaUtil.getStringList(goods.getTitle());
//            for (String string : stringList) {
//            if (string.length() == 1) continue;
//            if (flag) {
//                result.append(" ");
//            } else {
//                flag = true;
//            }
//            result.append(string);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return map;
    }


}
