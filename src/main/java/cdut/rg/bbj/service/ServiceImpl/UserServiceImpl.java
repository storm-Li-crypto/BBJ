package cdut.rg.bbj.service.ServiceImpl;

import cdut.rg.bbj.dao.UserMapper;
import cdut.rg.bbj.pojo.Result;
import cdut.rg.bbj.pojo.User;
import cdut.rg.bbj.service.UserService;
import cdut.rg.bbj.util.MailUtil;
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
    public Map<String,Object> login(HttpServletRequest request, User loginUser, String code) {
        Result result = new Result();//最终需要返回一个result值回去
        Map<String,Object> loginMap = new HashMap<>();
        Map<String,Object> dataMap = new HashMap<>();
        String token = null;
        //验证之前，需要先验证验证码是否正确，获取到验证码
        //之前将验证码放在了session中
        String codeValue = (String) request.getSession().getAttribute("code");//之前的key为code   强转
        if (codeValue.equalsIgnoreCase(code)) {
//        if (code.equals("555")) {
            //先用用户名去数据库查找用户，再判断密码
            //得到一个users对象
            User user = userMapper.selectByUserAccount(loginUser.getUserAccount());
            if (user == null || !user.getUserPassword().equals(Md5Utils.encryption(loginUser.getUserAccount(), loginUser.getUserPassword()))) {
                result.setCode(500);
                result.setMsg("用户名或密码不正确！");
            } else {
                token = TokenUtil.sign(loginUser.getUserAccount());//后端收到请求，验证用户名和密码，验证成功，就给前端返回一个token
                result.setCode(200);
                result.setMsg("登录成功");
                System.out.println("登陆成功");
            }
        } else {
            result.setCode(500);
            result.setMsg("验证码错误");
        }
        dataMap.put("token",token);
        loginMap.put("data",dataMap);
        loginMap.put("result",result);
        return loginMap;
    }

    @Override
    public Result sendMail(HttpServletRequest request, String userTel) {
        Result result = new Result();
        Integer code = null;
        try {
            code = MailUtil.sendMail(userTel);
            request.getSession().setAttribute("emailCode", code);
            result.setMsg("发送验证码成功！");
            result.setCode(200);
        } catch (Exception e) {
            result.setMsg("发送验证码失败");
            result.setCode(500);
            e.printStackTrace();
        }
        return result;
    }


    @Transactional
    @Override
    public Result register(HttpServletRequest request, User loginUser, String emailCode) {
        Result result = new Result();
        String userAccount = loginUser.getUserAccount();
        // 先去数据库中查找有无该用户名
        User user = userMapper.selectByUserAccount(userAccount);
        if (user == null) {   // 判断用户名是否已经注册过
            Integer codeValue = (Integer) request.getSession().getAttribute("emailCode");
            if (codeValue.toString().equals(emailCode)) {  // 验证用户输入的验证码是否正确
                try {
                    // 将用户密码加密
                    loginUser.setUserPassword(Md5Utils.encryption(loginUser.getUserAccount(), loginUser.getUserPassword()));
                    // 插入用户
                    int i = userMapper.insert(loginUser);
                    if (i > 0) {  // 插入成功
                        result.setCode(200);
                        result.setMsg("用户已成功注册！");
                    } else {  // 未插入
                        result.setCode(500);
                        result.setMsg("注册失败！");
                    }
                } catch (Exception e) {   // 插入时出现Exception
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.setCode(500);
                    result.setMsg("数据库插入错误！");
                }
            } else {  // 验证码错误
                result.setMsg("验证码输入错误！");
                result.setCode(500);
            }
        } else {  // 用户名已注册过
            result.setCode(500);
            result.setMsg("用户已存在！");
        }
        return result;
    }

    @Transactional
    @Override
    public Result changePassword(HttpServletRequest request, String oldPassword, String newPasswordOne, String newPasswordTwo) {
        Result result = new Result();
        String token = request.getHeader("Authorization");
        String account = TokenUtil.getUserID(token);
        if (!account.equals("")) {  // 请求成功
            // 获得当前用户
            User user = userMapper.selectByUserAccount(account);
            String password = Md5Utils.encryption(user.getUserAccount(), oldPassword);
            if (user.getUserPassword().equals(password)) {  // 判断旧密码是否正确
                if (newPasswordOne.equals(newPasswordTwo)) {  // 判断两个新密码是否一致
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
                } else {   // 新密码不一致
                    result.setCode(500);
                    result.setMsg("新密码不一致！");
                }
            } else {  // 旧密码错误
                result.setCode(500);
                result.setMsg("旧密码错误！");
            }
        } else {  // 请求无效
            result.setCode(500);
            result.setMsg("请求无效！");
        }
        return result;
    }

    @Override
    public User getUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String account = TokenUtil.getUserID(token);
        User user = null;
        if (account != "") {
            user = userMapper.selectByUserAccount(account);
        }
        // 返回当前用户
        return user;
    }

    @Transactional
    @Override
    public Result changeInformation(HttpServletRequest request, User loginUser) {
        Result result = new Result();
        String token = request.getHeader("Authorization");
        String account = TokenUtil.getUserID(token);
        User user = userMapper.selectByUserAccount(account);
        user.setUserAge(loginUser.getUserAge());
        user.setUserAnswer(loginUser.getUserAnswer());
        user.setUserSex(loginUser.getUserSex());
        user.setUserTel(loginUser.getUserTel());
        try {
            int i = userMapper.updateByPrimaryKey(user);
            if (i > 0) {
                result.setCode(200);
                result.setMsg("用户信息更改成功！");
            } else {
                result.setCode(500);
                result.setMsg("数据库未更改！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.setCode(500);
            result.setMsg("数据库更新失败！");
        }
        return result;
    }

    @Override
    public Result findPassword(HttpServletRequest request, String userAccount, String userTel, String newPassword, String scdPassword, String emailCode) {
        Result result = new Result();
        if (newPassword.equals(scdPassword)) {   // 判断两次新密码是否一致
            User user = userMapper.selectByUserAccount(userAccount);
            if (user == null) {  // 判断用户名存不存在
                result.setCode(500);
                result.setMsg("用户名不存在！");
            } else {    // 用户名存在
                if (user.getUserTel().equals(userTel)) { // 判断邮箱是否是该用户的
                    Integer codeValue = (Integer) request.getSession().getAttribute("emailCode");//之前的key为code   强转
                    if (codeValue.toString().equals(emailCode)) {   // 判断验证码是否正确
                        try {
                            user.setUserPassword(Md5Utils.encryption(userAccount, newPassword));
                            int i = userMapper.updateByPrimaryKey(user);
                            if (i > 0) {
                                result.setCode(200);
                                result.setMsg("密码找回成功！");
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
                        result.setMsg("验证码不正确！");
                    }
                } else {
                    result.setCode(500);
                    result.setMsg("该邮箱不是此用户的！");
                }
            }
        } else {
            result.setCode(500);
            result.setMsg("新密码不一致！");
        }
        return result;
    }

}