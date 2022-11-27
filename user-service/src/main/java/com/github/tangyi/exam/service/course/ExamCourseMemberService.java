package com.github.tangyi.exam.service.course;

import com.github.tangyi.api.exam.dto.ExamCourseMemberDto;
import com.github.tangyi.api.exam.model.ExamCourseMember;
import com.github.tangyi.api.user.model.User;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamCourseMemberMapper;
import com.github.tangyi.user.service.UserService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程学员Service
 * @author tangyi
 * @date 2022-11-27
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamCourseMemberService extends CrudService<ExamCourseMemberMapper, ExamCourseMember>
		implements ExamCacheName {

	private final UserService userService;

	@Override
	public ExamCourseMember get(Long id) {
		return super.get(id);
	}

	public List<ExamCourseMemberDto> toExamCourseMemberDto(List<ExamCourseMember> memberList) {
		List<ExamCourseMemberDto> list = Lists.newArrayListWithExpectedSize(memberList.size());
		for (ExamCourseMember member : memberList) {
			ExamCourseMemberDto dto = new ExamCourseMemberDto();
			BeanUtils.copyProperties(member, dto);
			User user = userService.get(member.getUserId());
			if (user != null) {
				dto.setUserName(user.getName());
				dto.setEmail(user.getEmail());
				dto.setPhone(user.getPhone());
				dto.setGender(user.getGender());
			}
			list.add(dto);
		}
		return list;
	}

	public Integer findMemberCountByCourseId(ExamCourseMember member) {
		return this.dao.findMemberCountByCourseId(member);
	}

	public List<ExamCourseMember> findMembersByCourseId(ExamCourseMember member) {
		return this.dao.findMembersByCourseId(member);
	}

	public ExamCourseMember findByCourseIdAndUserId(Long courseId, Long userId) {
		return this.dao.findByCourseIdAndUserId(courseId, userId);
	}

	@Override
	@Transactional
	public int insert(ExamCourseMember examCourseMember) {
		examCourseMember.setCommonValue();
		return super.insert(examCourseMember);
	}

	@Override
	@Transactional
	public int update(ExamCourseMember examCourseMember) {
		examCourseMember.setCommonValue();
		return super.update(examCourseMember);
	}

	@Override
	@Transactional
	public int delete(ExamCourseMember examCourseMember) {
		return super.delete(examCourseMember);
	}

	@Override
	@Transactional
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	public Integer deleteByCourseIdAndUserId(ExamCourseMember member) {
		return this.dao.deleteByCourseIdAndUserId(member);
	}
}
