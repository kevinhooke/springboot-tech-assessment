package kh.springbootassessment.fileparser.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;

/**
 * Values for Mock responses are obtained from following sources: source ip
 * addresses: - AWS ip ranges: https://ip-ranges.amazonaws.com/ip-ranges.json
 * countryCodes: https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
 * 
 */
@SpringBootTest()
@AutoConfigureMockMvc
@EnableWireMock({ @ConfigureWireMock(name = "ip-api-mock", property = "ip-api.root.url", port = 8090) })
public class FileParserControllerTest {

	private static final String LINE1 = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1";

	@InjectWireMock("ip-api-mock")
	private WireMockServer wiremock;

	@Value("${ip-api.root.url}")
	private String wiremockUrl;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testEntryFile_valid1Line_validCountry() throws Exception {

		// WireMock setup - mock response from ip-adi
		wiremock.stubFor(get("/json/1.1.1.1?fields=status,message,countryCode,isp,org,hosting,query")
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
						"""
							{"status":"success","message":"WireMock response","countryCode":"AU","isp":"Cloudflare, Inc","org":"APNIC and Cloudflare DNS Resolver project","hosting":true,"query":"1.1.1.1"},
						""")));

		this.mockMvc.perform(post("/entryfile")
				// mock ip address in HttpRequest
				.with(request -> {
					request.setRemoteAddr("1.1.1.1");
					return request;
				}).contentType(MediaType.TEXT_PLAIN).content(LINE1).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 200 response
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Name").value("John Smith"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Transport").value("Rides A Bike"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].TopSpeed").value("12.1"));
	}

	@Test
	void testEntryFile_blockedCountryCode_AWS_US() throws Exception {

		// WireMock setup - mock response from ip-adi
		wiremock.stubFor(get("/json/150.222.234.54?fields=status,message,countryCode,isp,org,hosting,query")
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
						"""
							{"status":"success","countryCode":"US","isp":"Amazon Technologies Inc","org":"Amazon Technologies Inc. (us-west-1)","hosting":false,"query":"150.222.234.54"},
						""")));
		this.mockMvc.perform(post("/entryfile")
				// mock ip address in HttpRequest
				.with(request -> {
					request.setRemoteAddr("150.222.234.54");
					return request;
				}).contentType(MediaType.TEXT_PLAIN).content(LINE1).accept(MediaType.APPLICATION_JSON))
				//expect an HTTP 403 response because of blocked country code validation error
				.andExpect(status().is4xxClientError());

	}
}