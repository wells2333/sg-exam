package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.model.ExamExaminationFavorites;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamExaminationFavoritesMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收藏Service
 * @author tangyi
 * @date 2022-08-18
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamExaminationFavoritesService
		extends CrudService<ExamExaminationFavoritesMapper, ExamExaminationFavorites> implements ExamCacheName {

	@Override
	@Cacheable(value = EXAMINATION_FAVORITES, key = "#id")
	public ExamExaminationFavorites get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(ExamExaminationFavorites examExaminationFavorites) {
		examExaminationFavorites.setCommonValue();
		return super.insert(examExaminationFavorites);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAMINATION_FAVORITES, key = "#examExaminationFavorites.id")
	public int update(ExamExaminationFavorites examExaminationFavorites) {
		examExaminationFavorites.setCommonValue();
		return super.update(examExaminationFavorites);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAMINATION_FAVORITES, key = "#examExaminationFavorites.id")
	public int delete(ExamExaminationFavorites examExaminationFavorites) {
		return super.delete(examExaminationFavorites);
	}

	@Override
	@Transactional
	@CacheEvict(value = EXAMINATION_FAVORITES, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	public List<ExamExaminationFavorites> findUserFavorites(Long userId) {
		return this.dao.findUserFavorites(userId);
	}

	public ExamExaminationFavorites getByUserIdAndExaminationId(ExamExaminationFavorites favorites) {
		return this.dao.getByUserIdAndExaminationId(favorites);
	}

	@Transactional
	public int deleteByUserIdAndExaminationId(ExamExaminationFavorites favorites) {
		return this.dao.deleteByUserIdAndExaminationId(favorites);
	}
}
