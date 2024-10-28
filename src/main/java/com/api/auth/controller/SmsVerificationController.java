package com.api.auth.controller;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.web.bind.annotation.*;
import net.nurigo.sdk.message.model.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

@RestController
public class SmsVerificationController {

    final DefaultMessageService messageService;
    private final Map<String, String> verificationCodes = new HashMap<>();

    public SmsVerificationController() {
        Properties properties = new Properties();
//        try {
//            // config.properties 파일을 읽어옴
//            FileInputStream fis = new FileInputStream("src/main/resources/apiKey.properties");
//            properties.load(fis);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("apiKey.properties")) {
            if (inputStream == null) {
                throw new IOException("파일을 찾을 수 없습니다: apiKey.properties");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // properties에서 API 키와 비밀 키를 가져옴
        String apiKey = properties.getProperty("COOLSMS_API_KEY");
        String apiSecret = properties.getProperty("COOLSMS_API_SECRET");
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * 인증번호 발송 API
     */
    @PostMapping("/api/send-verification-code")
    public String sendVerificationCode(@RequestParam("phoneNumber") String phoneNumber) {
        String verificationCode = generateVerificationCode();

        System.out.println(phoneNumber);
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        Message message = new Message();
        message.setFrom("01074363900");
        message.setTo(phoneNumber);
        message.setText("인증번호는 " + verificationCode + " 입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        // 인증번호를 해당 번호와 함께 저장
        verificationCodes.put(phoneNumber, verificationCode);

        System.out.println(verificationCode);

        return "인증번호가 발송되었습니다.";
    }

    /**
     * 인증번호 검증 API
     */
    @PostMapping("/api/verify-code")
    public String verifyCode(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("verifyCode") String verifyCode) {
        String storedCode = verificationCodes.get(phoneNumber);

        if (storedCode != null && storedCode.equals(verifyCode)) {
            // 인증번호가 일치하는 경우
            verificationCodes.remove(phoneNumber);  // 인증 성공 시 코드 삭제
            System.out.println("성공");
            return "인증에 성공했습니다.";
        } else {
            // 인증번호가 일치하지 않는 경우
            return "인증번호가 일치하지 않습니다.";
        }
    }

    /**
     * 6자리 랜덤 인증번호 생성
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);  // 100000 ~ 999999 범위의 숫자 생성
        return String.valueOf(code);
    }

}
