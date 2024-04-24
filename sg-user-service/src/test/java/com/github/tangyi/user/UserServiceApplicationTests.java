/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.tangyi.common.model.R;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

@AutoConfigureMockMvc
@ActiveProfiles("dev")
@SuppressWarnings("unchecked")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceApplicationTests extends BaseTests {

	@Autowired(required = false)
	private MockMvc mvc;

	private String token;

	@BeforeEach
	public void register() throws Exception {
		if (this.token != null) {
			return;
		}

		// 注册
		String registerUri = "/v1/user/anonymousUser/register?tenantCode=gitee&identifier=test_sg&email=test_sg@qq.com&credential=lBTqrKS0kZixOFXeZ0HRng%3D%3D&randomStr=85431710858543166&ignoreCode=1";
		MockHttpServletRequestBuilder registerBuilder = MockMvcRequestBuilders.post(registerUri)
				.header("Tenant-Code", "gitee");
		registerBuilder.contentType("application/json;charset=UTF-8");
		registerBuilder.accept("application/json, text/plain, */*");
		registerBuilder.content(
				"{\"identifier\":\"test_sg\",\"email\":\"test_sg@qq.com\",\"credential\":\"lBTqrKS0kZixOFXeZ0HRng==\"}");
		ResultActions action = mvc.perform(registerBuilder);
		String result = action.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();
		Assertions.assertNotNull(result);
		R<Boolean> r = JSON.parseObject(result, R.class);
		Boolean res = r.getResult();
		Assertions.assertTrue(res);

		// 登录
		String tokenLoginUri = "/login?grant_type=password&scope=read&ignoreCode=1&username=test_sg&credential=lBTqrKS0kZixOFXeZ0HRng==&remember=true";
		MockHttpServletRequestBuilder loginBuilder = MockMvcRequestBuilders.post(tokenLoginUri)
				.header("Tenant-Code", "gitee");
		ResultActions loginAction = mvc.perform(loginBuilder);
		result = loginAction.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();
		Assertions.assertNotNull(result);
		R<JSONObject> loginR = JSON.parseObject(result, R.class);
		JSONObject loginRes = loginR.getResult();
		Assertions.assertNotNull(loginRes);
		this.token = loginRes.get("token").toString();
		Assertions.assertNotNull(this.token);
		Assertions.assertNotNull(loginRes.get("tenantCode"));
	}

	@Test
	void testGetNotice() throws Exception {
		ResultActions action = mvc.perform(mockReq("/v1/notice/getNotice"));
		StatusResultMatchers status = MockMvcResultMatchers.status();
		ResultMatcher ok = status.isOk();
		String result = action.andDo(MockMvcResultHandlers.print()).andExpect(ok).andReturn().getResponse()
				.getContentAsString();
		Assertions.assertNotNull(result);
	}

	@Test
	void testGetSysDefaultConfig() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/v1/config/getDefaultSysConfig")
				.header("Tenant-Code", "gitee");
		ResultActions action = mvc.perform(builder);
		String result = action.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();
		Assertions.assertNotNull(result);
	}

	@Test
	void getGetUserExaminationList() throws Exception {
		ResultActions action = mvc.perform(mockReq("/v1/examination/userExaminationList"));
		String result = action.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn().getResponse().getContentAsString();
		Assertions.assertNotNull(result);
	}

	MockHttpServletRequestBuilder mockReq(String url) {
		return MockMvcRequestBuilders.get(url).header("Tenant-Code", "gitee").header("Authorization", token);
	}
}
