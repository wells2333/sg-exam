package com.github.tangyi.exam.service.fav;

import com.github.tangyi.api.exam.model.ExamFavStartCount;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.ExamCacheName;
import com.github.tangyi.exam.mapper.ExamFavStartCountMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class FavStartCountService extends CrudService<ExamFavStartCountMapper, ExamFavStartCount>
		implements ExamCacheName {

	@Override
	@Cacheable(value = ExamCacheName.FAV_START_COUNT, key = "#id")
	public ExamFavStartCount get(Long id) {
		return super.get(id);
	}

	public ExamFavStartCount findByTarget(Long id, Integer targetType) {
		return this.dao.findByTarget(id, targetType);
	}

	@Override
	@Transactional
	public int insert(ExamFavStartCount examFavStartCount) {
		examFavStartCount.setCommonValue();
		return super.insert(examFavStartCount);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.FAV_START_COUNT, key = "#examFavStartCount.id")
	public int update(ExamFavStartCount examFavStartCount) {
		examFavStartCount.setCommonValue();
		return super.update(examFavStartCount);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.FAV_START_COUNT, key = "#examFavStartCount.id")
	public int delete(ExamFavStartCount examFavStartCount) {
		return super.delete(examFavStartCount);
	}

	@Override
	@Transactional
	@CacheEvict(value = ExamCacheName.FAV_START_COUNT, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
