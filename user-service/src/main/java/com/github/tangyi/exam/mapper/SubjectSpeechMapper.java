package com.github.tangyi.exam.mapper;

import com.github.tangyi.api.exam.model.SubjectSpeech;
import com.github.tangyi.common.base.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * @author tangyi
 * @date 2022/6/23 1:35 下午
 */
@Repository
public interface SubjectSpeechMapper extends CrudMapper<SubjectSpeech> {

	/**
	 * 物理删除
	 *
	 * @param subjectSpeech subjectSpeech
	 * @return int
	 * @author tangyi
	 * @date 2019/06/16 22:54
	 */
	int physicalDelete(SubjectSpeech subjectSpeech);

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
