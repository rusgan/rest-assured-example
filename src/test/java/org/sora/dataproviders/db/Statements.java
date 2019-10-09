package org.sora.dataproviders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Statements {

    public static int getVerificationCode(Connection connection, String userId){

        int smsCode = 0;

        try{
            PreparedStatement pst = connection.prepareStatement("select * from phone_verification where user_id=?");
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                smsCode = rs.getInt("code");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return smsCode;
    }

    public static String getInviteCode(Connection connection, String uuid){

        String invCode = null;

        try{
            PreparedStatement pst = connection.prepareStatement("select code from INVITATION where app_form_id = ?::uuid order by created_date desc");
            pst.setString (1, uuid);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                invCode = rs.getString("code");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return invCode;
    }

    public static String getPhoneVerificationByEmail(Connection connection, String email){

        String verifCode = null;

        try {
            PreparedStatement pst = connection.prepareStatement("select * from phone_verification where user_id = (select user_id from application_forms where email = ?)");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                verifCode = rs.getString("code");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return verifCode;
    }
}
