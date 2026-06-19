package com.iuc.tpiuc.audit;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditTrace {

    AuditActions  action();
}
