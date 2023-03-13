package org.arvind.course.quarkus.microservices.number;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
@ApplicationPath("/rest")
@OpenAPIDefinition(info = @Info(title = "Book Api", description = "creates book", version = "1.0", contact = @Contact(name = "arvind")
                ), tags = {
        @Tag(name = "create book Tag", description = "microservice to create book")

                        }
                    )
public class BookMicroservice extends Application {
}
