package com.hopeofseed.hopeofseed.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/25 9:21
 * 修改人：whisper
 * 修改时间：2016/11/25 9:21
 * 修改备注：
 */
public class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();        if (rawType != String.class) {
            return null;        }
        return (TypeAdapter<T>) new StringNullAdapter();    }
}