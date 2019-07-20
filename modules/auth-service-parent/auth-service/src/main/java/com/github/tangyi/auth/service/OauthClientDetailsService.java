package com.github.tangyi.auth.service;

import com.github.tangyi.auth.api.module.OauthClientDetails;
import com.github.tangyi.auth.mapper.OauthClientDetailsMapper;
import com.github.tangyi.common.core.service.CrudService;
import org.springframework.stereotype.Service;

/**
 * Oauth2客户端Service
 *
 * @author tangyi
 * @date 2019/3/30 16:48
 */
@Service
public class OauthClientDetailsService extends CrudService<OauthClientDetailsMapper, OauthClientDetails> {
}
