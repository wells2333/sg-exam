package com.github.tangyi.exam.service.course;

import com.github.tangyi.common.base.id.IdFetcher;
import com.github.tangyi.exam.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseIdFetcher extends IdFetcher {

	private final CourseMapper mapper;

	public CourseIdFetcher(CourseMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<Long> fetchPage(long minId, Map<String, Object> params) {
		int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "100").toString());
		return this.mapper.findIdsOrderByIdAsc(minId, pageSize, params);
	}
}
