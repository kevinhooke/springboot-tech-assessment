package kh.springbootassessment.fileparser.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.fail;
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
 * Values for Mock responses are obtained from following sources:
 * source ip addresses:
 * - AWS ip ranges: https://ip-ranges.amazonaws.com/ip-ranges.json
 * - GCP ip ranges: https://www.gstatic.com/ipranges/cloud.json
 * - Azure ip ranges: https://www.microsoft.com/en-us/download/details.aspx?id=56519
 * 
 * source ip ranges by country: https://lite.ip2location.com/ip-address-ranges-by-country?lang=en_US
 * 
 * countryCodes: https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
 * 
 * Example test IPs by country:
 * UK (alowed): 103.10.251.1
 * US (blocked):
 * China (blocked):
 * Spain (blocked): 
 * 
 * Example test IPs by ISP:
 * GCP (blocked): 34.88.0.1, ip-api isp = "Google LLC"
 * AWS (blocked): 15.230.15.29, ip-api isp  "Amazon Technologies Inc"
 * Azure (blocked): 20.36.33.30, ip-api isp = "Microsoft Corporation"
 * Other UK ISP allowed IPs: 
 */
@SpringBootTest()
@AutoConfigureMockMvc
@EnableWireMock({ @ConfigureWireMock(name = "ip-api-mock", property = "ip-api.root.url", port = 8090) })
public class FileParserControllerTest {

	private static final String LINES1 = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1 ";

	private static final String LINES2 =
			"18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1 \n3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5 ";
			
	private static final String LINES3 =
			"18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1 \n3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5 \n1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|15.3 ";

	@InjectWireMock("ip-api-mock")
	private WireMockServer wiremock;

	@Value("${ip-api.root.url}")
	private String wiremockUrl;

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Tests ip source validation when country code is from an allowed country (not in the country code block list defined in
	 * application.properties: ip.origin.block.countrycodes
	 */
	@Test
	void testEntryFile_valid1Line_validCountryAU() throws Exception {

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
				}).contentType(MediaType.TEXT_PLAIN).content(LINES1).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 200 response
				.andExpect(status().isOk())
				//assert for expected values in OutcomeFile result
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Name").value("John Smith"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Transport").value("Rides A Bike"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].TopSpeed").value("12.1"));
	}

	/**
	 * Tests EntryFile with 2 lines, and ip source validation when country code is from an allowed country (not in the country code block list defined in
	 * application.properties: ip.origin.block.countrycodes
	 */
	@Test
	void testEntryFile_valid2Lines_validCountryAU() throws Exception {
		
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
				}).contentType(MediaType.TEXT_PLAIN).content(LINES2).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 200 response
				.andExpect(status().isOk())
				//assert for expected values in OutcomeFile result
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Name").value("John Smith"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Transport").value("Rides A Bike"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].TopSpeed").value("12.1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].Name").value("Mike Smith"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].Transport").value("Drives an SUV"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].TopSpeed").value("95.5"));
	}
	
	/**
	 * Tests EntryFile with 3 lines, and ip source validation when country code is from an allowed country (not in the country code block list defined in
	 * application.properties: ip.origin.block.countrycodes
	 */
	@Test
	void testEntryFile_valid3Lines_validCountryAU() throws Exception {
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
				}).contentType(MediaType.TEXT_PLAIN).content(LINES3).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 200 response
				.andExpect(status().isOk())
				//assert for expected values in OutcomeFile result
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Name").value("John Smith"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].Transport").value("Rides A Bike"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].TopSpeed").value("12.1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].Name").value("Mike Smith"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].Transport").value("Drives an SUV"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].TopSpeed").value("95.5"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].Name").value("Jenny Walters"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].Transport").value("Rides A Scooter"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].TopSpeed").value("15.3"));
	}
	
	
	//TODO missing input fields test
	
	//TODO field validation test
	
	/**
	 * Tests ip source validation when country code is US (in blocked country code list): 150.222.234.54
	 * 
	 * Expected result: HTTP status code 403 is returned with error message "Request from blocked country code"
	 */
	@Test
	void testEntryFile_blockedCountryCode_ispAWS_countryCodeUS() throws Exception {

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
				}).contentType(MediaType.TEXT_PLAIN).content(LINES1).accept(MediaType.APPLICATION_JSON))
				//expect an HTTP 403 response because of blocked country code validation error
				.andExpect(status().is(403))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: request rejected. IP address from blocked country code"));;
	}
	
	
	/**
	 * Tests ip source validation when ISP is GCP (in blocked IP list), but country code is valid: 34.88.0.1
	 * 
	 * Expected result: HTTP status code 403 is returned with error message "Request from blocked ISP"
	 */
	@Test
	void testEntryFile_blockedISP_ispGCP_countryCodeFI() throws Exception {
		
		// WireMock setup - mock response from ip-adi
		wiremock.stubFor(get("/json/34.88.0.1?fields=status,message,countryCode,isp,org,hosting,query")
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
						"""
							{"status":"success","countryCode":"FI","isp":"Google LLC","org":"Google Cloud (europe-north1)","hosting":true,"query":"34.88.0.1"}
						""")));

		this.mockMvc.perform(post("/entryfile")
				// mock ip address in HttpRequest
				.with(request -> {
					request.setRemoteAddr("34.88.0.1");
					return request;
				}).contentType(MediaType.TEXT_PLAIN).content(LINES1).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 403 response because of blocked ISP validation error
				.andExpect(status().is(403))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: request rejected, IP address from blocked ISP/Data Center IP range"));;
	}
	
	/**
	 * Tests ip source validation when ISP is Azure (in blocked IP list), but country code is valid: 20.36.33.30
	 * 
	 * Expected result: HTTP status code 403 is returned with error message "Request from blocked ISP"
	 */
	@Test
	void testEntryFile_blockedISP_ispAzure_countryCodeAU() throws Exception {
		
		// WireMock setup - mock response from ip-adi
		wiremock.stubFor(get("/json/20.36.33.30?fields=status,message,countryCode,isp,org,hosting,query")
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
						"""
							{"status":"success","countryCode":"AU","isp":"Microsoft Corporation","org":"Microsoft Azure Cloud (australiacentral)","hosting":true,"query":"20.36.33.30"}
						""")));

		this.mockMvc.perform(post("/entryfile")
				// mock ip address in HttpRequest
				.with(request -> {
					request.setRemoteAddr("20.36.33.30");
					return request;
				}).contentType(MediaType.TEXT_PLAIN).content(LINES1).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 403 response because of blocked ISP validation error
				.andExpect(status().is(403))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: request rejected, IP address from blocked ISP/Data Center IP range"));;
	}
	
	/**
	 * Tests ip source validation when ISP is AWS (in blocked IP list), but country code is valid: 15.230.15.29
	 * 
	 * Expected result: HTTP status code 403 is returned with error message "Request from blocked ISP"
	 */
	@Test
	void testEntryFile_blockedISP_ispAWS_countryCodeDE() throws Exception {
		
		// WireMock setup - mock response from ip-adi
		wiremock.stubFor(get("/json/15.230.15.29?fields=status,message,countryCode,isp,org,hosting,query")
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
						"""
							{"status":"success","countryCode":"DE","isp":"Amazon Technologies Inc","org":"Amazon Technologies Inc. (eu-central-1)","hosting":false,"query":"15.230.15.29"}
						""")));

		this.mockMvc.perform(post("/entryfile")
				// mock ip address in HttpRequest
				.with(request -> {
					request.setRemoteAddr("15.230.15.29");
					return request;
				}).contentType(MediaType.TEXT_PLAIN).content(LINES1).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 403 response because of blocked ISP validation error
				.andExpect(status().is(403))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: request rejected, IP address from blocked ISP/Data Center IP range"));
	}
	
	/**
	 * Tests ip source validation when country code is China (CN) (in blocked country code list): 1.80.0.1
	 * 
	 * Expected result: HTTP status code 403 is returned with error message "Request from blocked country code"
	 */
	@Test
	void testEntryFile_blockedCountryCode_countryCodeCN() throws Exception {
		
		// WireMock setup - mock response from ip-adi
		wiremock.stubFor(get("/json/1.80.0.1?fields=status,message,countryCode,isp,org,hosting,query")
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
						"""
							{"status":"success","countryCode":"CN","isp":"Chinanet","org":"Chinanet SN","hosting":false,"query":"1.80.0.1"}
						""")));

		this.mockMvc.perform(post("/entryfile")
				// mock ip address in HttpRequest
				.with(request -> {
					request.setRemoteAddr("1.80.0.1");
					return request;
				}).contentType(MediaType.TEXT_PLAIN).content(LINES1).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 403 response because of blocked ISP validation error
				.andExpect(status().is(403))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: request rejected. IP address from blocked country code"));
	}

	/**
	 * Tests ip source validation when country code is Spain (ES) (in blocked country code list): 1.178.224.1
	 * 
	 * Expected result: HTTP status code 403 is returned with error message
	 */
	@Test
	void testEntryFile_blockedCountryCode_countryCodeES() throws Exception {
		
		// WireMock setup - mock response from ip-adi
		wiremock.stubFor(get("/json/1.178.224.1?fields=status,message,countryCode,isp,org,hosting,query")
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
						"""
							{"status":"success","countryCode":"ES","isp":"Orange Espagne SA","org":"UNI2","hosting":false,"query":"1.178.224.1"}
						""")));

		this.mockMvc.perform(post("/entryfile")
				// mock ip address in HttpRequest
				.with(request -> {
					request.setRemoteAddr("1.178.224.1");
					return request;
				}).contentType(MediaType.TEXT_PLAIN).content(LINES1).accept(MediaType.APPLICATION_JSON))
		
				//expect an HTTP 403 response because of blocked ISP validation error
				.andExpect(status().is(403))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error: request rejected. IP address from blocked country code"));;
	}
}