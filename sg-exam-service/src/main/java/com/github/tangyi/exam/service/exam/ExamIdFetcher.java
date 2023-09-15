package com.github.tangyi.exam.service.exam;

import com.github.tangyi.common.base.id.IdFetcher;
import com.github.tangyi.exam.mapper.ExaminationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExamIdFetcher extends IdFetcher {

	private final ExaminationMapper mapper;

	public ExamIdFetcher(ExaminationMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<Long> fetchPage(long minId, Map<String, Object> params) {
		int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "100").toString());
		return this.mapper.findIdsOrderByIdAsc(minId, pageSize, params);
	}
}
