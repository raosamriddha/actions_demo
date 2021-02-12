package org.lattice.spectrum_backend_final.dao.util;

/**
 * @author RAHUL KUMAR MAURYA
 */

import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JsonMapper {

    /**
     * Convert ResultSet to json array.
     *
     * @param resultSet
     * @return json array.
     */
    public static JSONArray getJSONFromResultSet(ResultSet resultSet) {

        JSONArray jsonArray = new JSONArray();
        if (resultSet != null) {
            try {
                ResultSetMetaData metaData = resultSet.getMetaData();

                // In this loop we will converting resultSet into array of json object.
                while (resultSet.next()) {

                    JSONObject jsonObject = new JSONObject();
                    for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                        if (resultSet.getString(metaData.getColumnName(columnIndex)) != null){

                            jsonObject.put(metaData.getColumnLabel(columnIndex), resultSet.getString(metaData.getColumnName(columnIndex)));
                        }
                        else
                            jsonObject.put(metaData.getColumnLabel(columnIndex), "");
                    }
                    jsonArray.put(jsonObject);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    /**
     * @param resultSet
     * @param resultSetSize
     * @return
     */
    public static JSONArray getJSONFromMToMResultSet(ResultSet resultSet, int resultSetSize) {

        JSONArray jsonArray = new JSONArray();
        JSONObject userJson;
        JSONArray roleArray;

        int count = 0;

        JSONObject[] jArray = new JSONObject[resultSetSize];

        if (resultSet != null) {
            try {

                ResultSetMetaData metaData = resultSet.getMetaData();

                // In this loop we will converting resultSet into array of json object.
                // So that we can move forward and backward while converting to Json Array having nested roles json array because
                // ResultSet in Sqlite can move forward only.
                while (resultSet.next()) {

                    JSONObject jsonObject = new JSONObject();
                    for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                        if (resultSet.getString(metaData.getColumnName(columnIndex)) != null)
                            jsonObject.put(metaData.getColumnLabel(columnIndex), resultSet.getString(metaData.getColumnName(columnIndex)));
                        else
                            jsonObject.put(metaData.getColumnLabel(columnIndex), "");

                    }

                    jArray[count] = jsonObject;
                    count++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        try {

            String loginUserType = DbConnectionManager.getInstance().getTokenManager().getUserType();

            //In this loop we will finally converting the array of json object into Json Array having nested roles json array.
            for (int i = 0; i < count; i++) {
                JSONObject jobj1 = jArray[i];

                String recordUserType = null, fullName = null;

                //checks if the user_id is not equals to -1 because we have set user_id to -1 to avoid redundant user

                if (jobj1.getInt(ApiConstant.JSON_USER_ID) != -1) {


                    roleArray = new JSONArray();

                    userJson = new JSONObject();

                    if (
                            jobj1.getString(ApiConstant.MIDDLE_NAME).equals(ApiConstant.BLANK_SPACE) ||
                                    jobj1.getString(ApiConstant.MIDDLE_NAME).equals("")
                    ) {
                        fullName = jobj1.getString(ApiConstant.FIRST_NAME) + ApiConstant.BLANK_SPACE + jobj1.getString(ApiConstant.LAST_NAME);
                    } else {
                        fullName = jobj1.getString(ApiConstant.FIRST_NAME) + ApiConstant.BLANK_SPACE + jobj1.getString(ApiConstant.MIDDLE_NAME) + ApiConstant.BLANK_SPACE + jobj1.getString(ApiConstant.LAST_NAME);
                    }

                    userJson.put(ApiConstant.JSON_USER_ID, jobj1.getInt(ApiConstant.JSON_USER_ID));
                    userJson.put(ApiConstant.USERNAME, jobj1.getString(ApiConstant.USERNAME));
                    userJson.put(ApiConstant.IS_ACTIVE, jobj1.getString(ApiConstant.IS_ACTIVE));
                    userJson.put(ApiConstant.FULL_NAME, fullName);
                    userJson.put(ApiConstant.EMAIL_ID, jobj1.getString(ApiConstant.EMAIL_ID));
                    userJson.put(ApiConstant.CONTACT_NUMBER, jobj1.getString(ApiConstant.CONTACT_NUMBER));
                    userJson.put(ApiConstant.LAST_LOGIN_DATE, jobj1.getString(ApiConstant.LAST_LOGIN_DATE));
                    userJson.put(ApiConstant.PASS_IS_ACTIVE, jobj1.getString(ApiConstant.PASS_IS_ACTIVE));


                    roleArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION, jobj1.getString(ApiConstant.ROLE_DESCRIPTION)));

                    for (int j = i + 1; j < count; j++) {
                        JSONObject jobj2 = jArray[j];

                        if (jobj1.getInt(ApiConstant.JSON_USER_ID) == jobj2.getInt(ApiConstant.JSON_USER_ID)) {

                            roleArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION, jobj2.getString(ApiConstant.ROLE_DESCRIPTION)));
                            jobj2.put(ApiConstant.JSON_USER_ID, -1);

                            break;
                        }

                    }

                    recordUserType = BasicUtility.getUserRole(roleArray);


                    // checks if the login user has higher role than the current user's detail.
                    // If the role is high than only he is able to see reset password request.
                    if (BasicUtility.getUserPriority(loginUserType) < BasicUtility.getUserPriority(recordUserType))
                        userJson.put(ApiConstant.PASS_STATUS, jobj1.getString(ApiConstant.PASS_STATUS));
                    else
                        userJson.put(ApiConstant.PASS_STATUS, ApiConstant.STATUS_FOR_USER_CURRENT_PASSWORD);

                    userJson.put(ApiConstant.ROLES, roleArray);
                    jsonArray.put(userJson);

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return jsonArray;


    }

    /**
     * @param resultSet
     * @param resultSetSize
     * @return
     */
    public static JSONArray getSingleJSONFromMToMResultSet(ResultSet resultSet, int resultSetSize) {

        JSONArray jsonArray = new JSONArray();
        int size = resultSetSize;
        JSONObject userJson = null;
        JSONArray roleArray = null;

        int count = 0;

        JSONObject[] jarray = new JSONObject[size];
        if (resultSet != null) {
            try {

                ResultSetMetaData metaData = resultSet.getMetaData();

                // In this loop we will converting resultSet into array of json object.
                // So that we can move forward and backward while converting to Json Array having nested roles json array because
                // ResultSet in Sqlite can move forward only.
                while (resultSet.next()) {

                    JSONObject jsonObject = new JSONObject();
                    for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                        if (resultSet.getString(metaData.getColumnName(columnIndex)) != null)
                            jsonObject.put(metaData.getColumnLabel(columnIndex), resultSet.getString(metaData.getColumnName(columnIndex)));
                        else
                            jsonObject.put(metaData.getColumnLabel(columnIndex), "");

                    }

                    jarray[count] = jsonObject;

                    count++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        try {

            //In this loop we will finally converting the array of json object into Json Array having nested roles json array.
            for (int i = 0; i < count; i++) {
                JSONObject jobj1 = jarray[i];


                //checks if the user_id is not equals to -1 because we have set user_id to -1 to avoid redundant user
                if (jobj1.getInt(ApiConstant.JSON_USER_ID) != -1) {

                    roleArray = new JSONArray();

                    userJson = new JSONObject();

                    userJson.put(ApiConstant.JSON_USER_ID, jobj1.getInt(ApiConstant.JSON_USER_ID));
                    userJson.put(ApiConstant.USERNAME, jobj1.getString(ApiConstant.USERNAME));
                    userJson.put(ApiConstant.EMAIL_ID, jobj1.getString(ApiConstant.EMAIL_ID));
                    userJson.put(ApiConstant.PREFIX, jobj1.getString(ApiConstant.PREFIX));
                    userJson.put(ApiConstant.FIRST_NAME, jobj1.getString(ApiConstant.FIRST_NAME));
                    userJson.put(ApiConstant.MIDDLE_NAME, jobj1.getString(ApiConstant.MIDDLE_NAME));
                    userJson.put(ApiConstant.LAST_NAME, jobj1.getString(ApiConstant.LAST_NAME));
                    userJson.put(ApiConstant.IS_ACTIVE, jobj1.getString(ApiConstant.IS_ACTIVE));
                    userJson.put(ApiConstant.CONTACT_NUMBER, jobj1.getString(ApiConstant.CONTACT_NUMBER));
                    userJson.put(ApiConstant.PASS_STATUS, jobj1.getString(ApiConstant.PASS_STATUS));
                    userJson.put(ApiConstant.PASS_IS_ACTIVE, jobj1.getString(ApiConstant.PASS_IS_ACTIVE));
                    userJson.put(ApiConstant.PASSWORD, PasswordUtil.decrypt(jobj1.getString(ApiConstant.DEFAULT_PASSWORD), ApiConstant.SECRET));

                    roleArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION, jobj1.getString(ApiConstant.ROLE_DESCRIPTION)));


                    for (int j = i + 1; j < count; j++) {
                        JSONObject jobj2 = jarray[j];
                        if (jobj1.getInt(ApiConstant.JSON_USER_ID) == jobj2.getInt(ApiConstant.JSON_USER_ID)) {

                            roleArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION, jobj2.getString(ApiConstant.ROLE_DESCRIPTION)));
                            jobj2.put(ApiConstant.JSON_USER_ID, -1);

                            break;
                        }

                    }

                    userJson.put(ApiConstant.ROLES, roleArray);

                    jsonArray.put(userJson);

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonArray;


    }


    /**
     * @param resultSet
     * @param resultSetSize
     * @return
     */
    public static JSONArray getProfileJSONFromMToMResultSet(ResultSet resultSet, int resultSetSize) {

        JSONArray jsonArray = new JSONArray();
        int size = resultSetSize;
        JSONObject userJson = null;
        JSONArray roleArray = null;

        int count = 0;

        JSONObject[] jarray = new JSONObject[size];
        if (resultSet != null) {
            try {

                ResultSetMetaData metaData = resultSet.getMetaData();


                while (resultSet.next()) {

                    JSONObject jsonObject = new JSONObject();
                    for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                        if (resultSet.getString(metaData.getColumnName(columnIndex)) != null)
                            jsonObject.put(metaData.getColumnLabel(columnIndex), resultSet.getString(metaData.getColumnName(columnIndex)));
                        else
                            jsonObject.put(metaData.getColumnLabel(columnIndex), "");

                    }

                    jarray[count] = jsonObject;

                    count++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        try {

            for (int i = 0; i < count; i++) {
                JSONObject jobj1 = jarray[i];

                //checks if the user_id is not equals to -1 because we have set user_id to -1 to avoid redundant user
                if (jobj1.getInt(ApiConstant.JSON_USER_ID) != -1) {

                    roleArray = new JSONArray();

                    userJson = new JSONObject();

                    userJson.put(ApiConstant.JSON_USER_ID, jobj1.getInt(ApiConstant.JSON_USER_ID));
                    userJson.put(ApiConstant.USERNAME, jobj1.getString(ApiConstant.USERNAME));
                    userJson.put(ApiConstant.EMAIL_ID, jobj1.getString(ApiConstant.EMAIL_ID));
                    userJson.put(ApiConstant.PREFIX, jobj1.getString(ApiConstant.PREFIX));
                    userJson.put(ApiConstant.FIRST_NAME, jobj1.getString(ApiConstant.FIRST_NAME));
                    userJson.put(ApiConstant.MIDDLE_NAME, jobj1.getString(ApiConstant.MIDDLE_NAME));
                    userJson.put(ApiConstant.LAST_NAME, jobj1.getString(ApiConstant.LAST_NAME));
                    userJson.put(ApiConstant.IS_ACTIVE, jobj1.getString(ApiConstant.IS_ACTIVE));
                    userJson.put(ApiConstant.CONTACT_NUMBER, jobj1.getString(ApiConstant.CONTACT_NUMBER));
                    userJson.put(ApiConstant.PASS_STATUS, jobj1.getString(ApiConstant.PASS_STATUS));
                    userJson.put(ApiConstant.PASS_IS_ACTIVE, jobj1.getString(ApiConstant.PASS_IS_ACTIVE));


                    roleArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION, jobj1.getString(ApiConstant.ROLE_DESCRIPTION)));


                    for (int j = i + 1; j < count; j++) {
                        JSONObject jobj2 = jarray[j];
                        if (jobj1.getInt(ApiConstant.JSON_USER_ID) == jobj2.getInt(ApiConstant.JSON_USER_ID)) {

                            roleArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION, jobj2.getString(ApiConstant.ROLE_DESCRIPTION)));
                            jobj2.put(ApiConstant.JSON_USER_ID, -1);

                            break;
                        }

                    }

                    userJson.put(ApiConstant.ROLES, roleArray);

                    jsonArray.put(userJson);

                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonArray;


    }


    public static JSONArray getRecipeJSONFromResultSet(ResultSet resultSet) {

        JSONArray jsonArray = new JSONArray();
        if (resultSet != null) {
            try {
                ResultSetMetaData metaData = resultSet.getMetaData();

                // In this loop we will converting resultSet into array of json object.
                while (resultSet.next()) {

                    JSONObject recipeJson = new JSONObject();
                    for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                        if (resultSet.getString(metaData.getColumnName(columnIndex)) != null)
                            recipeJson.put(metaData.getColumnLabel(columnIndex), resultSet.getString(metaData.getColumnName(columnIndex)));
                        else
                            recipeJson.put(metaData.getColumnLabel(columnIndex), "");
                    }

                    if (
                            recipeJson.getString(ApiConstant.PUMP_NAME).equals(ApiConstant.KMPI) ||
                                    recipeJson.getString(ApiConstant.PUMP_NAME).equals(ApiConstant.FS500)
                    ) {
                        Float convertedFlowRate = recipeJson.getFloat(ApiConstant.FLOW_RATE) * 1000;

                        recipeJson.put(ApiConstant.FLOW_RATE, convertedFlowRate);


                    }

                    jsonArray.put(recipeJson);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }


    public static JSONArray getTubingJSONFromResultSet(ResultSet resultSet) {

        JSONArray jsonArray = new JSONArray();
        if (resultSet != null) {
            try {
                ResultSetMetaData metaData = resultSet.getMetaData();

                // In this loop we will converting resultSet into array of json object.
                while (resultSet.next()) {

                    JSONObject jsonObject = new JSONObject();
                    for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                        if (resultSet.getString(metaData.getColumnName(columnIndex)) != null)
                            jsonObject.put(metaData.getColumnLabel(columnIndex), resultSet.getString(metaData.getColumnName(columnIndex)));
                        else
                            jsonObject.put(metaData.getColumnLabel(columnIndex), "");
                    }

//                    //converting flow rate according to unit
//                    if (resultSet.getString(ApiConstant.FLOW_RATE_UNIT).equals(ApiConstant.LITER_PER_MIN)) {
//                        jsonObject.put(ApiConstant.MIN_FLOW_RATE, resultSet.getFloat(ApiConstant.MIN_FLOW_RATE) / 1000);
//                        jsonObject.put(ApiConstant.MAX_FLOW_RATE, resultSet.getFloat(ApiConstant.MAX_FLOW_RATE) / 1000);
//                    }
                    jsonArray.put(jsonObject);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }


//    public static JSONArray getEndPointJSONFromResultSet(ResultSet endPointRS , int resultSetSize) {
//
//        JSONArray jsonArray = new JSONArray();
//        int size = resultSetSize;
//        JSONObject endPointJson = null;
//        JSONArray endPointJsonArray = null;
//
//
//        int count = 0;
//
//        JSONObject[] jarray = new JSONObject[size];
//        if(endPointRS!=null)
//        {
//            try {
//
//                ResultSetMetaData metaData = endPointRS.getMetaData();
//
//                // In this loop we will converting resultSet into array of json object.
//                // So that we can move forward and backward while converting to Json Array having nested roles json array because
//                // ResultSet in Sqlite can move forward only.
//                while(endPointRS.next())
//                {
//
//                    JSONObject jsonObject = new JSONObject();
//                    for(int columnIndex=1;columnIndex<=metaData.getColumnCount() ;columnIndex++)
//                    {
//                        if(endPointRS.getString(metaData.getColumnName(columnIndex))!=null)
//                            jsonObject.put(metaData.getColumnLabel(columnIndex) ,  endPointRS.getString(metaData.getColumnName(columnIndex)));
//                        else
//                            jsonObject.put(metaData.getColumnLabel(columnIndex), "");
//
//                    }
//
//                    jarray[count] = jsonObject;
//
//                    count++;
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }catch(Exception e) {
//
//                e.printStackTrace();
//            }
//        }
//
//        try {
//
//            //In this loop we will finally converting the array of json object into Json Array having nested roles json array.
//            for(int i=0 ; i<count ; i++)
//            {
//                JSONObject jobj1 = jarray[i];
//
//
//                //checks if the user_id is not equals to -1 because we have set user_id to -1 to avoid redundant user
//                if(jobj1.getInt(ApiConstant.JSON_USER_ID) != -1)
//                {
//
//                    endPointJsonArray = new JSONArray();
//
//                    endPointJson = new JSONObject();
//
//                    endPointJson.put(ApiConstant.JSON_USER_ID, jobj1.getInt(ApiConstant.JSON_USER_ID));
//                    endPointJson.put(ApiConstant.USERNAME, jobj1.getString(ApiConstant.USERNAME));
//                    endPointJson.put(ApiConstant.EMAIL_ID, jobj1.getString(ApiConstant.EMAIL_ID));
//                    endPointJson.put(ApiConstant.PREFIX, jobj1.getString(ApiConstant.PREFIX));
//                    endPointJson.put(ApiConstant.FIRST_NAME, jobj1.getString(ApiConstant.FIRST_NAME));
//
//                    endPointJsonArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION,jobj1.getString(ApiConstant.ROLE_DESCRIPTION)));
//
//
//                    for(int j=i+1 ; j<count ; j++)
//                    {
//                        JSONObject jobj2 = jarray[j];
//                        if(jobj1.getInt(ApiConstant.JSON_USER_ID) == jobj2.getInt(ApiConstant.JSON_USER_ID))
//                        {
//
//                            endPointJsonArray.put(new JSONObject().put(ApiConstant.ROLE_DESCRIPTION,jobj2.getString(ApiConstant.ROLE_DESCRIPTION)));
//                            jobj2.put(ApiConstant.JSON_USER_ID, -1);
//
//                            break;
//                        }
//
//                    }
//
//                    endPointJson.put(ApiConstant.ROLES, endPointJsonArray);
//
//                    jsonArray.put(endPointJson);
//
//                }
//            }
//
//        }catch(Exception e) {
//
//            e.printStackTrace();
//        }
//
//        return jsonArray;
//
//    }
}
