package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.UserMapper;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserService;
import cdut.rg.bbj.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(String account, String password, String code, HttpServletRequest request) {
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
        User user = userMapper.selectByUserAccount(account);
        if (user == null || !user.getUserPassword().equals(Md5Utils.encryption(account, password))) {
            result.setCode(500);
            result.setMsg("用户名或密码不正确！");
        } else {
            result.setCode(200);
            result.setMsg("登录成功");
            System.out.println("登陆成功");
        }
        return result;
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
                } else {
                    result.setCode(500);
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.setCode(500);
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
    public Result change(String oldPassword, String newPasswordOne, String newPasswordTwo) {
        Result result = new Result();
        System.out.println(oldPassword);
        System.out.println(newPasswordOne);
        System.out.println(newPasswordTwo);
        return result;
    }


}