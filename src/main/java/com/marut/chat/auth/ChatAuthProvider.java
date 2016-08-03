package com.marut.chat.auth;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions;
import io.vertx.ext.auth.oauth2.OAuth2FlowType;

/**
 * Created by marutsingh on 7/18/16.
 */
public class ChatAuthProvider {

    public void authenticate(Vertx vertx){

        OAuth2Auth oauth2 = OAuth2Auth.create(vertx, OAuth2FlowType.AUTH_CODE, new OAuth2ClientOptions()
                        .setClientID("YOUR_CLIENT_ID")
                        .setClientSecret("YOUR_CLIENT_SECRET")
                        .setSite("https://github.com/login")
                        .setTokenPath("/oauth/access_token")
                        .setAuthorizationPath("/oauth/authorize")
        );

// when there is a need to access a protected resource or call a protected method,
// call the authZ url for a challenge

        String authorization_uri = oauth2.authorizeURL(new JsonObject()
                .put("redirect_uri", "http://localhost:8080/callback")
                .put("scope", "notifications")
                .put("state", "3(#0/!~"));

// when working with web application use the above string as a redirect url
// in this case GitHub will call you back in the callback uri one should now complete the handshake as:

        String code = "xxxxxxxxxxxxxxxxxxxxxxxx"; // the code is provided as a url parameter by github callback call

        oauth2.getToken(new JsonObject().put("code", code).put("redirect_uri", "http://localhost:8080/callback"), res -> {
            if (res.failed()) {
                // error, the code provided is not valid
            } else {
                // save the token and continue...
            }
        });
    }
}
