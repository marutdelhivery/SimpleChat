package com.marut.chat.integrations.github;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.Repo;
import com.jcabi.github.RtGithub;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marutsingh on 7/24/16.
 */
public class GithubService {

    public void subscribe() {
        Github github = new RtGithub("marutdelhivery", "deewana420");
        Repo repo = github.repos().get(new Coordinates.Simple("delhyiver", "GM-integrator"));
        Map<String,String> configMap = new HashMap<>();
        configMap.put("url","http://godam-url.com/webhook");
        configMap.put("content_type","json");
        try {
            repo.hooks().create("webhook",configMap,Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Issue issue = repo.issues().create("How are you?", "Please tell me...");
        //issue.comments().post("My first comment!");
    }

    public void subscribeEvents(){


    }
}
