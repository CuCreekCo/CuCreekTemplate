package com.cucreek.web.controller;

import com.cucreek.business.service.CodeTableAdminService;
import com.cucreek.business.service.HelloService;
import com.cucreek.common.BusinessException;
import com.cucreek.common.CodeTablePropertyEditor;
import com.cucreek.persistence.dto.CodeTableDTO;
import com.cucreek.persistence.dto.FuelUxDataGridVO;
import com.cucreek.persistence.dto.HelloDTO;
import com.cucreek.persistence.dto.ValidationMessageVO;
import com.cucreek.persistence.entity.UserRoleCode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/hello")
public class HelloController extends BaseController {

   @Autowired
   CodeTableAdminService codeTableAdminService;
   @Autowired
   HelloService helloService;

   /* Demo purposes only - any real system wouldn't do this...  */
   static final List<HelloDTO> helloDTOList = new ArrayList<>();
   /* Create sample hello data */
   static {
      for (int i = 0; i < 100; ++i) {
         HelloDTO helloDTO = new HelloDTO();
         helloDTO.setHelloId((long)i);
         helloDTO.setSaySomething(i+" - "+RandomStringUtils.randomAlphabetic(10));
         helloDTO.setHelloDate(DateUtils.addDays(Calendar.getInstance().getTime(), i + 1));
         helloDTO.setHelloTypeCode(new CodeTableDTO(
               RandomStringUtils.randomAlphabetic(6),
               RandomStringUtils.randomAlphabetic(6)));
         helloDTO.setUserRoleCode(new CodeTableDTO(
               RandomStringUtils.randomAlphabetic(6),
               RandomStringUtils.randomAlphabetic(6)));
         helloDTOList.add(helloDTO);
      }
   }
   /**
    * Register custom converter for code table DTO
    */
   @InitBinder
   public void initBinder(WebDataBinder binder) {
      super.initBinder(binder);
      binder.registerCustomEditor(CodeTableDTO.class, "userRoleCode",
            new CodeTablePropertyEditor(UserRoleCode.class, codeTableAdminService));
      binder.registerCustomEditor(CodeTableDTO.class, "helloTypeCode",
            new CodeTablePropertyEditor(UserRoleCode.class, codeTableAdminService));

   }

   /**
    * Fill in the common models for the page
    *
    * @param model view model
    * @throws BusinessException
    */
   @ModelAttribute
   public void fillModelAttributes(Model model) throws BusinessException {
      model.addAttribute("userRoleCodeList",
            codeTableAdminService.findCodesByTable(UserRoleCode.class, false));
   }

   /**
    * Default handler on the /hello request
    *
    * @param model
    * @return hello.jsp view
    */
   @RequestMapping(method = RequestMethod.GET)
   public String index(Model model) {
      model.addAttribute(new HelloDTO());
      return "hello";
   }

   /**
    * Handler for the hello autocomplete
    *
    * @param term - this is required as it is passed from the autocomplete.tag ajax query. The parameter is named 'term'
    *             ala ?term=Some%20Search
    * @return CodeTableDTO list
    */
   @RequestMapping(value = "/helloTypesAutocomplete", method = RequestMethod.GET, produces =
         {MediaType.APPLICATION_JSON_VALUE})
   public
   @ResponseBody
   List<CodeTableDTO> helloTypesAutocompleteAJAX(@RequestParam String term) {
      try {
         //TODO purely for illustrative purposes.
         List<String> helloTypesList = new ArrayList<>();
         for (int i = 0; i < 200; ++i) {
            helloTypesList.add("Fake Value " + StringUtils.leftPad(String.valueOf(i), 4, "0"));
         }
         List<CodeTableDTO> returnList = new ArrayList<>();
         for (String fakeValue : helloTypesList) {
            if (StringUtils.containsIgnoreCase(fakeValue, term)) {
               returnList.add(new CodeTableDTO(fakeValue, fakeValue));
            }
         }
         return returnList;
      }
      catch (Exception e) {
         List<CodeTableDTO> errorList = new ArrayList<>();
         CodeTableDTO errorObject = new CodeTableDTO(this.getClass().getSimpleName(), e.getMessage());
         errorObject.getValidationMessages().add(new ValidationMessageVO(
               ValidationMessageVO.ValidationMessageStatusEnum.ERROR,
               messageSource("error_business_exception", new String[]{e.getMessage()})));
         return errorList;
      }
   }

   /**
    * Controller method to hand the AJAX call from the hello list Fuel UX table.
    *
    * @param sortProperty field to sort on
    * @param sortDirection asc or desc
    * @param pageIndex starting row
    * @param pageSize rows to return
    * @return list of user's as JSON text
    */
   @RequestMapping(value="/list", produces =
         { MediaType.APPLICATION_JSON_VALUE })
   @ResponseBody
   FuelUxDataGridVO vehicleList(
         @RequestParam(required=false) String search,
         @RequestParam(required=false) final String sortProperty,
         @RequestParam(required=false) final String sortDirection,
         @RequestParam Integer pageIndex,
         @RequestParam Integer pageSize, HttpServletRequest request)
   {
      int rowStart = pageIndex*pageSize;
      try {
         Collections.sort(helloDTOList,new Comparator<HelloDTO>() {
            @Override
            public int compare(HelloDTO o1, HelloDTO o2) {
               if(StringUtils.equalsIgnoreCase(sortProperty,"saySomething")){
                  return StringUtils.equalsIgnoreCase(sortDirection,"asc")?
                         o1.getSaySomething().compareTo(o2.getSaySomething()):
                         o2.getSaySomething().compareTo(o1.getSaySomething());
               }
               else if (StringUtils.equalsIgnoreCase(sortProperty,"helloTypeCode")){
                  return StringUtils.equalsIgnoreCase(sortDirection,"asc")?
                         o1.getHelloTypeCode().getCodeValue().compareTo(o2.getHelloTypeCode().getCodeValue()):
                         o2.getHelloTypeCode().getCodeValue().compareTo(o1.getHelloTypeCode().getCodeValue());
               }
               else if (StringUtils.equalsIgnoreCase(sortProperty,"userRoleCode")){
                  return StringUtils.equalsIgnoreCase(sortDirection,"asc")?
                         o1.getUserRoleCode().getCodeValue().compareTo(o2.getUserRoleCode().getCodeValue()):
                         o2.getUserRoleCode().getCodeValue().compareTo(o1.getUserRoleCode().getCodeValue());
               }
               return o1.getSaySomething().compareTo(o2.getSaySomething());
            }
         });

         List<HelloDTO> pagedList = helloDTOList.subList(rowStart,
               Math.min(rowStart+pageSize,helloDTOList.size()));

         FuelUxDataGridVO fxvo = new FuelUxDataGridVO();
         fxvo.setData(pagedList);
         fxvo.setCount(helloDTOList.size());
         fxvo.setPerPage(pageSize);
         fxvo.setStart(1 + rowStart);
         fxvo.setEnd(rowStart + Math.min(pageSize, helloDTOList.size()));
         fxvo.setPage(1 + pageIndex);
         fxvo.setPages((helloDTOList.size() / pageSize));
         return fxvo;
      }
      catch (Exception e)
      {
         _logger.error(e.getMessage(),e);
         return null;
      }
   }


   /**
    * Save the Hello DTO to the database
    *
    * @param helloDTO           transfer object
    * @param bindingResult      spring binding
    * @param model              view model
    * @param redirectAttributes for redirect after post processing messages
    * @return view
    */
   @RequestMapping(value = "/save", method = RequestMethod.POST)
   public String save(@Valid HelloDTO helloDTO, BindingResult bindingResult,
         Model model,
         RedirectAttributes redirectAttributes) {
      try {
         helloDTO = helloService.validateHello(helloDTO);

         for (ValidationMessageVO vmo : helloDTO.getValidationMessages()) {
            bindingResult.rejectValue(vmo.getPath(),
                  "field_invalid", new String[]{vmo.getMessage()}, vmo.getMessage());
         }

         if (bindingResult.hasErrors()) {
            return "hello";
         }

         helloService.saveHello(helloDTO);
         redirectAttributes.addFlashAttribute(new ValidationMessageVO(
               ValidationMessageVO.ValidationMessageStatusEnum.SUCCESS,
               messageSource("global_success")));

         return "redirect:/hello"; //redirect after save to prevent back button re-post
      }
      catch (BusinessException e) {
         //DfsServiceExceptions are not a reject, but a message error
         _logger.error(e.getMessage(), e);
         model.addAttribute(new ValidationMessageVO(ValidationMessageVO.ValidationMessageStatusEnum.ERROR,
               messageSource("error_business_exception",
                     e.getMessage())));
      }
      return "hello";
   }

}