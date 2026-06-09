package rnd.statemachine.web.controller;

import rnd.statemachine.orderworkflow.data.OrderWorkflowDB;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({ "test" })
class OrderControllerTest {

        @Autowired
        private MockMvcTester mockMvc;

        private ObjectMapper objectMapper = MockData.getObjectMapper();

        @Test
        void withValidCart_whenCheckout_shouldReturnPaymentPending() throws Exception {

                MvcTestResult mvcTestResult = mockMvc.post().uri("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(MockData.createOrderSubmitDataJson())
                                .exchange();

                // Ensure the manager was called
                int status = mvcTestResult.getResponse().getStatus();
                assertThat(status).isEqualTo(201);
                OrderDataResponse orderData = objectMapper.readValue(mvcTestResult.getResponse().getContentAsString(),
                                OrderDataResponse.class);
                UUID createdOrderId = orderData.getOrderId();
                assertThat(createdOrderId).isNotNull();
        }

        // @Disabled
        @Test
        void withInvalidPayment_whenPay_shouldReturnPaymentError() throws Exception {

                MvcTestResult mvcTestResult = mockMvc.post().uri("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(MockData.createOrderSubmitDataJson())
                                .exchange();

                OrderDataResponse orderData = objectMapper.readValue(mvcTestResult.getResponse().getContentAsString(),
                                OrderDataResponse.class);
                UUID createdOrderId = orderData.getOrderId();

                mvcTestResult = mockMvc.put().uri("/api/orders/" + createdOrderId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(MockData.orderWrongPaySubmitDataJson(createdOrderId))
                                .exchange();

                // Ensure the manager was called
                int status = mvcTestResult.getResponse().getStatus();
                assertThat(status).isEqualTo(400);
                ProblemDetail payError = objectMapper.readValue(mvcTestResult.getResponse().getContentAsString(),
                                ProblemDetail.class);
                assertThat(payError.getDetail()).containsIgnoringCase("Payment error");
        }

        @Test
        void withValidPayment_whenPay_shouldReturnPaymentSuccess() throws Exception {

                MvcTestResult mvcTestResult = mockMvc.post().uri("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(MockData.createOrderSubmitDataJson())
                                .exchange();

                OrderDataResponse orderData = objectMapper.readValue(mvcTestResult.getResponse().getContentAsString(),
                                OrderDataResponse.class);
                UUID createdOrderId = orderData.getOrderId();

                mvcTestResult = mockMvc.put().uri("/api/orders/" + createdOrderId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(MockData.orderPaySubmitDataJson(createdOrderId))
                                .exchange();

                // Ensure the manager was called
                int status = mvcTestResult.getResponse().getStatus();
                assertThat(status).isEqualTo(200);
                orderData = objectMapper.readValue(mvcTestResult.getResponse().getContentAsString(),
                                OrderDataResponse.class);
                assertThat(orderData).isNotNull();
        }

}
