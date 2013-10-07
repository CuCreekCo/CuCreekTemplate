package com.cucreek.web;

import com.cucreek.persistence.entity.UserAccount;
import com.cucreek.persistence.entity.UserRole;
import com.cucreek.persistence.entity.UserRoleCode;
import com.cucreek.persistence.repository.HelloRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/resources/META-INF/spring/applicationContext.xml")
public class BusinessAppTests {

   @Autowired
   HelloRepository helloRepository;

   @Before
   public void setup() {
      ;
   }

   @Test
   public void createUser() {

      UserRoleCode userRoleCode = new UserRoleCode();
      userRoleCode.setRoleCode("MY_ROLE");
      userRoleCode.setEffectiveDate(Calendar.getInstance().getTime());
      userRoleCode.setRoleDescription("My role");
      helloRepository.save(userRoleCode);

      UserRole userRole = new UserRole();
      userRole.setUserRoleCode(userRoleCode);
      helloRepository.save(userRole);

      UserAccount userAccount = new UserAccount();
      userAccount.setEmail("fake@cucreek.co");
      userAccount.setPassword(DigestUtils.sha256Hex("fakepassword"));
      userAccount.setSuspended(false);
      userAccount.setUsername(userAccount.getEmail());
      userAccount.getUserRoles().add(userRole);
      helloRepository.save(userAccount);
   }
}
