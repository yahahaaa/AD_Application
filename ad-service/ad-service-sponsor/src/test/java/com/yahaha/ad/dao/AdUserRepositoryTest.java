package com.yahaha.ad.dao;

import com.yahaha.ad.entity.AdUser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdUserRepositoryTest {

    @Autowired
    private AdUserRepository adUserRepository;

    @Test
    public void findByUsername() {
        AdUser result = adUserRepository.findByUsername("测试用户");
        Assert.assertNotNull(result);
    }
}