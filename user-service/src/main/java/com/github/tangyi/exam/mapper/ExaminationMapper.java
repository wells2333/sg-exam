package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.Examination;
import com.github.tangyi.common.base.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 考试Mapper
 *
 * @author tangyi
 * @date 2018/11/8 21:11
 */
@Repository
public interface ExaminationMapper extends CrudMapper<Examination> {

	/**
	 * 查询考试数量
	 *
	 * @param examination examination
	 * @return int
	 * @author tangyi
	 * @date 2019/3/1 15:32
	 */
	int findExaminationCount(Examination examination);

	/**
	 * 查询参与考试人数
	 *
	 * @param examination examination
	 * @return int
	 * @author tangyi
	 * @date 2019/10/27 20:08:58
	 */
	int findExamUserCount(Examination examination);

	/**
	 * 批量更新考试次数
	 * @param examinations examinations
	 * @return int
	 */
	int bathUpdateStartCountAndFavorite(@Param("examinations") List<Examination> examinations);
}
