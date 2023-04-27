package com.multicampus.topicsation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.*;

@SpringBootApplication
//@ComponentScan(basePackages = "com.multicampus.topicsation.aspect")
public class TopicsationApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TopicsationApplication.class, args);
//
//        ProcessBuilder processBuilder = new ProcessBuilder("/usr/local/bin/python3", "NewsCrawler.py");
//        // 파이썬 실행 파일이 있는 경로와 스크립트 파일명을 전달하여 ProcessBuilder 객체를 생성합니다.
//        processBuilder.directory(new File("/Users/yeonu/Y_DEV/Topicsation-Project/src/main/resources/static/NewsTopicCrawler"));
//
//        // 명령어 실행 후 출력 결과를 가져옵니다.
//        Process process = processBuilder.start();
//        InputStream inputStream = process.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//        }
//
//        // 명령어 실행이 완료될 때까지 대기하고, 종료 코드를 확인합니다.
//        try {
//            int exitCode = process.waitFor();
//            System.out.println("Exit code: " + exitCode);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            // 예외 처리 로직 추가
//        }

    }

}
