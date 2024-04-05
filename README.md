# Cglib feature
 - Enhancer: allows us to create a proxy by dynamically extending a class by suing a setSuperclass() method
 - BeanGenerator: allow us to dynamically create beans and to add fields together with setter and getter method. use to generate simple POJO object
   - if getting this error `.CodeGenerationException: java.lang.reflect.InaccessibleObjectException-`. try to use `--add-opens java.base/java.lang=ALL-UNNAMED ` commands
 - Mixin allows combining multiple objects into one.