package com.example.demo.socketProgramming;
import com.example.demo.Entity.Phishing;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class deepDiveSocketCommunication {

    public static List<Integer> socketCommunication(String domain){
        List<Integer> _returnData = null;
        try (Socket client = new Socket()) {

            InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 9998); // 소켓에 접속하기 위한 접속 정보를 선언한다.
            client.connect(ipep);
            try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {
                String msg = domain;
                System.out.println("msg = " + msg);
                byte[] data = msg.getBytes();
                ByteBuffer b = ByteBuffer.allocate(4);
                b.order(ByteOrder.LITTLE_ENDIAN);
                b.putInt(data.length);
                sender.write(b.array(), 0, 4);
                sender.write(data);

                //============데이터 전송==============//
                data = new byte[4];
                // 데이터 길이를 받는다.
                receiver.read(data, 0, 4);
                b = ByteBuffer.wrap(data);
                b.order(ByteOrder.LITTLE_ENDIAN);
                int length = b.getInt();
                data = new byte[length];
                receiver.read(data, 0, length);
                msg = new String(data, "UTF-8"); //머신러닝 결과 값
                _returnData = splitMsg(msg);//String > List
                System.out.println("_returnData = " + _returnData);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return _returnData;
    }

    private static List<Integer> splitMsg(String msg) {
        // 대괄호와 공백을 제거한 문자열 생성
        String numbersOnly = msg.replaceAll("[\\[\\]\\s]", "");

        // 쉼표를 기준으로 문자열을 분리하여 배열로 변환
        String[] processedData = numbersOnly.split(",");
        List<String> list = Arrays.stream(processedData).toList();

        // 문자열 배열을 정수 리스트로 변환
        List<Integer> resultList = new ArrayList<>();
        for (String num : list) {
            resultList.add(Integer.parseInt(num));
        }
        return resultList;
    }
}
