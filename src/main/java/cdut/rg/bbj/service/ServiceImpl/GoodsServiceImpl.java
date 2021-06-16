package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.GoodsMapper;
import cdut.rg.bbj.dao.UserGoodsMapper;
import cdut.rg.bbj.pojo.Goods;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.UserGoods;
import cdut.rg.bbj.service.GoodsService;
import cdut.rg.bbj.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        List<Goods> list = goodsMapper.selectTitle(title, page, pnumber, cnumber);
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
        System.out.println(goods.getTitle());
        map.put("goods", goods);
        int num = 5;
        List<Goods> similarGoods = goodsMapper.selectByKind(goods.getKind());
        List<Goods> resultGoods = new ArrayList<Goods>();
        // 按相似度排序 降序
        Map<Float, Goods> similarMap = new TreeMap<>(new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o2.compareTo(o1);
            }
        });
        // 计算相似度
        for (Goods otherGoods : similarGoods) {
            if (otherGoods.getId() == goodId) continue;
            float index = StringUtil.getSimilarityRatio(goods.getTitle(), otherGoods.getTitle());
            similarMap.put(index, otherGoods);
        }
        // 取最相似的前五个商品
        int i = 0;
        for (Map.Entry<Float, Goods> entry : similarMap.entrySet()) {
            if (i == num) break;
            resultGoods.add(entry.getValue());
            System.out.println(entry.getKey()+ entry.getValue().getTitle());
            i++;
        }
        Result result = new Result();
        result.setCode(200);
        result.setData(resultGoods);
        result.setMsg("返回相似商品成功！");
        map.put("result", result);
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
//
//    @Override
//    public Result getRecommendation(HttpServletRequest request) {
//        Result result = new Result();
//
//        return result;
//    }

}
