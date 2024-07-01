package com.sajiman.mychat.utility;

import com.sajiman.mychat.dataModels.UserDto;

import java.util.ArrayList;
import java.util.List;

public class MiscellaneousUtils {

    public static StringBuilder getParam(UserDto userDto) {
        StringBuilder paramBuilder = new StringBuilder();
        paramBuilder.append("full_name=" + userDto.getFullName() +
                "&username=" + userDto.getUsername() +
                "&password=" + userDto.getPassword() +
                "&email=" + userDto.getEmail() +
                "&gender=" + userDto.getGender() +
                "&day=" + userDto.getDay() +
                "&month=" + userDto.getMonth() +
                "&year=" + userDto.getYear());
        return paramBuilder;
    }

    public List<UserDto> listParam(UserDto userDto) {
        List<UserDto> userInfoList = new ArrayList<>();

        return userInfoList;
    }

    public static StringBuilder getLoginParam(UserDto userDto) {
        StringBuilder paramBuilder = new StringBuilder();
        paramBuilder.append("username=" + userDto.getUsername() +
                "&password=" + userDto.getPassword());
        return paramBuilder;
    }

}
