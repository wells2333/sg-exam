package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.model.ExamExaminationMember;
import com.github.tangyi.api.exam.service.IExamExaminationMemberService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamExaminationMemberMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ExamExaminationMemberService extends CrudService<ExamExaminationMemberMapper, ExamExaminationMember>
		implements IExamExaminationMemberService, ExamCacheName {

	@Override
	@Cacheable(value = EXAM_MEMBER, key = "#id")
	public ExamExaminationMember get(Long id) {
		return super.get(id);
	}

	@Override
	public List<ExamExaminationMember> findListByExamId(Long examId) {
		return this.dao.findListByExamId(examId);
	}

	@Override
	@Transactional
	public int insert(ExamExaminationMember examExaminationMember) {
		examExaminationMember.setCommonValue();
		return super.insert(examExaminationMember);
	}

	@Override
	@Transactional
	public int insertBatch(List<ExamExaminationMember> members) {
		int res = this.dao.insertBatch(members);
		log.info("Insert examination member successfully, size: {}", members.size());
		return res;
	}

	@Transactional
	public void addMembers(Long examId, List<Long> memberIds, Integer examType, Integer memberType, String user,
			String tenantCode) {
		if (CollectionUtils.isEmpty(memberIds)) {
			return;
		}
		List<ExamExaminationMember> members = memberIds.stream().map(id -> {
			ExamExaminationMember m = new ExamExaminationMember();
			m.setCommonValue(user, tenantCode);
			m.setExamType(examType);
			m.setExamId(examId);
			m.setMemberId(id);
			m.setMemberType(memberType);
			return m;
		}).collect(Collectors.toList());
		int res = this.insertBatch(members);
		log.info("Add members successfully, size: {}, update: {}", memberIds.size(), res);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAM_MEMBER, key = "#examExaminationMember.id")
	public int update(ExamExaminationMember examExaminationMember) {
		examExaminationMember.setCommonValue();
		return super.update(examExaminationMember);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAM_MEMBER, key = "#examExaminationMember.id")
	public int delete(ExamExaminationMember examExaminationMember) {
		return super.delete(examExaminationMember);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAM_MEMBER, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	@Override
	@Transactional
	public int deleteByExamId(Long memberId) {
		return this.dao.deleteByExamId(memberId);
	}
}
