package com.github.tangyi.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.tangyi.common.model.R;
import org.junit.jupiter.api.Assertions;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceApplicationTests extends BaseTests {

	@Autowired(required = false)
	private MockMvc mvc;

	@Test
	public void testTokenLogin() throws Exception {
		String tokenLoginUri = "/login?grant_type=password&scope=read&ignoreCode=1&username=admin&credential=lBTqrKS0kZixOFXeZ0HRng==&remember=false";
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(tokenLoginUri)
				.header("Tenant-Code", "gitee");
		ResultActions action = mvc.perform(builder);
		StatusResultMatchers status = MockMvcResultMatchers.status();
		ResultMatcher ok = status.isOk();
		String result = action.andDo(MockMvcResultHandlers.print()).andExpect(ok).andReturn().getResponse()
				.getContentAsString();
		Assertions.assertNotNull(result);
		R<JSONObject> r = JSON.parseObject(result, R.class);
		Assertions.assertNotNull(r.getResult());
		Assertions.assertNotNull(r.getResult().get("token"));
		Assertions.assertNotNull(r.getResult().get("tenantCode"));
	}
}
