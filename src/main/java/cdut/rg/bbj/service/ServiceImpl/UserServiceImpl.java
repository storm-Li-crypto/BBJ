package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.UserMapper;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserService;
import cdut.rg.bbj.util.Md5Utils;
import cdut.rg.bbj.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String,Object> login(HttpServletRequest request, User loginUser) {
        Result result = new Result();//最终需要返回一个result值回去
        //验证之前，需要先验证验证码是否正确，获取到验证码
        //之前将验证码放在了session中
//        String codeValue = (String) request.getSession().getAttribute("code");//之前的key为code   强转
//        if (codeValue.equalsIgnoreCase(code)) {
//            //先用用户名去数据库查找用户，再判断密码
//            User users = UserMapper.selectByUsername(username);
//            //得到一个users对象
//            if (users == null || !users.getPassword().equals(Md5Utils.encryption(username, password))) {//没找到用户
//                //密码不相等
//                result.setCode(500);
//                result.setMsg("用户名或密码错误");
//            } else {
//                //执行成功代码
//                result.setMsg("success");
//                result.setCode(200);
//            }
//        } else {
//            result.setCode(500);
//            result.setMsg("验证码错误");
//        }
        Map<String,Object> loginMap = new HashMap<>();
        Map<String,Object> dataMap = new HashMap<>();
        System.out.println("用户账号"+loginUser.getUserAccount());
        System.out.println("用户密码"+loginUser.getUserPassword());
        String token = TokenUtil.sign(loginUser.getUserAccount());//后端收到请求，验证用户名和密码，验证成功，就给前端返回一个token
        dataMap.put("token",token);
        loginMap.put("data",dataMap);
        User user = userMapper.selectByUserAccount(loginUser.getUserAccount());
        if (user == null || !user.getUserPassword().equals(Md5Utils.encryption(loginUser.getUserAccount(), loginUser.getUserPassword()))) {
            result.setCode(500);
            result.setMsg("用户名或密码不正确！");
        } else {
            result.setCode(200);
            result.setMsg("登录成功");
            System.out.println("登陆成功");
        }
        loginMap.put("result",result);
        return loginMap;
    }

    @Transactional
    @Override
    public Result register(HttpServletRequest request, User loginUser) {
        Result result = new Result();
        String userAccount = loginUser.getUserAccount();
        User user = userMapper.selectByUserAccount(userAccount);
        if (user == null) {
            try {
                loginUser.setUserPassword(Md5Utils.encryption(loginUser.getUserAccount(), loginUser.getUserPassword()));
                int i = userMapper.insert(loginUser);
                if (i > 0) {
                    result.setCode(200);
                    result.setMsg("用户已成功注册！");
                } else {
                    result.setCode(500);
                    result.setMsg("注册失败！");
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setCode(500);
                result.setMsg("数据库插入错误！");
                return result;
            }
        } else {
            result.setCode(500);
            result.setMsg("用户已存在！");
            return result;
        }
    }

    @Transactional
    @Override
    public Result changePassword(HttpServletRequest request, String oldPassword, String newPasswordOne, String newPasswordTwo) {
        Result result = new Result();
        String token = request.getHeader("Authorization");
        String account = TokenUtil.getUserID(token);
        // 获得当前用户
        User user = userMapper.selectByUserAccount(account);
        String password = Md5Utils.encryption(user.getUserAccount(), oldPassword);
        if (user.getUserPassword().equals(password)) {
            if (newPasswordOne.equals(newPasswordTwo)) {
                try {
                    String newPassword = Md5Utils.encryption(user.getUserAccount(), newPasswordOne);
                    user.setUserPassword(newPassword);
                    int i = userMapper.updateByPrimaryKey(user);
                    if (i > 0) {
                        result.setCode(200);
                        result.setMsg("密码更改成功！");
                    } else {
                        result.setCode(500);
                        result.setMsg("数据库未更改！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.setCode(500);
                    result.setMsg("数据库插入失败！");
                }
            } else {
                result.setCode(500);
                result.setMsg("新密码不一致！");
            }
        } else {
            result.setCode(500);
            result.setMsg("旧密码错误！");
        }
        System.out.println(oldPassword);
        System.out.println(newPasswordOne);
        System.out.println(newPasswordTwo);
        return result;
    }

    @Override
    public User getUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("用户token"+token);
        String account = TokenUtil.getUserID(token);
        System.out.println("用户token"+account);
        // 返回当前用户
        return userMapper.selectByUserAccount(account);
    }


}