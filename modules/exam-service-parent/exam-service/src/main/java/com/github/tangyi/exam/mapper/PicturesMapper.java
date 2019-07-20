package com.github.tangyi.exam.mapper;

import com.github.tangyi.common.core.persistence.CrudMapper;
import com.github.tangyi.exam.api.module.Pictures;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图片mapper
 *
 * @author tangyi
 * @date 2019/6/16 14:50
 */
@Mapper
public interface PicturesMapper extends CrudMapper<Pictures> {
}
