package com.cucreek.business.service;

import com.cucreek.common.BusinessException;
import com.cucreek.persistence.dto.HelloDTO;

import java.util.List;

/**
 * Hello business service interface
 *
 * @author jljdavidson
 *
 */
public interface HelloService {
   List<HelloDTO> retrieveHellos();

   HelloDTO validateHello(HelloDTO helloDTO);

   HelloDTO saveHello(HelloDTO helloDTO) throws BusinessException;
}
