package com.mudit.checkout.paypal;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mudit.setting.PaymentSettingBag;
import com.mudit.setting.SettingService;

@Component
public class PayPalService {
	private static final String GET_ORDER_API = "/v2/checkout/orders/";
	
	@Autowired private SettingService settingService;
	
	public boolean validateOrder(String orderId) throws PayPalApiException {
		PayPalOrderResponse orderResponse = getOrderDetails(orderId);
		
		return orderResponse.validate(orderId);
	}

	private PayPalOrderResponse getOrderDetails(String orderId) throws PayPalApiException {
		ResponseEntity<PayPalOrderResponse> response = makeRequest(orderId);
		
		HttpStatusCode statusCode = response.getStatusCode();
		
		if (!statusCode.isSameCodeAs(HttpStatus.OK)) {
			throwExceptionForNonOKResponse(statusCode);
		}
		
		return response.getBody();
	}

	private ResponseEntity<PayPalOrderResponse> makeRequest(String orderId) {
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String baseURL = paymentSettings.getURL();
		String requestURL = baseURL + GET_ORDER_API + orderId;
		String clientId = paymentSettings.getClientID();
		String clientSecret = paymentSettings.getClientSecret();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en_US");
		headers.setBasicAuth(clientId, clientSecret);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.exchange(
				requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
	}

	private void throwExceptionForNonOKResponse(HttpStatusCode statusCode) throws PayPalApiException {
		String message = null;
		
		switch (statusCode.value()) {
		case 404: 
			message = "Order ID not found";
			
		case 400:
			message = "Bad Request to PayPal Checkout API";
			
		case 500:
			message = "PayPal server error";
			
		default:
			message = "PayPal returned non-OK status code";
		}
		
		throw new PayPalApiException(message);
	}
}