package com.github.tangyi.user.service;

import com.github.tangyi.api.user.constant.UserStudentConstant;
import com.github.tangyi.api.user.dto.StudentDto;
import com.github.tangyi.api.user.module.Student;
import com.github.tangyi.api.user.module.UserStudent;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.vo.UserVo;
import com.github.tangyi.user.mapper.StudentMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 学生Service
 *
 * @author tangyi
 * @date 2019/07/09 15:28
 */
@AllArgsConstructor
@Service
public class StudentService extends CrudService<StudentMapper, Student> {

    private final UserService userService;

    private final UserStudentService userStudentService;

    /**
     * 新增学生
     *
     * @param studentDto studentDto
     * @return int
     * @author tangyi
     * @date 2019/07/10 18:18:04
     */
    @Transactional
    public int add(StudentDto studentDto) {
        String currentUser = SysUtil.getUser(), tenantCode = SysUtil.getTenantCode();
        Long userId = studentDto.getUserId();
        if (userId != null) {
            // 查询当前用户
            UserVo userVo = userService.findUserByIdentifier(currentUser, tenantCode);
            if (userVo == null)
                throw new CommonException("Get user info failed");
            userId = userVo.getId();
        }
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);
        student.setCommonValue(currentUser, SysUtil.getSysCode(), tenantCode);
        // 新增用户学生关系
        UserStudent userStudent = new UserStudent();
        userStudent.setCommonValue(currentUser, SysUtil.getSysCode(), tenantCode);
        userStudent.setUserId(userId);
        userStudent.setStudentId(student.getId());
        // 默认关系类型是爸爸
        if (studentDto.getRelationshipType() == null)
            userStudent.setRelationshipType(UserStudentConstant.RELATIONSHIP_TYPE_FATHER);
        userStudentService.insert(userStudent);
        // 保存学生
        return this.insert(student);
    }
}
