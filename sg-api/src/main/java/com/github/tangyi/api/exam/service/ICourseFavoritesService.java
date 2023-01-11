package com.github.tangyi.api.exam.service;

import java.util.List;

public interface ICourseFavoritesService {

	void updateStartCount(List<Long> ids);

	void updateFavorite(List<Long> ids);
}
