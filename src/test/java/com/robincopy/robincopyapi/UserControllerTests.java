package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.dto.PortfolioSummaryDto;
import com.robincopy.robincopyapi.dto.StockInfoDto;
import com.robincopy.robincopyapi.dto.StockReducedInfoDto;
import com.robincopy.robincopyapi.dto.share.ShareDto;
import com.robincopy.robincopyapi.dto.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {

    @LocalServerPort
    int randomServerPort;

    private String path;

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    public void startup(){
        this.path = "http://localhost:" + randomServerPort;
    }

    /* TEST INTERNAL API, testing controller is effectively receiving requests and delegating processing to service
         * Creating a user should return created user info
         * Adding shares, should update owned shares
         * Getting user shares list, should return owned shares
         * Getting user stock details
         * Getting user stocks summary, should return user portfolio summary
     */

    @Test
    void test01_creatingNewUser_ShouldReturnNewUserInfo() {
        UserDto user = new UserDto("name", "lastName");

        HttpEntity<UserDto> body = new HttpEntity<>(user);
        ResponseEntity<UserDto> response = restTemplate.exchange(path + "/users", HttpMethod.POST, body, UserDto.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo(user.getFirstName());
    }

    @Test
    void test02_addingShares_ShouldAddShareToUser() {
        UserDto user = new UserDto("name", "lastName");
        ShareDto share = new ShareDto(getUserId(user), "TSLA", 5, 500.0);

        HttpEntity<ShareDto> body = new HttpEntity<>(share);
        ResponseEntity<ShareDto> response = restTemplate.exchange(path + "/users/shares", HttpMethod.POST, body, ShareDto.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSymbol()).isEqualTo("TSLA");
    }

    @Test
    void test03_getUserStocksList_ShouldReturnListOfOwnedStocks() {
        UserDto user = new UserDto("name", "lastName");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<StockReducedInfoDto> entity = new HttpEntity<>(headers);
        ResponseEntity<List<StockReducedInfoDto>> response = restTemplate.exchange(path + "/users/" + getUserId(user) + "/shares", HttpMethod.GET, entity, new ParameterizedTypeReference<List<StockReducedInfoDto>>() {});
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void test04_getUserStockInfo_ShouldReturnDetailedInformationAboutStock() {
        UserDto user = new UserDto("name", "lastName");
        String userId = getUserId(user);
        addShare(new ShareDto(userId, "TSLA", 3, 500.0));

        ResponseEntity<StockInfoDto> response = restTemplate.getForEntity(path + "/users/" + userId + "/shares/TSLA", StockInfoDto.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void test05_getUserSummary_ShouldReturnUserPortfolioSummary() {
        UserDto user = new UserDto("name", "lastName");

        ResponseEntity<PortfolioSummaryDto> response = restTemplate.getForEntity(path + "/users/" + getUserId(user) + "/summary", PortfolioSummaryDto.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }



    private String getUserId(UserDto user){
        HttpEntity<UserDto> body = new HttpEntity<>(user);
        ResponseEntity<UserDto> response = restTemplate.exchange(path + "/users", HttpMethod.POST, body, UserDto.class);
        return response.getBody().getUserId();
    }

    private void addShare(ShareDto shareDto){
        HttpEntity<ShareDto> body = new HttpEntity<>(shareDto);
        restTemplate.exchange(path + "/users/shares", HttpMethod.POST, body, ShareDto.class);
    }
}
