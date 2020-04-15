package com.taurus.www;

import com.taurus.api.controller.TestController;
import com.taurus.common.service.FileService;
import com.taurus.common.service.ThymeleafService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaWwwYinghuiApplicationTests {


    /**
     * 读入TXT文件
     */
    public static StringBuilder readFile() {
        String pathname = "D:\\test(1).html";
        StringBuilder sb = new StringBuilder();
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    @Test
    public void contextLoads() {
    }

}
