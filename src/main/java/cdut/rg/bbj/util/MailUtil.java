package cdut.rg.bbj.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

public class MailUtil {

    //发件人地址
    public static String senderAddress = "18030458672@163.com";
    //发件人账户名
    public static String senderAccount = "18030458672@163.com";

    public static Integer sendMail(String recipientAddress) throws Exception {

        Integer code = (int)((Math.random()*9+1)*100000);
        //发件人账户密码
        String senderPassword = "KPOROHOMTXPISTJQ";
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.ssl.enable", true);
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.163.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        // 设置邮箱发送服务器端口
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        //设置用户的认证方式
        props.put("mail.smtp.auth", "true");
        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //3、创建邮件的实例对象
        Message msg = getMimeMessage(session, recipientAddress,code.toString());
        //4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg,msg.getAllRecipients());

        //如果只想发送给指定的人，可以如下写法
        //transport.sendMessage(msg, new Address[]{new InternetAddress("xxx@qq.com")});

        //5、关闭邮件连接
        transport.close();
        return code;
    }


    public static MimeMessage getMimeMessage(Session session, String recipientAddress, String code) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddress));
        //设置邮件主题
        msg.setSubject("邮件主题","UTF-8");
        //设置邮件正文
        msg.setContent("你的验证码为" + code, "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        return msg;
    }
}
