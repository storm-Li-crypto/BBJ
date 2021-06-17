package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.GoodsMapper;
import cdut.rg.bbj.dao.UserGoodsMapper;
import cdut.rg.bbj.dao.UserMapper;
import cdut.rg.bbj.pojo.Goods;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.pojo.UserGoods;
import cdut.rg.bbj.service.UserGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

@Service
public class UserGoodsServiceImpl implements UserGoodsService {

    @Autowired
    private UserGoodsMapper userGoodsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    // 基于协同过滤的推荐算法
    // 基于领域的协同过滤算法主要有两种，一种是基于物品的，一种是基于用户的
    @Override
    public Result getRecommendation(String account) {
        int n = 100;
        Result result = new Result();
        List<Goods> goodsList = goodsMapper.selectAll();
        if (account == "") {
            List<Goods> resultGoods = new ArrayList<Goods>();
            for (int j = 0; j < n; j++) {
                Random random = new Random();
                Integer index = random.nextInt(goodsList.size());
                Goods g = goodsList.get(index);
                resultGoods.add(g);
                goodsList.remove(g);
            }
            result.setCode(200);
            result.setData(resultGoods);
            result.setCount((long) n);
            result.setMsg("返回推荐商品成功！");
            return result;
        }
        // 收藏的title 店铺 平均价格 种类 别人收藏数
        // 基于物品的协同过滤方法
        // 其基本思想是预先根据所有用户的历史偏好数据计算物品之间的相似性
        // 然后把与用户喜欢的物品相类似的物品推荐给用户，举个例子，物品a和c非常相似，因为喜欢a的用户同时也喜欢c，而用户A喜欢a，所以把c推荐给用户A
        // 基于用户的协同过滤方法 和你兴趣相投的人
        // 其基本思想是如果用户A喜欢物品a，用户B喜欢物品a、b、c，用户C喜欢a和c，那么认为用户A与用户B和C相似，因为他们都喜欢a，而喜欢a的用户同时也喜欢c，所以把c推荐给用户A。
        // 该算法用最近邻居（nearest-neighbor）算法找出一个用户的邻居集合，该集合的用户和该用户有相似的喜好，算法根据邻居的偏好对该用户进行预测。
        User user = userMapper.selectByUserAccount(account);
        int num = userMapper.countUser();
        int[][] sparseMatrix = new int[Math.toIntExact(num)][Math.toIntExact(num)];
        Map<Integer, Integer> userItemLength = new HashMap<>(); // 每个用户对应的不同物品总数
        Map<Integer, Set<Integer>> itemUserCollection = new HashMap<>(); // 物品到用户的倒排表
        Set<Integer> items = new HashSet<>();  // 辅助存储物品集合
        Map<Integer, Integer> userId = new HashMap<>(); // 辅助存储每个用户的ID映射
        Map<Integer, Integer> idUser = new HashMap<>(); // 辅助存储每个Id对应的用户映射
        List<UserGoods> userGoodsList = userGoodsMapper.selectAll();
        List<User> userList = userMapper.selectAll();
        for (int i = 0; i < num; i++) {
            Integer cur_user = userList.get(i).getUserId();
            List<UserGoods> userGoods1 = userGoodsMapper.selectByUserId(cur_user);
            userItemLength.put(cur_user, userGoods1.size());
            userId.put(cur_user, i);
            idUser.put(i,cur_user);
        }
        for (int i = 0; i < userGoodsList.size(); i++) {
            UserGoods userGoods = userGoodsList.get(i);
            if (items.contains(userGoods.getGoodsId())) {
                itemUserCollection.get(userGoods.getGoodsId()).add(userGoods.getUserId());
            } else {
                items.add(userGoods.getGoodsId());
                itemUserCollection.put(userGoods.getGoodsId(), new HashSet<>());
                itemUserCollection.get(userGoods.getGoodsId()).add(userGoods.getUserId());
            }
        }
        System.out.println(itemUserCollection.toString());
        // 计算相似度矩阵
        Set<Map.Entry<Integer, Set<Integer>>> entrySet = itemUserCollection.entrySet();
        Iterator<Map.Entry<Integer, Set<Integer>>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Set<Integer> commonUsers = iterator.next().getValue();
            for(Integer user_u: commonUsers) {
                for(Integer user_v : commonUsers) {
                    if (user_u.equals(user_v)) {
                        continue;
                    }
                    sparseMatrix[userId.get(user_u)][userId.get(user_v)] += 1; // 计算用户u与用户v都有正反馈的物品总数
                }
            }
        }
        //计算指定用户user的物品推荐度
        List<Goods> resultGoods = new ArrayList<Goods>();
        Map<Double, Goods> recommendMap = new TreeMap<>(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        });
        for (Integer item : items) {  // 遍历每个商品
            Set<Integer> users = itemUserCollection.get(item); // 得到购买当前物品的所有用户集合
            if( !users.contains(user.getUserId())) {  //如果被推荐用户没有购买当前物品，则进行推荐度计算
                double itemRecommendDegree = 0.0;
                for(Integer u : users) {
                    itemRecommendDegree += sparseMatrix[userId.get(user.getUserId())][userId.get(u)] / Math.sqrt(userItemLength.get(user.getUserId())*userItemLength.get(u));//推荐度计算
                }
                recommendMap.put(itemRecommendDegree, goodsMapper.selectByPrimaryKey(item));
                System.out.println("The item "+item+" for "+user.getUserId() +"'s recommended degree:"+itemRecommendDegree);
            }
        }
        int i = 0;
        for (Map.Entry<Double, Goods> entry : recommendMap.entrySet()) {
            if (i == n) break;
            resultGoods.add(entry.getValue());
            System.out.println(entry.getValue().getId() + " " + entry.getKey()+ " " + entry.getValue().getTitle());
            i++;
        }
        // 不足n个，随机再取
        while (resultGoods.size() != n) {
            Random random = new Random();
            Integer index = random.nextInt(goodsList.size());
            Goods g = goodsList.get(index);
            resultGoods.add(g);
            goodsList.remove(g);
        }
        result.setCode(200);
        result.setData(resultGoods);
        result.setCount((long) n);
        result.setMsg("返回推荐商品成功！");
        return result;
    }

    @Transactional
    @Override
    public Result change(User user,Integer goodsId, Integer isLinks) {
        Result result = new Result();
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        Integer links = goods.getLikes();
        try {
            if (isLinks == 0) { // 收藏
                UserGoods userGoods = null;
                userGoods.setUserId(user.getUserId());
                userGoods.setGoodsId(goodsId);
                int i = userGoodsMapper.insert(userGoods);
                links++;
                goods.setLikes(links);
                int j = goodsMapper.updateByPrimaryKey(goods);
                if (i > 0 && j > 0) {
                    result.setCode(200);
                    result.setMsg("收藏成功！");
                } else {
                    result.setCode(500);
                    result.setMsg("收藏失败！");
                }
                return result;
            } else {  // 取消收藏
                UserGoods userGoods = userGoodsMapper.selectByUG(user.getUserId(),goodsId);
                int i = userGoodsMapper.deleteByPrimaryKey(userGoods.getId());
                links--;
                goods.setLikes(links);
                int j = goodsMapper.updateByPrimaryKey(goods);
                if (i > 0 && j > 0) {
                    result.setCode(200);
                    result.setMsg("取消收藏成功！");
                } else {
                    result.setCode(500);
                    result.setMsg("取消收藏失败！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setCode(500);
            result.setMsg("数据库更改错误！");
            return result;
        }
        System.out.println("\033[47;4m" + "hhhhhh" + "\033[0m");
        return null;
    }

    @Override
    public Result getLinkList(User user) {
        Result result = new Result();
        List<UserGoods> userGoodsList = userGoodsMapper.selectByUserId(user.getUserId());
        List<Goods> resultList = new ArrayList<>();
        for (int i = 0; i < userGoodsList.size(); i++) {
            UserGoods userGoods = userGoodsList.get(i);
            Integer goodsId = userGoods.getGoodsId();
            Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
            resultList.add(goods);
        }
        result.setCount((long) resultList.size());
        result.setMsg("返回用户所有的收藏商品列表");
        result.setData(resultList);
        result.setCode(200);
        return result;
    }
}
