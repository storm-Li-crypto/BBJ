package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.GoodsMapper;
import cdut.rg.bbj.dao.UserGoodsMapper;
import cdut.rg.bbj.pojo.Goods;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
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
    public Map<String, Object> getAll(User user, String title, Integer page, String pnumber, String cnumber) {
        Map<String,Object> map = new HashMap<>();
        int limit = 20; // 设置每页显示的商品数量
        page = (page - 1) * limit;   // 计算查找开始行
        List<Goods> all = goodsMapper.selectAllByTitle(title);
        List<Goods> list = goodsMapper.selectTitle(title, page, limit, pnumber, cnumber);
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
        result.setMsg("查找成功！");
        map.put("result", result);
//        List link_list = new ArrayList();
//        for (int i = 0; i < list.size(); i++) {
//            Goods goods = list.get(i);
//            // 获得该页用户收藏的商品下标
//            UserGoods userGoods = userGoodsMapper.selectByUG(user.getUserId(), goods.getId());
//            if (userGoods != null) {
//                link_list.add(i);
//            }
//        }
//        map.put("links", link_list);
        return map;
    }

    @Override
    public Map<String, Object> getCompare(Integer goodId) {
        Map<String,Object> map = new HashMap<>();
        // 查询当前商品
        Goods goods = goodsMapper.selectByPrimaryKey(goodId);
        map.put("goods", goods);
        int num = 5;
        // 同类商品列表
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
        result.setCount((long) resultGoods.size());
        result.setMsg("返回相似商品成功！");
        map.put("result", result);
        return map;
    }

}
