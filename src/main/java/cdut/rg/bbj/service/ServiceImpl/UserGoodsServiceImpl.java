package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.UserGoodsMapper;
import cdut.rg.bbj.dao.UserMapper;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.pojo.UserGoods;
import cdut.rg.bbj.service.UserGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserGoodsServiceImpl implements UserGoodsService {

    @Autowired
    private UserGoodsMapper userGoodsMapper;

    @Autowired
    private UserMapper userMapper;

    // 基于协同过滤的推荐算法
    // 基于领域的协同过滤算法主要有两种，一种是基于物品的，一种是基于用户的
    @Override
    public Result getRecommendation(User user) {
        Result result = new Result();
        // 收藏的title 店铺 平均价格 种类 别人收藏数
        // 基于物品的协同过滤方法
        // 其基本思想是预先根据所有用户的历史偏好数据计算物品之间的相似性
        // 然后把与用户喜欢的物品相类似的物品推荐给用户，举个例子，物品a和c非常相似，因为喜欢a的用户同时也喜欢c，而用户A喜欢a，所以把c推荐给用户A
        // 基于用户的协同过滤方法 和你兴趣相投的人
        // 其基本思想是如果用户A喜欢物品a，用户B喜欢物品a、b、c，用户C喜欢a和c，那么认为用户A与用户B和C相似，因为他们都喜欢a，而喜欢a的用户同时也喜欢c，所以把c推荐给用户A。
        // 该算法用最近邻居（nearest-neighbor）算法找出一个用户的邻居集合，该集合的用户和该用户有相似的喜好，算法根据邻居的偏好对该用户进行预测。
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
            User cur = userList.get(i);
            List<UserGoods> userGoods1 = userGoodsMapper.selectByUserId(cur.getUserId());
            userItemLength.put(cur.getUserId(), userGoods1.size());
            userId.put(cur.getUserId(), i);
            idUser.put(i,cur.getUserId());
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
        for (Integer item : items) {
            Set<Integer> users = itemUserCollection.get(item); // 得到购买当前物品的所有用户集合
            if( !users.contains(user.getUserId())) {  //如果被推荐用户没有购买当前物品，则进行推荐度计算
                double itemRecommendDegree = 0.0;
                for(Integer u : users) {
                    itemRecommendDegree += sparseMatrix[userId.get(user.getUserId())][userId.get(u)] / Math.sqrt(userItemLength.get(user.getUserId())*userItemLength.get(u));//推荐度计算
                }
                System.out.println("The item "+item+" for "+user.getUserId() +"'s recommended degree:"+itemRecommendDegree);
            }
        }
        return result;
    }
}
