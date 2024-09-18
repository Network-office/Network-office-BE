package dev.office.networkoffice.user.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    @Test
    @DisplayName("로그인하지 않은 사용자는 자신의 정보를 조회할 수 없다.")
    void me_Unauthorized() {
        RestAssured.given().log().all()
                .header("Referer", "swagger-ui")
                .when().get("api/v1/users/profile")
                .then().log().all()
                .statusCode(401);
    }
}
