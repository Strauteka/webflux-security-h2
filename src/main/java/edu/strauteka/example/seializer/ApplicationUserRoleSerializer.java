package edu.strauteka.example.seializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import edu.strauteka.example.security.configuration.ApplicationUserRole;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ApplicationUserRoleSerializer extends StdSerializer<ApplicationUserRole> {

    private enum Keys {
        name,
        id
    }

    public ApplicationUserRoleSerializer() {
        this(ApplicationUserRole.class);
    }

    protected ApplicationUserRoleSerializer(Class<ApplicationUserRole> t) {
        super(t);
    }

    @Override
    public void serialize(ApplicationUserRole value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField(Keys.id.name(), value.id);
        gen.writeObjectField(Keys.name.name(), value.name());
        gen.writeEndObject();
    }
}