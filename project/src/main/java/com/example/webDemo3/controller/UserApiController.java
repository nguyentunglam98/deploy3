package com.example.webDemo3.controller;

import com.example.webDemo3.constant.Constant;
import com.example.webDemo3.dto.MessageDTO;
import com.example.webDemo3.dto.manageAccountResponseDto.ViewPerInforResponseDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.ChangePasswordRequestDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.EditPerInforRequestDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.LoginRequestDto;
import com.example.webDemo3.dto.request.manageAccountRequestDto.ViewPerInforRequestDto;
import com.example.webDemo3.security.CustomUserDetails;
import com.example.webDemo3.security.JwtTokenProvider;
import com.example.webDemo3.dto.manageAccountResponseDto.LoginResponseDto;
import com.example.webDemo3.service.manageAccountService.ChangePasswordService;
import com.example.webDemo3.service.manageAccountService.EditPerInforService;
import com.example.webDemo3.service.manageAccountService.LoginService;
import com.example.webDemo3.service.manageAccountService.ViewPerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private ChangePasswordService changePasswordService;

    @Autowired
    private ViewPerInfoService viewPerInfoService;

    @Autowired
    private EditPerInforService editPerInforService;

    @Autowired
    private LoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;


    /**
     * lamnt98
     * 23/06
     * catch request from client to change password
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/changepassword")
    public MessageDTO changePassword(@RequestBody ChangePasswordRequestDto model)
    {
        return changePasswordService.checkChangePasswordUser(model);
    }


    /**
     * lamnt98
     * 26/06
     * catch request from client to edit information of user
     * @param model
     * @return MessageDTO
     */
    @PostMapping("/editinformation")
    public MessageDTO editInformation(@RequestBody EditPerInforRequestDto model)
    {
        return editPerInforService.editUserInformation(model);
    }


    /**
     * yenvb
     * 10/8
     */
    @PostMapping("/get-authentication")
    public MessageDTO getAuthentication(){
        MessageDTO message = new MessageDTO();
        message.setMessageCode(1);
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            message.setMessageCode(0);
        }
        return message;
    }

    /**
     * yenvb
     * 10/8
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto model, HttpSession session)
    {
        LoginResponseDto output = loginService.checkLoginUser(model);

        if(output.getMessage().getMessageCode() == 0){
            // Xác thực từ username và password.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            model.getUsername(),
                            model.getPassword()
                    )
            );
            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Trả về jwt cho người dùng.
            String jwt = tokenProvider.generateToken(
                    (CustomUserDetails) authentication.getPrincipal());
            output.setAccessToken(jwt);
        }
        return output;
    }

    /**
     * yenvb
     * 10/8
     */
    @PostMapping(value = "/logout")
    public MessageDTO logout(HttpServletRequest request, HttpServletResponse response){
        MessageDTO message = new MessageDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            message = Constant.SUCCESS;
            return message;
        }else {
            message.setMessageCode(1);
            message.setMessage("Authentication is not exist");
            return message;
        }
    }

    /**
     * lamnt98
     * 25/06
     * catch request from client to find information of user
     * @param model
     * @return ViewPerInforResponseDto
     */
    @PostMapping("/viewinformation")
    public ViewPerInforResponseDto viewInformation(@RequestBody ViewPerInforRequestDto model)
    {
        ViewPerInforResponseDto responseDto = viewPerInfoService.getUserInformation(model);
        return responseDto;
    }

}
