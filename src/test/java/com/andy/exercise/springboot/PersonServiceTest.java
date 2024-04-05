package com.andy.exercise.springboot;

import com.andy.exercise.springboot.mixin.*;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.Mixin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    @Test
    public void should_returning_the_same_value() {
        String data = "Hello Andy!";
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        enhancer.setCallback((FixedValue) () -> data);

        PersonService proxy = (PersonService) enhancer.create();

        String result = proxy.sayHello("Tom");
        assertEquals(result, data);
    }

    @Test
    public void should_returning_depend_on_method_signature() {
        String data = "Hello Andy!";
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PersonService.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
           if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
               return data;
           } else {
               return proxy.invokeSuper(obj, args);
           }
        });

        PersonService proxy = (PersonService) enhancer.create();

        String result = proxy.sayHello("Tom");
        assertEquals(result, data);
        int lengthOfName = proxy.lengthOfName("Mary");

        assertEquals(4, lengthOfName);
    }

    @Test
    public void should_create_new_bean_creator() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.addProperty("name", String.class);
        Object myBean = beanGenerator.create();
        Method setter = myBean.getClass().getMethod("setName", String.class);
        setter.invoke(myBean, "some string value set by a cglib");
        Method getter = myBean.getClass().getMethod("getName");
        assertEquals("some string value set by a cglib", getter.invoke(myBean));
    }

    @Test
    public void should_create_mixin() {
        Mixin mixin = Mixin.create(new Class[] {Interface1.class, Interface2.class, MixinInterface.class}, new Object[]{new Class1(), new Class2()});
        MixinInterface mixinDelegate = (MixinInterface) mixin;
        assertEquals("first behaviour", mixinDelegate.first());
        assertEquals("second behaviour", mixinDelegate.second());
     }
}