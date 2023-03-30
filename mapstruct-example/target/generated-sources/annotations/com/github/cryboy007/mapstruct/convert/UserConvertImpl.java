package com.github.cryboy007.mapstruct.convert;

import com.github.cryboy007.mapstruct.Vo.UserEnum;
import com.github.cryboy007.mapstruct.Vo.UserVo;
import com.github.cryboy007.mapstruct.Vo.UserVo2;
import com.github.cryboy007.mapstruct.Vo.UserVo3;
import com.github.cryboy007.mapstruct.Vo.UserVo4;
import com.github.cryboy007.mapstruct.model.User;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-06T09:19:52+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_191 (Oracle Corporation)"
)
public class UserConvertImpl implements UserConvert {

    @Override
    public UserVo toConvertVO1(User source) {
        if ( source == null ) {
            return null;
        }

        UserVo userVo = new UserVo();

        userVo.setId( source.getId() );
        userVo.setName( source.getName() );
        userVo.setCreateTime( source.getCreateTime() );
        userVo.setUpdateTime( source.getUpdateTime() );

        return userVo;
    }

    @Override
    public User fromConvertEntity1(UserVo userVo) {
        if ( userVo == null ) {
            return null;
        }

        User user = new User();

        user.setId( userVo.getId() );
        user.setName( userVo.getName() );
        user.setCreateTime( userVo.getCreateTime() );
        user.setUpdateTime( userVo.getUpdateTime() );

        return user;
    }

    @Override
    public List<UserVo> toConvertVO1(List<User> source) {
        if ( source == null ) {
            return null;
        }

        List<UserVo> list = new ArrayList<UserVo>( source.size() );
        for ( User user : source ) {
            list.add( toConvertVO1( user ) );
        }

        return list;
    }

    @Override
    public UserVo2 toConvertVO2(User source) {
        if ( source == null ) {
            return null;
        }

        UserVo2 userVo2 = new UserVo2();

        userVo2.setId( source.getId() );
        userVo2.setName( source.getName() );
        userVo2.setCreateTime( source.getCreateTime() );

        return userVo2;
    }

    @Override
    public UserVo3 toConvertVO3(User source) {
        if ( source == null ) {
            return null;
        }

        UserVo3 userVo3 = new UserVo3();

        if ( source.getId() != null ) {
            userVo3.setId( String.valueOf( source.getId() ) );
        }
        userVo3.setName( source.getName() );
        if ( source.getUpdateTime() != null ) {
            userVo3.setUpdateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( source.getUpdateTime() ) );
        }

        userVo3.setCreateTime( cn.hutool.core.date.LocalDateTimeUtil.parse(source.getCreateTime(),"yyy-MM-dd HH:mm:ss") );

        return userVo3;
    }

    @Override
    public User fromConvertEntity3(UserVo3 userVO3) {
        if ( userVO3 == null ) {
            return null;
        }

        User user = new User();

        if ( userVO3.getId() != null ) {
            user.setId( Integer.parseInt( userVO3.getId() ) );
        }
        user.setName( userVO3.getName() );
        if ( userVO3.getCreateTime() != null ) {
            user.setCreateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( userVO3.getCreateTime() ) );
        }
        if ( userVO3.getUpdateTime() != null ) {
            user.setUpdateTime( java.time.LocalDateTime.parse( userVO3.getUpdateTime() ) );
        }

        return user;
    }

    @Override
    public UserVo4 toConvertVO5(UserEnum source) {
        if ( source == null ) {
            return null;
        }

        UserVo4 userVo4 = new UserVo4();

        if ( source.getUserEnum() != null ) {
            userVo4.setType( source.getUserEnum().name() );
        }
        userVo4.setId( source.getId() );
        userVo4.setName( source.getName() );

        return userVo4;
    }

    @Override
    public UserEnum fromConvertEntity5(UserVo4 userVo4) {
        if ( userVo4 == null ) {
            return null;
        }

        UserEnum userEnum = new UserEnum();

        userEnum.setId( userVo4.getId() );
        userEnum.setName( userVo4.getName() );

        return userEnum;
    }
}
