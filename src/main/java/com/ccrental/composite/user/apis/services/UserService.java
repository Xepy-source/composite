package com.ccrental.composite.user.apis.services;

import com.ccrental.composite.common.utillity.Converter;
import com.ccrental.composite.common.utillity.Sha512;
import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.user.apis.containers.EmailFindResultContainer;
import com.ccrental.composite.user.apis.daos.UserDao;
import com.ccrental.composite.user.apis.enums.*;
import com.ccrental.composite.user.apis.vos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserService {
    private static final String EMAIL_REGEX = "^(?=.{4,100}.$)([0-9a-zA-Z][0-9a-zA-Z\\-_.]*[0-9a-zA-Z])@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$";
    private static final String PASSWORD_REGEX = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{4,100})$";

    private final MailService mailService;
    private final DataSource dataSource;
    private final UserDao userDao;

    @Autowired
    public UserService(MailService mailService, DataSource dataSource, UserDao userDao) {
        this.dataSource = dataSource;
        this.userDao = userDao;
        this.mailService = mailService;
    }

    public UserLoginResult login(UserLoginVo userLoginVo)
            throws SQLException {
        UserLoginResult userLoginResult;
        if (!userLoginVo.getEmail().matches(EMAIL_REGEX) ||
                !userLoginVo.getPassword().matches(PASSWORD_REGEX)) {
            userLoginResult = UserLoginResult.FAILURE;
        } else {
            try (Connection connection = this.dataSource.getConnection()) {
                UserVo userVo = this.userDao.selectUser(connection, userLoginVo);
                if (userVo == null) {
                    userLoginResult = UserLoginResult.FAILURE;
                } else {
                    Converter.setUserVo(userLoginVo.getRequest(), userVo);
                    userLoginResult = UserLoginResult.SUCCESS;
                }
            }
        }
        return userLoginResult;
    }

    public UserRegisterResult Register(UserRegisterVo userRegisterVo)
            throws SQLException {
        UserRegisterResult userRegisterResult;
        try (Connection connection = this.dataSource.getConnection()) {
            if (this.userDao.selectEmailConut(connection, userRegisterVo.getUser_email()) > 0) {
                userRegisterResult = UserRegisterResult.EMAIL_DUPLICATE;
            } else if (this.userDao.selectContactCount(connection, userRegisterVo.getUser_contact()) > 0) {
                userRegisterResult = UserRegisterResult.CONTACT_DUPLICATE;
            } else {
                this.userDao.insertUser(connection, userRegisterVo);
                if (this.userDao.selectEmailConut(connection, userRegisterVo.getUser_email()) > 0) {
                    userRegisterResult = UserRegisterResult.SUCCESS;
                } else {
                    userRegisterResult = UserRegisterResult.FAILURE;
                }
            }
        }
        return userRegisterResult;
    }


    public EmailFindResultContainer findEmail(EmailFindVo emailFindVo)
            throws SQLException {
        if (emailFindVo.getName().equals("") || emailFindVo.getContact().equals("")) {
            return new EmailFindResultContainer(EmailFindResult.NORMALIZATION_FAILURE);
        } else {
            try (Connection connection = this.dataSource.getConnection()) {
                String email = this.userDao.selectEmail(connection, emailFindVo);
                if (email == null) {
                    return new EmailFindResultContainer(EmailFindResult.FAILURE);
                } else {
                    return new EmailFindResultContainer(EmailFindResult.SUCCESS, email);
                }
            }
        }
    }

    public PasswordFindResult findPassword(FindPasswordVo findPasswordVo) throws
            SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            UserVo userVo = this.userDao.selectUser(connection, findPasswordVo);
            if (userVo == null) {
                return PasswordFindResult.USER_NOT_FOUND;
            } else{
                String key = Sha512.hash(String.format("%s%s%s%s%f",
                        findPasswordVo.getEmail(),
                        findPasswordVo.getName(),
                        findPasswordVo.getContact(),
                        new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),
                        Math.random()
                ));
                this.userDao.insertPasswordKey(connection, findPasswordVo, key);
                SendMailVo sendMailVo = new SendMailVo(
                        "[사이트 이름] 비밀번호 재설정 링크",
                        String.format("<a href=\"http://127.0.0.1/main/?action=reset_password&key=%s\" target=\"_blank\">복사하여 주소록에 붙이시오. </a>",
                                key),
                        findPasswordVo.getEmail()
                );
                this.mailService.send(sendMailVo);
                return PasswordFindResult.EMAIL_SENT;
            }
        }
    }

    public ResetPasswordResult resetPassword(ResetPasswordVo resetPasswordVo) throws
            SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            String email = this.userDao.selectPasswordKeyEmail(connection, resetPasswordVo);
            if (email == null) {
                return ResetPasswordResult.FAILURE;
            }
            this.userDao.updatePasswordKeyUsed(connection, resetPasswordVo);
            this.userDao.updatePassword(connection, resetPasswordVo, email);
            return ResetPasswordResult.SUCCESS;
        }
    }
}


