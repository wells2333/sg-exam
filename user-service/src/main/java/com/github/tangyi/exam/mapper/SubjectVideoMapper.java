package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectVideo;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * @author tangyi
 * @date 2022/6/23 1:35 下午
 */
@Repository
public interface SubjectVideoMapper extends CrudMapper<SubjectVideo> {

	/**
	 * 物理删除
	 *
	 * @param subjectVideo subjectVideo
	 * @return int
	 * @author tangyi
	 * @date 2019/06/16 22:54
	 */
	int physicalDelete(SubjectVideo subjectVideo);

	/**
	 * 物理批量删除
	 *
	 * @param ids ids
	 * @return int
	 * @author tangyi
	 * @date 2019/06/16 22:54
	 */
	int physicalDeleteAll(Long[] ids);
}
