package kz.filipvkh.urlshortener;

import kz.filipvkh.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UrlRepository urlRepository;

	@BeforeEach
	void deleteAll() {
		urlRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void encodeUrlWithoutPathSpecifiedTest() throws Exception {
		String url = "http://orange.com";
		String encodedUrl = mockMvc.perform(post("/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"url\": \"" + url + "\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();
		mockMvc.perform(get(encodedUrl))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(url));
	}

	@Test
	void encodeUrlWithSpecifiedPathTest() throws Exception {
		String url = "http://orange.com";
		String path = "fruits";
		String encodedUrl = mockMvc.perform(post("/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"url\": \"" + url + "\"," +
						"\"path\": \"" + path + "\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();
		assertTrue(encodedUrl.contains(path));
		mockMvc.perform(get(encodedUrl))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(url));
	}

	@Test
	void nonexistentPathTest() throws Exception {
		String urlWithNonexistentPath = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/hello_world";
		mockMvc.perform(get(urlWithNonexistentPath))
				.andExpect(status().isNotFound())
				.andExpect(content().string("данная ссылка не существует или была удалена"));
	}

	@Test
	void occupyingABusyPathTest() throws Exception {
		String path = "fruits";
		mockMvc.perform(post("/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"url\": \"http://orange.com\"," +
						"\"path\": \"" + path + "\"}"))
				.andExpect(status().isOk());

		mockMvc.perform(post("/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"url\": \"http://vk.com\"," +
						"\"path\": \"" + path + "\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("данный адрес уже занят, попробуй другой"));
	}

	@Test
	void notAUrlTest() throws Exception {
		mockMvc.perform(post("/shorten")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"url\": \"hello world\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("введенный адрес не является ссылкой :("));
	}
}
