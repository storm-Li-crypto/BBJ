package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.GoodsMapper;
import cdut.rg.bbj.dao.UserGoodsMapper;
import cdut.rg.bbj.pojo.Goods;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
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
        int limit = 100;
        page = (page - 1) * limit;
        System.out.println(pnumber);
        System.out.println(cnumber);
        List<Goods> all = goodsMapper.selectAllByTitle(title);
        List<Goods> list = goodsMapper.selectTitle(title, page, pnumber, cnumber);
        Result result = new Result();
        result.setCode(200);
        if (all.size() == 0) {
            result.setCount(0L);
        } else if (all.size() % limit == 0) {
            result.setCount((long) (all.size()/limit));
        } else {
            result.setCount((long) (all.size()/limit+1));
        }
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
        System.out.println(goods.getId());
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
            if (otherGoods.getId().equals(goods.getId())) continue;
            float index = StringUtil.getSimilarityRatio(goods.getTitle(), otherGoods.getTitle());
            similarMap.put(index, otherGoods);
        }
        // 取最相似的前五个商品
        int i = 0;
        for (Map.Entry<Float, Goods> entry : similarMap.entrySet()) {
            if (i == num) break;
            resultGoods.add(entry.getValue());
            System.out.println(entry.getValue().getId() + " " + entry.getKey()+ entry.getValue().getTitle());
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

    @Override
    public Result getRecommendation(User user) {
        Result result = new Result();
        // 收藏的title 店铺 平均价格 种类 别人收藏数
        // 基于物品的协同过滤方法
        // 其基本思想是预先根据所有用户的历史偏好数据计算物品之间的相似性
        // 然后把与用户喜欢的物品相类似的物品推荐给用户，举个例子，物品a和c非常相似，因为喜欢a的用户同时也喜欢c，而用户A喜欢a，所以把c推荐给用户A
        // 基于用户的协同过滤方法
        // 其基本思想是如果用户A喜欢物品a，用户B喜欢物品a、b、c，用户C喜欢a和c，那么认为用户A与用户B和C相似，因为他们都喜欢a，而喜欢a的用户同时也喜欢c，所以把c推荐给用户A。
        // 该算法用最近邻居（nearest-neighbor）算法找出一个用户的邻居集合，该集合的用户和该用户有相似的喜好，算法根据邻居的偏好对该用户进行预测。
        return result;
    }

}
