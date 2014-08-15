package com.boutline.sports.models;

import java.util.HashMap;

/**
 * Created by user on 13/08/14.
 */
public class MeteorUser {

    HashMap<String,String> user = new HashMap<String,String>();
    String password;


   public MeteorUser(String email,String password)
   {

       if(email.contains("@"))
       user.put("email",email);
       else
       user.put("username",email);
     this.password=password;
   }
}
