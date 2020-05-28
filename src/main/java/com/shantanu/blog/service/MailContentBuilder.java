package com.shantanu.blog.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    String build(String msg) {
        Context context = new Context();
        context.setVariable("message", msg);
        return templateEngine.process("mailTemplate", context);
    }
}
