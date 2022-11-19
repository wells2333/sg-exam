package com.github.tangyi.exam.service;

import com.github.tangyi.api.exam.model.ExamSubjectFavorites;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamSubjectFavoritesMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收藏题目Service
 * @author tangyi
 * @date 2022-08-18
 */
@Slf4j
@Service
@AllArgsConstructor
public class ExamSubjectFavoritesService extends CrudService<ExamSubjectFavoritesMapper, ExamSubjectFavorites>
		implements ExamCacheName {

	@Override
	@Cacheable(value = SUBJECT_FAVORITES, key = "#id")
	public ExamSubjectFavorites get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	public int insert(ExamSubjectFavorites examSubjectFavorites) {
		examSubjectFavorites.setCommonValue();
		return super.insert(examSubjectFavorites);
	}

	@Override
	@Transactional
	@CacheEvict(value = SUBJECT_FAVORITES, key = "#examSubjectFavorites.id")
	public int update(ExamSubjectFavorites examSubjectFavorites) {
		examSubjectFavorites.setCommonValue();
		return super.update(examSubjectFavorites);
	}

	@Override
	@Transactional
	@CacheEvict(value = SUBJECT_FAVORITES, key = "#examSubjectFavorites.id")
	public int delete(ExamSubjectFavorites examSubjectFavorites) {
		return super.delete(examSubjectFavorites);
	}

	@Override
	@Transactional
	@CacheEvict(value = SUBJECT_FAVORITES, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}

	/**
	 * 收藏题目
	 * @param subjectId subjectId
	 */
	@Transactional
	public boolean favoriteSubject(Long userId, Long subjectId) {
		ExamSubjectFavorites favorites = new ExamSubjectFavorites();
		favorites.setCommonValue();
		favorites.setUserId(userId);
		favorites.setSubjectId(subjectId);
		return this.insert(favorites) > 0;
	}
}
