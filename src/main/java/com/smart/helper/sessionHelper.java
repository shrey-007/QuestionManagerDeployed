package com.smart.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//a class to remove session messages
@Component
public class sessionHelper {

    public void removeMessageFromSession(){
        try{
            System.out.println("removing message===================================================================================================================================");
            HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
