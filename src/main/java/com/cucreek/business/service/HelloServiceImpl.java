package com.cucreek.business.service;

import com.cucreek.persistence.dto.HelloDTO;
import com.cucreek.persistence.entity.HelloEntity;
import com.cucreek.persistence.repository.HelloRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Hello service business implementation
 *
 * @author jljdavidson
 *
 */
@Service
@Transactional(readOnly = true)
public class HelloServiceImpl extends BaseBusinessService implements HelloService {

   @Autowired
   HelloRepository helloRepository;

   @Override
   public List<HelloDTO> retrieveHellos(){
      return helloRepository.findAllHellos();
   }

   @Override
   public HelloDTO validateHello(HelloDTO helloDTO){

      helloDTO.clearMessages();

      if(!StringUtils.containsIgnoreCase(helloDTO.getSaySomething(), "nice")){
         helloDTO.addErrorMessage(HelloDTO.class.getSimpleName(),"saySomething",
               getMessage("error.0100.must.say.something.nice",helloDTO.getSaySomething()));
      }
      return helloDTO;
   }

   @Override
   public HelloDTO saveHello(HelloDTO helloDTO){
      if(validateHello(helloDTO).hasErrorMesssages())
      {
         return helloDTO;
      }

      HelloEntity helloEntity;
      if(helloDTO.getHelloId()==null){
         helloEntity = new HelloEntity();
      } else {
         helloEntity = helloRepository.findById(HelloEntity.class, helloDTO.getHelloId());
      }
      helloEntity.setSaySomething(helloDTO.getSaySomething());
      helloRepository.save(helloEntity);
      helloDTO.setHelloId(helloEntity.getHelloId());
      return helloDTO;
   }
}
