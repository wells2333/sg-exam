package com.github.tangyi.exam.service;

import com.github.tangyi.common.core.constant.CommonConstant;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.service.CrudService;
import com.github.tangyi.common.core.utils.ResponseUtil;
import com.github.tangyi.common.core.vo.DeptVo;
import com.github.tangyi.common.core.vo.UserVo;
import com.github.tangyi.exam.api.dto.ExaminationRecordDto;
import com.github.tangyi.exam.api.module.ExaminationRecord;
import com.github.tangyi.exam.mapper.ExamRecordMapper;
import com.github.tangyi.user.api.feign.UserServiceClient;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 考试记录service
 *
 * @author tangyi
 * @date 2018/11/8 21:20
 */
@AllArgsConstructor
@Service
public class ExamRecordService extends CrudService<ExamRecordMapper, ExaminationRecord> {

	private final UserServiceClient userServiceClient;

    /**
     * 查询考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Cacheable(value = "record#" + CommonConstant.CACHE_EXPIRE, key = "#examRecord.id")
    public ExaminationRecord get(ExaminationRecord examRecord) {
        return super.get(examRecord);
    }

    /**
     * 更新考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", key = "#examRecord.id")
    public int update(ExaminationRecord examRecord) {
        return super.update(examRecord);
    }

    /**
     * 删除考试记录
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2019/1/3 14:10
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", key = "#examRecord.id")
    public int insert(ExaminationRecord examRecord) {
        return super.insert(examRecord);
    }

    /**
     * 根据用户id、考试id查找
     *
     * @param examRecord examRecord
     * @return ExamRecord
     * @author tangyi
     * @date 2018/12/26 13:58
     */
    public ExaminationRecord getByUserIdAndExaminationId(ExaminationRecord examRecord) {
        return this.dao.getByUserIdAndExaminationId(examRecord);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     * @author tangyi
     * @date 2019/1/3 14:11
     */
    @Override
    @Transactional
    @CacheEvict(value = "record", allEntries = true)
    public int deleteAll(Long[] ids) {
        return super.deleteAll(ids);
    }

	/**
	 * 获取用户、部门相关信息
	 * @param examRecordDtoList examRecordDtoList
	 * @param userIds userIds
	 */
    public void fillExamUserInfo(List<ExaminationRecordDto> examRecordDtoList, Long[] userIds) {
		// 查询用户信息
		ResponseBean<List<UserVo>> returnT = userServiceClient.findUserById(userIds);
		if (ResponseUtil.isSuccess(returnT)) {
			// 查询部门信息
			ResponseBean<List<DeptVo>> deptResponseBean = userServiceClient.findDeptById(returnT.getData().stream().map(UserVo::getDeptId).distinct().toArray(Long[]::new));
			if (ResponseUtil.isSuccess(deptResponseBean)) {
				examRecordDtoList.forEach(tempExamRecordDto -> {
					// 查询、设置用户信息
					UserVo examRecordDtoUserVo = returnT.getData().stream()
							.filter(tempUserVo -> tempExamRecordDto.getUserId().equals(tempUserVo.getId()))
							.findFirst().orElse(null);
					if (examRecordDtoUserVo != null) {
						// 设置用户名
						tempExamRecordDto.setUserName(examRecordDtoUserVo.getName());
						// 查询、设置部门信息
						if (CollectionUtils.isNotEmpty(deptResponseBean.getData())) {
							DeptVo examRecordDtoDeptVo = deptResponseBean.getData().stream()
									// 根据部门ID过滤
									.filter(tempDept -> tempDept.getId().equals(examRecordDtoUserVo.getDeptId()))
									.findFirst().orElse(null);
							// 设置部门名称
							if (examRecordDtoDeptVo != null)
								tempExamRecordDto.setDeptName(examRecordDtoDeptVo.getDeptName());
						}
					}
				});
			}
		}
	}
}
