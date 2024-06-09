package com.example.user.services.freelance.utils.email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


@Getter
@Service
@Setter
@AllArgsConstructor
public class HtmlTemplateMange  {


    @Autowired
    private ApplicationContext context;

    public String getHtmlBodyAsString(String templateName) {
        ResourceLoader resourceLoader = context instanceof ResourceLoader ? (ResourceLoader) context : null;

        if (resourceLoader == null) {
            System.out.println("******resource loader is null*************");
            throw new RuntimeException("ResourceLoader not available in ApplicationContext");
        }

        Resource templateResource = resourceLoader.getResource("classpath:templates/" + templateName + ".html");
        System.out.println(resourceLoader.getResource("classpath:" + templateName + ".html"));
        try (
                InputStream inputStream = templateResource.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader)
        ) {
            System.out.println("******working on template *************");

            StringBuilder templateContentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                templateContentBuilder.append(line).append("\n");
            }
            System.out.println( "template output : "+templateContentBuilder.toString());
            System.out.println("******Done working on template *************");
            return templateContentBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error processing template: " + templateName, e);
        }

    }



}
