/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author Khalil
 */
public class UserSession extends user { 
     private static UserSession instance; 
     private boolean isLoggedIn;   
     private static user loggedInUser;
     
     
     
      private UserSession() {
        // Empêcher l'instanciation directe de la classe
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    

    public static void setInstance(UserSession instance) {
        UserSession.instance = instance;
    } 
    public void setLoggedInUser(user user) {
        loggedInUser = user;
    }

    public user getLoggedInUser() {
        return loggedInUser;
    }
      
     
     public boolean getIsLoggedIn(){
         return isLoggedIn;
     }
     public void setIsLoggedIn(boolean isLoggedIn){
         this.isLoggedIn = isLoggedIn;
     
     }
     
    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }
    
}
