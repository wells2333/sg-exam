package com.github.tangyi.api.exam.service;

import java.util.List;

public interface IExamFavoritesService {

	void updateStartCount(List<Long> ids);

	void updateFavorite(List<Long> ids);
}
